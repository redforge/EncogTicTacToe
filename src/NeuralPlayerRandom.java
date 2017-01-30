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
            n += this.doIteration();
        }
        return n;
    }

    public int doIteration() {
        PickRandomNumberGame game = new PickRandomNumberGame();
        game.initializeGame();

        //for(int i=0; i<Math.random()*10; i++)
            game.turn(this.network);

        //game.drawBoard();
        //game.drawBoard(game.getBoard());
        //System.out.println(game.getWinner());
        return game.getWinner();
    }
}

