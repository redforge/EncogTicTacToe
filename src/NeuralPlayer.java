 /**
 * Created by ethanm on 1/25/17.
 */

import org.encog.ml.CalculateScore;
import org.encog.ml.MLMethod;
import org.encog.neural.neat.NEATNetwork;
import org.encog.neural.networks.BasicNetwork;

public class NeuralPlayer {
    private NEATNetwork network;
    private NEATNetwork[] opponents;
    private int wins = 0;
    private int losses = 0;
    private int ties = 0;
    private int wins2 = 0;
    private int losses2 = 0;
    private int ties2 = 0;


    public NeuralPlayer(NEATNetwork network, NEATNetwork[] opponents) {
        this.opponents = opponents;
        this.network = network;
    }

    public int scorePlayer() {
        int n = 0;
        for (int i = 0; i < this.opponents.length; i++) {
            n += doIterationA(this.opponents[i]);
            n -= doIterationB(this.opponents[i]);
        }
        //System.out.println("W1:" + wins + " T1:" + ties + " L1:" + losses );
        //System.out.println("W2:" + wins2+ " T2:" + ties2+ " L2:" + losses2);
        //System.out.print(n/2+"\n\n");
        return n/2;
    }

    public int doIterationA(NEATNetwork opp) {

        TicTacToeGame game = new TicTacToeGame();
        game.initializeGame();
        for(int i = 0; i<9;i++)
            if(game.winner ==-2)
                game.turn(this.network, opp);
        //Test stuff
        if (game.winner == 1) wins++;
        if (game.winner == 0) ties++;
        if (game.winner == -1)losses++;

        return game.winner;
    }

    public int doIterationB(NEATNetwork opp) {

        TicTacToeGame game = new TicTacToeGame();
        game.initializeGame();
        for(int i = 0; i<9;i++)
            if(game.winner ==-2)
                game.turn2(this.network, opp);
        //Test stuff
        if (game.winner == -1)wins2++;
        if (game.winner == 0) ties2++;
        if (game.winner == 1) losses2++;
        return game.winner;
    }

    private int wl (int gameResult, int player) {
        if (gameResult == player*-1)
            return gameResult*5;
        else
            return gameResult;
    }

    private int playGame(NEATNetwork network1, NEATNetwork network2) {
        TicTacToeGame game = new TicTacToeGame();
            game.initializeGame();
            for(int i = 0; i<9;i++)
                if(game.winner ==-2)
                    game.turn(network1,network2);
            //game.drawBoard();
        return game.winner;
    }
}

