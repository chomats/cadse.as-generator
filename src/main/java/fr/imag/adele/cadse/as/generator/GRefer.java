package fr.imag.adele.cadse.as.generator;

import fr.imag.adele.cadse.core.Item;
import fr.imag.adele.cadse.core.ItemType;
import fr.imag.adele.cadse.core.transaction.delta.ImmutableItemDelta;
import fr.imag.adele.cadse.objectadapter.ObjectAdapter;

/**
 * Un type d'adapter permétant de configurer l'impact d'un changement pour le générateur.
 * Il est possible d'implementé {@link #refer(Item)} ou {@link #refers(Item)}.
 * Si le calcul dépend d'un delta ({@link ImmutableItemDelta}), il faut implémenter {@link #refers(Item, ImmutableItemDelta)}.
 * 
 * @author <a href="http://cadse.imag.fr">cadse team</a>
 *
 */
public abstract class GRefer extends ObjectAdapter<GRefer> {

	@Override
	public Class<GRefer> getClassAdapt() {
		return GRefer.class;
	}
	
	public GRefer(ItemType sourceType) {
		sourceType.addAdapter(this);
	}
	
	public GRefer() {
	}
	
	protected Item refer(Item currentItem) {
		return null;
	}
	
	public Item[] refers(Item currentItem) {
		Item r = refer(currentItem);
		if( r!= null)
			return new Item[]{r};
		return null;
	}
	
	public Item[] refers(Item currentItem, ImmutableItemDelta itemDelta){
		return refers(currentItem);
	}

}
