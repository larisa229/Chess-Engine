package Chess;

import Pieces.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Board extends JPanel {

    // the FEN strings store the state of the board at each point of the game
    // each letter of a FEN string represents a piece, and each number the number of empty squares
    // rook(r), knight(n), bishop(b), queen(q), king(k), pawn(p)
    // '/' represents new line
    // 'w' separated by spaces means it's white's turn to move, when it's black's turn it changes to 'b'
    // the next section represents where kings can castle: king side - 'k/K', queen side - 'q/Q'
    // the place where the '-' is represents what square the player can en passant on
    // the 0 represents the half-move clock, counting the number of half-moves since the last pawn move or capture
    // the 1 represents the full-move number, the count of the number of full moves in the game (one turn by white followed by one turn by black)
    // lowercase represents black pieces and uppercase white pieces
    public String fenStartingPosition = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    // Size of each tile on the chessboard in pixels
    public int tileSize = 85;

    int cols = 8;
    int rows = 8;

    // List that stores the chess pieces currently on the board
    ArrayList<Piece> pieceList = new ArrayList<>();

    public Piece selectedPiece;

    Controller input = new Controller(this);

    public CheckScanner checkScanner = new CheckScanner(this);

    public int enPassantTile = -1;

    private boolean isWhiteToMove = true;
    private boolean isGameOver = false;

    // Add a new field for the game status label
    private JLabel gameStatusLabel;

    public Board() {
        // Create a new layout that allows vertical stacking
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Create the game status label
        gameStatusLabel = new JLabel(" ");
        gameStatusLabel.setForeground(Color.WHITE); // White text
        gameStatusLabel.setFont(new Font("Arial", Font.BOLD, 70)); // Large, bold font
        gameStatusLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center align the label
        gameStatusLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
        gameStatusLabel.setVerticalAlignment(SwingConstants.CENTER);
        gameStatusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        // Add the label to the board
        add(gameStatusLabel);

        this.setPreferredSize(new Dimension(cols * tileSize, rows * tileSize));

        this.addMouseListener(input);
        this.addMouseMotionListener(input);

        loadPositionFromFEN(fenStartingPosition);
    }

    public void resetGame() {
        loadPositionFromFEN(fenStartingPosition);
        isWhiteToMove = true;
        isGameOver = false;
        gameStatusLabel.setText(" ");
        repaint();
    }

    // Get a piece from a specific position
    public Piece getPiece(int col, int row) {
        for (Piece piece : pieceList) {
            if(piece.col == col && piece.row == row) {
                return piece;
            }
        }
        return null;
    }

    // When a piece is captured we remove it from the list of pieces currently in the game
    public void capture(Piece piece) {
        pieceList.remove(piece);
    }

    // Check if 2 pieces are in the same team
    public boolean sameTeam(Piece piece1, Piece piece2) {
        if (piece1 == null || piece2 == null) {
            return false;
        }
        return piece1.isWhite == piece2.isWhite;
    }

    // Convert the (col, row) coordinates into a single number for tracking and comparing the en passant tile
    public int getTileNum(int col, int row) {
        return row * rows + col;
    }

    // Find the king in the list of pieces
    Piece findKing(boolean isWhite) {
        for(Piece piece : pieceList) {
            if(isWhite == piece.isWhite && piece.name.equals("King")) {
                return piece;
            }
        }
        return null;
    }

    public void makeMove(Move move) {

        if(move.piece.name.equals("Pawn")) {
            movePawn(move); // Adapt the move for a pawn
        } else {
            enPassantTile = -1;
        }

        if (move.piece.name.equals("King")) {
                moveKing((move)); // Adapt the move for a king
        }

        // Update the position of the moved piece to the new row and column
        move.piece.col = move.newCol;
        move.piece.row = move.newRow;
        // Also update the position on the frame
        move.piece.xPos = move.newCol * tileSize;
        move.piece.yPos = move.newRow * tileSize;

        // After the move is made we mark isFirstMove as false
        move.piece.isFirstMove = false;

        // If a piece is captured with this move, we remove it
        capture(move.capture);

        // The move is made, so we make sure to change turns
        isWhiteToMove = !isWhiteToMove;

        // Get the updated game state
        updateGameState();
    }

    private void moveKing(Move move) {
        // Check if the king is trying to move 2 squares on a row (castling)
        if (Math.abs(move.piece.col - move.newCol) == 2) {
            Piece rook;
            if (move.piece.col < move.newCol) { // if the king wants to move to the right, we take the rook from the 7th column
                rook = getPiece(7, move.piece.row);
                rook.col = 5; // this will be the rook's column after castling
            } else { // if the king is moving towards the left side, we take the rook from the left
                rook = getPiece(0, move.piece.row);
                rook.col = 3;
            }
            rook.xPos = rook.col * tileSize; // we update also the position of the rook on the screen
        }
    }

    // Promote the pawn by replacing it with a piece of the user's choosing of the same color, the original pawn is removed
    private void promotePawn(Move move) {
        String[] options = {"Queen", "Rook", "Bishop", "Knight"}; // the options for replacement
        String choice = (String) JOptionPane.showInputDialog(
                null,
                "Choose a piece for promotion:",
                "Pawn Promotion",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0] // Default to Queen
        );

        Piece newPiece = null;

        // Create the chosen piece
        switch (choice) {
            case "Rook":
                newPiece = new Rook(this, move.newCol, move.newRow, move.piece.isWhite);
                break;
            case "Bishop":
                newPiece = new Bishop(this, move.newCol, move.newRow, move.piece.isWhite);
                break;
            case "Knight":
                newPiece = new Knight(this, move.newCol, move.newRow, move.piece.isWhite);
                break;
            case "Queen": // Default to Queen
            default:
                newPiece = new Queen(this, move.newCol, move.newRow, move.piece.isWhite);
                break;
        }

        // Add the new piece to the list and remove the pawn
        pieceList.add(newPiece);
        capture(move.piece);
    }

    public void movePawn(Move move) {

        int colorIndex = move.piece.isWhite ? 1 : -1;

        // En Passant
        // If the tile to which the move is made matches the en passant tile, the captured piece is the one directly behind the pawn's current position
        if(getTileNum(move.newCol, move.newRow) == enPassantTile) {
            move.capture = getPiece(move.newCol, move.newRow + colorIndex);
        }
        // If the pawn moves 2 squares forward, set the en passant tile on the tile behind the pawn's new position
        if(Math.abs(move.piece.row - move.newRow) == 2) {
            enPassantTile = getTileNum(move.newCol, move.newRow + colorIndex);
        } else {
            enPassantTile = -1; // Otherwise reset it to -1, meaning en passant is not possible
        }

        // Promotions
        // A pawn gets promoted when it reaches the opposite side of the board, row 0 for white and row 7 for black
        colorIndex = move.piece.isWhite ? 0 : 7;
        if(move.newRow == colorIndex) {
            promotePawn(move);
        }
    }

    public boolean isValidMove(Move move) {
        // If the game is over, allow no more moves
        if (isGameOver) {
            return false;
        }
        // Allow someone to move only if it's their turn
        if (move.piece.isWhite != isWhiteToMove) {
            return false;
        }
        // If the piece moved and the piece captured are in the same team, don't allow the move
        if (sameTeam(move.piece, move.capture)) {
            return false;
        }
        // If the move is not valid by the rules of chess don't allow it
        if (!move.piece.isValidMovement(move.newCol, move.newRow)) {
            return false;
        }
        // If the piece can't be moved because of other pieces in its way, don't allow the move
        if (move.piece.moveCollidesWithPiece(move.newCol, move.newRow)) {
            return false;
        }
        // If the king is checked, don't allow him to move
        if(checkScanner.isKingChecked(move)) {
            return false;
        }
        return true;
    }

    public void loadPositionFromFEN(String fenString) {
        pieceList.clear();
        String[] parts = fenString.split(" "); // split the string by spaces to get the different sections
        // Process the part that stores the pieces placements
        String position = parts[0];
        int row = 0;
        int col = 0;
        for(int i = 0; i < position.length(); i++) {
            char ch = position.charAt(i); // go through each character
            if (ch == '/') { // if it's a '/' go down to the next row, and begin again from column 0
                row++;
                col = 0;
            } else if (Character.isDigit(ch)) { // if the character is a digit, add it to the column
                col += Character.getNumericValue(ch);
            } else {
                boolean isWhite = Character.isUpperCase(ch); // if the character is uppercase, the piece is white
                char pieceChar = Character.toLowerCase(ch); // get the pieces all in lowercase
                // go through each character and add the corresponding piece
                switch (pieceChar) {
                    case 'r' :
                        pieceList.add(new Rook(this, col, row, isWhite));
                        break;
                    case 'n' :
                        pieceList.add(new Knight(this, col, row, isWhite));
                        break;
                    case 'b' :
                        pieceList.add(new Bishop(this, col, row, isWhite));
                        break;
                    case 'q' :
                        pieceList.add(new Queen(this, col, row, isWhite));
                        break;
                    case 'k' :
                        pieceList.add(new King(this, col, row, isWhite));
                        break;
                    case 'p' :
                        pieceList.add(new Pawn(this, col, row, isWhite));
                        break;
                }
                col++; // go to the next square in the row
            }
        }
        isWhiteToMove = parts[1].equals("w"); // see whose turn it is to move
        // Castling section
        // Get the piece from each corner and if it's a rook, see if the king can castle in that direction
        Piece bqr = getPiece(0, 0);
        if(bqr instanceof Rook) {
            bqr.isFirstMove = parts[2].contains("q");
        }
        Piece bkr = getPiece(7, 0);
        if(bkr instanceof Rook) {
            bkr.isFirstMove = parts[2].contains("k");
        }
        Piece wqr = getPiece(0, 7);
        if(wqr instanceof Rook) {
            wqr.isFirstMove = parts[2].contains("Q");
        }
        Piece wkr = getPiece(7, 7);
        if(wkr instanceof Rook) {
            wkr.isFirstMove = parts[2].contains("K");
        }
        // En passant section
        if(parts[3].equals("-")) {
            enPassantTile = -1; // if it's a '-' you can't en passant
        } else {
            // convert the chess notation into a number that can be given to the en passant tile
            enPassantTile = (7 - (parts[3].charAt(1) - '1')) * 8 + (parts[3].charAt(0) - 'a');
        }

    }

    private void updateGameState() {
        gameStatusLabel.setText("");
        // Find the king of the player who is currently supposed to move
        Piece king = findKing(isWhiteToMove);
        if (checkScanner.isGameOver(king)) {
            // If the game is over because of the king being in check, based on whose turn it is, print the result o the game
            if (checkScanner.isKingChecked(new Move(this, king, king.col, king.row))) {
                String winner = isWhiteToMove ? "Black" : "White";
                gameStatusLabel.setText(winner + " wins!");
            } else {
                // If the game is over, but the king is not in check, that means stalemate
                gameStatusLabel.setText("Stalemate!");
            }
            isGameOver = true;
        } else if(checkScanner.isKingChecked(new Move(this, king, king.col, king.row))) {
            gameStatusLabel.setText("Check!");
        } else if (insufficientMaterial(true) && insufficientMaterial(false)) {
            // If both players have insufficient pieces to continue the game, the game is also over
            gameStatusLabel.setText("Draw!");
            isGameOver = true;
        }
    }

    private boolean insufficientMaterial(boolean isWhite) {
        ArrayList<String> names = pieceList.stream() // converts the list of pieces into a stream so that we can process it using map and filter
                .filter(p -> p.isWhite == isWhite) // filters the pieces in the stream to only include the passed color
                .map(p -> p.name) // transforms each piece into its name
                .collect(Collectors.toCollection(ArrayList :: new)); // collects the transformed data into an ArrayList
        if (names.contains("Queen") || names.contains("Rook") || names.contains("Pawn")) {
            // If the remaining pieces include a queen, a rook or a pawn, it means the player has sufficient material to continue the game
            return false;
        }
        return names.size() < 3; // If the player has no queen, rook or pawn and has less than 3 pieces, he has insufficient material
    }

    public void paintComponent(Graphics g) {

        //Use Graphics for drawing on the components
        Graphics2D g2d = (Graphics2D) g;

        // Paint the board
        for(int r = 0; r < rows; r++) {
            for(int c = 0; c < cols; c++) {
                // Set the tile color based on its position
                g2d.setColor((c + r) % 2 == 0 ? new Color(212, 145, 212) : new Color(113, 38, 113));
                // Fill the rectangle for the current tile
                g2d.fillRect(c * tileSize, r * tileSize, tileSize, tileSize);
            }
        }

        // Paint highlights when a piece is selected
        if(selectedPiece != null) {
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    // Repaint the squares that indicate the places where the selected piece can be moved
                    if (isValidMove(new Move(this, selectedPiece, c, r))) {
                        g2d.setColor(new Color(255, 0, 255, 179));
                        g2d.fillRect(c * tileSize, r * tileSize, tileSize, tileSize);
                    }
                }
            }
        }

        // Paint all pieces in the pieceList
        for(Piece piece : pieceList) {
            piece.paint(g2d);
        }
    }
}
