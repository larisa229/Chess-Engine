package Pieces;

import Chess.Board;

import java.awt.image.BufferedImage;

public class Rook extends Piece {
    public Rook(Board board, int col, int row, boolean isWhite) {
        super(board);
        this.col = col;
        this.row = row;
        this.xPos = col * board.tileSize;
        this.yPos = row * board.tileSize;
        this.isWhite = isWhite;
        this.name = "Rook";
        this.sprite = sheet.getSubimage(4 * sheetScale, isWhite ? 0 : sheetScale, sheetScale, sheetScale)
                .getScaledInstance(board.tileSize, board.tileSize, BufferedImage.SCALE_SMOOTH);
    }

    // The rook can move vertically or horizontally, so either the column or the row stays the same, while the other changes
    public boolean isValidMovement(int col, int row) {
        return this.col == col || this.row == row;
    }

    public boolean moveCollidesWithPiece(int col, int row) {

        // If a piece is found on the way to the position we want to move to, the rook collides with it and it won't be permitted to move
        // Check left direction
        if(this.col > col) {
            for(int c = this.col - 1; c > col; c--) {
                if(board.getPiece(c, this.row) != null) {
                    return true;
                }
            }
        }

        // Check right direction
        if(this.col < col) {
            for(int c = this.col + 1; c < col; c++) {
                if(board.getPiece(c, this.row) != null) {
                    return true;
                }
            }
        }

        // Check up direction
        if(this.row > row) {
            for(int r = this.row - 1; r > row; r--) {
                if(board.getPiece(this.col, r) != null) {
                    return true;
                }
            }
        }

        // Check down direction
        if(this.row < row) {
            for(int r = this.row + 1; r < row; r++) {
                if(board.getPiece(this.col, r) != null) {
                    return true;
                }
            }
        }
        return false;
    }
}
