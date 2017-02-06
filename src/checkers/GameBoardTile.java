package checkers;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Represents one tile in the whole gameboard
 * Is JLabel with tile as an ImageIcon
 * Tiles can be occupied by checker piece
 * Hovering the mouse over a tile will give debug information
 */
public class GameBoardTile extends JLabel {

    private TileColors tilecolor;

    private int yGrid;
    private int xGrid;

    private boolean isOccupied = false;
    private CheckerPiece currentPiece;


    private BufferedImage tile;

    private JLabel selectedLabel = new JLabel();
    private BufferedImage selected;
    private boolean isSelected = false;
    private LegalMove possibleTileMove;

    public GameBoardTile(TileColors color, int yGrid, int xGrid) {
        tilecolor = color;
        this.yGrid = yGrid;
        this.xGrid = xGrid;

        // Set image based on color tile
        try {
            if (tilecolor.equals(TileColors.BLACK)) {
                tile = ImageIO.read(getClass().getResource("BlackTile.png"));
            } else {
                tile = ImageIO.read(getClass().getResource("GreyTile.png"));
            }
            selected = ImageIO.read(getClass().getResource("Selected.png"));
        } catch (IOException io) {
            io.printStackTrace();
        }

        selectedLabel.setIcon(new ImageIcon(selected));
        setIcon(new ImageIcon(tile));
        addMouseListener(new MouseComponentListener());
        setLayout(new GridBagLayout());
    }

    public void movePiece(CheckerPiece piece) throws InvalidMoveException {
        // Get the tile the piece was on before and remove it
        GameBoardTile previousTile = piece.getCurrentTile();
        previousTile.removePiece(piece);

        // Move the piece the new tile
        isOccupied = true;
        currentPiece = piece;
        add(piece);
        repaint();
    }

    private void selectTile(LegalMove possibleTileMove) {
        this.possibleTileMove = possibleTileMove;

        isSelected = true;
        add(selectedLabel);
        repaint();
        validate();
    }

    private void unselectTile() {
        if (!isSelected)
            System.out.println("Tile already clear");
        else {
            remove(selectedLabel);
            repaint();
            validate();
            isSelected = false;
        }
    }

    // Removes the piece from this tile
    public void removePiece(CheckerPiece piece) throws InvalidMoveException {
        if (isOccupied) {
            isOccupied = false;
            currentPiece = null;
            remove(piece);
            repaint();
        } else
            throw new InvalidMoveException("Attempted to remove unoccupied tile");
    }

    // Setup method for resetting board. SHOULD ONLY BE CALLED ONCE DURING BOARD SETUP
    public void setUp(CheckerPiece piece) {
        isOccupied = true;
        currentPiece = piece;
        add(piece);
    }

    public boolean getIsOccupied() {
        return isOccupied;
    }

    public CheckerPiece getCurrentPiece() {
        return currentPiece;
    }

    public int returnX() {
        return xGrid;
    }

    public int returnY() {
        return yGrid;
    }

    @Override
    public String toString() {
        return ("(Row: " + yGrid + " Col: " + xGrid + ") \t" + "Current: " + currentPiece);
    }

    private class MouseComponentListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON3) // Mouse2
                unselectTile();

            else if (e.getButton() == MouseEvent.BUTTON1) { // Mouse1
                if (!isSelected) { // Should I check for tiles, or should I examine a checkers.LegalMove
                    for (LegalMove move : currentPiece.getAllMoves()) {
                        System.out.println("Legal Move: " + move.getNewTile());
                        move.getNewTile().selectTile(move);
                    }
                } else {
                    System.out.println(possibleTileMove.getPastMoves());
                }
            }

             else if (e.getButton() == MouseEvent.BUTTON2) { // Middle Mouse Button
                System.out.println("(Row: " + yGrid + " Col: " + xGrid + ") \t" + "Current: " + currentPiece);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }


        @Override
        public void mouseEntered(MouseEvent e) {
            System.out.println("(Row: " + yGrid + " Col: " + xGrid + ") \t" + "Current: " + currentPiece);
        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

}
