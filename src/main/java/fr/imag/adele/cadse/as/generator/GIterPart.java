package fr.imag.adele.cadse.as.generator;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import fr.imag.adele.cadse.core.GenContext;
import fr.imag.adele.cadse.core.Item;
import fr.imag.adele.cadse.core.Link;

public class GIterPart extends GIter {

	
	protected LinkedList<Item> stack = new LinkedList<Item>();
	Item _next = null;
	private Iterator<Link> _linkIterator;
	
	

	@Override
	public Item next() {
		return _next;
	}
	
	@Override
	public boolean hasNext() {
		_next = null;
		while(true) {
			while(_linkIterator == null) {
				if (stack.isEmpty())
					return false;
				Item i = stack.remove(0);
				List<Link> links = i.getOutgoingLinks();
				if (links.isEmpty()) continue;
				_linkIterator = links.iterator();
				break;
			}
			while (_linkIterator.hasNext()) {
				Link link = _linkIterator.next();
				if (link != null && link.isLinkResolved() && link.getLinkType().isPart()) {
					_next = link.getDestination();
					if (_next != null) {
						stack.add(_next);
						return true;
					}
				}
			}
			_linkIterator = null;
		}
	}
	
	@Override
	public void beginAll(Item currentItem, GGenFile gf, GToken kind,
			GenContext context, GGenerator gGenerator) {
		stack.add(currentItem);
	}
	
	
}
