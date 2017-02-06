package checkers;

import org.encog.ml.CalculateScore;
import org.encog.ml.MLMethod;
import org.encog.neural.neat.NEATNetwork;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Represents the Blue checkers.Player
 */
public class BluePlayer implements Player {

    private CheckerPiece[] bluePieces = new CheckerPiece[12];
    private GameBoard board;
    private boolean canJump = false;

    private final PieceColors teamColor = PieceColors.BLUE;

    private NEATNetwork network;

    public BluePlayer(GameBoard board) {
        this.board = board;
    }

    @Override
    public CheckerPiece[] getPieces() {
        return bluePieces;
    }

    // Returns every single move that every single piece can make
    private ArrayList<ArrayList<LegalMove>> getAllPossibleMoves() {
        ArrayList<ArrayList<LegalMove>> allPossibleMoves = new ArrayList<>();

        for (CheckerPiece piece : bluePieces) {
            ArrayList<LegalMove> temp = piece.getAllMoves();

            if (temp == null) {
                //System.out.println("Null moves");

            } else if (!temp.isEmpty() && !piece.isCaptured()) {
                allPossibleMoves.add(temp);

                for (LegalMove move : temp) {
                    if (move.getJumpedTile() != null) { //Is there a jumped move
                        canJump = true;
                        break;
                    }
                }
            } else {
                for (LegalMove move : temp) {
                    if (move.getJumpedTile() != null) { //Is there a jumped move
                        canJump = true;
                        break;
                    }
                }
            }
        }

        return allPossibleMoves;
    }

    // Returns array of valid moves (factoring in force jumping)
    @Override
    public ArrayList<LegalMove> getAllPossibleValidMoves() {
        ArrayList<LegalMove> allPossibleValidMoves = new ArrayList<>();

        // If no moves available, return empty array
        ArrayList<ArrayList<LegalMove>> possibleMoves = getAllPossibleMoves();
        if (possibleMoves.size() == 0)
            return allPossibleValidMoves;

        for (ArrayList<LegalMove> movesArray : possibleMoves) {
            for (LegalMove moves : movesArray) {
                if (moves.getMoveAfter().get(0) == null && moves.getMoveAfter().size() == 1) // If move is "final move" (has no more jumps)
                    if (canJump) { //Am I forced to jump or not
                        if (moves.getJumpedTile() != null) { // Am forced to jump, only add pieces that can jump
                            allPossibleValidMoves.add(moves);
                        }
                    } else {
                        allPossibleValidMoves.add(moves); // Not forced to jump, add non jumped piece
                    }
            }
        }

        // Return values and reset jump flag
        canJump = false;
        return allPossibleValidMoves;
    }

    @Override
    public GameBoard getBoard() {
        return board;
    }

    public void movePiece(LegalMove move) throws InvalidMoveException {
        boolean isValid = false;

        if (getAllPossibleValidMoves().isEmpty())
            throw new InvalidMoveException("Blue checkers.Player has no possible moves");

        for (LegalMove validMove : getAllPossibleValidMoves()) {
            if (validMove.equals(move)) {
                isValid = true;
                break;
            }
        }
        if (isValid) { // Valid Move
            CheckerPiece currentPiece = move.getRootMove().getOldPiece();

            move.captureJumpedPieces();
            currentPiece.movePiece(move.getNewTile());

            // Piece has reached king spot!
            if (move.getNewTile().returnY() == 0) {
                currentPiece.makeKing();
            }
        } else { // Not valid
            throw new InvalidMoveException("Move is not valid!");
        }
    }

    public PieceColors getTeamColor() {
        return teamColor;
    }

    public double[] convertBoard() {
        GameBoardTile[] tileArray = board.getTileOneArray();

        double[] boardData = new double[tileArray.length];
        for (int i = 0; i < tileArray.length; i++) {
            CheckerPiece piece = tileArray[i].getCurrentPiece();

            if (piece == null) {
                boardData[i] = 0;
            } else if (piece.getColor() == PieceColors.BLUE) {
                if (piece.getIsKing())
                    boardData[i] = 1;
                else
                    boardData[i] = 0.5; // normal piece
            } else { // Tile has red piece
                if (piece.getIsKing())
                    boardData[i] = -1;
                else
                    boardData[i] = -0.5; // normal piece
            }
        }

        return boardData;
    }

    public boolean allPiecesCaptured() {
        for (CheckerPiece piece : bluePieces) {
            if (!piece.isCaptured())
                return false;
        }
        return true;
    }

    public void setNetwork(NEATNetwork net) {
        network = net;
    }
    public NEATNetwork getNetwork() {
        return network;
    }
}
