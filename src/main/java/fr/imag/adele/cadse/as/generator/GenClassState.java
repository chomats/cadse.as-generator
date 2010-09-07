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
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

import fede.workspace.eclipse.java.JavaIdentifier;
import fr.imag.adele.cadse.as.generator.annotation.OverrideGen;
import fr.imag.adele.cadse.core.var.ContextVariable;
import fr.imag.adele.cadse.util.ArraysUtil;

public class GenClassState extends GenState {
	/** The package name. */
	public String _packageName;

	/** The class name. */
	public String _className;

	/** The extended class name. */
	public String _extendedClassName;

	/** The extended package name. */
	public String _extendedPackageName;

	/** The generate class. */
	public boolean _generateClass;

	/** The implements class name. */
	public String[] _implementsClassName = null;

	/** The implements package name. */
	public String[] _implementsPackageName;

	/** The is class. */
	public boolean _isClass = true;

	/** The can overwrite etends class. */
	public boolean _canOverwriteEtendsClass = false;

	/** The type. */
	public IType _type;

	/** The cxt. */
	public ContextVariable cxt;

	public void init() {
		if (_extendedClassName != null) {
			String[] p = JavaIdentifier
					.getPackageAndClassName(_extendedClassName);
			if (p[0] != null) {
				_extendedClassName = p[1];
				_extendedPackageName = p[0];
			}
		}
		if (_implementsClassName != null) {
			_implementsPackageName = new String[_implementsClassName.length];
			for (int i = 0; i < _implementsClassName.length; i++) {
				String s = _implementsClassName[i];
				String[] p = JavaIdentifier.getPackageAndClassName(s);
				if (p[0] != null) {
					_implementsClassName[i] = p[1];
					_implementsPackageName[i] = p[0];
				}
			}
		}
	}

	public void addImplements(String... qualifiedInterfaces) {
		if (qualifiedInterfaces != null) {
			String[] packageName = new String[qualifiedInterfaces.length];
			String[] className = new String[qualifiedInterfaces.length];
			for (int i = 0; i < qualifiedInterfaces.length; i++) {
				String s = qualifiedInterfaces[i];
				String[] p = JavaIdentifier.getPackageAndClassName(s);
				if (p[0] != null) {
					className[i] = p[1];
					packageName[i] = p[0];
				}
			}
			_implementsClassName = ArraysUtil.addList(String.class,
					_implementsClassName, className);
			_implementsPackageName = ArraysUtil.addList(String.class,
					_implementsPackageName, className);
		}
	}

	/**
	 * Gets the implements class name.
	 * 
	 * @return the implements class name
	 */
	public String[] getImplementsClassName() {
		HashSet<String> ret = new HashSet<String>();
		if (_implementsClassName != null) {
			ret.addAll(Arrays.asList(_implementsClassName));
		}

		try {
			if (_type != null && _type.exists()) {
				String[] itfs;
				itfs = _type.getSuperInterfaceNames();
				ret.addAll(Arrays.asList(itfs));
			}
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ret.toArray(new String[ret.size()]);
	}

	/**
	 * Gets the class name.
	 * 
	 * @return the class name
	 */
	public String getClassName() {
		return _className;
	}

	/**
	 * Gets the extend class name.
	 * 
	 * @return the extend class name
	 */
	public String getExtendClassName() {
		return _extendedClassName;
	}

	/**
	 * Checks if is generate class.
	 * 
	 * @return true, if is generate class
	 */
	public boolean isGenerateClass() {
		return _generateClass;
	}

	/**
	 * Gets the package name.
	 * 
	 * @return the package name
	 */
	public String getPackageName() {
		return _packageName;
	}

	/**
	 * Gets the user class name.
	 * 
	 * @return the user class name
	 */
	public String getUserClassName() {
		if (isGenerateClass()) {
			return getClassName();
		}
		if (_extendedClassName != null) {
			return JavaIdentifier.getlastclassName(_extendedClassName);
		}
		throw new IllegalArgumentException(
				"Cannot find the user class name");
	}

	/**
	 * Sets the class.
	 * 
	 * @param isClass
	 *            the new class
	 */
	public void setClass(boolean isClass) {
		this._isClass = isClass;
	}

	public void setCanOverwriteEtendsClass(boolean canOverwriteEtendsClass) {
		_canOverwriteEtendsClass = canOverwriteEtendsClass;
	}

	public IAnnotation getAnnotationOwGen() {
		if (this._type == null || !this._type.exists())
			return null;
		IAnnotation[] an;
		try {
			an = this._type.getAnnotations();
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		for (int i = 0; i < an.length; i++) {
			String name = an[i].getElementName();
			if (name.equals(OverrideGen.class.getSimpleName())
					|| name.equals(OverrideGen.class.getName()))
				return an[i];
		}
		return null;
	}

}