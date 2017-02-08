
import org.encog.ml.CalculateScore;
import org.encog.ml.MLMethod;
import org.encog.neural.neat.NEATNetwork;
import org.encog.neural.networks.BasicNetwork;

/**
 * Created by ethanm on 1/25/17.
 */
public class PlayerScore implements CalculateScore {
    private MLMethod[] opponents;
    public PlayerScore (MLMethod[] opponents) {
        this.opponents = opponents;
    }

    @Override
    public double calculateScore(MLMethod network) {
        //Cast opponents
        NEATNetwork[] opponentsCast = new NEATNetwork[this.opponents.length];
        for (int i=0; i<this.opponents.length; i++)
            opponentsCast[i] = (NEATNetwork) this.opponents[i];
        //Make player
        NeuralPlayer player = new NeuralPlayer((NEATNetwork) network, opponentsCast);
        //Score player
        //System.out.print(player.scorePlayer() + " ");
        return player.scorePlayer();
    }

    public boolean shouldMinimize() {
        return false;
    }


    @Override
    public boolean requireSingleThreaded() {
        return false;
    }
}

