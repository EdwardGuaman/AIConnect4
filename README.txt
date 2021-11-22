The program was written in java so javac would be used to compile it.
There are three java files named AlphaBeta, act, and Connect4Gui
AlphaBeta is the alpha beta algorithm with iterative deepening.
'act' is an object that holds the state and the first move used only in first call of alpha beta.
Connect4Gui is the actual game and the one that contains main.

The depth is currently hard coded in the gui the variable is named depth if it needs to be changed.
The program starts by asking the time limit with the input being in seconds(ex: 10 = 10seconds).
The program then asks who starts the game and the game begins.
The game has the required gui so the only inputs are the opponents move.
The move should be in the form of a letter between a and h immediatly followed by a number between 1 and 8.
(ex: A1 or a8 ; not case sensitive)
The letter goes throgh a hashtable to be converted to an int to be used to calculate the index the reason for this is that I made use of a one dimensional array.
As of writing this there is no catch for bad inputs and the game does not end once someone wins.
Therefore all inputs should be good ones and the program must manualy be ended.