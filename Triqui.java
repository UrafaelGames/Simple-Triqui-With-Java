package com.javier.proyecto;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Proyecto
{
    private static char[][] board = {{' ', ' ', ' '}, {' ', ' ', ' '}, {' ', ' ', ' '}}; 
    private static char playerActual = 'X'; 
    private static JButton[][] buttons = new JButton[3][3]; 

    private static int scoreX = 0;  
    private static int scoreO = 0; 

    private static String player1Name = "Jugador 1";  
    private static String player2Name = "Jugador 2"; 

    // Crear el tablero de botones
    public static JPanel createBoard(JLabel scoreLabel)
    {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 3)); 

        // Crear y configurar los botones
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton(" ");
                buttons[i][j].setFont(new Font("Arial", Font.PLAIN, 60));
                buttons[i][j].setFocusPainted(false);
                buttons[i][j].setBackground(Color.WHITE);
                buttons[i][j].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JButton buttonPulsed = (JButton) e.getSource();
                        int row = -1, col = -1;

                        // Encontrar la posición del botón en el arreglo
                        outerloop:
                        for (int i = 0; i < 3; i++) {
                            for (int j = 0; j < 3; j++) {
                                if (buttons[i][j] == buttonPulsed) {
                                    row = i;
                                    col = j;
                                    break outerloop;
                                }
                            }
                        }

                        // Si la casilla está vacía, se marca con el jugador actual
                        if (board[row][col] == ' ') {
                            board[row][col] = playerActual;
                            buttonPulsed.setText(String.valueOf(playerActual));

                            // Comprobar si hay un ganador
                            if (checkWinner()) {
                                // Actualizar el puntaje
                                if (playerActual == 'X') {
                                    scoreX++;
                                } else {
                                    scoreO++;
                                }

                                // Actualizar la etiqueta de puntajes
                                scoreLabel.setText("Puntajes: " + player1Name + ": " + scoreX + " | " + player2Name + ": " + scoreO);
                                JOptionPane.showMessageDialog(null, "¡El jugador " + (playerActual == 'X' ? player1Name : player2Name) + " ha ganado!\nPuntajes:\n" + player1Name + ": " + scoreX + " | " + player2Name + ": " + scoreO);
                                restartGame();
                            } else if (boardFull()) {
                                JOptionPane.showMessageDialog(null, "¡Empate!");
                                restartGame();
                            } else {
                                // Cambiar al siguiente jugador
                                playerActual = (playerActual == 'X') ? 'O' : 'X';
                            }
                        }
                    }
                });
                panel.add(buttons[i][j]);
            }
        }

        return panel;
    }

    // Función para verificar si el tablero está lleno (empate)
    public static boolean boardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    return false; // Hay una casilla vacía
                }
            }
        }
        return true; // No hay casillas vacías, el tablero está lleno
    }

    // Función para verificar si un jugador ha ganado
    public static boolean checkWinner() {
        // Comprobar filas
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == playerActual && board[i][1] == playerActual && board[i][2] == playerActual) {
                return true;
            }
        }
        
        // Comprobar columnas
        for (int i = 0; i < 3; i++) {
            if (board[0][i] == playerActual && board[1][i] == playerActual && board[2][i] == playerActual) {
                return true;
            }
        }

        // Comprobar diagonales
        if (board[0][0] == playerActual && board[1][1] == playerActual && board[2][2] == playerActual) {
            return true;
        }
        if (board[0][2] == playerActual && board[1][1] == playerActual && board[2][0] == playerActual) {
            return true;
        }

        return false; // No hay ganador
    }

    // Función para reiniciar el juego
    public static void restartGame() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = ' ';
                buttons[i][j].setText(" ");
            }
        }
        playerActual = 'X'; // Volver a empezar con el jugador X
    }

    // Función principal para ejecutar la aplicación
    public static void main(String[] args) {
        // Crear la ventana
        JFrame window = new JFrame("Triqui");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(400, 400);
        window.setLocationRelativeTo(null); // Centrar la ventana

        // Panel para ingresar los nombres de los jugadores
        JPanel namePanel = new JPanel();
        namePanel.setLayout(new GridLayout(3, 2));

        JTextField player1TextField = new JTextField(player1Name); // Campo para el nombre del jugador 1
        JTextField player2TextField = new JTextField(player2Name); // Campo para el nombre del jugador 2

        namePanel.add(new JLabel("Jugador 1:"));
        namePanel.add(player1TextField);
        namePanel.add(new JLabel("Jugador 2:"));
        namePanel.add(player2TextField);

        JButton startButton = new JButton("Comenzar Juego");
        namePanel.add(startButton);

        // Acción del botón de inicio
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Actualizar los nombres de los jugadores
                player1Name = player1TextField.getText().isEmpty() ? "Jugador 1" : player1TextField.getText();
                player2Name = player2TextField.getText().isEmpty() ? "Jugador 2" : player2TextField.getText();

                // Crear el panel de puntajes
                JPanel scorePanel = new JPanel();
                JLabel scoreLabel = new JLabel("Puntajes: " + player1Name + ": " + scoreX + " | " + player2Name + ": " + scoreO);
                scorePanel.add(scoreLabel);

                // Crear el tablero y añadirlo a la ventana
                JPanel boardPanel = createBoard(scoreLabel);
                window.remove(namePanel); 
                window.add(scorePanel, BorderLayout.NORTH); 
                window.add(boardPanel, BorderLayout.CENTER);
                window.revalidate(); 
            }
        });

        // Añadir el panel de nombres a la ventana
        window.add(namePanel);
        window.setVisible(true);
    }
}
