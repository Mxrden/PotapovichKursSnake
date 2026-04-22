package ViewTests;

import Model.Game;
import Model.GameField.Cell;
import Model.GameField.Direction;
import Model.GameField.GameField;
import Model.Snake.SnakeSegment;
import Model.SnakeController;
import Model.View.CellWidget;
import Model.View.GameFieldView;
import Model.View.SnakeView;
import Model.Units.SimpleRodent;
import Model.Units.Stone;
import Model.Units.Wall;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class RenderingTest {

    @BeforeAll
    static void headless() {
        System.setProperty("java.awt.headless", "true");
    }

    @Test
    void cellWidgetRendersBackgroundAndUnits() throws Exception {
        GameField field = new GameField(1, 1);
        Cell cell = field.getCell(0, 0);
        CellWidget widget = new CellWidget(cell);

        assertColor(widget, new Color(30, 36, 50), 16, 16);

        cell.putUnit(new Wall());
        assertColor(widget, new Color(180, 100, 60), 16, 16);

        cell.extractUnit();
        cell.putUnit(new Stone());
        assertColor(widget, Color.DARK_GRAY, 16, 16);

        cell.extractUnit();
        cell.putUnit(new SimpleRodent());
        assertColor(widget, Color.RED, 16, 16);

        cell.extractUnit();
        SnakeSegment head = new SnakeSegment(true, 1.0f, cell);
        head.setDirection(Direction.east());
        cell.putUnit(head);
        assertColor(widget, Color.GREEN, 6, 6);

        cell.extractUnit();
        SnakeSegment body = new SnakeSegment(false, 1.5f, cell);
        body.setDirection(Direction.east());
        cell.putUnit(body);
        assertColor(widget, new Color(255, 140, 0), 16, 16);
    }

    @Test
    void gameFieldViewBuildsGridOfCellWidgets() throws Exception {
        Game game = new Game(4, 3, 3);
        SnakeController controller = new SnakeController(game);
        GameFieldView view = new GameFieldView(game, controller);

        try {
            assertTrue(view.getLayout() instanceof GridLayout);
            assertEquals(12, view.getComponentCount());
            assertTrue(view.getComponent(0) instanceof CellWidget);
        } finally {
            view.dispose();
        }
    }

    @Test
    void snakeViewShowsHudValues() throws Exception {
        Game game = new Game(20, 15, 3);
        SnakeController controller = new SnakeController(game);
        SnakeView view = new SnakeView(game, controller);

        try {
            JLabel scoreLabel = getField(view, "_scoreLabel", JLabel.class);
            JLabel lengthLabel = getField(view, "_lengthLabel", JLabel.class);
            JLabel hpLabel = getField(view, "_hpLabel", JLabel.class);
            JButton newGameButton = getField(view, "_newGameButton", JButton.class);

            assertEquals("\u0421\u0447\u0435\u0442: 0", scoreLabel.getText());
            assertEquals("\u0414\u043b\u0438\u043d\u0430: 3", lengthLabel.getText());
            assertEquals("\u0425\u041f: " + game.getSnake().getLife(), hpLabel.getText());
            assertEquals("\u041d\u043e\u0432\u0430\u044f \u0438\u0433\u0440\u0430", newGameButton.getText());
        } finally {
            view.dispose();
        }
    }

    @Test
    void snakeViewShowsGameOverOverlay() throws Exception {
        Game game = new Game(20, 15, 3);
        SnakeController controller = new SnakeController(game);
        SnakeView view = new SnakeView(game, controller);

        try {
            SwingUtilities.invokeAndWait(() -> {
                game.getSnake().kill();
                game.step();
            });

            JLabel gameOverLabel = getField(view, "_gameOverLabel", JLabel.class);
            assertTrue(gameOverLabel.isVisible());
            assertEquals("\u0412\u042b \u0423\u041c\u0415\u0420\u041b\u0418", gameOverLabel.getText());
        } finally {
            view.dispose();
        }
    }

    @Test
    void newGameButtonResetsHudAndHidesOverlay() throws Exception {
        Game game = new Game(20, 15, 3);
        SnakeController controller = new SnakeController(game);
        SnakeView view = new SnakeView(game, controller);

        try {
            SwingUtilities.invokeAndWait(() -> {
                game.getSnake().kill();
                game.step();
            });

            JButton newGameButton = getField(view, "_newGameButton", JButton.class);
            JLabel scoreLabel = getField(view, "_scoreLabel", JLabel.class);
            JLabel lengthLabel = getField(view, "_lengthLabel", JLabel.class);
            JLabel gameOverLabel = getField(view, "_gameOverLabel", JLabel.class);

            SwingUtilities.invokeAndWait(newGameButton::doClick);

            assertEquals("\u0421\u0447\u0435\u0442: 0", scoreLabel.getText());
            assertEquals("\u0414\u043b\u0438\u043d\u0430: 3", lengthLabel.getText());
            assertFalse(gameOverLabel.isVisible());
            assertFalse(game.isOver());
        } finally {
            view.dispose();
        }
    }

    private static void assertColor(JComponent component, Color expected, int x, int y) throws Exception {
        BufferedImage image = paint(component, 32, 32);
        Color actual = new Color(image.getRGB(x, y), true);
        assertEquals(expected.getRGB(), actual.getRGB());
    }

    private static BufferedImage paint(JComponent component, int width, int height) throws Exception {
        final BufferedImage[] image = new BufferedImage[1];
        SwingUtilities.invokeAndWait(() -> {
            component.setSize(width, height);
            component.doLayout();
            BufferedImage rendered = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = rendered.createGraphics();
            component.paint(g);
            g.dispose();
            image[0] = rendered;
        });
        return image[0];
    }

    private static <T> T getField(Object target, String name, Class<T> type) throws Exception {
        Field field = target.getClass().getDeclaredField(name);
        field.setAccessible(true);
        return type.cast(field.get(target));
    }
}