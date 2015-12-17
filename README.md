# java-card-game-engine
Specifications and source code for a Java based card game engine.

Game rules are specified in a rules.<game>.txt file. The rules must specify the correct deck and board files to be used. The game engine will then dynamically create all necessary components to play the game, with game play following the rules file. See plugin/PluginKeyword for list of known Plugin keywords.

##Licensing
The MIT License (MIT). See LICENSE.txt.

##To Do
* Add engine operations
* Create rules for additional games

##Complete
* Basic strutures built and tested (playing card, deck, cardpile, board)
* Reading and parsing plugin files using keywords
* Reading rules to create CardDealer - initially deal cards to piles
* Engine capable of reading conditions and performing actions
* Simple GUI created to test engine and show cards moving around
* Simple welome screen created to get choose a game and get player information
* Driver created to load the welcome GUI, create an engine, and load a main GUI

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
	|		|___/CardGameDriver.java	=> Main driver class for the engine and GUI
	|		|
	|		|___/engine			=> games engine package and core components
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
	|___README.md		=> General information (What you are currently reading!)

