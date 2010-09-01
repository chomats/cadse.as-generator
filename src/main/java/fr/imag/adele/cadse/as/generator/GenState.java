package fr.imag.adele.cadse.as.generator;

import java.util.Set;
import java.util.TreeSet;
/**
 * @author <a href="http://cadse.imag.fr">cadse team</a>
 *
 */
public class GenState {
	private Set<String> _imports = null;

	public void addImports(String i) {
		if (_imports == null)
			_imports = new TreeSet<String>();
		_imports .add(i);
	}
	
	public Set<String> getImports() {
		if (_imports == null)
			_imports = new TreeSet<String>();
		
		return _imports;
	}
}
