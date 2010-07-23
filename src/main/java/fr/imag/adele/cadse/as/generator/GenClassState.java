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
	public String fClassName;

	/** The extended class name. */
	public String fExtendedClassName;

	/** The extended package name. */
	public String fExtendedPackageName;

	/** The generate class. */
	public boolean fGenerateClass;

	/** The implements class name. */
	public String[] fImplementsClassName = null;

	/** The implements package name. */
	public String[] fImplementsPackageName;

	/** The is class. */
	public boolean isClass = true;

	/** The can overwrite etends class. */
	public boolean fCanOverwriteEtendsClass = false;

	/** The type. */
	public IType type;

	/** The cxt. */
	public ContextVariable cxt;

	private Set<String> imports = new TreeSet<String>();

	public void init() {
		if (fExtendedClassName != null) {
			String[] p = JavaIdentifier
					.getPackageAndClassName(fExtendedClassName);
			if (p[0] != null) {
				fExtendedClassName = p[1];
				fExtendedPackageName = p[0];
			}
		}
		if (fImplementsClassName != null) {
			fImplementsPackageName = new String[fImplementsClassName.length];
			for (int i = 0; i < fImplementsClassName.length; i++) {
				String s = fImplementsClassName[i];
				String[] p = JavaIdentifier.getPackageAndClassName(s);
				if (p[0] != null) {
					fImplementsClassName[i] = p[1];
					fImplementsPackageName[i] = p[0];
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
			fImplementsClassName = ArraysUtil.addList(String.class,
					fImplementsClassName, className);
			fImplementsPackageName = ArraysUtil.addList(String.class,
					fImplementsPackageName, className);
		}
	}

	/**
	 * Gets the implements class name.
	 * 
	 * @return the implements class name
	 */
	public String[] getImplementsClassName() {
		HashSet<String> ret = new HashSet<String>();
		if (fImplementsClassName != null) {
			ret.addAll(Arrays.asList(fImplementsClassName));
		}

		try {
			if (type != null && type.exists()) {
				String[] itfs;
				itfs = type.getSuperInterfaceNames();
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
		return fClassName;
	}

	/**
	 * Gets the extend class name.
	 * 
	 * @return the extend class name
	 */
	public String getExtendClassName() {
		return fExtendedClassName;
	}

	/**
	 * Checks if is generate class.
	 * 
	 * @return true, if is generate class
	 */
	public boolean isGenerateClass() {
		return fGenerateClass;
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
		if (fExtendedClassName != null) {
			return JavaIdentifier.getlastclassName(fExtendedClassName);
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
		this.isClass = isClass;
	}

	public void setCanOverwriteEtendsClass(boolean canOverwriteEtendsClass) {
		fCanOverwriteEtendsClass = canOverwriteEtendsClass;
	}

	public IAnnotation getAnnotationOwGen() {
		if (this.type == null)
			return null;
		IAnnotation[] an;
		try {
			an = this.type.getAnnotations();
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