/***********************************************************************//**
* @file			AbstractAttribute.java
* @author		Kurt E. Clothier
* @date			November 14, 2015
*
* @breif		base class for Immutable string attributes
*
* @pre			Compiler: Eclipse - Mars Release (4.5.0)
* @pre			Java: JRE 7 or greater
*
* @see			http://www.projectsbykec.com/
*
* @copyright	The MIT License (MIT) - see LICENSE.txt
****************************************************************************/

package games.engine.util;

import java.io.Serializable;
public abstract class AbstractAttribute<T extends AbstractAttribute<T>> 
		implements Cloneable, Serializable, Comparable<T> {

	private static final long serialVersionUID = 5995768698784185027L;
	private final String field;	
	
	/**
	 * Constructs a new <tt>AbstractAttribute</tt>.
	 * For use by extended classes only.
	 * 
	 * @param field String this attribute represents
	 * @throws IllegalArgumentException if the parameter is null
	 */
	protected AbstractAttribute(final String field) throws IllegalArgumentException {
		if (field == null) {
			throw new IllegalArgumentException("Attribute cannot be null!");
		}
		this.field = field;
	}
	
	/**
	 * Returns the string representation of this attribute.
	 * 
	 * @return the string representation of this attribute
	 */
	protected final String get() {
		return field;
	}
	
	/**
	 * Creates and returns a (shallow) copy of this object.
	 * 
	 * @return a clone of this instance
	 * @throws CloneNotSupportedException if the Cloneable interface is not supported
	 */
	@Override public final Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	/**
	 * Compares this object with the specified object for order, lexicographically.
	 * String case is ignored; null < non-null
	 * 
	 * @param that the object to be compared
	 * @return N, where N = {-n,0,n if this <,==,> that}
	 */
	@Override public int compareTo(final T that) {
		return that == null ? 1 : this.field.compareToIgnoreCase(that.get());
	}
	
	/**
	 * Indicates whether some other object is "equal to" this one.
	 * 
	 * @param that the reference object with which to compare
	 * @return <tt>true</tt> if this object is the same as that
	 */
	@Override public boolean equals(final Object that) {
		return	that != null &&
				that.getClass() == this.getClass() &&
				((AbstractAttribute<?>)that).field.equals(this.field);
	}
	
	/**
	 * Returns a hash code value for the object.
	 * 
	 * @returns a hash code value for this object
	 */
	@Override public final int hashCode() {
		return 17 * 31 + field.hashCode();
	}
	
	/**
	 * Returns a string representation of the object.
	 * 
	 * @return a string representation of the object
	 */
	@Override public final String toString() {
		return field;
	}
}
