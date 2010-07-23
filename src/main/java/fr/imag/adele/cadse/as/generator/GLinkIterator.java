package fr.imag.adele.cadse.as.generator;

import java.util.ArrayList;
import java.util.Iterator;

import fr.imag.adele.cadse.core.GenContext;
import fr.imag.adele.cadse.core.Item;
import fr.imag.adele.cadse.core.Link;
import fr.imag.adele.cadse.core.LinkType;

public class GLinkIterator extends GIter {
	Item _next = null;
	private Iterator<Link> _linkIterator;
	
	public GLinkIterator(Item currentItem, LinkType linkID) {
		ArrayList<Link> arrayList = new ArrayList<Link>(currentItem.getOutgoingLinks(linkID));
		_linkIterator = arrayList.iterator();
	}
	
	public GLinkIterator() {
	}

	@Override
	public Item next() {
		return _next;
	}
	
	@Override
	public void beginAll(Item currentItem, GGenFile gf, GToken kind,
			GenContext context, GGenerator gGenerator) {
		if (_linkIterator != null)
			return;
		ArrayList<Link> arrayList = new ArrayList<Link>(currentItem.getOutgoingLinks());
		_linkIterator = arrayList.iterator();
	}
	
	@Override
	public boolean hasNext() {
		_next = null;
		while (_linkIterator.hasNext()) {
			Link link = (Link) _linkIterator.next();
			if (link != null && link.isLinkResolved()) {
				_next = link.getDestination();
				break;
			}
		}
		return _next != null;
	}
}
