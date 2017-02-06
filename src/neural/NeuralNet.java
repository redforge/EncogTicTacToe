package neural;

import checkers.GameBoardTile;
import checkers.LegalMove;
import checkers.Player;
import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.neural.neat.NEATNetwork;

import java.util.ArrayList;

/**
 * Created by darwin on 2/4/17.
 */
public class NeuralNet {

    public static LegalMove getMoveNN(NEATNetwork network, double[] board, Player player) {
        ArrayList<LegalMove> possibleValidMoves = player.getAllPossibleValidMoves();

        MLData boardData = new BasicMLData(board);
        MLData moveData = network.compute(boardData);

        int indexOfHighestValue    =  0 ;
        double ignoreValuesAbove   =  99;
        double currentHighestValue = -1;
        boolean isFirstIteration   = true;
        boolean foundAnything      = false;
        do {
            //Exclude values above if this isn't the first iteration
            if (!isFirstIteration)
                ignoreValuesAbove = currentHighestValue;
            //Reset current highest
            currentHighestValue = -1;
            foundAnything = false;
            //Find highest that's not excluded
            for (int i = 0; i < board.length; i++) {
                double weight = moveData.getData(i);
                if (weight > currentHighestValue && weight < ignoreValuesAbove) {
                    indexOfHighestValue = i;
                    currentHighestValue = weight;
                    foundAnything = true;
                }
            }

            isFirstIteration = false;

            //If nothing was found, return first valid move
            if(!foundAnything)
                return possibleValidMoves.get(0);

            // Checks if highest move is valid
            GameBoardTile tileToCheck = player.getBoard().getTileOneArray()[indexOfHighestValue];
            for (LegalMove moveToCheck : possibleValidMoves) {
                if (moveToCheck.getNewTile().equals(tileToCheck))
                    return moveToCheck;
            }

        } while (true);

    }
}
