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

    public NeuralPlayer(NEATNetwork network, NEATNetwork[] opponents) {
        this.opponents = opponents;
        this.network = network;
    }

    public int scorePlayer() {
        int n = 0;
        for (int i = 0; i < this.opponents.length; i++) {
            n += this.playGame(this.network, this.opponents[i]);
            n -= wl(this.playGame(this.opponents[i], this.network),-1);
        }
        return n/5 + 50;
    }

    private int wl (int gameResult, int player) {
        if (gameResult == player*-1)
            return gameResult*5;
        else
            return gameResult;
    }

    private int playGame(NEATNetwork network1, NEATNetwork network2) {
        TicTacToeGame game = new TicTacToeGame();
            game.initializeGame();
            for(int i = 0; i<9;i++)
                if(game.winner ==-2)
                    game.turn(network1,network2);
            //game.drawBoard();
        return game.winner;
    }
}

