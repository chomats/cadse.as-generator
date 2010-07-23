package fr.imag.adele.cadse.as.generator;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import fr.imag.adele.cadse.core.GenContext;
import fr.imag.adele.cadse.core.Item;
import fr.imag.adele.cadse.objectadapter.ObjectAdapter;

public class GGenPartFile extends ObjectAdapter<GGenPartFile> implements GObject {
	
	protected GToken[] _tokens;
	private Set<GToken> _matchedToken = new HashSet<GToken>();
	private GGenFile _genfile;
	
	public GToken[] tokens() {
		return _tokens;
	}
	
	public void init() {	
	}
	
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
	
	public boolean match(GGenFile gf, GToken t) {
		return getMatchedToken().contains(t);
	}
	
	public Set<GToken> getMatchedToken() {
		return _matchedToken ;
	}

	public GGenFile getGenFile() {
		return _genfile;
	}

	@Override
	public GAggregator getAggregator() {
		return null;
	}
	
	public void setGenfile(GGenFile genfile) {
		_genfile = genfile;
	}
	
}
