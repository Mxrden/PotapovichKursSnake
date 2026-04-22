import Model.Game;
import Model.SnakeController;
import Model.View.SnakeView;

import javax.swing.*;

public class MainApplication {

    public static void main(String[] args) {

        Game game = new Game(
                20,
                15,
                3
        );

        SnakeController controller = new SnakeController(game);
        SnakeView view = new SnakeView(game, controller);

        JFrame frame = new JFrame("Snake");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(view);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        view.requestGameFocus();
    }
}
