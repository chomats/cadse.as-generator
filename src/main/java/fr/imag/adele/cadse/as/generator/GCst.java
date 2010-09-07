/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 * Copyright (C) 2006-2010 Adele Team/LIG/Grenoble University, France
 */
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
	
	final static public GToken mf_import_package = new GToken("import-package");
	final static public GToken mf_export_package = new GToken("export-package");
	final static public GToken mf_require_bundle = new GToken("require-bundle");

	
	
	
}
