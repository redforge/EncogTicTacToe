package checkers;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Darwin on 1/29/2017.
 */
public class GenerationViewer extends JFrame {
    private GameBoard[] board = new GameBoard[20];
    private RedPlayer redPlayer[] = new RedPlayer[board.length];
    private BluePlayer bluePlayer[] = new BluePlayer[board.length];

    public GenerationViewer() {
        super("Generation");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new GridLayout(4, 10, 5, 5));

        for (int i = 0; i < board.length; i++) {
            redPlayer[i] = new RedPlayer(board[i]);
            bluePlayer[i] = new BluePlayer(board[i]);

            board[i] = new GameBoard(redPlayer[i], bluePlayer[i]);
            board[i].setUpGameBoard();
            add(board[i]);
        }

    }

    public void display() {
        pack();
        setVisible(true);
    }
}
