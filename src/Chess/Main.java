package Chess;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Main {

    public static void main(String[] args) {

        JFrame frame = new JFrame(); // Create the main game window
        frame.getContentPane().setBackground(Color.BLACK); // Set the background color of the frame's content area to black
        frame.setLayout(new GridBagLayout()); // Set the layout for the frame to grid-based
        frame.setMinimumSize(new Dimension(1000, 1000)); // Set the minimum size of the frame to 1000x1000 pixels
        frame.setLocationRelativeTo(null); // Center the frame on the screen

        Board board = new Board();
        frame.add(board); // Add the board instance to the frame

        JButton resetButton = new JButton("Start New Game");
        resetButton.setPreferredSize(new Dimension(board.getPreferredSize().width, 40));
        resetButton.setBackground(new Color(212, 145, 212));
        resetButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        resetButton.setFont(new Font("Arial", Font.BOLD, 20));
        resetButton.addActionListener(e -> { board.resetGame(); });
        frame.add(resetButton, new GridBagConstraints() {{
            gridx = 0;
            gridy = 1;
            weightx = 1.0;
            fill = GridBagConstraints.NONE;
            anchor = GridBagConstraints.CENTER;
            insets = new Insets(10, 0, 0, 0);
        }});

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true); // Make the frame visible to the user

    }
}
