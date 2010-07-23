package fr.imag.adele.cadse.as.generator;

import java.util.Collection;

import fr.imag.adele.cadse.core.Item;
import fr.imag.adele.cadse.core.ItemType;
import fr.imag.adele.cadse.core.LinkType;

public class GReferIncomingLink extends GRefer {

	LinkType lt;

	public GReferIncomingLink(ItemType typeSource, LinkType lt) {
		super();
		this.lt = lt;
		typeSource.addAdapter(this);
	}
	
	@Override
	public Item[] refers(Item currentItem) {
		Collection<Item> items = currentItem.getIncomingItems(lt);
		return (Item[]) items.toArray(new Item[items.size()]);
	}
}
