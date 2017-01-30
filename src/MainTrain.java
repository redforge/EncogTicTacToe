
import org.encog.engine.network.activation.ActivationTANH;
import org.encog.ml.MLMethod;
import org.encog.ml.MLResettable;
import org.encog.ml.MethodFactory;
import org.encog.ml.genetic.MLMethodGeneticAlgorithm;
import org.encog.ml.train.MLTrain;
import org.encog.neural.neat.training.species.OriginalNEATSpeciation;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.pattern.FeedForwardPattern;

import org.encog.ml.ea.train.EvolutionaryAlgorithm;
import org.encog.neural.hyperneat.substrate.Substrate;
import org.encog.neural.hyperneat.substrate.SubstrateFactory;
import org.encog.neural.neat.*;
import org.encog.neural.neat.NEATPopulation;
import org.encog.neural.neat.NEATUtil;
import org.encog.neural.neat.training.species.OriginalNEATSpeciation;
import org.encog.util.Format;


/**
 * Created by ethanm on 1/25/17.
 */
public class MainTrain {

    public static NEATPopulation createPop(int size) { //Generate a template network
        int inputNeurons = 9;
        int outputNeurons = 9;
        NEATPopulation network = new NEATPopulation(inputNeurons, outputNeurons, size);
        network.reset();
        return network;
    }

    public static int popSize;
    public static int bestFitness;

    public static void main(String[] args) {
        {
            NEATPopulation pop = createPop(100);

            //Create training
            EvolutionaryAlgorithm train;

            //Do initial training vs random
            train = NEATUtil.constructNEATTrainer(pop, new PlayerScoreRandom());
            //Speciation
            OriginalNEATSpeciation speciation = new OriginalNEATSpeciation();
            train.setSpeciation(speciation);

            int epoch = 1;

            for (int i = 0; i < 500; i++) {
                train.iteration();
                System.out.println("Random - " + "Epoch #" + epoch + " Score:" + train.getError());
                epoch++;
            }
            train.finishTraining();
            //previousBests = append(previousBests, train.getMethod());

            System.out.println("Random Training Complete");

            NEATNetwork network;
            network = (NEATNetwork) train.getCODEC().decode(pop.getBestGenome());

             /*while (true) {
                PickRandomNumberGame humanGame = new PickRandomNumberGame();
                humanGame.initializeGame();
                humanGame.humanTurn(network);
                humanGame.drawBoard();
                System.out.println(humanGame.getWinner());
            }*/

            while (true) {
                TicTacToeGame humanGame = new TicTacToeGame();
                humanGame.initializeGame();
                while (humanGame.getWinner() == -2)
                    humanGame.turnHuman(network);
            }
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
}