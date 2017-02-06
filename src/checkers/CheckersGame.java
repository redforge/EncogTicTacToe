package checkers;

import neural.NeuralNet;
import org.encog.neural.neat.NEATNetwork;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by darwin on 2/5/17.
 */
public class CheckersGame extends JFrame {
    private GameBoard board;
    public RedPlayer redPlayer;
    public BluePlayer bluePlayer;

    private JButton start = new JButton("Start");
    private JButton startGUI = new JButton("Graphical Start");

    private boolean isBlueTurn = true;

    public int winner = -2;
    private int redGamesWon = 0;
    private int blueGamesWon = 0;

    public CheckersGame() {
        super("Train Neural Network");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new GridLayout());

        start.addActionListener(new ButtonListener());
        startGUI.addActionListener(new ButtonListener());
        add(start);
        add(startGUI);
    }

    public void display() {
        pack();
        setVisible(true);
    }

    public void initializeGame() {
        winner = -2;
        redGamesWon = 0;
        blueGamesWon = 0;

        redPlayer = new RedPlayer(board);
        bluePlayer = new BluePlayer(board);
        board = new GameBoard(redPlayer, bluePlayer);
        board.setUpGameBoard();
    }

    public void playGame() {
        int i = 0;
        while (i != 500) {
            initializeGame();
            while (winner == -2) {
                turn();
            }

            i++;
        }
        System.out.println("Red Games: " + redGamesWon);
        System.out.println("Blue Games: " + blueGamesWon);
    }

    public void turn() {
        Random rng = new Random();
        try {
            if (board.whoWon() == redPlayer) {
                winner = -1;
                System.out.println("Red Won");
                redGamesWon++;
            } else if (board.whoWon() == bluePlayer) {
                winner = 1;
                System.out.println("Blue Won");
                blueGamesWon++;
            } else {
                if (isBlueTurn) { //Blue turn (NN)
                    LegalMove nextMove = NeuralNet.getMoveNN(bluePlayer.getNetwork(), bluePlayer.convertBoard(), bluePlayer);
                    bluePlayer.movePiece(nextMove);
                    // TODO Uncomment the lines above and comment the things below @Ethan
                    /*ArrayList<LegalMove> possibleMoves = bluePlayer.getAllPossibleValidMoves();
                    int upperBound = possibleMoves.size();
                    LegalMove randomMove = possibleMoves.get(rng.nextInt(upperBound));
                    bluePlayer.movePiece(randomMove); // executes random move
                    isBlueTurn = false;*/

                } else { // Red's turn (random)
                    ArrayList<LegalMove> possibleMoves = redPlayer.getAllPossibleValidMoves();
                    int upperBound = possibleMoves.size();
                    LegalMove randomMove = possibleMoves.get(rng.nextInt(upperBound));
                    redPlayer.movePiece(randomMove); // executes random move
                    isBlueTurn = true;
                }
            }
        } catch (InvalidMoveException ime) {
            ime.printCustomError();
        }

    // Comment this section to just run games forever
    // This is just for debugging
//            int choice = JOptionPane.showConfirmDialog(this, "View game?", "Question", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
//            if (choice == 0) {
//                BoardViewer view = new BoardViewer(board);
//                int quitchoice = JOptionPane.showConfirmDialog(this, "Close?", "Question", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
//                while (quitchoice != 0) {
//
//                }
//            }
    }

    private class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == start) {
                playGame();
            } else if (e.getSource() == startGUI) {
                new PlayGUI();
            }
        }
    }

    private class BoardViewer extends JFrame {
        public BoardViewer(GameBoard board) {
            super("Viewer");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            add(board);
            pack();
            setVisible(true);

        }

    }

    private class PlayGUI extends JFrame implements ActionListener {

        private JButton turn = new JButton("Next Turn");

        public PlayGUI() {
             super("Checkers");
             setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
             setLayout(new GridLayout());

             turn.addActionListener(this);
             add(turn);

             initializeGame();
             add(board);

             pack();
             setVisible(true);

        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == turn) {
                if (winner == -2)
                    turn();
                else {
                    int choice = JOptionPane.showConfirmDialog(this, "Start new game?", "Question", JOptionPane.YES_NO_OPTION);
                    if (choice == 0) {
                        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
                        new PlayGUI();
                    }
                }
            }
        }
    }

}
