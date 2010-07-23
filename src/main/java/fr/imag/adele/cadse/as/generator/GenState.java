package fr.imag.adele.cadse.as.generator;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class GenState {
	private Set<String> _imports = null;

	public void addImports(String i) {
		if (_imports == null)
			_imports = new TreeSet<String>();
		_imports .add(i);
	}
	
	public Set<String> getImports() {
		if (_imports == null)
			_imports = new HashSet<String>();
		
		return _imports;
	}
}
