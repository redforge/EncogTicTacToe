/**
 * Created by ethanm on 1/25/17.
 */

import org.encog.neural.neat.NEATNetwork;
import org.encog.neural.networks.BasicNetwork;

public class NeuralPlayerRandom {
    private NEATNetwork network;

    public NeuralPlayerRandom(NEATNetwork network) {
        this.network = network;
    }


    public int scorePlayer() {
        int n = 0;
        for (int i=0; i<100; i++) {
            n += this.doIterationA() + this.doIterationB();
        }
        return n/2;
    }

    public int doIterationA() {

        TicTacToeGame game = new TicTacToeGame();
        game.initializeGame();
        for(int i = 0; i<9;i++)
            if(game.winner ==-2)
                game.turnR(this.network);
        return game.winner;
    }

    public int doIterationB() {

        TicTacToeGame game = new TicTacToeGame();
        game.initializeGame();
        for(int i = 0; i<9;i++)
            if(game.winner ==-2)
                game.turnR2(this.network);
        return -game.winner;
    }
}

