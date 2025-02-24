    private static JButton[][] buttons = new JButton[3][3];
    private static int scoreX = 0, scoreO = 0;
    private static String player1Name = "Jugador 1", player2Name = "Jugador 2";
    private static JLabel scoreLabel;
    private static JFrame gameWindow;

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

    public static JPanel createBoard(JLabel scoreLabel) {
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

@@ -26,7 +54,6 @@ public static JPanel createBoard(JLabel scoreLabel) {
                buttons[i][j].addActionListener(e -> {
                    JButton button = (JButton) e.getSource();
                    int row = -1, col = -1;

                    for (int x = 0; x < 3; x++) {
                        for (int y = 0; y < 3; y++) {
                            if (buttons[x][y] == button) {
@@ -36,29 +63,39 @@ public static JPanel createBoard(JLabel scoreLabel) {
                            }
                        }
                    }

                    if (board[row][col] == ' ') {
                        board[row][col] = playerActual;
                        button.setText(String.valueOf(playerActual));
                        button.setEnabled(false);
                        button.setBackground(playerActual == 'X' ? new Color(255, 200, 200) : new Color(200, 200, 255));
                        Color cellColor = playerActual == 'X' ? new Color(255, 200, 200) : new Color(200, 200, 255);
                        animateButton(button, cellColor);

                        if (checkWinner()) {
                            if (playerActual == 'X') scoreX++;
                            else scoreO++;
                            for (int x = 0; x < 3; x++) {
                                for (int y = 0; y < 3; y++) {
                                    if (board[x][y] == playerActual) {
                                        animateButton(buttons[x][y], Color.YELLOW);
                                    }
                                }
                            }

                            if (playerActual == 'X') {
                                scoreX++;
                            } else {
                                scoreO++;
                            }
                            scoreLabel.setText(player1Name + " " + scoreX + " | " + player2Name + " " + scoreO);
                            JOptionPane.showMessageDialog(null, "¡" + (playerActual == 'X' ? player1Name : player2Name) + " ha ganado!");
                            animateScore();
                            JOptionPane.showMessageDialog(gameWindow, "¡" + (playerActual == 'X' ? player1Name : player2Name) + " ha ganado!");
                            restartGame();
                        } else if (boardFull()) {
                            JOptionPane.showMessageDialog(null, "¡Empate!");
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
@@ -76,8 +113,12 @@ public static boolean boardFull() {

    public static boolean checkWinner() {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == playerActual && board[i][1] == playerActual && board[i][2] == playerActual) return true;
            if (board[0][i] == playerActual && board[1][i] == playerActual && board[2][i] == playerActual) return true;
            if (board[i][0] == playerActual && board[i][1] == playerActual && board[i][2] == playerActual) {
                return true;
            }
            if (board[0][i] == playerActual && board[1][i] == playerActual && board[2][i] == playerActual) {
                return true;
            }
        }
        return (board[0][0] == playerActual && board[1][1] == playerActual && board[2][2] == playerActual) ||
               (board[0][2] == playerActual && board[1][1] == playerActual && board[2][0] == playerActual);
@@ -95,17 +136,37 @@ public static void restartGame() {
        playerActual = 'X';
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
    }

    public static void main(String[] args) {
        JFrame window = new JFrame("Triqui :D");
        JFrame window = new JFrame("Triqui :D - Inicio");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(500, 600);
        window.setSize(400, 250);
        window.setLocationRelativeTo(null);
        window.getContentPane().setBackground(new Color(50, 50, 50));

        // Panel principal con GridBagLayout para centrar elementos
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(60, 60, 60));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
@@ -119,35 +180,27 @@ public static void main(String[] args) {
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
        startButton.addActionListener(e -> {
            player1Name = player1TextField.getText();
            player2Name = player2TextField.getText();
            window.dispose();
            startGame();
        });

        gbc.gridy = 3;
        gbc.gridx = 0;
@@ -156,25 +209,5 @@ public static void main(String[] args) {

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
