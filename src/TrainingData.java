import org.encog.ml.MLMethod;
import org.encog.neural.neat.NEATPopulation;
import org.encog.neural.networks.BasicNetwork;

import java.io.Serializable;

/**
 * Created by Ethan on 1/31/2017.
 * Stores data for training
 */
public class TrainingData implements Serializable{
    public int epoch;
    public int bestFitness;
    public int i;
    public MLMethod[] previousBests;
    public NEATPopulation[] pops;

    public void reset() {
        this.i = 0;
        this.epoch = 0;
        this.bestFitness = -100;
        this.previousBests = new BasicNetwork[0];
        this.pops = null;
    }
}
