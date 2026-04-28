
import model.Cart;
import model.CartItem;
import model.PurchaseRecord;
import javax.swing.*;

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

        setLayout(new GridLayout(6, 1, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        Color green = new Color(33, 158, 33);
        Font tombolFont = new Font("Basketball", Font.PLAIN, 18);

        // Label Informasi Pembayaran
        pdMethod = new JLabel("METODE PEMBAYARAN : -", SwingConstants.CENTER);
        pdMethod.setFont(new Font("Basketball", Font.BOLD, 20));
        pdMethod.setForeground(Color.BLACK);

        pdNorek = new JLabel("Nomor Rekening : 1234 5678 9876", SwingConstants.CENTER);
        pdNorek.setFont(new Font("Basketball", Font.PLAIN, 16));
        pdNorek.setForeground(Color.BLACK);

        pdTotal = new JLabel("TOTAL BAYAR : Rp 0", SwingConstants.CENTER);
        pdTotal.setFont(new Font("Basketball", Font.BOLD, 20));
        pdTotal.setForeground(green);

        JButton pdUpload = new JButton("Upload Bukti Pembayaran");
        JButton pdPayNow = new JButton("Bayar Sekarang");
        JButton pdBack = new JButton("Kembali");

        pdUpload.setFont(tombolFont);
        pdPayNow.setFont(tombolFont);
        pdBack.setFont(tombolFont);

        style(pdUpload, green);
        style(pdPayNow, green);
        style(pdBack, Color.GRAY);

        add(pdMethod);
        add(pdNorek);
        add(pdTotal);
        add(pdUpload);
        add(pdPayNow);
        add(pdBack);

        // Action Listeners
        pdUpload.addActionListener(e -> showReceipt());
        pdPayNow.addActionListener(e -> showReceipt());
        pdBack.addActionListener(e -> app.showPanel("pay"));
    }

    /**
     * Dipanggil dari PaymentPanel saat memilih metode pembayaran
     */
    public void setPaymentMethod(String method, String norekText, double total) {
        pdMethod.setText("METODE PEMBAYARAN : " + method);
        pdNorek.setText(norekText);
        pdTotal.setText("TOTAL BAYAR : Rp " + total);
    }

    /**
     * Method utama untuk menampilkan struk dengan animasi loading
     */
    private void showReceipt() {
        // Tampilkan Loading Screen
        app.showLoading("Sedang memproses pembayaran...");

        // Simulasi proses pembayaran (delay 1.8 detik)
        Timer loadingTimer = new Timer(1800, e -> {
            // Sembunyikan loading
            app.hideLoading();

            // Proses menampilkan struk
            processReceipt();
        });

        loadingTimer.setRepeats(false);
        loadingTimer.start();
    }

    /**
     * Logic menampilkan struk dan menyimpan riwayat
     */
    private void processReceipt() {
        String method = pdMethod.getText().replace("METODE PEMBAYARAN : ", "");

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        double subtotal = cart.getTotal();
        double total = subtotal + app.getShippingCost();

        // Build daftar item
        StringBuilder itemList = new StringBuilder();
        itemList.append("-----------------------------\n");
        itemList.append("Item Belanja:\n");

        for (CartItem ci : cart.getItems()) {
            itemList.append(ci.getItem().getName()).append("\n");
            itemList.append("  ").append(ci.getQuantity())
                    .append(" x Rp ").append(ci.getItem().getPrice())
                    .append("   =   Rp ").append(ci.getTotalPrice()).append("\n");
        }
        itemList.append("-----------------------------\n");

        // Build struk lengkap
        StringBuilder sb = new StringBuilder();
        sb.append("========= STRUK BELANJA =========\n");
        sb.append("Tanggal           : ").append(now.format(fmt)).append("\n");
        sb.append("Metode Pembayaran : ").append(method).append("\n");
        sb.append(itemList);
        sb.append("Subtotal          : Rp ").append(subtotal).append("\n");
        sb.append("Ongkir            : Rp ").append(app.getShippingCost()).append("\n");
        sb.append("TOTAL BAYAR       : Rp ").append(total).append("\n");
        if (app.getProofPath() != null) {
            sb.append("Bukti Pembayaran  : ").append(app.getProofPath()).append("\n");
        }
        sb.append("=================================\n");
        sb.append("Terima kasih telah berbelanja!\n");
        sb.append("=================================\n");

        // Tampilkan Struk
        JOptionPane.showMessageDialog(this, sb.toString(), 
                "Struk Pembayaran", JOptionPane.INFORMATION_MESSAGE);

        // Tambahkan ke Riwayat Pembelian
        for (CartItem ci : cart.getItems()) {
            PurchaseRecord record = new PurchaseRecord(
                    ci.getItem().getName(),
                    ci.getItem().getPrice(),
                    ci.getQuantity(),
                    now.toLocalDate().toString()
            );
            app.addToPurchaseHistory(record);
        }

        // Simpan struk ke file
        saveReceiptToFile(sb.toString());

        // Reset aplikasi setelah pembayaran selesai
        app.resetAfterPayment();
    }

    private void saveReceiptToFile(String receiptText) {
        try {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Simpan Struk");
            chooser.setSelectedFile(new java.io.File("struk.txt"));

            int option = chooser.showSaveDialog(this);

            if (option == JFileChooser.APPROVE_OPTION) {
                java.io.File file = chooser.getSelectedFile();
                java.io.FileWriter writer = new java.io.FileWriter(file);
                writer.write(receiptText);
                writer.close();

                JOptionPane.showMessageDialog(this,
                        "Struk berhasil disimpan ke:\n" + file.getAbsolutePath(),
                        "Berhasil Disimpan",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Gagal menyimpan struk: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void style(JButton b, Color c) {
        b.setBackground(c);
        b.setForeground(Color.WHITE);
    }
}