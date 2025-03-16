import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;
import javax.swing.*;

public class Proyecto {
    private static char[][] board = new char[3][3];
    private static char playerActual = 'X';
    private static JButton[][] buttons = new JButton[3][3];
    private static int scoreX = 0, scoreO = 0;
    private static String player1Name = "Jugador 1", player2Name = "Jugador 2";
    private static JLabel scoreLabel;
    private static JFrame gameWindow;
    private static Clip menuMusic; // Música del menú
    private static Clip gameMusic; // Música del juego
    private static Clip xSound;
    private static Clip oSound;
    private static Clip winSound; // Efecto de sonido para el ganador

    // Colores para la transición del fondo
    private static final Color[] COLORS = {
        new Color(255, 165, 0),   // Anaranjado
        new Color(0, 0, 255),       // Azul
        new Color(0, 255, 0),       // Verde
        new Color(128, 0, 128),     // Violeta
        new Color(255, 255, 0),     // Amarillo
        new Color(255, 182, 193)    // Rosado claro
    };
    private static int currentColorIndex = 0;
    private static float colorTransition = 0.0f;

    // Clase personalizada para el panel con fondo animado
    static class AnimatedBackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Interpolación entre colores
            Color startColor = COLORS[currentColorIndex];
            Color endColor = COLORS[(currentColorIndex + 1) % COLORS.length];
            Color currentColor = interpolateColor(startColor, endColor, colorTransition);

