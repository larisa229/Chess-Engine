package Chess;

import Pieces.Piece;

public class Move {

    int oldCol, oldRow;
    int newCol, newRow;

    Piece piece; // the piece that will be moved
    Piece capture; // the piece that will be captured

    public Move(Board board, Piece piece, int newCol, int newRow) {

        // the original position of the piece to move
        this.oldCol = piece.col;
        this.oldRow = piece.row;
        // the position where the piece will be moved
        this.newCol = newCol;
        this.newRow = newRow;

        this.piece = piece;
        this.capture = board.getPiece(newCol, newRow); // get the piece from the new position as capture
    }
}
