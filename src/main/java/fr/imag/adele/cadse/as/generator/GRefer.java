package fr.imag.adele.cadse.as.generator;

import fr.imag.adele.cadse.core.Item;
import fr.imag.adele.cadse.core.ItemType;
import fr.imag.adele.cadse.core.transaction.delta.ImmutableItemDelta;
import fr.imag.adele.cadse.objectadapter.ObjectAdapter;

public class GRefer extends ObjectAdapter<GRefer> {

	@Override
	public Class<GRefer> getClassAdapt() {
		return GRefer.class;
	}
	
	public GRefer(ItemType sourceType) {
		sourceType.addAdapter(this);
	}
	
	public GRefer() {
	}
	
	public Item refer(Item currentItem) {
		return null;
	}
	
	public Item[] refers(Item currentItem) {
		Item r = refer(currentItem);
		if( r!= null)
			return new Item[]{r};
		return null;
	}
	
	public Item[] refers(Item currentItem, ImmutableItemDelta itemDelta){
		return null;
	}

}
