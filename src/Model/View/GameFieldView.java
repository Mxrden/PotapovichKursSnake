package Model.View;

import Model.GameField.Cell;
import Model.GameField.GameField;
import Model.SnakeController;
import Model.Timer.TickTimer;

import javax.swing.*;
import java.awt.*;

public class GameFieldView extends JPanel {

    private final GameField _field;
    private final CellWidget[][] _widgets;
    private final TickTimer _tickTimer = new TickTimer();
    private final Runnable _onTick;

    public GameFieldView(Model.GameField.GameField field, SnakeController controller, Runnable onTick) {
        _field = field;
        _onTick = onTick;
        int w = field.getWidth();
        int h = field.getHeight();
        setLayout(new GridLayout(h, w));
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

    /**
     * Проверяет целостность состояния поля: убеждается, что все юниты
     * находятся в допустимых пределах игрового поля.
     * Может использоваться для отладки или расширения игровой логики.
     * @return true если состояние корректно
     */
    public boolean validateFieldState() {
        if (_field == null) return false;
        int h = _field.getHeight();
        int w = _field.getWidth();
        for (int row = 0; row < h; row++) {
            for (int col = 0; col < w; col++) {
                Cell cell = _field.getCell(row, col);
                if (cell == null) return false;

                var unit = cell.getUnit();
                if (unit != null && unit.getPos() != cell) {
                    System.err.println("Несоответствие позиции юнита в клетке [" + row + "," + col + "]");
                    return false;
                }
            }
        }
        return true;
    }
}