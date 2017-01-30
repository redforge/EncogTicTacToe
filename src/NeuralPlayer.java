 /**
 * Created by ethanm on 1/25/17.
 */

import org.encog.ml.CalculateScore;
import org.encog.ml.MLMethod;
import org.encog.neural.neat.NEATNetwork;
import org.encog.neural.networks.BasicNetwork;

public class NeuralPlayer {
    private NEATNetwork network;
    private NEATNetwork[] opponents;

    public NeuralPlayer (NEATNetwork network, NEATNetwork[] opponents) {
        this.opponents = opponents;
        this.network = network;
    }

    public int scorePlayer() {
        int n = 0;
        for (int i=0; i<this.opponents.length; i++) {
            n += this.doIteration(this.opponents[i]);
        }
        return n;
    }
    public int doIteration(NEATNetwork opponent) {
        TicTacToeGame game = new TicTacToeGame();
        game.initializeGame();
        for (int i = 0; i<9; i++)
            if (game.winner == -2)
                game.turn(this.network, opponent);

        game.drawBoard(game.getBoard());
        //System.out.println(game.getWinner());
        return game.winner;
    }


}