            // Dibujar el fondo con el color actual
            g.setColor(currentColor);
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        // Interpolación lineal entre dos colores
        private Color interpolateColor(Color start, Color end, float fraction) {
            int red = (int) (start.getRed() + (end.getRed() - start.getRed()) * fraction);
            int green = (int) (start.getGreen() + (end.getGreen() - start.getGreen()) * fraction);
            int blue = (int) (start.getBlue() + (end.getBlue() - start.getBlue()) * fraction);
            return new Color(red, green, blue);
        }
    }

    public static void animateScore() {
        Timer timer = new Timer(100, null);
        final int[] step = {0};
        timer.addActionListener(e -> {
            scoreLabel.setFont(new Font("Arial", Font.BOLD, 20 + (step[0] % 4) * 2));
            step[0]++;
            if (step[0] > 8) timer.stop();
        });
        timer.start();
    }

    public static void animateButton(JButton button, Color color) {
        Timer timer = new Timer(50, null);
        final int[] step = {0};
        timer.addActionListener(e -> {
            if (step[0] % 2 == 0) {
                button.setBackground(color.darker());
            } else {
                button.setBackground(color);
            }
            step[0]++;
            if (step[0] > 6) timer.stop();
        });
        timer.start();
    }

    public static JPanel createBoard() {
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
                        Color cellColor = playerActual == 'X' ? new Color(255, 200, 200) : new Color(200, 200, 255);
                        animateButton(button, cellColor);

                        // Reproducir sonido dependiendo del jugador
                        if (playerActual == 'X') {
                            playSound(xSound);
                        } else {
                            playSound(oSound);
                        }

                        if (checkWinner()) {
                            for (int x = 0; x < 3; x++) {
                                for (int y = 0; y < 3; y++) {
                                    if (board[x][y] == playerActual) {
                                        animateButton(buttons[x][y], Color.ORANGE);
                                    }
                                }
                            }

                            if (playerActual == 'X') {
                                scoreX++;
                            } else {
                                scoreO++;
                            }
                            scoreLabel.setText(player1Name + " " + scoreX + " | " + player2Name + " " + scoreO);
                            animateScore();
                            playSound(winSound); // Reproducir sonido de victoria
                            JOptionPane.showMessageDialog(gameWindow, "¡" + (playerActual == 'X' ? player1Name : player2Name) + " ha ganado!");
                            restartGame();
                        } else if (boardFull()) {
                            JOptionPane.showMessageDialog(gameWindow, "¡Empate!");
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
            if (board[i][0] == playerActual && board[i][1] == playerActual && board[i][2] == playerActual) {
                return true;
            }
            if (board[0][i] == playerActual && board[1][i] == playerActual && board[2][i] == playerActual) {
                return true;
            }
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

        // Reiniciar la música del juego
        stopMusic(gameMusic);
        playMusic(gameMusic);
    }

    public static void startGame() {
        gameWindow = new JFrame("Triqui :D");
        gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameWindow.setSize(500, 600);
        gameWindow.setLocationRelativeTo(null);
        gameWindow.getContentPane().setBackground(new Color(50, 50, 50));
        gameWindow.setLayout(new BorderLayout());

        scoreLabel = new JLabel(player1Name + " " + scoreX + " | " + player2Name + " " + scoreO, SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        scoreLabel.setForeground(Color.WHITE);
        gameWindow.add(scoreLabel, BorderLayout.NORTH);
        gameWindow.add(createBoard(), BorderLayout.CENTER);

        JButton resetButton = new JButton("Reiniciar");
        resetButton.setFont(new Font("Arial", Font.BOLD, 16));
        resetButton.addActionListener(e -> restartGame());
        gameWindow.add(resetButton, BorderLayout.SOUTH);

        gameWindow.setVisible(true);

        // Cambiar a la música del juego
        stopMusic(menuMusic);
        playMusic(gameMusic);
    }

    public static void playMusic(Clip clip) {
        if (clip != null) {
            clip.setFramePosition(0); // Reinicia la música al principio
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public static void stopMusic(Clip clip) {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    public static void playSound(Clip clip) {
        if (clip != null) {
            clip.setFramePosition(0);
            clip.start();
        }
    }

    public static void loadSounds() {
        try {
            // Cargar música del menú
            File menuMusicFile = new File("menu_music.wav");
            AudioInputStream menuAudioInputStream = AudioSystem.getAudioInputStream(menuMusicFile);
            menuMusic = AudioSystem.getClip();
            menuMusic.open(menuAudioInputStream);

            // Cargar música del juego
            File gameMusicFile = new File("game_music.wav");
            AudioInputStream gameAudioInputStream = AudioSystem.getAudioInputStream(gameMusicFile);
            gameMusic = AudioSystem.getClip();
            gameMusic.open(gameAudioInputStream);

            // Cargar sonidos de X y O
            File xSoundFile = new File("x_sound.wav");
            AudioInputStream xAudioInputStream = AudioSystem.getAudioInputStream(xSoundFile);
            xSound = AudioSystem.getClip();
            xSound.open(xAudioInputStream);

            File oSoundFile = new File("o_sound.wav");
            AudioInputStream oAudioInputStream = AudioSystem.getAudioInputStream(oSoundFile);
            oSound = AudioSystem.getClip();
            oSound.open(oAudioInputStream);

            // Cargar sonido de victoria
            File winSoundFile = new File("win_sound.wav");
            AudioInputStream winAudioInputStream = AudioSystem.getAudioInputStream(winSoundFile);
            winSound = AudioSystem.getClip();
            winSound.open(winAudioInputStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        loadSounds();

        // Reproducir música del menú al inicio
        playMusic(menuMusic);

        JFrame window = new JFrame("Triqui :D - Inicio");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(400, 250);
        window.setLocationRelativeTo(null);

        // Crear un panel con fondo animado
        AnimatedBackgroundPanel mainPanel = new AnimatedBackgroundPanel();
        mainPanel.setLayout(new GridBagLayout());
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
        JTextField player2TextField = new JTextField(player2Name);
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
        startButton.addActionListener(e -> {
            player1Name = player1TextField.getText();
            player2Name = player2TextField.getText();
            window.dispose();
            startGame();
        });

        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        mainPanel.add(startButton, gbc);

        window.add(mainPanel);
        window.setVisible(true);

        // Animación del fondo
        Timer colorTimer = new Timer(50, e -> {
            colorTransition += 0.01f;
            if (colorTransition >= 1.0f) {
                colorTransition = 0.0f;
                currentColorIndex = (currentColorIndex + 1) % COLORS.length;
            }
            mainPanel.repaint();
        });
        colorTimer.start();
    }
}
