package Model.View;

import Model.Game;
import Model.GameOverListener;
import Model.GameField.Cell;
import Model.SnakeController;
import Model.RodentEatenListener;
import Model.SnakeMovedListener;
import Model.Timer.TickTimer;

import javax.swing.*;
import java.awt.*;

public class GameFieldView extends JPanel {

    private final Game _game;
    private final CellWidget[][] _widgets;
    private final TickTimer _tickTimer = new TickTimer();
    private final SnakeMovedListener _snakeMovedListener;
    private final RodentEatenListener _rodentEatenListener;
    private final GameOverListener _gameOverListener;

    public GameFieldView(Game game, SnakeController controller) {
        _game = game;
        int w = game.getField().getWidth();
        int h = game.getField().getHeight();
        setLayout(new GridLayout(h, w));
        _widgets = new CellWidget[h][w];

        for (int row = 0; row < h; row++) {
            for (int col = 0; col < w; col++) {
                Cell cell = game.getField().getCell(row, col);
                CellWidget widget = new CellWidget(cell);
                _widgets[row][col] = widget;
                add(widget);
            }
        }

        setFocusable(true);
        addKeyListener(controller);
        _snakeMovedListener = (snake, direction) -> repaint();
        _rodentEatenListener = snake -> repaint();
        _gameOverListener = () -> {
            _tickTimer.stop();
            repaint();
        };
        _game.addSnakeMovedListener(_snakeMovedListener);
        _game.addRodentEatenListener(_rodentEatenListener);
        _game.addGameOverListener(_gameOverListener);
        _tickTimer.start(700, () -> {
            if (!_game.isOver()) {
                _game.step();
            }
        });
    }

    public void dispose() {
        _tickTimer.stop();
        _game.removeSnakeMovedListener(_snakeMovedListener);
        _game.removeRodentEatenListener(_rodentEatenListener);
        _game.removeGameOverListener(_gameOverListener);
    }

    @Override
    public void removeNotify() {
        dispose();
        super.removeNotify();
    }

    @Override
    public void repaint() {
        super.repaint();
        if (_widgets == null) return;
        for (var row : _widgets)
            for (var widget : row)
                widget.repaint();
    }
}
