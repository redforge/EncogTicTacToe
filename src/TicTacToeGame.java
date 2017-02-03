
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
    public int[] board = new int[numberOfSpaces];

    public int winner = -2;
    public int playerTurn = 1;
    public int turns = 0;

    private boolean displayBoardNext;

    public void turn(NEATNetwork network) {
        //Do Move
        if (this.playerTurn == 1)
            this.doMove(1, this.getMoveNN(network, this.board));
        else if (this.playerTurn == -1)
            this.doMove(-1, this.getMoveRandom());

        //Check if its winning move
        if (this.isWinning(this.playerTurn, this.board))
            this.winner = this.playerTurn;
        //Check if its a draw
        if (this.turns >= 8 && this.winner == -2)
            this.winner = 0;

        this.playerTurn *= -1;
        this.turns++;
    }

    public void turnHuman(NEATNetwork network) {
        //Do Move
        if (this.playerTurn == 1)
            this.doMove(1, this.getMoveNN(network, this.board));
        else if (this.playerTurn == -1)
            this.doMove(-1, this.getMoveHuman());

        //Check if its winning move
        if (this.isWinning(this.playerTurn, this.board))
            this.winner = this.playerTurn;
        //Check if its a draw
        if (this.turns >= 8 && this.winner == -2)
            this.winner = 0;

        this.drawBoard();
        this.playerTurn *= -1;
        this.turns++;
    }

    public void turn(NEATNetwork network1, NEATNetwork network2) {
        this.displayBoardNext = false;
        //Do Move
        if (this.playerTurn == 1) {
            this.doMove(1, this.getMoveNN(network1, this.board));
            this.playerTurn = -1;
        } else if (this.playerTurn == -1) {
            this.doMove(-1, this.getMoveNN(network2, invert(this.board)));
            this.playerTurn = 1;
        }

        if (this.displayBoardNext)
            this.drawBoard();

        //Check if its winning move
        if (this.isWinning(this.playerTurn, this.board))
            this.winner = this.playerTurn;
        //Check if its a draw
        if (this.turns >= 8 && this.winner == -2)
            this.winner = 0;

        this.playerTurn *= -1;
        this.turns++;
    }

    public int[] invert(int[] array) {
        for (int i=0; i<array.length; i++)
            array[i] *= -1;
        return array;
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

    public void drawBoard() {
        int[] grid = this.board;
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

    public int getMoveNN(NEATNetwork network, int[] board) {

        MLData boardData = new BasicMLData(toDoubleArray(board));
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

            //If nothing was found, return first valid move
            if(!foundAnything) {
                System.out.println("Before" + playerTurn);
                this.drawBoard();
                this.displayBoardNext= true;
                return this.getMoveFirst(board);

            }

        } while (!this.isMoveValid(indexOfHighestValue));
        return indexOfHighestValue;
    }

    public int getMoveRandom() {
        int [] validMoves = new int[0];
        for (int i=0; i < this.board.length; i++) {
            if (this.isMoveValid(i)) {
                validMoves = append(validMoves, i);
            }
        }

        int index;
        index = (int)(Math.random()*validMoves.length);
        return validMoves[index];
    }

    public int getMoveFirst(int[] board) {
        int [] validMoves = new int[0];
        for (int i=0; i < this.board.length; i++) {
            if (this.isMoveValid(i)) {
                validMoves = append(validMoves, i);
            }
        }

        int index;
        index = 0;
        return validMoves[index];
    }

    private int[] append(int[] prev, int toAdd) {
        int[] out = new int[prev.length+1];
        for (int i=0; i<prev.length; i++)
            out[i] = prev[i];
        out[prev.length] = toAdd;
        return out;
    }

    public boolean isMoveValid(int position) {
        if (!(position >= 0 && position < 9))
            return false;
        return (this.board[position] == 0);
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
            drawBoard();
            //this.board[i] = player;
            //System.out.println("result");
            //drawBoard(this.board);
        }
        this.board[i] = player;
        bd();

    }

    public double[] toDoubleArray(int[] intArray) {
        double[] doubleArray = new double[intArray.length];
        for (int i=0; i<intArray.length; i++)
            doubleArray[i] = (double) intArray[i];
        return doubleArray;
    }

    public void bd() {
        if (!isBoardValid()) {
            System.out.println("ERROR: Impossible move");
            this.drawBoard();
        }
    }
    public boolean isBoardValid() {
        //TTT debug tool, check if current board state is possible
        int n = 0;
        for (int i=0; i<board.length; i++)
            n+=board[i];
        return (n==0 || n==1);
    }
}



