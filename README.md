# java-card-game-engine
Specifications and source code for a Java based card game engine.

Game rules are specified in a rules.<game>.txt file. The rules must specify the correct deck and board files to be used. The game engine will then dynamically create all necessary components to play the game, with game play following the rules file. See plugin/PluginKeyword for list of known Plugin keywords.

##Licensing
The MIT License (MIT). See LICENSE.txt.

##TO DO
* Build rules engine by reading rules plugin file
* Basic testing of following rules
* Create test suite of plugin files to test various possible inputs
* Expand test benches to test over multiple test suites
* Organize documentation

##Complete
* Basic strutures built and tested (playing card, deck, cardpile, board)
* Reading and parsing plugin files using keywords
* Reading rules to create CardDealer - initially deal cards to piles

##Directory Structure
	/root
	|
	|___/doc			=> Project Documentation
	|
	|___/plugin			=> rules, board, and deck files (.txt)
	|
	|___/res			=> project resources (graphical)
	|
	|___/src			=> source code (.java)
	|	|
	|	|___/games			=> java games package
	|		|
	|		|___/engine			=> games engine package
	|			|
	|			|___/gui			=> graphical user interface
	|			|
	|			|___/plugin			=> engine plugin utilities
	|			|
	|			|___/tests			=> code test benches
	|			|
	|			|___/util			=> engine backbone utilities
	|
	|___LICENSE.txt		=> License and warranty info
	|
	|___README.md		=> General information 

