package fr.imag.adele.cadse.as.generator;

import java.util.HashSet;

import org.eclipse.core.runtime.CoreException;

import fr.imag.adele.cadse.core.GenContext;
import fr.imag.adele.cadse.core.Item;
import fr.imag.adele.cadse.core.ItemState;
import fr.imag.adele.cadse.core.ItemType;
import fr.imag.adele.cadse.core.WorkspaceListener;
import fr.imag.adele.cadse.core.impl.CadseCore;
import fr.imag.adele.cadse.core.transaction.delta.ImmutableItemDelta;
import fr.imag.adele.cadse.core.transaction.delta.ImmutableWorkspaceDelta;

public class GListener extends WorkspaceListener {
	GAction ga = new GAction();
	
	public GListener() {
		setKind(ListenerKind.BUILD);
		CadseCore.getLogicalWorkspace().addListener(this, 0xFFFFF);
	}

	
	
	@Override
	public void workspaceChanged(ImmutableWorkspaceDelta wd) {
		HashSet<Item> itemsToGenerate = new HashSet<Item>();
		
		for (ImmutableItemDelta itemDelta : wd.getItems()) {
			Item item = itemDelta.getItem();
			if (item == null || item.getState() == ItemState.DELETED)
				continue;
		
			computeToGenerate(itemsToGenerate, item);
			ItemType it = item.getType();
			
			GRefer[] refers = it.adapts(GRefer.class);
			if (refers != null) {
				for (GRefer gRefer : refers) {
					Item[] itemsToCompute = gRefer.refers(item, itemDelta);
					if (itemsToCompute == null) continue;
					for (Item itemToC : itemsToCompute) {
						computeToGenerate(itemsToGenerate, itemToC);	
					}
				}
			}
			
		}

		for (Item currentItem : itemsToGenerate) {
			GGenFile[] gf = currentItem.getType().adapts(GGenFile.class);
			if (gf != null) {
				for (GGenFile gGenFile : gf) {
					GenContext cxt = new GenContext(null);
					try {
						ga.generate(gGenFile, currentItem, cxt );
					} catch (CoreException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	private void addToGenerate(HashSet<Item> toRegenerate, Item item) {
		if (item != null && item.getState() != ItemState.DELETED) {
			toRegenerate.add(item);
		}
	}

	/**
	 * true find item to generate and stop cycle
	 * @param toRegenerate set of item to regenerate
	 * @param item the current item
	 * @return true if stop analyse
	 */
	void computeToGenerate(HashSet<Item> toRegenerate, Item item) {
		if (item == null) {
			return;
		}
		if (toRegenerate.contains(item))
			return;
		
		addToGenerate(toRegenerate, item);
		
		ItemType it = item.getType();
		
		GRefer[] refers = it.adapts(GRefer.class);
		if (refers != null) {
			for (GRefer gRefer : refers) {
				Item[] itemsToCompute = gRefer.refers(item);
				if (itemsToCompute == null) continue;
				for (Item itemToC : itemsToCompute) {
					computeToGenerate(toRegenerate, itemToC);	
				}
			}
		}
		
		while (item.getPartParent() != null) {
			item = item.getPartParent();
			computeToGenerate(toRegenerate, item);
		}
	}

}
