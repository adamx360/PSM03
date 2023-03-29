import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

public class PendPanel extends JPanel {
    private static final BigDecimal G = new BigDecimal("10");
    private static final BigDecimal L = BigDecimal.ONE;
    private static final BigDecimal DT = new BigDecimal("0.01");
    private static final BigDecimal DAMPING_FACTOR = new BigDecimal("0.1");
    private static final BigDecimal INIT_THETA = BigDecimal.valueOf(Math.PI / 4);
    private static final BigDecimal INIT_OMEGA = BigDecimal.ZERO;
    private final int mode;
    BigDecimal elapsedTime = new BigDecimal(0);
    final Map<Integer, Integer> points = new HashMap<>();
    private BigDecimal theta;
    private BigDecimal omega;


    public PendPanel(int n, EnergyFrame energyFrame) {
        mode = n;
        theta = INIT_THETA;
        omega = INIT_OMEGA;

        Thread simulationThread = new Thread(() -> {
            while (true) {
                BigDecimal alpha = (G.divide(L, 6, RoundingMode.HALF_UP)).negate().multiply(BigDecimal.valueOf(Math.sin(theta.doubleValue()))).subtract(DAMPING_FACTOR.multiply(omega)).setScale(6, RoundingMode.HALF_UP);

                BigDecimal divide = alpha.multiply(DT).divide(BigDecimal.valueOf(2), MathContext.DECIMAL128);
                switch (mode) {
                    case 1 -> {
                        theta = theta.add(omega.multiply(DT)).setScale(6, RoundingMode.HALF_UP);
                        omega = omega.add(alpha.multiply(DT)).setScale(6, RoundingMode.HALF_UP);
                    }
                    case 2 -> {
                        BigDecimal thetaMid = theta.add(omega.multiply(DT).divide(BigDecimal.valueOf(2), MathContext.DECIMAL128));
                        BigDecimal omegaMid = omega.add(divide);
                        BigDecimal alphaMid = (G.divide(L, 6, RoundingMode.HALF_UP)).negate().multiply(BigDecimal.valueOf(Math.sin(thetaMid.doubleValue()))).subtract(DAMPING_FACTOR.multiply(omegaMid)).setScale(6, RoundingMode.HALF_UP);
                        theta = theta.add(omegaMid.multiply(DT)).setScale(6, RoundingMode.HALF_UP);
                        omega = omega.add(alphaMid.multiply(DT)).setScale(6, RoundingMode.HALF_UP);
                    }
                    case 3 -> {
                        BigDecimal k1_theta = omega;
                        BigDecimal k2_theta = omega.add(divide);
                        BigDecimal k2_omega = (G.divide(L, 6, RoundingMode.HALF_UP)).negate().multiply(BigDecimal.valueOf(Math.sin(theta.add(k1_theta.multiply(DT).divide(BigDecimal.valueOf(2), MathContext.DECIMAL128)).doubleValue()))).subtract(DAMPING_FACTOR.multiply(omega.add(divide))).setScale(6, RoundingMode.HALF_UP);
                        BigDecimal bigDecimal = k2_omega.multiply(DT).divide(BigDecimal.valueOf(2), MathContext.DECIMAL128);
                        BigDecimal k3_theta = omega.add(bigDecimal);
                        BigDecimal k3_omega = (G.divide(L, 6, RoundingMode.HALF_UP)).negate().multiply(BigDecimal.valueOf(Math.sin(theta.add(k2_theta.multiply(DT).divide(BigDecimal.valueOf(2), MathContext.DECIMAL128)).doubleValue()))).subtract(DAMPING_FACTOR.multiply(omega.add(bigDecimal))).setScale(6, RoundingMode.HALF_UP);
                        BigDecimal k4_theta = omega.add(k3_omega.multiply(DT));
                        BigDecimal k4_omega = (G.divide(L, 6, RoundingMode.HALF_UP)).negate().multiply(BigDecimal.valueOf(Math.sin(theta.add(k3_theta.multiply(DT)).doubleValue()))).subtract(DAMPING_FACTOR.multiply(omega.add(k3_omega.multiply(DT)))).setScale(6, RoundingMode.HALF_UP);
                        theta = theta.add(k1_theta.add(k2_theta.multiply(BigDecimal.valueOf(2))).add(k3_theta.multiply(BigDecimal.valueOf(2))).add(k4_theta).multiply(DT).divide(BigDecimal.valueOf(6), MathContext.DECIMAL128)).setScale(6, RoundingMode.HALF_UP);
                        omega = omega.add(alpha.add(k2_omega.multiply(BigDecimal.valueOf(2))).add(k3_omega.multiply(BigDecimal.valueOf(2))).add(k4_omega).multiply(DT).divide(BigDecimal.valueOf(6), MathContext.DECIMAL128)).setScale(6, RoundingMode.HALF_UP);
                    }
                }
                double potentialEnergy = G.doubleValue() * L.doubleValue() * (1 - Math.cos(theta.doubleValue()));
                double kineticEnergy = 0.5 * Math.pow(L.doubleValue() * omega.doubleValue(), 2);
                double totalEnergy = potentialEnergy + kineticEnergy;

                energyFrame.addDataPoint(elapsedTime.doubleValue(), potentialEnergy, kineticEnergy, totalEnergy);
                repaint();

                try {
                    int time = (int) (DT.multiply(BigDecimal.valueOf(1000)).doubleValue() / 2);
                    elapsedTime = elapsedTime.add(BigDecimal.valueOf(time));
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        simulationThread.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2 - getHeight() / 10;

        int x = centerX + (int) (L.multiply(BigDecimal.valueOf(Math.sin(theta.doubleValue()))).multiply(BigDecimal.valueOf(centerY)).doubleValue());
        int y = centerY + (int) (L.multiply(BigDecimal.valueOf(Math.cos(theta.doubleValue()))).multiply(BigDecimal.valueOf(centerY)).doubleValue());

        points.put(x, y);
        g2.setColor(Color.RED);
        points.forEach((k, v) -> g2.fillOval(k, v, 3, 3));


        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2));
        g2.drawLine(centerX, centerY, x, y);
        g2.fillOval(x - 10, y - 10, 20, 20);
    }
}








