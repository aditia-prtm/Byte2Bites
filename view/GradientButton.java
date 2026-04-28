package view;

import main.FoodOrderApp;
import javax.swing.*;

import java.awt.*;

/**
 * GradientButton - Komponen tombol dengan efek gradient yang reusable
 */
public class GradientButton extends JButton {

    private Color color1;
    private Color color2;
    private int cornerRadius = 20;

    /**
     * Constructor dengan dua warna gradient
     */
    public GradientButton(String text, Color color1, Color color2) {
        super(text);
        this.color1 = color1;
        this.color2 = color2;

        // Setup tampilan tombol
        setupButton();
    }

    /**
     * Constructor dengan satu warna (gradient dari terang ke gelap)
     */
    public GradientButton(String text, Color baseColor) {
        this(text, baseColor.brighter(), baseColor.darker());
    }

    private void setupButton() {
        setContentAreaFilled(false);    // Hilangkan background default
        setFocusPainted(false);         // Hilangkan border fokus
        setBorderPainted(false);        // Hilangkan border default
        setForeground(Color.WHITE);     // Warna teks putih
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Font default (bisa diubah kemudian)
        setFont(new Font("Angels", Font.PLAIN, 20));
    }

    /**
     * Mengubah warna gradient
     */
    public void setGradientColors(Color color1, Color color2) {
        this.color1 = color1;
        this.color2 = color2;
        repaint();
    }

    /**
     * Mengubah radius sudut tombol
     */
    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        
        // Mengaktifkan anti-aliasing untuk tampilan lebih halus
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        // Buat gradient dari atas ke bawah
        GradientPaint gradient = new GradientPaint(
            0, 0, color1,           // Titik awal (atas)
            0, height, color2       // Titik akhir (bawah)
        );

        g2.setPaint(gradient);
        g2.fillRoundRect(0, 0, width, height, cornerRadius, cornerRadius);

        // Panggil paintComponent dari parent (untuk menampilkan teks)
        super.paintComponent(g2);
        
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        // Tidak menggambar border default
    }
}