package Pieces;

import Chess.Board;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Piece {

    public int col, row; // Chess piece's position on the board
    public int xPos, yPos; // Pixel coordinates of the piece on the screen

    public boolean isWhite;
    public String name;
    public int value;

    public boolean isFirstMove = true;

    BufferedImage sheet; // The sprite sheet that contains all chess piece images

    // Static block for loading the sprite sheet from resources, executed when the class is loaded into memory
    {
        try {
            // Loads the chess piece image from the resource file
            sheet = ImageIO.read(ClassLoader.getSystemResourceAsStream("chessPieces.png"));
            // ImageIO.read() reads an image from an InputStream and returns a BufferedImage object representing the image.
            // ClassLoader.getSystemResourceAsStream loads a resource from the classpath, returning an InputStream.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // The size of each piece image in the sprite sheet
    protected int sheetScale = sheet.getWidth() / 6; // The sprite sheet contains 6 columns of images

    // The sprite image for the piece
    Image sprite;

    Board board;

    // Constructor that associates the piece with the chessboard
    public Piece(Board board) {
        this.board = board;
    }

    public boolean isValidMovement(int col, int row) {
        return true;
    }

    public boolean moveCollidesWithPiece(int col, int row) {
        return false;
    }

    public void paint(Graphics2D g2d) {

        // Enable rendering hints for smoother image scaling and rendering quality
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw the piece's image at its current position on the screen
        g2d.drawImage(sprite, xPos, yPos, null);

    }
}
