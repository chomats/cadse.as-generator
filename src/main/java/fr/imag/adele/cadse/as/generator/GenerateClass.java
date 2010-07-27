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
 * @author <a href="mailto:stephane.chomat@imag.fr">Stephane Chomat</a>
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
			gcs.fClassName = cn;
			gcs.type = javatype;
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

			if (clState.fImplementsClassName != null) {
				for (int i = 0; i < clState.fImplementsClassName.length; i++) {
					String implClassName = clState.fImplementsClassName[i];
					String packageName = clState.fImplementsPackageName[i];
					if (packageName != null
							&& (clState._packageName == null || !clState._packageName
									.equals(packageName))) {
						clState.getImports().add(packageName + "." + implClassName);
					}
				}
			}
			if (clState.isClass) {
				extendClassName = clState.fExtendedClassName;
				rTemp = _resultMap.get(GCst.t_extends);
				if (rTemp != null && rTemp.getLines().length == 1) {
					extendClassName = rTemp.getLines()[0];
				}
				IAnnotation annotation = clState.getAnnotationOwGen();
				if (annotation != null) {
					
				}
				if (extendClassName != null) {
					if (!clState._packageName.equals(clState.fExtendedPackageName)) {
						clState.getImports().add(clState.fExtendedPackageName+"."+extendClassName);
					}
				}
				
			}

			for (String itf : clState.getImports()) {
				sb.newline().append("import ").append(itf).append(";");
			}

			sb.newline();
			sb.appendGeneratedTag();
			sb.newline().append("public ");
			if (clState.isClass) {
				sb.append("class ");
			} else {
				sb.append("interface ");
			}
			sb.append(clState.fClassName);
			if (clState.isClass && extendClassName != null) {
				sb.append(" extends ").append(extendClassName);
			}

			String[] implementsClassName = clState.getImplementsClassName();
			if (implementsClassName != null && implementsClassName.length != 0) {
				if (clState.isClass) {
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
			if (clState.isClass) {
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

	// /**
	// * Compute imports package.
	// *
	// * @param imports
	// * the imports
	// */
	// public void computeImportsPackage(Set<String> imports) {
	// String className = getExtendClassName();
	// if (className != null) {
	// String packageName = JavaIdentifier.packageName(className);
	// imports.add(packageName);
	// }
	// }

	// /**
	// * Gets the content.
	// *
	// * @return the content
	// */
	// public String getContent() {
	// Object generator;
	// GResult sb = new GResult(generator);
	//
	// GenContext context = new GenContext(null);
	// generateClass(sb, imports, context);
	//
	// GResult sb2 = new GResult();
	// String license = getLicense();
	// if (license != null) {
	// sb.append(license).append("\n");
	// }
	// sb2.append("package ").append(_packageName).append(";");
	// sb2.newline();
	// sb2.newline();
	// for (String itf : imports) {
	// sb2.newline().append("import ").append(itf).append(";");
	// }
	// sb2.newline();
	// sb2.newline();
	// sb2.append(sb.toString());
	//
	// sb2.newline();
	//
	// return sb2.toString();
	//
	// }

}