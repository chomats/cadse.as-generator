package fr.imag.adele.cadse.as.generator;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import fr.imag.adele.cadse.core.GenContext;
import fr.imag.adele.cadse.core.Item;
import fr.imag.adele.cadse.objectadapter.ObjectAdapter;

/**
 * Permet de participer à la génération d'un ensemble de token relatif ou absolue.
 * 
 * @author <a href="http://cadse.imag.fr">cadse team</a>
 *
 */
public class GGenPartFile extends ObjectAdapter<GGenPartFile> implements GObject {
	
	protected GToken[] _tokens;
	private Set<GToken> _matchedToken = new HashSet<GToken>();
	private GGenFile _genfile;
	
	public GToken[] tokens() {
		return _tokens;
	}
	
	/**
	 * Permet de configurer l'instance en ajoutant les tokens à matcher
	 * @param tokens liste des token pour lesquels ce bout de générateur souhaite participer.
	 * @return l'instance
	 */
	public GGenPartFile matchedToken(GToken ...tokens) {
		_matchedToken.addAll(Arrays.asList(tokens));
		return this;
	}
	
	
	
	public void generatePartFile(GResult r, Item currentItem, 
			GGenFile gf, GToken kind, GenContext context, GGenerator gGenerator, GenState state) {
	}

	@Override
	public Class<GGenPartFile> getClassAdapt() {
		return GGenPartFile.class;
	}
	
	/**
	 * Est-ce que je souhaite participer ?
	 * @param gf current type of generated file
	 * @param t current token
	 * @param currentItem current item
	 * @return true if must participate to the generating process.
	 */
	public boolean match(GGenFile gf, GToken t, Item currentItem) {
		return getMatchedToken().contains(t);
	}
	
	/**
	 * List of matched tokens
	 * @return list of matched tokens
	 */
	public Set<GToken> getMatchedToken() {
		return _matchedToken ;
	}

	/** 
	 * The associated type of generated file or null if not set. 
	 * You can associated type generated file with method setGentFile.
	 * 
	 * @return associated type of generated file or null if not set.
	 */
	public GGenFile getGenFile() {
		return _genfile;
	}

	/**
	 * Not used.
	 */
	@Override
	public GAggregator getAggregator() {
		return null;
	}
	
	/**
	 * 
	 * @param genfile
	 */
	public void setGenfile(GGenFile genfile) {
		_genfile = genfile;
	}
	
}
