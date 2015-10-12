# java-card-game-engine
Specifications and source code for a Java based card game engine.

Game rules are specified in a rules.<game>.txt file. The rules must specify the correct deck and board files to be used. The game engine will then dynamically create all necessary components to play the game, with game play following the rules file.

##Licensing
The MIT License (MIT). See LICENSE.txt.

##TO DO
* Create rules.<game>.txt files
* Complete Design Requirements
* Clean up documentation

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
	|			|___/utils			=> engine utilities
	|
	|___LICENSE.txt		=> License and warranty info
	|
	|___README.md		=> General information 

