package fr.imag.adele.cadse.as.generator;

import fr.imag.adele.cadse.core.Item;
import fr.imag.adele.cadse.core.ItemType;

/**
 * Un item a été changé alors un parent de type <code>it</code> est changé.
 * 
 * @author <a href="http://cadse.imag.fr">cadse team</a>
 *
 */
public class GReferPart extends GRefer {
	ItemType it;
	
	/**
	 * 
	 * @param it le type parent souhaité.
	 */
	public GReferPart(ItemType it) {
		this.it = it;
	}
	
	public GReferPart(ItemType typeSource, ItemType typeParent) {
		this(typeParent);
		typeSource.addAdapter(this);
	}

	@Override
	protected Item refer(Item currentItem) {
		return currentItem.getPartParent(it);
	}
}
