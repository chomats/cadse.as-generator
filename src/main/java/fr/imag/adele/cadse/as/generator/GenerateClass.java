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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.IType;

import fede.workspace.eclipse.java.manager.JavaFileContentManager;
import fr.imag.adele.cadse.core.GenContext;
import fr.imag.adele.cadse.core.Item;

/**
 * The Class GenerateClass.
 * 
 * @author <a href="http://cadse.imag.fr">cadse team</a>
 */
public class GenerateClass<S extends GenClassState> extends GGenFile<S> {

	public GenerateClass(GToken key) {
		super(GCst.t_class);
		_key = key;
		relatif(GCst.t_class).setAggregator(new GenerateClassAggregator());
	}

	@Override
	protected void init(S state, Item currentItem, GGenerator g,
			GenContext cxt) {
		super.init(state, currentItem, g, cxt);
		GenClassState gcs = (GenClassState) state;
		JavaFileContentManager jf = g.getJavaFileContentManager(getKey(), currentItem);
		if (jf != null) {
			String cn = jf.getClassName(cxt);
			String pn = jf.getPackageName(cxt);
			IType javatype = jf.getJavaType(cxt);
			gcs._packageName = pn;
			gcs._className = cn;
			gcs._type = javatype;
		}
	}
	
	@Override
	protected S createState() {
		return (S) new GenClassState();
	}

	class GenerateClassAggregator extends GAggregator {
		Map<GToken, GResult> _resultMap = new HashMap<GToken, GResult>();
		

		@Override
		public void beginAll(Item currentItem, GGenFile gf, GToken kind,
				GenContext context, GGenerator gGenerator) {
			_resultMap.clear();
		}

		@Override
		public void endAll(Item currentItem, GGenFile gf, GToken kind,
				GenContext context, GGenerator gGenerator) {

			GenClassState clState = (GenClassState) _state;
			GResult sb = _result;
			GResult license = _resultMap.get(GCst.t_license);
			if (license != null) {
				sb.append(license).append("\n");
			}
			
			sb.append("package ").append(clState._packageName).append(";");
			sb.newline();
			sb.newline();

			GResult rTemp;
			rTemp = _resultMap.get(GCst.t_import);
			if (rTemp != null)
				clState.getImports().addAll(Arrays.asList(rTemp.getLines()));
			String extendClassName = null;

			if (clState._implementsClassName != null) {
				for (int i = 0; i < clState._implementsClassName.length; i++) {
					String implClassName = clState._implementsClassName[i];
					String packageName = clState._implementsPackageName[i];
					if (packageName != null
							&& (clState._packageName == null || !clState._packageName
									.equals(packageName))) {
						clState.getImports().add(packageName + "." + implClassName);
					}
				}
			}
			if (clState._isClass) {
				extendClassName = clState._extendedClassName;
				rTemp = _resultMap.get(GCst.t_extends);
				if (rTemp != null && rTemp.getLines().length == 1) {
					extendClassName = rTemp.getLines()[0];
				}
				IAnnotation annotation = clState.getAnnotationOwGen();
				if (annotation != null) {
					
				}
				if (extendClassName != null) {
					if (!clState._packageName.equals(clState._extendedPackageName)) {
						clState.getImports().add(clState._extendedPackageName+"."+extendClassName);
					}
				}
				
			}

			for (String itf : clState.getImports()) {
				sb.newline().append("import ").append(itf).append(";");
			}

			sb.newline();
			sb.appendGeneratedTag();
			sb.newline().append("public ");
			if (clState._isClass) {
				sb.append("class ");
			} else {
				sb.append("interface ");
			}
			sb.append(clState._className);
			if (clState._isClass && extendClassName != null) {
				sb.append(" extends ").append(extendClassName);
			}

			String[] implementsClassName = clState.getImplementsClassName();
			if (implementsClassName != null && implementsClassName.length != 0) {
				if (clState._isClass) {
					sb.append(" implements ");
				} else {
					sb.append(" extends ");
				}
				for (String itf : implementsClassName) {
					sb.append(" ").append(itf).append(",");
				}
				sb.decrementLength();
			}
			sb.append(" {");

			sb.begin();
			sb.append(_resultMap.get(GCst.t_inner_class));
			sb.append(_resultMap.get(GCst.t_staticfield));
			sb.append(_resultMap.get(GCst.t_cstes));
			sb.append(_resultMap.get(GCst.t_field));
			sb.append(_resultMap.get(GCst.t_method_static));
			if (clState._isClass) {
				sb.append(_resultMap.get(GCst.t_constructor));
			}
			sb.append(_resultMap.get(GCst.t_method));

			sb.end();
			sb.newline();
			sb.newline().append("}");
			sb.newline();
		}

		@Override
		public void append(GResult result, GToken cToken) {
			_resultMap.put(cToken.abs(), result);
		}
	}
}
