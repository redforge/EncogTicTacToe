package checkers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

/**
 * This is the class that the main interface will run off of.
 * It will contain the checkers board and other buttons / I/O methods to interact with
 */

// Main GUI interface for interacting with the program
// Will need to implement a Gameboard object that draws board and checker piece objects

public class CheckersMainMenu extends JFrame {

    // Main containers/components initialization
    private GameBoard board;
    private JButton test = new JButton("Move");
    private JButton getAllBlue = new JButton("Blue Moves");
    private JButton generation = new JButton("Generation");
    private JButton capture = new JButton("Capture");
    private JButton train = new JButton("Train");

    RedPlayer redPlayer = new RedPlayer(board);
    BluePlayer bluePlayer = new BluePlayer(board);

    // temp variable to control whose turn it is
    private boolean isRed = false;

    public CheckersMainMenu() {
        super("Checkers Program");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new FlowLayout());

        // Sets up the game board
        board = new GameBoard(redPlayer, bluePlayer);
        board.setUpGameBoard();

        // Button to test different features
        test.addActionListener(new testclass());
        getAllBlue.addActionListener(new testclass());
        capture.addActionListener(new testclass());
        JPanel testPane = new JPanel();

        testPane.add(getAllBlue);
        testPane.add(test);
        testPane.add(capture);

        generation.addActionListener(new ButtonListener());
        train.addActionListener(new ButtonListener());
        add(generation);
        add(train);
        add(testPane);
        add(board);
    }


    public void display() {
        pack();
        setVisible(true);
    }

    // Temporary test class to test features
    private class testclass implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == test) {
                bluePlayer.getPieces()[5].movePiece(board.getTile()[4][3]);
                redPlayer.getPieces()[5].makeKing();
                redPlayer.getPieces()[11].movePiece(board.getTile()[3][6]);
                bluePlayer.getPieces()[7].movePiece(board.getTile()[4][5]);
                bluePlayer.getPieces()[5].makeKing();
                redPlayer.getPieces()[9].movePiece(board.getTile()[3][4]);
                redPlayer.getPieces()[6].movePiece(board.getTile()[3][2]);
                redPlayer.getPieces()[6].movePiece(board.getTile()[2][3]);
                redPlayer.getPieces()[3].makeKing();
                redPlayer.getPieces()[8].movePiece(board.getTile()[4][1]);

                bluePlayer.getPieces()[5].movePiece(board.getTile()[3][2]);
                redPlayer.getPieces()[9].movePiece(board.getTile()[5][2]);
                redPlayer.getPieces()[10].movePiece(board.getTile()[3][4]);
                redPlayer.getPieces()[3].movePiece(board.getTile()[1][7]);


                redPlayer.getPieces()[1].movePiece(board.getTile()[2][1]);
                redPlayer.getPieces()[6].movePiece(board.getTile()[1][4]);
                bluePlayer.getPieces()[0].makeKing();
                bluePlayer.getPieces()[2].movePiece(board.getTile()[5][6]);
                bluePlayer.getPieces()[10].movePiece(board.getTile()[3][0]);

                for (int i = 0; i < 10; i++) {
                    System.out.print("Waiting\n");
                }

            /*for (checkers.GameBoardTile list : redPlayer.getPieces()[8].checkAvailableTiles(redPlayer.getPieces()[8].getCurrentTile()))
                System.out.println(list);
                */
            } else if (e.getSource() == getAllBlue) {

                System.out.println("MEME!");

                for (LegalMove move : bluePlayer.getAllPossibleValidMoves()) {
                    System.out.print(move);
                    System.out.print("Root: " + move.getRootMove());
                    System.out.print("Jump Tiles: ");
                    for (GameBoardTile jumpedTile : move.getTotalJumpedTiles())
                        System.out.println("\n\t" + jumpedTile);
                    System.out.println();
                }
            } else if (e.getSource() == capture) {
                Random rng = new Random();
                try {
                    if (board.whoWon() == redPlayer)
                        System.out.println("red");
                    else if (board.whoWon() == bluePlayer)
                        System.out.println("blue");
                    else {
                        if (!isRed) {
                            ArrayList<LegalMove> possibleMoves = bluePlayer.getAllPossibleValidMoves();
                            int upperBound = possibleMoves.size();
                            LegalMove randomMove = possibleMoves.get(rng.nextInt(upperBound));
                            bluePlayer.movePiece(randomMove); // executes random move
                            isRed = !isRed;
                        } else {
                            ArrayList<LegalMove> possibleMoves = redPlayer.getAllPossibleValidMoves();
                            int upperBound = possibleMoves.size();
                            LegalMove randomMove = possibleMoves.get(rng.nextInt(upperBound));
                            redPlayer.movePiece(randomMove); // executes random move
                            isRed = !isRed;
                        }
                    }

                } catch (InvalidMoveException ime) {
                    ime.printCustomError();
                }

            }

        }
    }

    private class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == generation) {
                GenerationViewer view = new GenerationViewer();
                view.display();
            } else if (e.getSource() == train) {
                CheckersGame view = new CheckersGame();
                view.display();
            }
        }
    }

}
