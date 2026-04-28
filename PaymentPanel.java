
import model.Cart;
import javax.swing.*;

import java.awt.*;

public class PaymentPanel extends JPanel {

    private final FoodOrderApp app;
    private final Cart cart;
    private JLabel totalLabel;

    public PaymentPanel(FoodOrderApp app, Cart cart) {
        this.app = app;
        this.cart = cart;

        setLayout(new GridLayout(5, 1, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        Color green = new Color(33, 158, 33);
        Font tombolFont = new Font("Basketball", Font.PLAIN, 18);

        totalLabel = new JLabel();
        totalLabel.setFont(new Font("Basketball", Font.PLAIN, 20));
        totalLabel.setHorizontalAlignment(SwingConstants.CENTER);
        totalLabel.setForeground(green);

        JButton cashBtn = new JButton("Cash");
        JButton tfBtn = new JButton("Transfer Bank");
        JButton ewBtn = new JButton("E-Wallet");
        JButton back3 = new JButton("Kembali");

        cashBtn.setFont(tombolFont);
        tfBtn.setFont(tombolFont);
        ewBtn.setFont(tombolFont);
        back3.setFont(tombolFont);

        style(cashBtn, green);
        style(tfBtn, green);
        style(ewBtn, green);
        style(back3, Color.GRAY);

        add(totalLabel);
        add(cashBtn);
        add(tfBtn);
        add(ewBtn);
        add(back3);

        // Action Listeners
        cashBtn.addActionListener(e -> handleCashPayment());
        tfBtn.addActionListener(e -> handleTransferPayment());
        ewBtn.addActionListener(e -> handleEWalletPayment());
        back3.addActionListener(e -> app.showPanel("home"));
    }

    private void updateTotalLabel() {
        double total = cart.getTotal() + app.getShippingCost();
        totalLabel.setText("<html>Total: Rp " + total + "<br>( + Ongkir Rp " + app.getShippingCost() + ")</html>");
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            updateTotalLabel();
        }
    }

    private void handleCashPayment() {
        double total = cart.getTotal() + app.getShippingCost();
        app.getPaymentDetailPanel().setPaymentMethod("Cash", "Pembayaran dilakukan secara tunai.", total);
        app.showPanel("payDetail");
    }

    private void handleTransferPayment() {
        double total = cart.getTotal() + app.getShippingCost();
        app.getPaymentDetailPanel().setPaymentMethod("Transfer Bank", "Nomor Rekening : 1234 5678 9876", total);
        app.showPanel("payDetail");
    }

    private void handleEWalletPayment() {
        double total = cart.getTotal() + app.getShippingCost();
        app.getPaymentDetailPanel().setPaymentMethod("E-Wallet", "Nomor Rekening : 1234 5678 9876", total);
        app.showPanel("payDetail");
    }

    private void style(JButton b, Color c) {
        b.setBackground(c);
        b.setForeground(Color.WHITE);
    }
}