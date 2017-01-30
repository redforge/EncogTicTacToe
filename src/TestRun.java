/**
 * Created by Ethan on 1/26/2017.
 */
public class TestRun {
    public static void main(String[] args) {
        while (true)
            doGame();
    }
    public static void doGame() {
        TicTacToeGame game = new TicTacToeGame();
        game.initializeGame();
        //Do Move
        while (game.getWinner() == -2) {
            if (game.playerTurn == 1)
                game.doMove(1, game.getMoveHuman());
            else
                game.doMove(-1, game.getMoveRandom());
            game.drawBoard(game.getBoard());
            //Check if its winning move
            if (game.isWinning(game.playerTurn, game.getBoard())) {
                game.winner = game.playerTurn;
            }
            //Check if its a draw
            game.turns++;
            if (game.turns == 9) {
                game.winner = 0;
            }
            //Change player
            game.playerTurn *= -1;
        }
    }
}
