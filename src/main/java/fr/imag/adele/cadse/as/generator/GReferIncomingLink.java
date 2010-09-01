package fr.imag.adele.cadse.as.generator;

import java.util.Collection;

import fr.imag.adele.cadse.core.Item;
import fr.imag.adele.cadse.core.ItemType;
import fr.imag.adele.cadse.core.LinkType;

/**
 * Toutes les sources des liens incomings de type lt sont changé.
 * 
 * @author <a href="http://cadse.imag.fr">cadse team</a>
 *
 */
public class GReferIncomingLink extends GRefer {

	/**
	 * type du lien recherché
	 */
	LinkType lt;

	/**
	 * 
	 * @param typeSource type source à attacher l'adapter
	 * @param lt type du lien recherché
	 */
	public GReferIncomingLink(ItemType typeSource, LinkType lt) {
		super(typeSource);
		this.lt = lt;
	}
	
	@Override
	public Item[] refers(Item currentItem) {
		Collection<Item> items = currentItem.getIncomingItems(lt);
		return (Item[]) items.toArray(new Item[items.size()]);
	}
}
