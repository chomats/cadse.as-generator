package fr.imag.adele.cadse.as.generator;

import java.util.UUID;

public interface IRuntimeGenerator {

	IGenerator get(UUID id);
	
	IGenerator[] getGenerators();
}
