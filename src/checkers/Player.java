package checkers;

import org.encog.ml.CalculateScore;

import java.util.ArrayList;

/**
 * Created by Darwin on 1/31/2017.
 */
public interface Player  {
    CheckerPiece[] getPieces();
    ArrayList<LegalMove> getAllPossibleValidMoves();
    GameBoard getBoard();
    boolean allPiecesCaptured();
}
