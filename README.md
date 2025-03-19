# Java Chess Engine

This is a two-player chess engine implemented in Java, featuring move validation, checkmate detection, and a structured object-oriented design. The project aims to simulate a playable chess game with rule enforcement and piece interactions.

## Features
- Fully functional chessboard with all standard chess rules.
- Move validation for each piece (king, queen, rook, bishop, knight, and pawn).
- Check and checkmate detection.
- Basic user interface (GUI using Java Swing).

## Technologies Used
- **Language:** Java
- **IDE:** IntelliJ IDEA
- **Concepts Used:** Object-Oriented Programming (OOP), Algorithms, Game Logic

## 📌 How to Run
1. Clone the repository:
   ```sh
   git clone https://github.com/yourusername/chess-engine.git
   ```
2. Open the project in IntelliJ IDEA (or any Java IDE).
3. Compile and run the main class:
   ```sh
   javac Main.java
   java Main
   ```
4. Play the game in the console (GUI version under development).

## How It Works
- The game uses an **8×8 board representation**, where each piece is an object.
- Move validation follows standard chess rules.
- The engine checks for checkmate and prevents illegal moves.

## Future Improvements
- Improving the graphical user interface (GUI).
- Adding AI to allow single-player mode against a computer opponent.
- Keep track of the players' movements and display them in the GUI.

