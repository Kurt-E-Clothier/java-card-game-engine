/***********************************************************************//**
* @file			PluginKeywordCode.java
* @author		Kurt E. Clothier
* @date			November 20, 2015
* 
* @breif		Pluign Keyword Codes
*
* @pre			Compiler: Eclipse - Mars Release (4.5.0)
* @pre			Java: JRE 7 or greater
*
* @see			http://www.projectsbykec.com/
*
* @copyright	The MIT License (MIT) - see LICENSE.txt
****************************************************************************/
package games.engine.plugin;

public enum PluginKeywordCode {

	/** Neither Keyword Exists */
	NEITHER_EXISTS,
	
	/** A Exists, B does not */
	A_EXISTS,
	
	/** B Exists, A does not */
	B_EXISTS,
	
	/** Both exist, A has greater index */
	A_GREATER,
	
	/** Both exist, B has greater index */
	B_GREATER,
	
	/** Both exist, and are the same keyword */
	EQUAL;
}
