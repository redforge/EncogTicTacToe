import org.encog.ml.MLMethod;
import org.encog.ml.ea.genome.Genome;
import org.encog.ml.ea.species.Species;
import org.encog.neural.neat.NEATNetwork;
import org.encog.neural.neat.training.species.OriginalNEATSpeciation;

import org.encog.ml.ea.train.EvolutionaryAlgorithm;
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
    private static final int INT_LIMIT = 2147483647;

    private static TrainingData playerData;
    private static final int popSize = 50;


    public static void main(String[] args) {
        readFiles();

        //Train
        while (true) {
            System.out.println("\nEpoch: " + playerData.epoch);
            trainIteration();
            playerData.epoch++;
            writeFiles();
        }
    }

    public static void readFiles() {
        ObjectInputStream in;
        try {
            //Get player data
            in = new ObjectInputStream(new FileInputStream("training-data.td"));
            playerData = (TrainingData) in.readObject();
        } catch (IOException e) {
            //When nothing found
            playerData  = new TrainingData();
            playerData .reset();
            playerData.pop = createPop(popSize);
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

        //Set up training & Score
        ShittyPopCopyThing popClone = new ShittyPopCopyThing(playerData.pop);//Set up training & Score
        EvolutionaryAlgorithm train; //Create training
        PlayerScore trainingScore = new PlayerScore(popClone.getPop());
        train = NEATUtil.constructNEATTrainer(playerData.pop, trainingScore);
        OriginalNEATSpeciation speciation = new OriginalNEATSpeciation();
        train.setSpeciation(speciation);
        trainingScore.setTrain(train);

        //Run an iteration of training
        train.iteration();

        //This is just some stuff to show how it's going, comment out if you want
        PlayerScore testScore = new PlayerScore(playerData.pop);
        testScore.setTrain(train);
        System.out.println("Competitive - " + " Score:" + testScore.calculateScore(customGetBestGenome(playerData.pop, testScore, train)) + " Population size: " + getPopSize(playerData.pop));

        NeuralPlayerRandom npr = new NeuralPlayerRandom((NEATNetwork)customGetBestGenome(playerData.pop, testScore, train));
        System.out.println(npr.scorePlayer());
    }

    private static int getPopSize(NEATPopulation pop) {
        int num = 0;
        List<Species> speciesList = pop.getSpecies();

        for (int i=0; i<speciesList.size(); i++) {
            List<Genome> genomeList = speciesList.get(i).getMembers();

            if(genomeList.size() > 0) {
                for (int j=0; j<genomeList.size(); j++) {
                    num++;
                }
            }
        }
        return num;
    }

    private static MLMethod customGetBestGenome(NEATPopulation pop, PlayerScore score, EvolutionaryAlgorithm train) {
        MLMethod bestNet = null;
        int bestFitness = -INT_LIMIT;
        List<Species> speciesList = pop.getSpecies();

        for (int i=0; i<speciesList.size(); i++) {
            List<Genome> genomeList = speciesList.get(i).getMembers();

            if(genomeList.size() > 0) {
                for (int j=0; j<genomeList.size(); j++) {
                    MLMethod currentMethod = train.getCODEC().decode(genomeList.get(j));
                    int currentFitness = (int) score.calculateScore(currentMethod);

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