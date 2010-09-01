package fr.imag.adele.cadse.as.generator;

import java.util.Map;

import fr.imag.adele.cadse.core.iter.ItemIterable;
import fr.imag.adele.cadse.core.iter.ItemPartIterable;
import fr.imag.adele.cadse.util.ArraysUtil;

/**
 * Un token est un point d'entree dans un générateur. Un token peut avoir des fils et un parent.
 * Il peut etre absolue ou relatif à un GGenFile.
 * 
 * @author <a href="http://cadse.imag.fr">cadse team</a>
 *
 */
public class GToken implements GObject {
	GObject _owner;
	GToken _superToken;
	String _name;
	GToken[] _children;
	GToken _parent;
	private GAggregator _aggregator;
	int _max = -1;
	private Class<? extends ItemIterable> _iterClass;
	
	/**
	 * Creer un nouveau token static ou relatif. This Token will be added à son propriétaire (if owner is not null)
	 * @param owner son propriétaire null si static.
	 * @param name son nom
	 */
	public GToken(GObject owner, String name) {
		this(null, owner, name, true);
	}
	
	/**
	 * Creer un nouveau token static ou relatif. This Token will be added à son propriétaire (if owner is not null
	 * @param superToken son le token absolut dont le token dependens. peut etre null
	 * @param owner son propriértaire peut etre null
	 * @param name son nom
	 */
	public GToken(GToken superToken, GObject owner, String name) {
		this(superToken, owner, name, true);
	}
	
	/**
	 * Creer un nouveau token static ou relatif
	 * @param superToken null or le token absolu a partir duquel ce token relatif est creer.
	 * @param owner son propriértaire peut etre null
	 * @param name son nom
	 * @param add true if this token must be added à son propriétaire.
	 */
	private GToken(GToken superToken, GObject owner, String name, boolean add) {
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
	
	
	/**
	 *  * @param name son nom
	 */
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
	
	/**
	 * set le nombre maximun de participation.
	 * @param max le nombre maximun de participation. (valeur de l'infinie est -1).
	 * @return this
	 */
	public GToken setMax(int max) {
		_max = max;
		return this;
	}
	
	/**
	 * Set la class d'iteration.
	 * @param iterClass
	 * @return this.
	 */
	public GToken setIterClass(Class<? extends ItemIterable> iterClass) {
		_iterClass = iterClass;
		return this;
	}
	
	/**
	 * Le nombre maximun de participation pour ce token. La valeur par défaut est l'infinie (-1).
	 * @return le nombre maximun de participation.
	 */
	public int getMax() {
		return _max;
	}

	/**
	 * Creer les token relatifs recurcivement en mettant à jour la map de reference (static -> relatif).
	 * @param gObj un token static
	 * @param relatifTokens la map de tokens (static -> relatif)
	 * @return le token relatif.
	 */
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

	/**
	 * Return la representation static (non relatif à un genfile ou un part ...)
	 * Relatif si aucune représentation static est trouvé.
	 * Le resultat n'est jamais null.
	 * @return this if {@link #_superToken} == null sinon {@link #_superToken}.
	 */
	public GToken abs() {
		return _superToken == null ? this : _superToken;
	}

	/**
	 * @return l'iterateur devant parcourir les items. Il peut etre défini via {@link #setIterClass(Class)}.
	 * Par défaut l'implementaton {@link ItemPartIterable}.
	 */
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
		if (_superToken != null) {
			return _superToken.getIter();
		}
		return new ItemPartIterable();
	}

	@Override
	public String toString() {
		return "<"+_name+">";
	}
}