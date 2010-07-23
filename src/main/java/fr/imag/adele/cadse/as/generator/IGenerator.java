package fr.imag.adele.cadse.as.generator;

import java.util.UUID;

public interface IGenerator {

	UUID getID();
	
	UUID[] getRequireGenerator();
	
	UUID[] getRequireCadse();
	
	public void load(IRuntimeGenerator runtimeGenerator);
	
	public void unload(IRuntimeGenerator runtimeGenerator);

	
}

