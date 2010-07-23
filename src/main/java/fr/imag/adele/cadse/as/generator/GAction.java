package fr.imag.adele.cadse.as.generator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;

import fede.workspace.eclipse.composition.java.EclipsePluginContentManger;
import fede.workspace.tool.eclipse.MappingManager;
import fr.imag.adele.cadse.as.generator.GGenFile;
import fr.imag.adele.cadse.as.generator.GGenerator;
import fr.imag.adele.cadse.as.generator.GToken;
import fr.imag.adele.cadse.core.GenContext;
import fr.imag.adele.cadse.core.Item;
import fr.imag.adele.cadse.objectadapter.ObjectAdapter;
import fr.imag.adele.fede.workspace.si.view.View;

public class GAction extends ObjectAdapter<GAction> {

	@Override
	public Class<GAction> getClassAdapt() {
		return GAction.class;
	}

	public void generate(GGenerator g, Item currentItem, GenContext cxt) throws CoreException {
		GGenFile[] gf = currentItem.adapts(GGenFile.class);
		if (gf == null)
			return;
		for (GGenFile gGenFile : gf) {
			generate(gGenFile, currentItem, cxt);
		}
	}
	
	public void generate(Item currentItem, GenContext cxt) throws CoreException {
		GGenFile[] gf = currentItem.adapts(GGenFile.class);
		if (gf == null)
			return;
		for (GGenFile gGenFile : gf) {
			generate(gGenFile, currentItem, cxt);
		}
	}
	
	public void generate(GGenFile gf, Item currentItem, GenContext cxt) throws CoreException {
		if (gf == null)
			return;
		GGenerator g = gf.getGenerator();
		if (g == null)
			return;
		generate(g, currentItem, cxt, gf, gf.getKey());
	}

	
	private void generate(GGenerator g, Item currentItem, GenContext cxt, 
			GGenFile gFile, GToken fileToken)
			throws CoreException {
	
		IFile file = g.getFile(currentItem, fileToken, cxt);
		String content = gFile.generate(g, currentItem, cxt);
		try {
			if ("java".equals(file.getFileExtension()))
				EclipsePluginContentManger.generateJava(file, content, View.getDefaultMonitor());
			else
				MappingManager.generate(file.getProject(), file.getParent().getProjectRelativePath(), file.getName(), content, View.getDefaultMonitor());
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
	

	
}
