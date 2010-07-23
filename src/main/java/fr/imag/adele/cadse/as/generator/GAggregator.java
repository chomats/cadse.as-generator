package fr.imag.adele.cadse.as.generator;

import fr.imag.adele.cadse.core.GenContext;
import fr.imag.adele.cadse.core.Item;

public class GAggregator {

	protected GResult _result;

	protected GenState _state;
	
	public void beginAll(Item currentItem, GGenFile gf, GToken kind,
			GenContext context, GGenerator gGenerator) {
	}

	public void endAll(Item currentItem, GGenFile gf, GToken kind,
			GenContext context, GGenerator gGenerator) {
	}

	public void append(GResult result) {
		_result.appendLines(result.lines);
	}

	public GResult setResult(GGenerator gGenerator, Item currentItem,
			GGenFile gf, GToken kind, GenContext context) {
		_result = new GResult(gGenerator, currentItem, gf, kind, context);
		return _result;
	}
	
	public GResult getResult() {
		return _result;
	}

	public void append(GResult result, GToken cToken) {
		_result.appendLines(result.lines);
	}

	public void setState(GenState state) {
		_state = state;
	}
}
