import org.encog.ml.MLMethod;
import org.encog.ml.ea.genome.Genome;
import org.encog.ml.ea.rules.ConstraintRule;
import org.encog.ml.ea.rules.RewriteRule;
import org.encog.ml.ea.rules.RuleHolder;
import org.encog.neural.neat.training.species.OriginalNEATSpeciation;
import org.encog.neural.networks.BasicNetwork;

import org.encog.ml.ea.train.EvolutionaryAlgorithm;
import org.encog.neural.neat.NEATNetwork;
import org.encog.neural.neat.NEATPopulation;
import org.encog.neural.neat.NEATUtil;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.util.List;


/**
 * Created by ethanm on 1/25/17.
 * Trains network using competitive unsupervised learning with NEAT
 */
public class MainTrain {

    private static TrainingData playerData;

    private static final int numPops = 15;
    private static final int popSize = 2500;
    private static final int numExtraRounds = 250;

    public static void main(String[] args) {
        readFiles();
        //Train
        actuallyTrain();
    }

    public static void readFiles() {
        ObjectInputStream in;
        try {
            //Get player data
            in = new ObjectInputStream(new FileInputStream("training-data.td"));
            playerData  = (TrainingData) in.readObject();
        } catch (IOException e) {
            //When nothing found
            playerData = new TrainingData();
            playerData.reset();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void writeFiles() {
        ObjectOutputStream out;

        try {
            //Write previous bests
            out = new ObjectOutputStream(new FileOutputStream("training-data.td"));
            out.writeObject(playerData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static NEATPopulation createPop(int size) { //Generate a template population
        int inputNeurons = 9;
        int outputNeurons = 10;
        NEATPopulation network = new NEATPopulation(inputNeurons, outputNeurons, size);
        network.reset();
        return network;
    }

    private static void actuallyTrain() {
        NEATPopulation pop;
        //Create population
        pop = createPop(popSize);

        EvolutionaryAlgorithm train; //Create training
        train = NEATUtil.constructNEATTrainer(pop, new PlayerScoreRandom());

        //Train to beat
        while (true) {
            train.iteration();
            playerData.pop = pop;
            playerData.best = (MLMethod) train.getCODEC().decode(pop.getBestGenome());
            playerData.epoch++;

            NeuralPlayerRandom testScore = new NeuralPlayerRandom((NEATNetwork) playerData.best);
            playerData.bestFitness = testScore.scorePlayer();

            System.out.println("Epoch "+playerData.epoch+" Score: "+playerData.bestFitness);
            writeFiles();
        }
    }

    private static MLMethod[] append(MLMethod[] oldArray, MLMethod toAppend) {
        MLMethod[] newArray = new MLMethod[oldArray.length + 1];
        for (int i = 0; i < oldArray.length; i++) {
            newArray[i] = oldArray[i];
        }
        newArray[newArray.length - 1] = toAppend;
        return newArray;
    }

    private static MLMethod[] limitLength(MLMethod[] oldArray, int length) {
        if (oldArray.length <= length)
            return oldArray;

        MLMethod[] newArray = new MLMethod[length];
        for (int i=0; i<length; i++)
            newArray[i] = oldArray[oldArray.length-1 - i];
        return newArray;
    }
}