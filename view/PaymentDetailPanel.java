package view;


import model.Cart;
import model.CartItem;
import model.PurchaseRecord;

import javax.swing.*;

import main.FoodOrderApp;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PaymentDetailPanel extends JPanel {

    private final FoodOrderApp app;
    private final Cart cart;

    private JLabel pdMethod;
    private JLabel pdNorek;
    private JLabel pdTotal;

    public PaymentDetailPanel(FoodOrderApp app, Cart cart) {
        this.app = app;
        this.cart = cart;
        initUI();
    }

    private void initUI() {
        setLayout(new GridLayout(6, 1, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        Color green = new Color(33, 158, 33);
        Font font = new Font("Basketball", Font.PLAIN, 18);

        pdMethod = new JLabel("METODE PEMBAYARAN : -", SwingConstants.CENTER);
        pdMethod.setFont(new Font("Basketball", Font.BOLD, 20));

        pdNorek = new JLabel("Nomor Rekening : 1234 5678 9876", SwingConstants.CENTER);
        pdNorek.setFont(new Font("Basketball", Font.PLAIN, 16));

        pdTotal = new JLabel("TOTAL BAYAR : Rp 0", SwingConstants.CENTER);
        pdTotal.setFont(new Font("Basketball", Font.BOLD, 20));
        pdTotal.setForeground(green);

        JButton uploadBtn = createStyledButton("Upload Bukti Pembayaran", green);
        JButton payNowBtn = createStyledButton("Bayar Sekarang", green);
        JButton backBtn = createStyledButton("Kembali", Color.GRAY);

        add(pdMethod);
        add(pdNorek);
        add(pdTotal);
        add(uploadBtn);
        add(payNowBtn);
        add(backBtn);

        uploadBtn.addActionListener(e -> showReceipt());
        payNowBtn.addActionListener(e -> showReceipt());
        backBtn.addActionListener(e -> app.showPanel("pay"));
    }

    public void setPaymentMethod(String method, String norekText, double total) {
        pdMethod.setText("METODE PEMBAYARAN : " + method);
        pdNorek.setText(norekText);
        pdTotal.setText("TOTAL BAYAR : Rp " + (int)total);
    }

    private void showReceipt() {
        app.showLoading("Sedang memproses pembayaran...");

        Timer timer = new Timer(1800, e -> {
            app.hideLoading();
            processReceipt();
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void processReceipt() {
        String method = pdMethod.getText().replace("METODE PEMBAYARAN : ", "");
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        double subtotal = cart.getTotal();
        double total = subtotal + app.getShippingCost();

        // Build struk
        StringBuilder sb = new StringBuilder();
        sb.append("========= STRUK BELANJA =========\n");
        sb.append("Tanggal : ").append(now.format(fmt)).append("\n");
        sb.append("Metode Pembayaran : ").append(method).append("\n\n");

        sb.append("Item Belanja:\n");
        for (CartItem ci : cart.getItems()) {
            sb.append(ci.getItem().getName())
              .append(" (").append(ci.getQuantity()).append("x)\n");
        }

        sb.append("\nSubtotal : Rp ").append((int)subtotal);
        sb.append("\nOngkir   : Rp ").append((int)app.getShippingCost());
        sb.append("\nTOTAL    : Rp ").append((int)total);
        sb.append("\n=================================\n");
        sb.append("Terima kasih telah berbelanja!\n");

        JOptionPane.showMessageDialog(this, sb.toString(), "Struk Pembayaran", JOptionPane.INFORMATION_MESSAGE);

        // Simpan ke history
        for (CartItem ci : cart.getItems()) {
            PurchaseRecord record = new PurchaseRecord(
                    ci.getItem().getName(),
                    ci.getItem().getPrice(),
                    ci.getQuantity(),
                    now.toLocalDate().toString()
            );
            app.addToPurchaseHistory(record);
        }

        saveReceiptToFile(sb.toString());
        app.resetAfterPayment();
    }

    private void saveReceiptToFile(String receiptText) {
        try {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Simpan Struk");
            chooser.setSelectedFile(new java.io.File("struk.txt"));

            if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                java.io.File file = chooser.getSelectedFile();
                java.io.FileWriter writer = new java.io.FileWriter(file);
                writer.write(receiptText);
                writer.close();

                JOptionPane.showMessageDialog(this, "Struk berhasil disimpan ke:\n" + file.getAbsolutePath(),
                        "Berhasil", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan struk: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Basketball", Font.PLAIN, 18));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        return btn;
    }
}