import org.encog.ml.MLMethod;
import org.encog.ml.ea.genome.Genome;
import org.encog.ml.ea.rules.ConstraintRule;
import org.encog.ml.ea.rules.RewriteRule;
import org.encog.ml.ea.rules.RuleHolder;
import org.encog.ml.ea.species.Species;
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

    private static final int popSize = 500;
    private static final int extraGens = 10;

    public static void main(String[] args) {
        readFiles();
        //Train
        while(true) {
            playerData.epoch++;
            System.out.println("Epoch "+playerData.epoch);
            trainIteration();
            writeFiles();
        }
    }

    public static void readFiles() {
        ObjectInputStream in;
        try {
            //Get player data
            in = new ObjectInputStream(new FileInputStream("training-data.td"));
            playerData  = (TrainingData) in.readObject();
        } catch (IOException e) {
            //When nothing found
            playerData  = new TrainingData();
            playerData .reset();
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
        int inputNeurons = 10;
        int outputNeurons = 9;
        NEATPopulation network = new NEATPopulation(inputNeurons, outputNeurons, size);
        network.reset();
        return network;
    }

    private static void trainIteration() {
        NEATPopulation pop;
        pop = createPop(popSize); //Create population

        EvolutionaryAlgorithm train; //Create training
        train = NEATUtil.constructNEATTrainer(pop, new PlayerScore(playerData.previousBests));

        OriginalNEATSpeciation speciation = new OriginalNEATSpeciation();
        train.setSpeciation(speciation);

        //Train to beat
        PlayerScore score = new PlayerScore(playerData.previousBests);
        do {
            train.iteration();
            System.out.print(":" +train.getError() + " ");
        } while (score.calculateScore(ghettoGetBestGenome(pop, train)) < playerData.bestFitness);

        System.out.println("Normal training done. " + extraGens + " rounds remain");
        for (int i = 0; i < extraGens; i++) {
            if (train.getError() == playerData.previousBests.length * 2)
                break;
            train.iteration();
        }

        train.finishTraining();

        //Check if better
        System.out.println("Competitive - " + " Opponents: " + playerData.previousBests.length + " Score:" + train.getError() + " Population size: " + popSize);

        //Output a random score
        NeuralPlayerRandom npr = new NeuralPlayerRandom((NEATNetwork) ghettoGetBestGenome(pop, train));
        System.out.println(npr.scorePlayer());
        {
            PlayerScore testScore = new PlayerScore(playerData.previousBests);
            System.out.println(testScore.calculateScore(ghettoGetBestGenome(pop, train)));
        }
        playerData.previousBests = append(playerData.previousBests, ghettoGetBestGenome(pop, train));

        playerData.bestFitness = (int)train.getError();

    }

    private static MLMethod ghettoGetBestGenome(NEATPopulation pop, EvolutionaryAlgorithm train) {
        MLMethod bestNet = null;
        int bestFitness = -100;
        PlayerScore testScore = new PlayerScore(playerData.previousBests);
        List<Species> speciesList = pop.getSpecies();

        for (int i=0; i<speciesList.size(); i++) {
            List<Genome> genomeList = speciesList.get(i).getMembers();

            if(genomeList.size() > 0) {
                for (int j=0; j<genomeList.size(); j++) {
                    MLMethod currentMethod = train.getCODEC().decode(genomeList.get(j));
                    int currentFitness = (int) testScore.calculateScore(currentMethod);

                    if (currentFitness > bestFitness) {
                        bestNet = currentMethod;
                        bestFitness = currentFitness;
                    }
                }
            }
        }
        return bestNet;
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