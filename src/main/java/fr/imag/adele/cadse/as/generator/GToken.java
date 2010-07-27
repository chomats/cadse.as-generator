package fr.imag.adele.cadse.as.generator;

import java.util.Map;

import fr.imag.adele.cadse.util.ArraysUtil;

public class GToken implements GObject {
	GObject _owner;
	GToken _superToken;
	String _name;
	GToken[] _children;
	GToken _parent;
	private GAggregator _aggregator;
	int _max = -1;
	private Class<? extends ItemIterable> _iterClass;
	
	public GToken(GObject owner, String name) {
		this(null, owner, name, true);
	}
	
	public GToken(GToken superToken, GObject owner, String name) {
		this(superToken, owner, name, true);
	}
	
	public GToken(GToken superToken, GObject owner, String name, boolean add) {
		_name = name;
		_owner = owner;
		_superToken = superToken;
		if(!add) return;
		
		if (owner instanceof GToken) {
			GToken tonwer = (GToken) owner;
			tonwer._children = ArraysUtil.add(GToken.class, tonwer._children, this);
		} else if (owner instanceof GGenFile) {
			GGenFile tonwer = (GGenFile) owner;
			tonwer._tokens = ArraysUtil.add(GToken.class, tonwer._tokens, this);
		} else if (owner instanceof GGenPartFile) {
			GGenPartFile tonwer = (GGenPartFile) owner;
			tonwer._tokens = ArraysUtil.add(GToken.class, tonwer._tokens, this);
		} else if (owner instanceof GGenerator) {
			GGenerator tonwer = (GGenerator) owner;
			tonwer._tokens = ArraysUtil.add(GToken.class, tonwer._tokens, this);
		}
	}
	
	
	
	public GToken(String name) {
		_name = name;
		_owner = null;
	}
	
	
	
	
	public void setParent(GToken parent) {
		this._parent = parent;
	}
	
	public GToken getParent() {
		return _parent;
	}
	
	public GToken[] getChildren() {
		return _children;
	}
	
	public GObject getOwner() {
		return _owner;
	}
	
	public String getName() {
		return _name;
	}
	
	public GToken getSuperToken() {
		return _superToken;
	}

	@Override
	public GAggregator getAggregator() {
		return _aggregator;
	}
	
	public GToken setAggregator(GAggregator aggregator) {
		_aggregator = aggregator;
		return this;
	}
	
	public boolean equals(GToken obj) {
		return this == obj || _superToken == obj;
	}
	
	public GToken setMax(int max) {
		_max = max;
		return this;
	}
	
	public GToken setIterClass(Class<? extends ItemIterable> iterClass) {
		_iterClass = iterClass;
		return this;
	}
	
	public int getMax() {
		return _max;
	}

	public GToken relatif(GObject gObj, Map<GToken, GToken> relatifTokens) {
		GToken r = new GToken(this, gObj, _name, false).setMax(_max).setAggregator(_aggregator);
		relatifTokens.put(this, r);
		if (_children != null) {
			r._children = new GToken[_children.length];
			for (int i = 0; i < _children.length; i++) {
				r._children[i] = _children[i].relatif(r, relatifTokens);
			}
		}
		return r;
	}

	/*
	 * Return la representation static (non relatif ˆ un genfile) ou un part ...
	 * Relatif si aucune repsenstation static
	 * Jamais null
	 */
	public GToken abs() {
		return _superToken == null ? this : _superToken;
	}

	public ItemIterable getIter() {
		if (_iterClass != null)
			try {
				return _iterClass.newInstance();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return new ItemPartIterable();
	}

	@Override
	public String toString() {
		return "<"+_name+">";
	}
}