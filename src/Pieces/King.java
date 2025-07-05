package Pieces;

import Chess.Board;
import Chess.Move;

import java.awt.image.BufferedImage;

public class King extends Piece {
    public King(Board board, int col, int row, boolean isWhite) {
        super(board);
        this.col = col;
        this.row = row;
        this.xPos = col * board.tileSize;
        this.yPos = row * board.tileSize;
        this.isWhite = isWhite;
        this.name = "King";
        this.sprite = sheet.getSubimage(0, isWhite ? 0 : sheetScale, sheetScale, sheetScale)
                .getScaledInstance(board.tileSize, board.tileSize, BufferedImage.SCALE_SMOOTH);
    }

    public boolean isValidMovement(int col, int row) {
        // The king can move one square in any direction, and also can perform castling
        // The diagonal moves are represented by the product of the differences equal to 1 (the differences can be either 1 and 1 or -1 and -1)
        // The vertical and horizontal moves are given by the sum of differences equal to 1 (one of the differences is 1 and one is 0)
        return Math.abs((col - this.col) * (row - this.row)) == 1
                || Math.abs(col - this.col) + Math.abs(row - this.row) == 1 || canCastle(col, row);
    }

    // Castling is a special move in chess where the King moves two squares towards a rook, and that rook moves to the other side of the King.
    // Castling is allowed only under specific conditions:
    // - The King and rook involved have not moved before.
    // - There are no pieces between the King and the rook.
    // - The King is not in check, and the squares the King moves across are not under attack.
    private boolean canCastle(int col, int row) {
        // Check that the king is moving within the same row
        if (this.row == row) {
            if(col == 6) { // the king should be trying to move to the column 6 for castling to the right
                Piece rook = board.getPiece(7, row); // get the piece on the column 7, which should be the rook
                if (rook != null && rook.isFirstMove && isFirstMove) { // if the piece is indeed a rook, and neither it nor the king has moved before
                    return board.getPiece(5, row) == null && // check if the square between the king and the rook is empty
                           board.getPiece(6, row) == null && // check if the square where the king is trying to move is empty
                           !board.checkScanner.isKingChecked(new Move(board, this, 5, row)); // ensure that the king is not in check when it moves to column 5
                }
            } else if(col == 2) { // the king should be trying to move to the column 2 for castling to the left
                // This is done symmetrically on the right side of the king
                Piece rook = board.getPiece(0, row);
                if (rook != null && rook.isFirstMove && isFirstMove) {
                    return board.getPiece(3, row) == null &&
                           board.getPiece(2, row) == null &&
                           board.getPiece(1, row) == null &&
                           !board.checkScanner.isKingChecked(new Move(board, this, 3, row));
                }
            }
        }
        return false;
    }

}

