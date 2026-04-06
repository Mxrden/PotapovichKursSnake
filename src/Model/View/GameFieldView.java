package Model.View;

import Model.Game;
import Model.GameField.Cell;
import Model.SnakeController;

import javax.swing.*;
import java.awt.*;

public class GameFieldView extends JPanel {

    private final Game _game;
    private final CellWidget[][] _widgets;

    public GameFieldView(Game game) {
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
        addKeyListener(new SnakeController(game, this));

        // -----------------------------
        // ÈÃÐÎÂÎÉ ÖÈÊË (òàéìåð)
        // -----------------------------
        Timer timer = new Timer(700, e -> {
            if (!_game.isOver()) {
                _game.step();
                repaint();
            }
        });
        timer.start();
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
