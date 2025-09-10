# Java Chess Engine

This project is a two-player chess engine implemented in Java, designed to simulate a complete chess game with proper rule enforcement and piece interactions. It supports move validation, check and checkmate detection, and legal move restrictions to ensure gameplay follows standard chess rules.

## Features
- Standard chess rules - Fully functional chessboard with legal moves for all pieces.
- Move validation - Ensures only legal moves are allowed for king, queen, rook, bishop, knight, and pawn.
- Game state handling - Detection of check, checkmate, and stalemate.
- Draw handling - Stalemate and insufficient material.
- Graphical interface - Basic Java Swing UI for interactive two-player gameplay.

## Technologies Used
- **Language:** Java
- **Frameworks & Tools:** Java Swing, IntelliJ IDEA
- **Concepts Used:** Object-Oriented Programming (OOP), Algorithms, Game Logic

## How It Works
The game represents the chessboard as an 8x8 grid, where each piece is modeled as an object with its own movement rules. When a move is attempted, the engine validates it against chess rules. The system prevents illegal moves and updates the game state. The engine continuously checks for check and checkmate conditions to enforce victory detection.

## Future Improvements
- Enhance the GUI with better visuals and user-friendly interactions.
- Add an AI opponent for single-player mode.
- Implement a move history log to track and display players' moves.
