package fr.imag.adele.cadse.as.generator;

import java.util.Iterator;

import fr.imag.adele.cadse.core.GenContext;
import fr.imag.adele.cadse.core.Item;
import fr.imag.adele.cadse.objectadapter.ObjectAdapter;

public class GIter extends ObjectAdapter<GIter> implements Iterable<Item>, Iterator<Item> {

	
	public void beginAll(Item currentItem, GGenFile gf, GToken kind,
			GenContext context, GGenerator gGenerator) {
		
	}

	
	public void endAll(Item currentItem, GGenFile gf, GToken kind,
			GenContext context, GGenerator gGenerator) {
		
	}

	@Override
	public Iterator<Item> iterator() {
		return this;
	}

	@Override
	public boolean hasNext() {
		return false;
	}

	@Override
	public Item next() {
		return null;
	}

	@Override
	public void remove() {
		
	}

	@Override
	public Class<GIter> getClassAdapt() {
		return GIter.class;
	}

}
