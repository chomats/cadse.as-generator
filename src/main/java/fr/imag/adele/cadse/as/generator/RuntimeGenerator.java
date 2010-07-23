package fr.imag.adele.cadse.as.generator;

import java.util.UUID;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.*;

import fr.imag.adele.cadse.core.impl.CadseCore;

@Component(name = "fr.imag.adele.cadse.runtimeGenerator", immediate = true, architecture = true)
@Provides(specifications = { IRuntimeGenerator.class })
@Instantiate(name="fr.imag.adele.cadse.runtimeGenerator.instance")
public class RuntimeGenerator implements IRuntimeGenerator, Runnable {
	GListener generatorListener = null;
	private final static int DELAY = 10000;

	@Requires
	IGenerator[] _generators;
	private boolean end;

	public RuntimeGenerator() {
	}

	@Override
	public IGenerator get(UUID id) {
		if (_generators != null)
			for (IGenerator g : _generators) {
				if (g.getID().equals(id))
					return g;
			}
		return null;
	}

	@Override
	public IGenerator[] getGenerators() {
		return _generators;
	}

	@Validate
	public void starting() {
		Thread T = new Thread(this);
		end = false;
		T.start();
	}

	@Invalidate
	public void stopping() {
		end = true;
		if (_generators != null) {
			for (IGenerator g : _generators) {
				g.unload(this);
			}
		}
	}

	@Override
	public void run() {
		while (!CadseCore.isCadseRuntimeRunning())
			try {
				wait(10);
				if (end)
					return;
			} catch (InterruptedException e) {
				return;
			}
		if (_generators != null) {
			for (IGenerator g : _generators) {
				g.load(this);
			}
		}
		generatorListener = new GListener();
	}
}
