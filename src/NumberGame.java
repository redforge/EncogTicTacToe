import com.sun.org.apache.regexp.internal.RE;
import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.neural.neat.NEATNetwork;
import org.encog.util.kmeans.Centroid;

/**
 * Created by ethanm on 2/17/17.
 */
public class NumberGame {
    private int[] boardP1 = new int[5];
    private int[] boardP2 = new int[5];

    private int index = 0;
    private int turnNum = 0;
    public  int winner = -2;


    public void initializeGame(){}

    public void turn(NEATNetwork network) {
        doMove(getMoveNN(network), 1);
        turnNum++;
        if (turnNum > 4) {
            if (index == 2)
                this.winner = 1;
            else
                this.winner =-1;
        }
        if (index > 2) {
            int i = 1 + 1;
        }
        //System.out.println(board);
    }

    public void doMove (boolean go, int player) {
        if (go) {
            board[index] = player;
            index++;
        }
    }

    public boolean getMoveNN(NEATNetwork network) {
        MLData boardData = new BasicMLData(toDoubleArray(board));
        MLData outML = network.compute(boardData);

        return outML.getData(0) > 0.5;
    }

    private double[] toDoubleArray(int[] intArray) {
        double[] doubleArray = new double[intArray.length];
        for (int i=0; i<intArray.length; i++)
            doubleArray[i] = (double) intArray[i];
        return doubleArray;
    }
}

