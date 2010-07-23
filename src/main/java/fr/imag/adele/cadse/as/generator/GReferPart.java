package fr.imag.adele.cadse.as.generator;

import fr.imag.adele.cadse.core.Item;
import fr.imag.adele.cadse.core.ItemType;

public class GReferPart extends GRefer {
	ItemType it;
	
	public GReferPart(ItemType it) {
		this.it = it;
	}
	
	public GReferPart(ItemType typeSource, ItemType typeParent) {
		this(typeParent);
		typeSource.addAdapter(this);
	}

	@Override
	public Item refer(Item currentItem) {
		return currentItem.getPartParent(it);
	}
}
