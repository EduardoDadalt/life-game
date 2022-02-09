import javax.swing.*;
import java.awt.*;

public class App {
    public static void main(String[] args) {
        JFrame window = new JFrame("Jogo");
        window.setSize(600, 600);
        Game game = new Game();
        window.setResizable(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(new BorderLayout());
        window.add(game);
        window.pack();
        window.setVisible(true);
    }
}
