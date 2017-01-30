
import org.encog.neural.neat.NEATNetwork;
import org.encog.neural.networks.BasicNetwork;
import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;

import java.util.Scanner;
import static java.lang.Math.floor;
import static java.lang.Math.random;


/**
 * Created by Ethan on 1/7/2017.
 */


public class TicTacToeGame {
    private final int numberOfSpaces = 9;
    private int[] board = new int[numberOfSpaces];

    public int winner = -2;
    public int playerTurn = 1;
    public int turns = 0;

    public void turn(NEATNetwork network) {
        //Do Move
        if (this.playerTurn == 1)
            this.doMove(1, this.getMoveNN(network));
        else if (this.playerTurn == -1)
            this.doMove(-1, this.getMoveRandom());

        //Check if its winning move
        if (this.isWinning(1, this.getBoard()))
            this.winner = 1;
        if (this.isWinning(2, this.getBoard()))
            this.winner = 2;
        //Check if its a draw
        if (this.turns >= 8 && this.winner == -2)
            this.winner = 0;

        this.playerTurn *= -1;
        this.turns++;
    }

    public void turnHuman(NEATNetwork network) {
        //Do Move
        if (this.playerTurn == 1) {
            this.doMove(1, this.getMoveNN(network));
            playerTurn = -1;
        } else if (this.playerTurn == -1) {
            this.doMove(-1, this.getMoveHuman());
            playerTurn = 1;
        }

        //Check if its winning move
        if (this.isWinning(1, this.getBoard()))
            this.winner = 1;
        if (this.isWinning(-1, this.getBoard()))
            this.winner = -1;
        //Check if its a draw
        if (this.turns >= 8 && this.winner == -2)
            this.winner = 0;

        this.drawBoard(this.getBoard());
        this.turns++;
    }

    public void turn(NEATNetwork network1, NEATNetwork network2) {
        //Do Move
        if (this.playerTurn == 1) {
            this.doMove(1, this.getMoveNN(network1));
            this.playerTurn = -1;
        } else if (this.playerTurn == -1) {
            this.doMove(-1, this.getMoveNN(network2));
            this.playerTurn = 1;
        }

        //Check if its winning move
        if (this.isWinning(1, this.getBoard()))
            this.winner = 1;
        if (this.isWinning(-1, this.getBoard()))
            this.winner = -1;
        //Check if its a draw
        if (this.turns >= 8 && this.winner == -2) {
            this.winner = 0;
            System.out.println("THIS IS TIE");
        }

        this.turns++;
    }

    public int[] getBoard() {
        return this.board;
    }

    public int getWinner() {
        return this.winner;
    }

    public void initializeGame() {
        //Initialize board
        for (int i = 0; i < this.board.length; i++) {
            this.board[i] = 0;
        }
        this.winner = -2;
        this.playerTurn = 1;
        this.turns = 0;
    }

    public void drawBoard(int[] grid) {
        int j = 0;
        String ln = "";
        for (int i = 0; i < grid.length; i++) {
            char thisChar = ' ';
            if (grid[i] == 0)
                thisChar = '.';
            else if (grid[i] == 1)
                thisChar = 'X';
            else if (grid[i] == -1)
                thisChar = 'O';
            ln = ln + " " + thisChar;

            j++;
            if (j >= 3) {
                ln += '\n';
                j = 0;
            }
        }
        System.out.println(ln);

    }

    public int getMoveHuman() {
        Scanner scan = new Scanner(System.in);  // Reading from System.in
        int i;
        do {
            i = scan.nextInt() - 1;
        } while (!this.isMoveValid(i));

        return i;
    }

    public int getMoveNN(NEATNetwork network) {

        MLData boardData = new BasicMLData(toDoubleArray(this.board));
        MLData moveData = network.compute(boardData);

        int indexOfHighestValue    =  0 ;
        double ignoreValuesAbove   =  99;
        double currentHighestValue = -1;
        boolean isFirstIteration   = true;
        boolean foundAnything      = false;
        do {
            //Exclude values above if this isn't the first iteration
            if (!isFirstIteration)
                ignoreValuesAbove = currentHighestValue;
            //Reset current highest
            currentHighestValue = -1;
            foundAnything = false;
            //Find highest that's not excluded
            for (int i = 0; i < 9; i++) {
                double weight = moveData.getData(i);
                if (weight > currentHighestValue && weight < ignoreValuesAbove) {
                    indexOfHighestValue = i;
                    currentHighestValue = weight;
                    foundAnything = true;
                }
            }

            isFirstIteration = false;

            //If nothing was found, look for any valid move
            if(!foundAnything) {
                System.out.println("Error! Neural network couldn't find a valid move");
                System.out.println("Finding any valid move...");
                int i = 0;
                do {
                    //Note: indexOfHighestValue is the move that is to be made
                    indexOfHighestValue = i;
                    i++;
                    if (i > numberOfSpaces) {
                        this.drawBoard(this.getBoard());
                        System.out.println ("Couldn't find any valid moves...");
                        indexOfHighestValue = 1;
                        break;
                    }
                } while(!this.isMoveValid(i));
                break;
            }

        } while (!this.isMoveValid(indexOfHighestValue));
        return indexOfHighestValue;
    }

    public int getMoveRandom() {
        int i = 0;
        int iterations = 0;
        do {
            iterations++;
            if (iterations > 20)
                break;

            i = (int) floor(Math.random()*9);
        } while(!this.isMoveValid(i));
        if (iterations < 10)
            return i;
        else {

            for (int j = 0; j <= 9; j++) {
                if (this.isMoveValid(j)){
                    return j;
                }
            }
            //System.out.println("WHAT");
            return 1;
        }
    }

    public boolean isMoveValid(int position) {
        if (!(position >= 0 && position < 9))
            return false;
        return this.board[position] == 0;
    }

    public boolean isWinning(int player, int[] boardIn) {
        //Shitty conversion to 2D
        int[][] boardArg = new int[3][3];
        boardArg[0][0] = boardIn[0];
        boardArg[0][1] = boardIn[1];
        boardArg[0][2] = boardIn[2];
        boardArg[1][0] = boardIn[3];
        boardArg[1][1] = boardIn[4];
        boardArg[1][2] = boardIn[5];
        boardArg[2][0] = boardIn[6];
        boardArg[2][1] = boardIn[7];
        boardArg[2][2] = boardIn[8];

        //Check vertical/horizontal
        for (int i = 0; i < boardArg.length; i++) {
            if (boardArg[i][0] == player && boardArg[i][1] == player && boardArg[i][2] == player) {
                return true;
            }
        }
        for (int i = 0; i < boardArg[0].length; i++) {
            if (boardArg[0][i] == player && boardArg[1][i] == player && boardArg[2][i] == player) {
                return true;
            }
        }
        //Check diagonals
        if (boardArg[0][0] == player && boardArg[1][1] == player && boardArg[2][2] == player)
            return true;
        if (boardArg[2][0] == player && boardArg[1][1] == player && boardArg[0][2] == player)
            return true;
        return false;
    }

    public void doMove(int player, int i) {
        if (!this.isMoveValid(i)) {
            System.out.println("Error: Impossible Move " + i + " Attempted by player " + player);
            drawBoard(this.board);
            //this.board[i] = player;
            //System.out.println("result");
            //drawBoard(this.board);
        }
        this.board[i] = player;

    }

    public double[] toDoubleArray(int[] intArray) {
        double[] doubleArray = new double[intArray.length];
        for (int i=0; i<intArray.length; i++)
            doubleArray[i] = (double) intArray[i];
        return doubleArray;
    }
}


