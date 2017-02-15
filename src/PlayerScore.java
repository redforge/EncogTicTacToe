
import org.encog.ml.CalculateScore;
import org.encog.ml.MLMethod;
import org.encog.ml.ea.codec.GeneticCODEC;
import org.encog.ml.ea.species.Species;
import org.encog.ml.ea.train.EvolutionaryAlgorithm;
import org.encog.neural.neat.NEATNetwork;
import org.encog.neural.neat.NEATPopulation;

/**
 * Created by ethanm on 1/25/17.
 */
public class PlayerScore implements CalculateScore {
    private NEATPopulation pop;
    private EvolutionaryAlgorithm train;
    private GeneticCODEC codec;

    public PlayerScore (NEATPopulation pop) {
        this.pop = pop;
        this.train = null;
        this.codec = null;
    }

    public void setTrain(EvolutionaryAlgorithm train) {
        this.train = train;
        this.codec = train.getCODEC();
    }




    @Override
    public double calculateScore(MLMethod network) {
        MLMethod[] opponents = new MLMethod[0];

        for (int i=0; i< pop.getSpecies().size(); i++) {
            Species species = pop.getSpecies().get(i);
            for (int j = 0; j < species.getMembers().size(); j++) {
                opponents = append(opponents, this.codec.decode(species.getMembers().get(j)));

            }
        }



        //Cast opponents
        NEATNetwork[] opponentsCast = new NEATNetwork[opponents.length];
        for (int i=0; i<opponents.length; i++)
            opponentsCast[i] = (NEATNetwork) opponents[i];
        //Make player
        NeuralPlayer player = new NeuralPlayer((NEATNetwork) network, opponentsCast);
        //Score player
        return player.scorePlayer();
    }

    public boolean shouldMinimize() {
        return false;
    }

    private static MLMethod[] append(MLMethod[] oldArray, MLMethod toAppend) {
        MLMethod[] newArray = new MLMethod[oldArray.length + 1];
        for (int i = 0; i < oldArray.length; i++) {
            newArray[i] = oldArray[i];
        }
        newArray[newArray.length - 1] = toAppend;
        return newArray;
    }
    @Override
    public boolean requireSingleThreaded() {
        return false;
    }
}

