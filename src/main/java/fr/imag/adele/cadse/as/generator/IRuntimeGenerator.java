package fr.imag.adele.cadse.as.generator;

import java.util.UUID;

import fr.imag.adele.cadse.core.Item;

public interface IRuntimeGenerator {

	IGenerator get(UUID id);
	
	IGenerator[] getGenerators();
	
	void generate(Item item);
	
	void generate(Item item, GGenFile<?> file);
}
