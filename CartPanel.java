
import model.Cart;
import model.CartItem;
import model.MenuItem;
import service.SoundService;

import javax.swing.*;

import java.awt.*;
import java.text.DecimalFormat;

public class CartPanel extends JPanel {

    private final FoodOrderApp app;
    private final Cart cart;
    private final SoundService soundService;

    private JPanel cartListPanel;
    private JLabel totalLabel;
    private JButton clearCartButton;

    public CartPanel(FoodOrderApp app, Cart cart, SoundService soundService) {
        this.app = app;
        this.cart = cart;
        this.soundService = soundService;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel untuk daftar item
        cartListPanel = new JPanel();
        cartListPanel.setLayout(new GridLayout(0, 1, 10, 10));

        JScrollPane cartScroll = new JScrollPane(cartListPanel);
        cartScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        cartScroll.getVerticalScrollBar().setUnitIncrement(8);

        // Panel bawah (Total + Tombol)
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 0));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));

        totalLabel = new JLabel("Total: Rp 0", SwingConstants.CENTER);
        totalLabel.setFont(new Font("Basketball", Font.BOLD, 20));
        totalLabel.setForeground(new Color(33, 158, 33));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));

        JButton backButton = new JButton("Kembali");
        backButton.setFont(new Font("Basketball", Font.PLAIN, 18));
        style(backButton, Color.GRAY);

        clearCartButton = new JButton("Kosongkan Keranjang");
        clearCartButton.setFont(new Font("Basketball", Font.PLAIN, 16));
        clearCartButton.setBackground(new Color(220, 53, 69));
        clearCartButton.setForeground(Color.WHITE);

        buttonPanel.add(backButton);
        buttonPanel.add(clearCartButton);

        bottomPanel.add(totalLabel, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Tambahkan ke layout utama
        add(cartScroll, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Action Listeners
        backButton.addActionListener(e -> app.showPanel("home"));
        clearCartButton.addActionListener(e -> clearCart());
    }

    /**
     * Refresh tampilan keranjang (dipanggil saat masuk ke halaman cart)
     */
    public void refreshCart() {
        cartListPanel.removeAll();

        if (cart.getItems().isEmpty()) {
            JLabel emptyLabel = new JLabel("Keranjang masih kosong", SwingConstants.CENTER);
            emptyLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            emptyLabel.setForeground(Color.GRAY);
            cartListPanel.add(emptyLabel);
        } else {
            for (CartItem ci : cart.getItems()) {
                cartListPanel.add(createCartItemPanel(ci));
            }
        }

        updateTotal();
        cartListPanel.revalidate();
        cartListPanel.repaint();
    }

    private JPanel createCartItemPanel(CartItem ci) {
        MenuItem m = ci.getItem();

        JPanel itemPanel = new JPanel(new BorderLayout(10, 5));
        itemPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        itemPanel.setBackground(new Color(245, 245, 245));
        itemPanel.setPreferredSize(new Dimension(300, 130));

        // Gambar
        ImageIcon rawFood = new ImageIcon(m.getImagePath());
        Image scaled = rawFood.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
        JLabel imgLabel = new JLabel(new ImageIcon(scaled));

        // Nama dan Harga
        JLabel nameLabel = new JLabel(m.getName());
        nameLabel.setFont(new Font("Basketball", Font.PLAIN, 18));

        JLabel priceLabel = new JLabel("Rp " + m.getPrice() + " x " + ci.getQuantity() + " = Rp " + ci.getTotalPrice());
        priceLabel.setFont(new Font("Arial", Font.BOLD, 14));
        priceLabel.setForeground(new Color(37, 174, 37));

        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        textPanel.setOpaque(false);
        textPanel.add(nameLabel);
        textPanel.add(priceLabel);

        // Spinner Quantity
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(ci.getQuantity(), 1, 999, 1);
        JSpinner quantitySpinner = new JSpinner(spinnerModel);
        quantitySpinner.setPreferredSize(new Dimension(70, 30));

        quantitySpinner.addChangeListener(e -> {
            ci.setQuantity((int) quantitySpinner.getValue());
            priceLabel.setText("Rp " + m.getPrice() + " x " + ci.getQuantity() + " = Rp " + ci.getTotalPrice());
            updateTotal();
        });

        JPanel spinnerPanel = new JPanel();
        spinnerPanel.setOpaque(false);
        spinnerPanel.add(quantitySpinner);

        // Tombol Hapus
        JButton removeBtn = new JButton("Hapus");
        removeBtn.setFont(new Font("Basketball", Font.PLAIN, 14));
        removeBtn.setBackground(new Color(220, 53, 69));
        removeBtn.setForeground(Color.WHITE);
        removeBtn.addActionListener(e -> {
            cart.getItems().remove(ci);
            refreshCart();
        });

        // Susun layout item
        itemPanel.add(imgLabel, BorderLayout.WEST);
        itemPanel.add(textPanel, BorderLayout.CENTER);
        itemPanel.add(spinnerPanel, BorderLayout.EAST);
        itemPanel.add(removeBtn, BorderLayout.SOUTH);

        return itemPanel;
    }

    private void updateTotal() {
        double total = cart.getTotal();
        DecimalFormat df = new DecimalFormat("#,###");
        totalLabel.setText("Total: Rp " + df.format(total));
    }

    private void clearCart() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Apakah Anda yakin ingin mengosongkan keranjang?",
                "Konfirmasi",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            cart.clear();
            refreshCart();
        }
    }

    private void style(JButton b, Color c) {
        b.setBackground(c);
        b.setForeground(Color.WHITE);
    }
}