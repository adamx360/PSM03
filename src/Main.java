import javax.swing.*;
import java.util.concurrent.TimeUnit;

public class Main {
    JFrame euler, improvedEuler, rk4;
    EnergyFrame energyEuler, energyImprovedEuler, energyRk4;

    public Main() {
        createFrames(1);
        createFrames(2);
        createFrames(3);

        SwingUtilities.invokeLater(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            euler.setLocation(0, 0);
            improvedEuler.setLocation(euler.getX() + euler.getWidth(), euler.getY());
            rk4.setLocation(improvedEuler.getX() + improvedEuler.getWidth(), improvedEuler.getY());

            energyEuler.setLocation(0, euler.getHeight());
            energyImprovedEuler.setLocation(euler.getX() + euler.getWidth(), improvedEuler.getHeight());
            energyRk4.setLocation(improvedEuler.getX() + improvedEuler.getWidth(), rk4.getHeight());
        });
    }

    public static void main(String[] args) {
        new Main();
    }

    public void createFrames(int n) {
        SwingUtilities.invokeLater(() -> {
            String name = switch (n) {
                case 1 -> "Metoda Eulera";
                case 2 -> "Ulepszona Metoda Eulera";
                case 3 -> "RK4";
                default -> "";
            };
            EnergyFrame energyFrame = new EnergyFrame(name);
            energyFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            energyFrame.setLocationRelativeTo(null);
            energyFrame.setVisible(true);

            JFrame frame = new PendFrame(n, energyFrame);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            switch (n) {
                case 1 -> {
                    euler = frame;
                    energyEuler = energyFrame;
                }
                case 2 -> {
                    improvedEuler = frame;
                    energyImprovedEuler = energyFrame;
                }
                case 3 -> {
                    rk4 = frame;
                    energyRk4 = energyFrame;
                }
            }
        });
    }
}
