import Model.Game;
import Model.View.GameFieldView;

import javax.swing.*;

public class MainApplication {

    public static void main(String[] args) {

        // Создаём модель игры
        Game game = new Game(
                20,   // ширина
                15,   // высота
                3     // минимальная длина змеи
        );

        // Создаём представление
        GameFieldView view = new GameFieldView(game);

        // Создаём окно
        JFrame frame = new JFrame("Snake");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(view);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Фокус на поле, чтобы ловить клавиши
        view.requestFocusInWindow();
    }
}
