package Model.View;

import Model.GameField.Cell;
import Model.GameField.GameField;
import Model.SnakeController;
import Model.Timer.TickTimer;
import Model.Units.Unit;

import javax.swing.*;
import java.awt.*;

public class GameFieldView extends JPanel {

    private final GameField _field;
    private final CellWidget[][] _widgets;
    private final TickTimer _tickTimer = new TickTimer();
    private final Runnable _onTick;

    public GameFieldView(GameField field, SnakeController controller, Runnable onTick) {
        _field = field;
        _onTick = onTick;
        int w = field.getWidth();
        int h = field.getHeight();
        setLayout(new GridLayout(h, w));
        setBackground(new Color(30, 36, 50));
        setOpaque(true);
        _widgets = new CellWidget[h][w];

        for (int row = 0; row < h; row++) {
            for (int col = 0; col < w; col++) {
                Cell cell = field.getCell(row, col);
                CellWidget widget = new CellWidget(cell);
                _widgets[row][col] = widget;
                add(widget);
            }
        }

        setFocusable(true);
        addKeyListener(controller);
        _tickTimer.start(700, () -> {
            if (_onTick != null) {
                _onTick.run();
            }
        });
    }

    public void dispose() {
        _tickTimer.stop();
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

    public GameField getField() {
        return _field;
    }

    public boolean validateFieldState() {
        if (_field == null) return false;
        int h = _field.getHeight();
        int w = _field.getWidth();
        for (int row = 0; row < h; row++) {
            for (int col = 0; col < w; col++) {
                Cell cell = _field.getCell(row, col);
                if (cell == null) return false;
                for (Unit unit : cell.getUnits()) {
                    if (unit != null && unit.getPos() != cell) {
                        System.err.println("Обнаружена несогласованность позиции юнита в клетке [" + row + "," + col + "]");
                        return false;
                    }
                }
            }
        }
        return true;
    }
}