import javax.swing.*;
import java.awt.*;

public class PendFrame extends JFrame {
    private static final int WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 3;
    private static final int HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2;

    public PendFrame(int n, EnergyFrame energyFrame) {
        setTitle("Symulacja Wahadła");
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        PendPanel panel = new PendPanel(n, energyFrame);
        add(panel);
    }
}