package com.javier.proyecto;

import javax.swing.*;
import java.awt.*;

public class Proyecto {
    private static char[][] board = new char[3][3];
    private static char playerActual = 'X';
    private static JButton[][] buttons = new JButton[3][3];
    private static int scoreX = 0, scoreO = 0;
    private static String player1Name = "Jugador 1", player2Name = "Jugador 2";

    public static JPanel createBoard(JLabel scoreLabel) {
        JPanel panel = new JPanel(new GridLayout(3, 3, 8, 8));
        panel.setBackground(Color.DARK_GRAY);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = ' ';
                buttons[i][j] = new JButton(" ");
                buttons[i][j].setFont(new Font("Arial", Font.BOLD, 50));
                buttons[i][j].setFocusPainted(false);
                buttons[i][j].setBackground(new Color(240, 240, 240));
                buttons[i][j].setForeground(Color.BLACK);

                buttons[i][j].addActionListener(e -> {
                    JButton button = (JButton) e.getSource();
                    int row = -1, col = -1;

                    for (int x = 0; x < 3; x++) {
                        for (int y = 0; y < 3; y++) {
                            if (buttons[x][y] == button) {
                                row = x;
                                col = y;
                                break;
                            }
                        }
                    }

                    if (board[row][col] == ' ') {
                        board[row][col] = playerActual;
                        button.setText(String.valueOf(playerActual));
                        button.setEnabled(false);
                        button.setBackground(playerActual == 'X' ? new Color(255, 200, 200) : new Color(200, 200, 255));

                        if (checkWinner()) {
                            if (playerActual == 'X') scoreX++;
                            else scoreO++;

                            scoreLabel.setText(player1Name + " " + scoreX + " | " + player2Name + " " + scoreO);
                            JOptionPane.showMessageDialog(null, "¡" + (playerActual == 'X' ? player1Name : player2Name) + " ha ganado!");
                            restartGame();
                        } else if (boardFull()) {
                            JOptionPane.showMessageDialog(null, "¡Empate!");
                            restartGame();
                        } else {
                            playerActual = (playerActual == 'X') ? 'O' : 'X';
                        }
                    }
                });

                panel.add(buttons[i][j]);
            }
        }
        return panel;
    }

    public static boolean boardFull() {
        for (char[] row : board) {
            for (char cell : row) {
                if (cell == ' ') return false;
            }
        }
        return true;
    }

    public static boolean checkWinner() {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == playerActual && board[i][1] == playerActual && board[i][2] == playerActual) return true;
            if (board[0][i] == playerActual && board[1][i] == playerActual && board[2][i] == playerActual) return true;
        }
        return (board[0][0] == playerActual && board[1][1] == playerActual && board[2][2] == playerActual) ||
               (board[0][2] == playerActual && board[1][1] == playerActual && board[2][0] == playerActual);
    }

    public static void restartGame() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = ' ';
                buttons[i][j].setText(" ");
                buttons[i][j].setEnabled(true);
                buttons[i][j].setBackground(new Color(240, 240, 240));
            }
        }
        playerActual = 'X';
    }

    public static void main(String[] args) {
        JFrame window = new JFrame("Triqui :D");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(500, 600);
        window.setLocationRelativeTo(null);
        window.getContentPane().setBackground(new Color(50, 50, 50));

        // Panel principal con GridBagLayout para centrar elementos
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(60, 60, 60));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Ingrese los nombres", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);

        JTextField player1TextField = new JTextField(player1Name);
        player1TextField.setFont(new Font("Arial", Font.PLAIN, 14));
        player1TextField.setBackground(Color.WHITE);
        player1TextField.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JTextField player2TextField = new JTextField(player2Name);
        player2TextField.setFont(new Font("Arial", Font.PLAIN, 14));
        player2TextField.setBackground(Color.WHITE);
        player2TextField.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Jugador 1:", SwingConstants.RIGHT), gbc);

        gbc.gridx = 1;
        mainPanel.add(player1TextField, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Jugador 2:", SwingConstants.RIGHT), gbc);

        gbc.gridx = 1;
        mainPanel.add(player2TextField, gbc);

        JButton startButton = new JButton("Comenzar Juego");
        startButton.setFont(new Font("Arial", Font.BOLD, 16));
        startButton.setBackground(new Color(255, 140, 0));
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false);

        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        mainPanel.add(startButton, gbc);

        window.add(mainPanel);
        window.setVisible(true);

        startButton.addActionListener(e -> {
            player1Name = player1TextField.getText().trim().isEmpty() ? "Jugador 1" : player1TextField.getText().trim();
            player2Name = player2TextField.getText().trim().isEmpty() ? "Jugador 2" : player2TextField.getText().trim();

            JPanel scorePanel = new JPanel();
            scorePanel.setBackground(new Color(80, 80, 80));
            JLabel scoreLabel = new JLabel(player1Name + " " + scoreX + " | " + player2Name + " " + scoreO);
            scoreLabel.setFont(new Font("Arial", Font.BOLD, 18));
            scoreLabel.setForeground(Color.WHITE);
            scorePanel.add(scoreLabel);

            JPanel boardPanel = createBoard(scoreLabel);

            window.getContentPane().removeAll();
            window.add(scorePanel, BorderLayout.NORTH);
            window.add(boardPanel, BorderLayout.CENTER);
            window.revalidate();
            window.repaint();
        });
    }
}
