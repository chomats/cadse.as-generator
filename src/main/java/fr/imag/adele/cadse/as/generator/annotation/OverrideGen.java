package fr.imag.adele.cadse.as.generator.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface OverrideGen {

	@interface Element{
		KindGen kind();
		
		String qName();
		
		String removedItfName() default "";
	}
	
	Element[] value();
	
}
