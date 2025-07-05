package Pieces;

import Chess.Board;

import java.awt.image.BufferedImage;

public class Knight extends Piece {

    public Knight(Board board, int col, int row, boolean isWhite) {
        super(board);
        this.col = col;
        this.row = row;
        // The position of the piece on the screen, calculated by tileSize
        this.xPos = col * board.tileSize;
        this.yPos = row * board.tileSize;
        this.isWhite = isWhite;
        this.name = "Knight";
        this.sprite = sheet.getSubimage(3 * sheetScale, isWhite ? 0 : sheetScale, sheetScale, sheetScale)
                .getScaledInstance(board.tileSize, board.tileSize, BufferedImage.SCALE_SMOOTH);
        // Extract the knight's image from the sprite sheet and scale it to fit the size of the board tiles
        // BufferedImage.SCALE_SMOOTH: aims to give smooth scaling so that the image doesn't appear pixelated
    }

    // Check if the knight's move from the current position is valid
    // col and row represent the destination position
    public boolean isValidMovement(int col, int row) {
        // The knight can move 2 squares in one direction and one in the other or vice versa, so the product of the column and row differences needs to be 2
        return Math.abs(col - this.col) * Math.abs(row - this.row) == 2;
    }

}
