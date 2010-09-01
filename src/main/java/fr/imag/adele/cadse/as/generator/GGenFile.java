package fr.imag.adele.cadse.as.generator;

import java.util.HashMap;
import java.util.Map;

import fr.imag.adele.cadse.core.GenContext;
import fr.imag.adele.cadse.core.Item;
import fr.imag.adele.cadse.objectadapter.ObjectAdapter;
import fr.imag.adele.cadse.util.ArraysUtil;

public class  GGenFile<S extends GenState> extends ObjectAdapter<GGenFile> implements GObject {
	protected GToken _key;

	protected GToken[] _tokens;

	private GAggregator _aggregator;
	private Map<GToken, GToken> _relatifTokens = new HashMap<GToken, GToken>();

	private Map<GToken, GGenPartFile[]> _participants = null;

	private GGenerator _generator;

	public void addParticipant(GGenPartFile g) {
		if (_participants == null)
			_participants = new HashMap<GToken, GGenPartFile[]>();
		for (GToken t : g.getMatchedToken()) {
			GGenPartFile[] a = _participants.get(t);
			a = ArraysUtil.add(GGenPartFile.class, a, g);
			_participants.put(t, a);
		}
	}

	public GGenFile(GToken... tokens) {
		_tokens = new GToken[tokens.length];
		for (int i = 0; i < tokens.length; i++) {
			_tokens[i] = tokens[i].relatif(this, _relatifTokens);
		}
	}

	public GGenFile participant(GGenPartFile... gGenPartFiles) {
		for (GGenPartFile gGenPartFile : gGenPartFiles) {
			addParticipant(gGenPartFile);
		}
		return this;
	}

	public GToken getKey() {
		return _key;
	}

	public GToken[] tokens() {
		return _tokens;
	}

	
	public GToken relatif(GToken t) {
		return _relatifTokens.get(t);
	}

	@Override
	public Class<GGenFile> getClassAdapt() {
		return GGenFile.class;
	}

	/**
	 * 
	 * @param g le genérateur associé
	 * @param currentItem l'item courrant
	 * @param cxt le context
	 * @return le contenu du fichier ou null si déjà sauvé.
	 */
	public String generate(GGenerator g, Item currentItem, GenContext cxt) {
		S state = createState();
		init(state, currentItem, g, cxt);
		GResult r = g.generate(currentItem, this, cxt, state);
		return r.toString();
	}

	protected void init(S state, Item currentItem, GGenerator g, GenContext cxt) {		
	}

	public GToken[] getTokens() {
		return _tokens;
	}

	@Override
	public GAggregator getAggregator() {
		return _aggregator;
	}

	public void setAggregator(GAggregator aggregator) {
		_aggregator = aggregator;
	}

	public boolean match(GToken kind) {
		GToken s;
		return (_participants != null && ((_participants.get(kind) != null) || ((s = kind
				.getSuperToken()) != null && _participants.get(s) != null)));
	}

	public void generatePartFile(GResult g, Item currentItem, GGenFile gf,
			GToken kind, GenContext context, GGenerator gGenerator, GenState state) {
		if (_participants != null) {
			GGenPartFile[] p = _participants.get(kind);
			generatePartFile(g, currentItem, gf, kind, context, gGenerator, p, state);
			if (kind.getSuperToken() != null)
				generatePartFile(g, currentItem, gf, kind, context, gGenerator,
						_participants.get(kind.getSuperToken()), state);
		}
	}

	private void generatePartFile(GResult g, Item currentItem, GGenFile gf,
			GToken kind, GenContext context, GGenerator gGenerator,
			GGenPartFile[] p, GenState state) {
		if (p != null)
			for (GGenPartFile gGenPartFile : p) {
				try {
					gGenPartFile.generatePartFile(g, currentItem, gf, kind,
							context, gGenerator, state);
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
	}
	
	protected S createState() {
		return (S) new GenState();
		
	}

	public GGenerator getGenerator() {
		return _generator;
	}
	
	public void setGenerator(GGenerator generator) {
		_generator = generator;
	}
}
