
import org.encog.ml.CalculateScore;
import org.encog.ml.MLMethod;
import org.encog.neural.neat.NEATNetwork;

/**
 * Created by ethanm on 1/25/17.
 */
public class PlayerScore2 implements CalculateScore {
    private MLMethod[] opponents;
    public PlayerScore2(MLMethod[] opponents) {
        this.opponents = opponents;
    }

    @Override
    public double calculateScore(MLMethod network) {
        //Cast opponents
        NEATNetwork[] opponentsCast = new NEATNetwork[opponents.length];
        for (int i=0; i<opponents.length; i++)
            opponentsCast[i] = (NEATNetwork) opponents[i];
        //Make player
        NeuralPlayer player = new NeuralPlayer((NEATNetwork) network, opponentsCast);
        //Score player
        return player.scorePlayer2();
    }

    public boolean shouldMinimize() {
        return false;
    }


    @Override
    public boolean requireSingleThreaded() {
        return true;
    }
}

