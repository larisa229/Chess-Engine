package Pieces;

import Chess.Board;

import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;

public class Pawn extends Piece {
    public Pawn(Board board, int col, int row, boolean isWhite) {
        super(board);
        this.col = col;
        this.row = row;
        // Calculate the coordinates based on column/row and tile size
        this.xPos = col * board.tileSize;
        this.yPos = row * board.tileSize;
        this.isWhite = isWhite;
        this.name = "Pawn";
        this.sprite = sheet.getSubimage(5 * sheetScale, isWhite ? 0 : sheetScale, sheetScale, sheetScale)
                .getScaledInstance(board.tileSize, board.tileSize, BufferedImage.SCALE_SMOOTH);
    }

    public boolean isValidMovement(int col, int row) {

        // Determine the direction of the pawn movement based on color: if it's white it can only go up, if it's black only down
        int colorIndex = isWhite ? 1 : -1;

        // Push pawn 1 square
        // The pawn stays in the same column, but moves a row in the correct direction
        if(this.col == col && row == this.row - colorIndex && board.getPiece(col, row) == null)
            return true;

        // Push pawn 2 squares
        // The first move of a pawn starts from row 6 for a white one and row 1 for a black one
        // It can move 2 rows in the correct direction, but both squares need to be empty
        if(this.row == (isWhite ? 6 : 1) && this.col == col && row == this.row - colorIndex * 2
                && board.getPiece(col, row) == null && board.getPiece(col, row + colorIndex) == null)
            return true;

        // Capture diagonally to the left
        if(col == this.col - 1 && row == this.row - colorIndex && board.getPiece(col, row) != null) // Check if a piece is there
            return true;

        // Capture diagonally to the right
        if(col == this.col + 1 && row == this.row - colorIndex && board.getPiece(col, row) != null)
            return true;

        // If the previous move was the first move and the opponent's pawn moved 2 squares, the pawn can capture it by moving behind it
        // En Passant left
        if(board.getTileNum(col, row) == board.enPassantTile && col == this.col - 1 && row == this.row - colorIndex
                && board.getPiece(col, row + colorIndex) != null)
            return true;

        // En Passant right
        if(board.getTileNum(col, row) == board.enPassantTile && col == this.col + 1 && row == this.row - colorIndex
                && board.getPiece(col, row + colorIndex) != null)
            return true;

        return false;
    }

}
