package Pieces;

import Chess.Board;

import java.awt.image.BufferedImage;

public class Bishop extends Piece {
    public Bishop(Board board, int col, int row, boolean isWhite) {
        super(board);
        this.col = col;
        this.row = row;
        this.xPos = col * board.tileSize;
        this.yPos = row * board.tileSize;
        this.isWhite = isWhite;
        this.name = "Bishop";
        this.sprite = sheet.getSubimage(2 * sheetScale, isWhite ? 0 : sheetScale, sheetScale, sheetScale)
                .getScaledInstance(board.tileSize, board.tileSize, BufferedImage.SCALE_SMOOTH);
    }

    public boolean isValidMovement(int col, int row) {
        // The bishop can move diagonally, so when the column and row differences are equal
        // (the distance moved horizontally needs to be equal to the distance moved vertically)
        return Math.abs(this.col - col) == Math.abs(this.row - row);
    }

    public boolean moveCollidesWithPiece(int col, int row) {

        // If a piece is found on the way to the position we want to move to, the bishop collides with it
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
        return false;
    }

}
