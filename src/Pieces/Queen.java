package Pieces;

import Chess.Board;

import java.awt.image.BufferedImage;

public class Queen extends Piece {
    public Queen(Board board, int col, int row, boolean isWhite) {
        super(board);
        this.col = col;
        this.row = row;
        this.xPos = col * board.tileSize;
        this.yPos = row * board.tileSize;
        this.isWhite = isWhite;
        this.name = "Queen";
        this.sprite = sheet.getSubimage(sheetScale, isWhite ? 0 : sheetScale, sheetScale, sheetScale)
                .getScaledInstance(board.tileSize, board.tileSize, BufferedImage.SCALE_SMOOTH);
    }

    // The queen can move in any direction, any number of squares
    public boolean isValidMovement(int col, int row) {
        // Column equality represents the vertical movement and row equality represents horizontal movement
        // When the column and row differences are equal, that is a diagonal movement
        return this.col == col || this.row == row || Math.abs(this.col - col) == Math.abs(this.row - row);
    }

    public boolean moveCollidesWithPiece(int col, int row) {

        // The queen has the rook's and the bishop's rules combined
        if(this.col == col || this.row == row) { // In the case the queen moves like a rook
            // Check left direction
            if (this.col > col) {
                for (int c = this.col - 1; c > col; c--) {
                    if (board.getPiece(c, this.row) != null) {
                        return true;
                    }
                }
            }

            // Check right direction
            if (this.col < col) {
                for (int c = this.col + 1; c < col; c++) {
                    if (board.getPiece(c, this.row) != null) {
                        return true;
                    }
                }
            }

            // Check up direction
            if (this.row > row) {
                for (int r = this.row - 1; r > row; r--) {
                    if (board.getPiece(this.col, r) != null) {
                        return true;
                    }
                }
            }

            // Check down direction
            if (this.row < row) {
                for (int r = this.row + 1; r < row; r++) {
                    if (board.getPiece(this.col, r) != null) {
                        return true;
                    }
                }
            }
        } else { // If the queen moves like a bishop
            // Check up left direction
            if(this.col > col && this.row > row) {
                for(int i = 1; i < Math.abs(this.col - col); i++) {
                    if(board.getPiece(this.col - i, this.row - i) != null) {
                        return true;
                    }
                }
            }

            // Check up right direction
            if(this.col < col && this.row > row) {
                for(int i = 1; i < Math.abs(this.col - col); i++) {
                    if(board.getPiece(this.col + i, this.row - i) != null) {
                        return true;
                    }
                }
            }

            // Check down left direction
            if(this.col > col && this.row < row) {
                for(int i = 1; i < Math.abs(this.col - col); i++) {
                    if(board.getPiece(this.col - i, this.row + i) != null) {
                        return true;
                    }
                }
            }

            // Check down right direction
            if(this.col < col && this.row < row) {
                for(int i = 1; i < Math.abs(this.col - col); i++) {
                    if(board.getPiece(this.col + i, this.row + i) != null) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}

