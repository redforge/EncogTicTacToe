import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.neural.neat.NEATNetwork;

import java.util.Scanner;

import static java.lang.Math.floor;


/**
 * Created by Ethan on 1/7/2017.
 * A game where you try to pick the higher number (1-10) without exceeding five
 */


public class PickRandomNumberGame {
    private static int p1guess;
    private static int p2guess;

    public int winner = -2;


    public void turn(NEATNetwork network) {
        this.p1guess = getMoveRandom();
        this.p2guess = getMoveNN(network);
    }

    public void humanTurn(NEATNetwork network) {
        this.p1guess = getMoveHuman();
        this.p2guess = getMoveNN(network);
    }

    public int getWinner() {
        if (p1guess > 5) {
            if (p2guess > 5)
                winner = 0;
            else
                winner = 1;
        } else {
            if (p2guess > 5)
                winner = -1;
            else {
                if (p1guess > p2guess)
                    winner = -1;
                else
                    winner = 1;
            }
        }
        return winner;
    }

    public void initializeGame() {
        this.p1guess = 0;
        this.p2guess = 0;
    }

    public void drawBoard() {
        System.out.println("P1: " + p1guess + "  P2: " + p2guess);
    }

    public int getMoveHuman() {
        Scanner scan = new Scanner(System.in);  // Reading from System.in
        return scan.nextInt();
    }

    public int getMoveNN(NEATNetwork network) {
        //Convert guesses to array
        double[] dataDouble = new double[2];
        dataDouble[0] = this.p1guess/10;
        dataDouble[1] = this.p2guess/10;
        //Make MLData
        MLData data = new BasicMLData(dataDouble);
        //Compute and output
        MLData result = network.compute(data);
        double out = result.getData(0);
        return (int) (out*10);

    }

    public int getMoveRandom() {
        return (int) (Math.random()*10);
    }


    public double[] toDoubleArray(int[] intArray) {
        double[] doubleArray = new double[intArray.length];
        for (int i=0; i<intArray.length; i++)
            doubleArray[i] = (double) intArray[i];
        return doubleArray;
    }
}


