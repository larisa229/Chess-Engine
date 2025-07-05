package Chess;

import Pieces.Piece;

public class CheckScanner {

    Board board;

    // The CheckScanner scans the board for potential threats to the king by other pieces

    public CheckScanner(Board board) {
        this.board = board;
    }

    // Checks if the king is in check after a particular move
    public boolean isKingChecked(Move move) {

        Piece king = board.findKing(move.piece.isWhite);
        assert king != null; // ensure that the king exists

        // Get the current position of the king
        int kingCol = king.col;
        int kingRow = king.row;

        if(board.selectedPiece != null && board.selectedPiece.name.equals("King")) { // If the king is the piece being moved
            kingCol = move.newCol; // Update the position of the king to the new column and row
            kingRow = move.newRow;
        }

        return
               // Check for threats from rooks or queens
               hitByRook(move.newCol, move.newRow, king, kingCol, kingRow, 0, 1) || // up
               hitByRook(move.newCol, move.newRow, king, kingCol, kingRow, 1, 0) || // right
               hitByRook(move.newCol, move.newRow, king, kingCol, kingRow, 0, -1) || // down
               hitByRook(move.newCol, move.newRow, king, kingCol, kingRow, -1, 0) || // left
               // Check for threats from bishops or queens
               hitByBishop(move.newCol, move.newRow, king, kingCol, kingRow, -1, -1) || // up left
               hitByBishop(move.newCol, move.newRow, king, kingCol, kingRow, 1, -1) || // up right
               hitByBishop(move.newCol, move.newRow, king, kingCol, kingRow, 1, 1) || // down right
               hitByBishop(move.newCol, move.newRow, king, kingCol, kingRow, -1, 1) || // down left
               // Check for threats from knights, pawns and the enemy king
               hitByKnight(move.newCol, move.newRow, king, kingCol, kingRow) ||
               hitByPawn(move.newCol, move.newRow, king, kingCol, kingRow) ||
               hitByKing(king, kingCol, kingRow);
    }

    // colVal and rowVal determine the direction in which we iterate to check if the king is threatened, horizontally or vertically
    private boolean hitByRook(int col, int row, Piece king, int kingCol, int kingRow, int colVal, int rowVal) {
        for(int i = 1; i < 8; i++) {
            if(kingCol + (i * colVal) == col && kingRow + (i * rowVal) == row) {
                break; // if the loop reaches the square where the current piece is moving, stop the scan in that direction
            }

            Piece piece = board.getPiece(kingCol + (i * colVal), kingRow + (i * rowVal)); // retrieves the piece at the square in the current direction
            if(piece != null && piece != board.selectedPiece) {
                // If we encounter a piece that is not the currently moving one and it's a rook or a queen from the opposite team, we stop searching
                if(!board.sameTeam(piece, king) && (piece.name.equals("Rook") || piece.name.equals("Queen"))) {
                    return true; // the king is in check
                }
                break;
            }
        }
        return false;
    }

    // Checks if the king is attacked by a bishop or a queen along diagonal lines
    private boolean hitByBishop(int col, int row, Piece king, int kingCol, int kingRow, int colVal, int rowVal) {
        for(int i = 1; i < 8; i++) {
            if(kingCol - (i * colVal) == col && kingRow - (i * rowVal) == row) {
                break;
            }

            Piece piece = board.getPiece(kingCol - (i * colVal), kingRow - (i * rowVal));
            if(piece != null && piece != board.selectedPiece) {
                if(!board.sameTeam(piece, king) && (piece.name.equals("Bishop") || piece.name.equals("Queen"))) {
                    return true;
                }
                break;
            }
        }
        return false;
    }

    private boolean hitByKnight(int col, int row, Piece king, int kingCol, int kingRow) {
        // Check all 8 possible knight moves relative to the king's position
        return checkKnight(board.getPiece(kingCol - 1, kingRow - 2), king, col, row) ||
               checkKnight(board.getPiece(kingCol + 1, kingRow - 2), king, col, row) ||
               checkKnight(board.getPiece(kingCol + 2, kingRow - 1), king, col, row) ||
               checkKnight(board.getPiece(kingCol + 2, kingRow + 1), king, col, row) ||
               checkKnight(board.getPiece(kingCol + 1, kingRow + 2), king, col, row) ||
               checkKnight(board.getPiece(kingCol - 1, kingRow + 2), king, col, row) ||
               checkKnight(board.getPiece(kingCol - 2, kingRow + 1), king, col, row) ||
               checkKnight(board.getPiece(kingCol - 2, kingRow - 1), king, col, row);
    }

    private boolean checkKnight(Piece p, Piece k, int col, int row) {
        return p != null && !board.sameTeam(p, k) && p.name.equals("Knight") && !(p.col == col && p.row == row);
    }

    private boolean hitByKing(Piece king, int kingCol, int kingRow) {
        // Check all the king moves relative to the attacked king's position
        return checkKing(board.getPiece(kingCol - 1, kingRow - 1), king) ||
               checkKing(board.getPiece(kingCol + 1, kingRow - 1), king) ||
               checkKing(board.getPiece(kingCol, kingRow - 1), king) ||
               checkKing(board.getPiece(kingCol - 1, kingRow), king) ||
               checkKing(board.getPiece(kingCol + 1, kingRow), king) ||
               checkKing(board.getPiece(kingCol - 1, kingRow + 1), king) ||
               checkKing(board.getPiece(kingCol + 1, kingRow + 1), king) ||
               checkKing(board.getPiece(kingCol, kingRow + 1), king);
    }

    private boolean checkKing(Piece p, Piece k) {
        return p != null && !board.sameTeam(p, k) && p.name.equals("King");
    }

    private boolean hitByPawn(int col, int row, Piece king, int kingCol, int kingRow) {
        int colorVal = king.isWhite ? -1 : 1; // determine the attack direction of pawns, upwards for white and downwards for black
        // Check the 2 diagonal positions where pawns can attack the king
        return checkPawn(board.getPiece(kingCol + 1, kingRow + colorVal), king, col, row) ||
               checkPawn(board.getPiece(kingCol - 1, kingRow + colorVal), king, col, row);
    }

    private boolean checkPawn(Piece p, Piece k, int col, int row) {
        return p != null && !board.sameTeam(p, k) && p.name.equals("Pawn") && !(p.col == col && p.row == row);
    }

    // Iterate through all the current player's pieces, for each one try every possible move on the boards
    // If any move is valid, the game is not over
    // If no moves are valid, the king is in check
    public boolean isGameOver(Piece king) {
        for (Piece piece : board.pieceList) {
            if (board.sameTeam(piece, king)) {
                board.selectedPiece = (piece == king) ? king : null;
                for (int row = 0; row < board.rows; row++) {
                   for (int col = 0; col < board.cols; col++) {
                       Move move = new Move(board, piece, col, row);
                       if (board.isValidMove(move)) {
                           return false;
                       }
                   }
                }
            }
        }
        return true;
    }
}
