/***********************************************************************//**
* @file			EngineComponentSet.java
* @author		Kurt E. Clothier
* @date			December 5, 2015
*
* @breif		A set of engine components
*
* @pre			Compiler: Eclipse - Mars Release (4.5.0)
* @pre			Java: JRE 7 or greater
*
* @see			http://www.projectsbykec.com/
* @see			PlayingCard
*
* @copyright	The MIT License (MIT) - see LICENSE.txt
****************************************************************************/
package games.engine;

import java.util.Arrays;

import games.Strings;

public final class EngineComponentSet<T extends EngineComponent> {

	private final T[] components;
	
	/**
	 * Constructs a new <tt>CardGameMoveCollecion</tt> with the given components.
	 * 
	 * @param components CardGameMoves to put in this collection
	 * @throws IllegalArgumentException if set is null or contains null or duplicate components
	 */
	public EngineComponentSet (final T[] set) throws IllegalArgumentException {
		EngineComponentSet.check(set);
		this.components = Arrays.copyOf(set, set.length);
	}
	
	/**
	 * Returns the number of <tt>EngineComponents</tt> in this set.
	 * 
	 * @return the number of <tt>EngineComponents</tt> in this set
	 */
	public int getSize() {
		return components.length;
	}
	
	/**
	 * Returns <tt>true</tt> if this set is empty.
	 * 
	 * @return <tt>true</tt> if this set is empty
	 */
	public boolean isEmpty() {
		return components.length == 0;
	}
	
	/**
	 * Returns the <tt>EngineComponent</tt> at the given index.
	 * 
	 * @param index index of 
	 * @return the common card pile with the given name
	 * @throws IndexOutOfBoundsException if either index is out of range (index < 0 || index >= size())
	 */
	public T get(final int index) throws IndexOutOfBoundsException {
		return components[index];
	}
	
	/**
	 * Returns this <tt>EngineComponentSet</tt> as an array of <tt>EngineComponents</tt>. 
	 * 
	 * @return this <tt>EngineComponentSet</tt> as an array of <tt>EngineComponents</tt>
	 */
	public T[] toArray() {
		return Arrays.copyOf(components, components.length);
	}
	
	/**
	 * Returns the <tt>EngineComponent</tt> with the given name.
	 * Returns <tt>null</tt> if the specified name is not found.
	 * 
	 * @param name name of the pile to return
	 * @return  the <tt>EngineComponent</tt> with the given name
	 * @throws IllegalArgumentException if the specified name is not found
	 */
	public T get(final String name) throws IllegalArgumentException {
		if ( this.contains(name)) {
			return components[this.getIndexOf(name)];
		}
		else {
			throw new IllegalArgumentException("Unknown component: " + name);
		}
	}
	
	/**
	 * Returns <tt>true</tt> if an <tt>EngineComponent</tt> with the given name exists.
	 * 
	 * @param name name of the pile to locate
	 * @return <tt>true</tt> if an <tt>EngineComponent</tt> with the given name exists
	 * @throws IllegalArgumentException if name is null or blank
	 */
	public boolean contains(final String name) throws IllegalArgumentException {
		return this.getIndexOf(name) < 0 ? false : true;
	}
	
	/**
	 * Returns <tt>true</tt> if this set contains the specified <tt>EngineComponent</tt>.
	 * 
	 * @param component the component being searched for
	 * @return <tt>true</tt> if this set contains the specified <tt>EngineComponent</tt>
	 * @throws IllegalArgumentException if component is null
	 */
	public boolean contains(final EngineComponent component) throws IllegalArgumentException {
		if (component == null) {
			throw new IllegalArgumentException("Component cannot be null.");
		}
		return this.getIndexOf(component.getName()) < 0 ? false : true;
	}
	
	/*
	 * Returns the index of the <tt>EngineAction</tt> with the given name.
	 * 
	 * @param name name of the pile to locate
	 * @return index of the card pile, or -1 if not found.
	 * @throws IllegalArgumentException if name is null or blank
	 */
	private int getIndexOf(final String name) throws IllegalArgumentException {
		if (name == null || name.equals("")) {
			throw new IllegalArgumentException("Name cannot be null or blank!");
		}
		int index = -1;
		for (int i = 0; i < components.length; i++) {
			if (components[i].getName().equals(name)) {
				index = i;
				break;
			}
		}
		return index;
	}
	
	/*
	 * Checks the <tt>EngineComponent</tt> set for issues...
	 *
	 * @param set an array of <tt>EngineComponents</tt> to be checked
	 * @return <tt>true</tt> if no issues are found
	 * @throws IllegalArgumentException if the set is null or contains null or duplicate components
	 */
	private static boolean check(EngineComponent[] set) throws IllegalArgumentException {
		if (set == null) {
			throw new IllegalArgumentException("Set cannot be null.");
		}
		for (int i = 0; i < set.length; i++) {
			if (set[i].getName() == null) {
				throw new IllegalArgumentException("Set cannot contain null components!");
			}
			for (int j = i+1; j < set.length; j++) {
				if (set[j].getName() == null) {
					throw new IllegalArgumentException("Set cannot contain null components!");
				}
				else if (set[j].getName().equalsIgnoreCase(set[i].getName())) {
					throw new IllegalArgumentException("Set cannot contain duplicate components!");
				}
			}
		}
		return true;
	}
	
	/**
	 * Return information.
	 *
	 * @return string containing information
	 */
	@Override public String toString() {
		final StringBuilder str = new StringBuilder();
		for (T t : components) {
			str.append(t.toString()).append(Strings.NEW_LINE);
		}
		return str.toString();
	}
}
