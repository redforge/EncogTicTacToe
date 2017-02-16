import org.encog.ml.MLMethod;
import org.encog.neural.neat.NEATNetwork;


import java.io.*;


/**
 * Created by ethanm on 1/25/17.
 * Gets the best neural network and plays a game against it
 */
public class PlayVsNN {

    public static MLMethod networkMLM;

    public static void main(String[] args) {
        //Get network
        readFiles();
        System.out.println("Starting game...");
        //Play
        playVsHuman();
    }

    public static void readFiles() {
        ObjectInputStream in;
        try {
            //Read best network
            in = new ObjectInputStream(new FileInputStream("training-data.td"));
            TrainingData td = (TrainingData) in.readObject();
            //networkMLM = td.previousBests[td.previousBests.length-1];
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void playVsHuman() {
        NEATNetwork network;
        network = (NEATNetwork) networkMLM;
        while (true) {
            TicTacToeGame humanGame = new TicTacToeGame();
            humanGame.initializeGame();
            while (humanGame.winner == -2) {
                humanGame.turnHuman(network);
            }
        }
    }
}