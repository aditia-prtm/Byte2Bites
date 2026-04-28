package view;


import model.Cart;

import javax.swing.*;

import main.FoodOrderApp;

import java.awt.*;

public class PaymentPanel extends JPanel {

    private final FoodOrderApp app;
    private final Cart cart;

    private JLabel totalLabel;
    private double shippingCost = 0;

    public PaymentPanel(FoodOrderApp app, Cart cart) {
        this.app = app;
        this.cart = cart;
        initUI();
    }

    private void initUI() {
        setLayout(new GridLayout(5, 1, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        Color green = new Color(33, 158, 33);
        Font font = new Font("Basketball", Font.PLAIN, 18);

        totalLabel = new JLabel("", SwingConstants.CENTER);
        totalLabel.setFont(new Font("Basketball", Font.PLAIN, 20));
        totalLabel.setForeground(green);

        JButton cashBtn = createStyledButton("Cash", green);
        JButton tfBtn = createStyledButton("Transfer Bank", green);
        JButton ewBtn = createStyledButton("E-Wallet", green);
        JButton backBtn = createStyledButton("Kembali", Color.GRAY);

        add(totalLabel);
        add(cashBtn);
        add(tfBtn);
        add(ewBtn);
        add(backBtn);

        // Action Listeners
        cashBtn.addActionListener(e -> handleCashPayment());
        tfBtn.addActionListener(e -> handleTransferPayment());
        ewBtn.addActionListener(e -> handleEWalletPayment());
        backBtn.addActionListener(e -> app.showPanel("home"));
    }

    public void setShippingCost(double shippingCost) {
        this.shippingCost = shippingCost;
    }

    public double getShippingCost() {
        return shippingCost;
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            updateTotalLabel();
        }
    }

    private void updateTotalLabel() {
        double subtotal = cart.getTotal();
        double total = subtotal + shippingCost;
        totalLabel.setText(String.format("<html>Total: Rp %s<br>( + Ongkir Rp %s)</html>",
                (int)total, (int)shippingCost));
    }

    private void handleCashPayment() {
        double total = cart.getTotal() + shippingCost;
        app.getPaymentDetailPanel().setPaymentMethod("Cash", "Pembayaran dilakukan secara tunai.", total);
        app.showPanel("payDetail");
    }

    private void handleTransferPayment() {
        double total = cart.getTotal() + shippingCost;
        app.getPaymentDetailPanel().setPaymentMethod("Transfer Bank", "Nomor Rekening : 1234 5678 9876", total);
        app.showPanel("payDetail");
    }

    private void handleEWalletPayment() {
        double total = cart.getTotal() + shippingCost;
        app.getPaymentDetailPanel().setPaymentMethod("E-Wallet", "Nomor Rekening : 1234 5678 9876", total);
        app.showPanel("payDetail");
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Basketball", Font.PLAIN, 18));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        return btn;
    }
}