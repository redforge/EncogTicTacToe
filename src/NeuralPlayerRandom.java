/**
 * Created by ethanm on 1/25/17.
 */

import org.encog.neural.neat.NEATNetwork;
import org.encog.neural.networks.BasicNetwork;

public class NeuralPlayerRandom {
    private int wins = 0;
    private int losses = 0;
    private int ties = 0;
    private int wins2 = 0;
    private int losses2 = 0;
    private int ties2 = 0;
    private NEATNetwork network;

    public NeuralPlayerRandom(NEATNetwork network) {
        this.network = network;
    }


    public int scorePlayer() {
        int n = 0;
        for (int i=0; i<100; i++) {
            n += this.doIterationA();
            n -= this.doIterationB();
        }
        System.out.println("RANDOM TEST");
        System.out.println("RANDOM W1:" + wins + " T1:" + ties + " L1:" + losses );
        System.out.println("RANDOM W2:" + wins2+ " T2:" + ties2+ " L2:" + losses2);
        return n/2;
    }

    public int doIterationA() {

        TicTacToeGame game = new TicTacToeGame();
        game.initializeGame();
        for(int i = 0; i<9;i++)
            if(game.winner ==-2)
                game.turnR(this.network);
        //Test stuff
        if (game.winner == 1) wins++;
        if (game.winner == 0) ties++;
        if (game.winner == -1)losses++;

        return game.winner;
    }

    public int doIterationB() {

        TicTacToeGame game = new TicTacToeGame();
        game.initializeGame();
        for(int i = 0; i<9;i++)
            if(game.winner ==-2)
                game.turnR2(this.network);
        //Test stuff
        if (game.winner == -1)wins2++;
        if (game.winner == 0) ties2++;
        if (game.winner == 1) losses2++;
        return game.winner;
    }
}

