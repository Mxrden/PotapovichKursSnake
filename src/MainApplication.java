import Model.Game;
import Model.View.GameFieldView;

import javax.swing.*;

public class MainApplication {

    public static void main(String[] args) {

        Game game = new Game(
                20,
                15,
                3
        );

        GameFieldView view = new GameFieldView(game);

        JFrame frame = new JFrame("Snake");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(view);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        view.requestFocusInWindow();
    }
}
