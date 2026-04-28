
import model.PurchaseRecord;

import javax.swing.*;

import java.awt.*;
import java.io.File;

public class HomePanel extends JPanel {

    private final FoodOrderApp app;

    public HomePanel(FoodOrderApp app) {
        this.app = app;
        setLayout(new GridLayout(7, 1, 0, 10));
        setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        Color green = new Color(33, 158, 33);
        Color greenLight = new Color(71, 211, 71);
        Color gray = new Color(128, 128, 128);
        Color arsenik = new Color(59, 68, 75);

        Font tombolFont = new Font("Basketball", Font.PLAIN, 18);

        // Logo
        ImageIcon rawLogo = new ImageIcon(app.getIconService().getIcon());
        Image scaledLogo = rawLogo.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(scaledLogo), SwingConstants.CENTER);

        JLabel title = new JLabel("BYTE2BITES", SwingConstants.CENTER);
        title.setForeground(green);
        title.setBorder(BorderFactory.createEmptyBorder(-20, 0, -30, 0));

        try {
            title.setFont(Font.createFont(Font.TRUETYPE_FONT, new File("font/Angels.ttf")).deriveFont(39f));
        } catch (Exception e) {
            title.setFont(new Font("Arial", Font.BOLD, 39));
        }

        JLabel subtitle = new JLabel("Silahkan order makananmu!", SwingConstants.CENTER);
        subtitle.setForeground(green);
        try {
            subtitle.setFont(Font.createFont(Font.TRUETYPE_FONT, new File("font/Basketball.otf")).deriveFont(14f));
        } catch (Exception e) {
            subtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        }

        JButton btnMenu = new GradientButton("Lihat Menu", greenLight, green);
        JButton btnCart = new GradientButton("Lihat Keranjang", greenLight, green);
        JButton btnPay = new GradientButton("Bayar", greenLight, green);
        JButton historyButton = new GradientButton("Riwayat Pembelian", gray, arsenik);

        btnMenu.setFont(tombolFont);
        btnCart.setFont(tombolFont);
        btnPay.setFont(tombolFont);
        historyButton.setFont(tombolFont);

        add(historyButton);
        add(logoLabel);
        add(title);
        add(subtitle);
        add(btnMenu);
        add(btnCart);
        add(btnPay);

        // Action Listeners
        btnMenu.addActionListener(e -> app.showPanel("menu"));
        btnCart.addActionListener(e -> app.showPanel("cart"));
        btnPay.addActionListener(e -> handlePayButton());
        historyButton.addActionListener(e -> showHistory());
    }

    private void handlePayButton() {
        if (app.getCart().getItems().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Keranjang masih kosong!", "Tidak Bisa Bayar", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String input = JOptionPane.showInputDialog(this, "Masukkan jarak rumah ke toko (km):");
        if (input == null || input.isEmpty()) return;

        try {
            double km = Double.parseDouble(input);
            if (km < 0) return;

            app.setShippingCost(km * 2000);
            app.showPanel("pay");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Input harus angka positif!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showHistory() {
        if (app.getPurchaseHistory().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Anda belum pernah melakukan pembelian!", 
                "History Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JFrame historyFrame = new JFrame("Riwayat Pembelian");
        historyFrame.setSize(450, 350);
        historyFrame.setLocationRelativeTo(this);

        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (PurchaseRecord pr : app.getPurchaseHistory()) {
            listModel.addElement(pr.getDate() + " - " + pr.getItemName() +
                    " (" + pr.getQuantity() + "x) : Rp" + (pr.getPrice() * pr.getQuantity()));
        }

        JList<String> list = new JList<>(listModel);
        historyFrame.add(new JScrollPane(list));
        historyFrame.setVisible(true);
    }
}