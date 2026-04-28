
import javax.swing.*;
import java.awt.*;

public class GradientButton extends JButton {
    private Color c1, c2;

    public GradientButton(String text, Color c1, Color c2) {
        super(text);
        this.c1 = c1;
        this.c2 = c2;

        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setForeground(Color.WHITE);
        setFont(new Font("Angels", Font.PLAIN, 20));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        int w = getWidth(), h = getHeight();

        GradientPaint gp = new GradientPaint(0, 0, c1, 0, h, c2);
        g2.setPaint(gp);
        g2.fillRoundRect(0, 0, w, h, 20, 20);

        super.paintComponent(g);
    }
}