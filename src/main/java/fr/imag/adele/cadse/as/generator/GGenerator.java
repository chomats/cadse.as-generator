package fr.imag.adele.cadse.as.generator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeSet;
import java.util.UUID;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;

import fede.workspace.eclipse.java.manager.JavaFileContentManager;
import fr.imag.adele.cadse.core.GenContext;
import fr.imag.adele.cadse.core.Item;
import fr.imag.adele.cadse.core.LinkType;
import fr.imag.adele.cadse.core.content.ContentItem;

public class GGenerator implements GObject, IGenerator {

	private static final UUID[] NO = new UUID[0];

	protected Map<GObject, GAggregator> _aggregator = new HashMap<GObject, GAggregator>();

	public GToken[] _tokens;

	private UUID _id;

	public GGenerator(UUID id) {
		_id = id;
	}
	
	public class ClassAggregator extends GAggregator {
		@Override
		public void append(GResult result, GToken cToken) {
			if (cToken.equals(GCst.t_license)) {
				_result.append(result.toString());
				_result.newline();
			}
			if (cToken.equals(GCst.t_packagename)) {
				_result.append("package ").append(result.getLines()[0])
						.append(";");
				_result.newline();
			}
			if (cToken.equals(GCst.t_import)) {
				TreeSet<String> imports = new TreeSet<String>();
				imports.addAll(Arrays.asList(result.getLines()));
				for (String i : imports) {
					_result.append("import ").append(i).append(";").newline();
				}
			}
			if (cToken.equals(GCst.t_packagename)) {
				_result.append("package ").append(result.getLines()[0])
						.append(";");
				_result.newline();
			}
		}
	}

	public GResult generate(Item currentItem, GGenFile<?> gf, GenContext context,
			GenState state) {
		GAggregator gaggregator = getAggregator(gf);
		GResult resultAll = gaggregator.setResult(this, currentItem, gf, null,
				context);
		gaggregator.beginAll(currentItem, gf, null, context, this);
		GToken[] children = gf.getTokens();
		if (children != null) {
			for (GToken cToken : children) {
				GResult resultCtoken = generate(currentItem, gf, cToken,
						context, state);
				if (resultCtoken != null) {
					gaggregator.append(resultCtoken, cToken);
				}
			}
		}
		gaggregator.endAll(currentItem, gf, null, context, this);

		return resultAll;
	}

	/**
	 * Generate.
	 * 
	 * @param item
	 *            the item
	 * @param sb
	 *            the sb
	 * @param type
	 *            the type
	 * @param kind
	 *            the kind
	 * @param imports
	 *            the imports
	 * @param context
	 *            the context
	 */

	public GResult generate(Item currentItem, GGenFile<?> gf, GToken kind,
			GenContext context, GIter giter, GenState state) {

		giter.beginAll(currentItem, gf, kind, context, this);
		GAggregator gaggregator = getAggregator(kind);
		GResult resultAll = gaggregator.setResult(this, currentItem, gf, kind,
				context);
		gaggregator.setState(state);
		gaggregator.beginAll(currentItem, gf, kind, context, this);
		GToken[] children = kind.getChildren();
		if (children != null) {
			for (GToken cToken : children) {
				GResult resultCtoken = generate(currentItem, gf, cToken,
						context, state);
				if (resultCtoken != null) {
					gaggregator.append(resultCtoken, cToken);
				}
			}
		}
		int count = 0;
		int max = kind.getMax();

		ONE: for (Item anItem : giter) {

			// TODO manage a cache
			GGenPartFile[] producteurs = anItem.getType().adapts(
					GGenPartFile.class);

			for (int i = 0; i < producteurs.length; i++) {
				GGenPartFile p = producteurs[i];
				if (!p.match(gf, kind))
					continue;
				try {
					p.generatePartFile(resultAll, currentItem, gf, kind,
							context, this, state);
				} catch (Throwable e) {
					logException(producteurs[i], e);
				}
				count++;
				if (max == count)
					break ONE;
			}
		}
		if (max != count && gf.match(kind)) {
			gf.generatePartFile(resultAll, currentItem, gf, kind, context, this, state);
		}
		gaggregator.endAll(currentItem, gf, kind, context, this);
		giter.endAll(currentItem, gf, kind, context, this);

		return resultAll;
	}

	private GAggregator getAggregator(GObject kind) {
		GAggregator r = _aggregator.get(kind);
		if (r == null)
			r = kind.getAggregator();
		if (r == null)
			r = new GAggregator();
		return r;
	}

	public String generate(Item currentItem, GGenFile gf, GToken kind,
			GenContext context) {
		return gf.generate(this, currentItem, context);
	}
	
	public GResult generate(Item currentItem, GGenFile gf, GToken kind,
			GenContext context, GenState state) {
		GIter iter = null;
		iter = currentItem.getType().adapt(GIter.class);
		if (iter == null)
			iter = kind.getIter();
		
		return generate(currentItem, gf, kind, context, iter, state);
	}

	private void logException(GGenPartFile producteur, Throwable e) {
		// TODO Auto-generated method stub

	}

	public GResult generate(LinkType linkID, Item currentItem, GGenFile gf,
			GToken kind, GenContext context, GenState state) {
		return generate(currentItem, gf, kind, context, new GLinkIterator(
				currentItem, linkID), state);
	}

	public IProject getProject(GToken t, Item owner) {
		return null;
	}

	public JavaFileContentManager getJavaFileContentManager(GToken t, Item owner) {
		ContentItem cm = owner.getContentItem();
		if (cm instanceof JavaFileContentManager)
			return (JavaFileContentManager) cm;
		return null;
	}

	@Override
	public GAggregator getAggregator() {
		return null;
	}

	public IFile getFile(Item currentItem, GToken fileToken, GenContext context) {
		JavaFileContentManager cm = getJavaFileContentManager(fileToken,
				currentItem);
		if (cm != null)
			return cm.getFile();
		return null;
	}

	@Override
	public UUID getID() {
		return _id;
	}

	@Override
	public UUID[] getRequireGenerator() {
		return NO;
	}

	@Override
	public UUID[] getRequireCadse() {
		return NO;
	}

	@Override
	public void load(IRuntimeGenerator runtimeGenerator) {
	}

	@Override
	public void unload(IRuntimeGenerator runtimeGenerator) {
	}

}
