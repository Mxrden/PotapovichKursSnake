package Model;

import Model.GameField.Direction;
import Model.Snake.Snake;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class SnakeController implements KeyListener {

    private final Game _game;

    public SnakeController(Game game) {
        _game = game;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (_game.isOver()) return;

        Snake snake = _game.getSnake();

        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> snake.setDirection(Direction.north());
            case KeyEvent.VK_DOWN -> snake.setDirection(Direction.south());
            case KeyEvent.VK_LEFT -> snake.setDirection(Direction.west());
            case KeyEvent.VK_RIGHT -> snake.setDirection(Direction.east());
        }
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
}
