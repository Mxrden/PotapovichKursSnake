package Model.View;

import Model.Game;
import Model.SnakeController;

import javax.swing.*;
import java.awt.*;

/**
 * Основной экран игры.
 * Отвечает за HUD, кнопку новой игры и игровое поле.
 */
public class SnakeView extends JPanel {

    private final Game _game;
    private final SnakeController _controller;
    private final JLabel _scoreLabel = new JLabel();
    private final JLabel _lengthLabel = new JLabel();
    private final JLabel _hpLabel = new JLabel();
    private final JLabel _gameOverLabel = new JLabel("\u0412\u042b \u0423\u041c\u0415\u0420\u041b\u0418", SwingConstants.CENTER);
    private final JButton _newGameButton = new JButton("\u041d\u043e\u0432\u0430\u044f \u0438\u0433\u0440\u0430");
    private final JPanel _boardLayer = new JPanel();
    private GameFieldView _boardView;
    private final Model.SnakeMovedListener _snakeMovedListener;
    private final Model.RodentEatenListener _rodentEatenListener;
    private final Model.GameOverListener _gameOverListener;

    public SnakeView(Game game, SnakeController controller) {
        _game = game;
        _controller = controller;

        setLayout(new BorderLayout(0, 8));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        setBackground(new Color(18, 22, 32));

        add(buildTopBar(), BorderLayout.NORTH);
        rebuildBoard();
        updateHud();

        _newGameButton.addActionListener(e -> restartGame());
        _snakeMovedListener = (snake, direction) -> updateHud();
        _rodentEatenListener = snake -> updateHud();
        _gameOverListener = this::onGameOver;
        _game.addSnakeMovedListener(_snakeMovedListener);
        _game.addRodentEatenListener(_rodentEatenListener);
        _game.addGameOverListener(_gameOverListener);

    }

    public void requestGameFocus() {
        if (_boardView != null) {
            _boardView.requestFocusInWindow();
        }
    }

    public void dispose() {
        if (_boardView != null) {
            _boardView.dispose();
        }
        _game.removeSnakeMovedListener(_snakeMovedListener);
        _game.removeRodentEatenListener(_rodentEatenListener);
        _game.removeGameOverListener(_gameOverListener);
    }

    private JComponent buildTopBar() {
        JPanel topBar = new JPanel(new GridLayout(1, 2, 12, 0));
        topBar.setOpaque(false);

        JPanel stats = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        stats.setOpaque(false);

        configureStatLabel(_scoreLabel);
        configureStatLabel(_lengthLabel);
        configureStatLabel(_hpLabel);

        _newGameButton.setFocusable(false);
        _newGameButton.setMargin(new Insets(6, 12, 6, 12));

        stats.add(_scoreLabel);
        stats.add(_lengthLabel);
        stats.add(_hpLabel);

        JPanel buttonWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonWrap.setOpaque(false);
        buttonWrap.add(_newGameButton);

        topBar.add(stats);
        topBar.add(buttonWrap);
        return topBar;
    }

    private void configureStatLabel(JLabel label) {
        label.setForeground(Color.WHITE);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 16f));
    }

    private void restartGame() {
        if (_boardView != null) {
            _boardView.dispose();
        }

        _game.restart();
        rebuildBoard();
        updateHud();
        revalidate();
        repaint();

        SwingUtilities.invokeLater(() -> _boardView.requestFocusInWindow());
    }

    private void rebuildBoard() {
        _boardView = new GameFieldView(_game, _controller);
        _boardLayer.removeAll();
        _boardLayer.setOpaque(false);
        _boardLayer.setLayout(new OverlayLayout(_boardLayer));
        _boardView.setAlignmentX(0.5f);
        _boardView.setAlignmentY(0.5f);
        _gameOverLabel.setAlignmentX(0.5f);
        _gameOverLabel.setAlignmentY(0.5f);
        _gameOverLabel.setVisible(false);
        _gameOverLabel.setForeground(new Color(255, 80, 80));
        _gameOverLabel.setFont(_gameOverLabel.getFont().deriveFont(Font.BOLD, 28f));
        _boardLayer.add(_gameOverLabel);
        _boardLayer.add(_boardView);
        if (_boardLayer.getParent() != this) {
            add(_boardLayer, BorderLayout.CENTER);
        }
    }

    private void updateHud() {
        _scoreLabel.setText("\u0421\u0447\u0435\u0442: " + _game.getScore());
        _lengthLabel.setText("\u0414\u043b\u0438\u043d\u0430: " + _game.getSnake().getBodySize());
        _hpLabel.setText("\u0425\u041f: " + _game.getSnake().getLife());
        _gameOverLabel.setVisible(_game.isOver());
        _gameOverLabel.repaint();
    }

    private void onGameOver() {
        updateHud();
        repaint();
    }
}
