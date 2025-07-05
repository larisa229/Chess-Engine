package Chess;

import Pieces.Piece;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// MouseAdapter allows us to handle mouse events without implementing all the methods of MouseListener and MouseMotionListener
public class Controller extends MouseAdapter {

    Board board;

    public Controller(Board board) {
        this.board = board;
    }

    @Override
    public void mousePressed(MouseEvent e) {

        // Get the position of the clicked piece on the chessboard
        int col = e.getX() / board.tileSize;
        int row = e.getY() / board.tileSize;

        if (board.selectedPiece == null) {
            // If no piece was selected yet, retrieve the piece from the position the user clicked on
            Piece pieceXY = board.getPiece(col, row);
            if (pieceXY != null) {
                board.selectedPiece = pieceXY; // And set that as the selected piece
            }
        } else {
            // A piece is already selected, so try to move it
            Move move = new Move(board, board.selectedPiece, col, row);

            // If the intended move is permitted by the rules of chess we make it
            if (board.isValidMove(move)) {
                board.makeMove(move);
            }

            // Deselect the piece regardless of whether the move was valid
            board.selectedPiece = null;
        }

        // Repaint the board after the interaction to see the changes made
        board.repaint();

    }

}
