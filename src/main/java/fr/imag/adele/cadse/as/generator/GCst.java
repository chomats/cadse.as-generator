package fr.imag.adele.cadse.as.generator;


public class GCst {
	final static public GToken  t_class = new GToken("class");
	final static public GToken  t_license = new GToken(t_class, "license");
	
	final static public GToken  t_packagename = new GToken(t_class, "packagename");
	final static public GToken  t_import = new GToken(t_class, "import");
	final static public GToken  t_classname = new GToken(t_class, "classname");
	final static public GToken  t_extends = new GToken(t_class, "extends");
	final static public GToken  t_implements = new GToken(t_class, "implements");
	final static public GToken  t_inner_class = new GToken(t_class, "inner-class");
	
	final static public GToken  t_cstes = new GToken(t_class, "cstes");
	final static public GToken  t_staticfield = new GToken(t_class, "staticfield");
	
	final static public GToken  t_field = new GToken(t_class, "field");
	final static public GToken  t_method_static = new GToken(t_class, "method_static");
	final static public GToken  t_constructor = new GToken(t_class, "constructor");
	final static public GToken  t_method = new GToken(t_class, "method");
	
	final static public GToken  t_project = new GToken("project");
	final static public GToken  t_srcpath = new GToken("srcpath");
	
	
}
