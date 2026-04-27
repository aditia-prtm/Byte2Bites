import model.MenuItem;
import model.CartItem;
import model.Cart;
import model.PurchaseRecord;
import service.IconService;
import service.MenuService;
import service.SoundService;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;


// ========================= GUI =========================

public class FoodOrderGUI extends JFrame {
    
    private JPanel cartListPanel = new JPanel();
    private CardLayout layout = new CardLayout();
    private JPanel mainPanel = new JPanel(layout);

    private MenuService menuService = new MenuService();
    private IconService iconService = new IconService();
    private SoundService soundService = new SoundService();

    private Cart cart = new Cart();

    private double shippingCost = 0;
    private ArrayList<PurchaseRecord> purchaseHistory = new ArrayList<>();

    private String proofPath = null; // untuk bukti pembayaran

    private JLabel totalLabel = new JLabel();

    public FoodOrderGUI() {

        setTitle("Project Kelompok 2");
        setSize(450, 520);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        Color green = new Color(33, 158, 33);
        Color greenLight = new Color(71, 211, 71);

        // ================= HOME PAGE =================
        JPanel home = new JPanel(new GridLayout(7, 1, 0, 10));
        home.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        ImageIcon rawLogo = new ImageIcon(iconService.getIcon());
        Image scaledLogo = rawLogo.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(scaledLogo), SwingConstants.CENTER);

        JLabel title = new JLabel("BYTE2BITES", SwingConstants.CENTER);

        try {
            title.setFont(Font.createFont(Font.TRUETYPE_FONT, new File("font/Angels.ttf")).deriveFont(39f));
        } catch (Exception e) {
            title.setFont(new Font("Arial", Font.BOLD, 39));
        }

        title.setForeground(green);
        title.setBorder(BorderFactory.createEmptyBorder(-20, 0, -30, 0));

        JLabel subtitle = new JLabel("Silahkan order makananmu!", SwingConstants.CENTER);

        try {
            subtitle.setFont(Font.createFont(Font.TRUETYPE_FONT, new File("font/Basketball.otf")).deriveFont(14f));
        } catch (Exception e) {
            subtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        }

        subtitle.setForeground(green);


        Font tombolFont = new Font("Basketball", Font.PLAIN, 18);

        JButton btnMenu = new GradientButton("Lihat Menu", greenLight, green);
        JButton btnCart = new GradientButton("Lihat Keranjang", greenLight, green);
        JButton btnPay = new GradientButton("Bayar", greenLight, green);
        JButton historyButton = new GradientButton("Riwayat Pembelian", green, greenLight);

        btnMenu.setFont(tombolFont);
        btnCart.setFont(tombolFont);
        btnPay.setFont(tombolFont);

        home.add(historyButton);
        home.add(logoLabel);
        home.add(title);
        home.add(subtitle);
        home.add(btnMenu);
        home.add(btnCart);
        home.add(btnPay);

        historyButton.addActionListener(e -> {
            if(purchaseHistory.isEmpty()){
                JOptionPane.showMessageDialog(
                this,
                "Anda belum pernah melakukan pembelian!",
                "History Information",
                JOptionPane.INFORMATION_MESSAGE);
            }else{
                JFrame historyFrame = new JFrame("Riwayat Pembelian");
                historyFrame.setSize(400, 300);
    
                DefaultListModel<String> listModel = new DefaultListModel<>();
    
                for (PurchaseRecord pr : purchaseHistory) {
                    listModel.addElement(
                            pr.getDate() + " - " + pr.getItemName() +
                                    " (" + pr.getQuantity() + "x) : Rp" + (pr.getPrice() * pr.getQuantity()));
    
                    JList<String> list = new JList<>(listModel);
    
                    historyFrame.add(new JScrollPane(list));
    
                    historyFrame.setVisible(true);
                }
            }

        });

        // ================= MENU PAGE =================
        JPanel menuPage = new JPanel(new BorderLayout());

        JPanel menuListPanel = new JPanel();
        menuListPanel.setLayout(new GridLayout(0, 1, 10, 10));

        for (MenuItem m : menuService.getMenu()) {

            JPanel itemPanel = new JPanel(new BorderLayout());
            itemPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
            itemPanel.setBackground(new Color(245, 245, 245));
            itemPanel.setPreferredSize(new Dimension(300, 100));

            ImageIcon rawFood = new ImageIcon(m.getImagePath());
            Image scaled = rawFood.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);

            JLabel imgLabel = new JLabel(new ImageIcon(scaled));
            imgLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            JLabel name = new JLabel(m.getName());
            name.setFont(new Font("Basketball", Font.PLAIN, 18));

            JLabel price = new JLabel("Rp " + m.getPrice());
            price.setFont(new Font("Arial", Font.BOLD, 14));
            price.setForeground(new Color(29, 200, 29));

            JPanel textPanel = new JPanel(new GridLayout(2, 1));
            textPanel.add(name);
            textPanel.add(price);

            JPanel ratingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            JLabel[] stars = new JLabel[5];

            for (int i = 0; i < 5; i++) {
                stars[i] = new JLabel("☆");
                stars[i].setFont(new Font("Dialog", Font.BOLD, 20));
                int starIndex = i;

                stars[i].addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent e) {
                        m.setRating(starIndex + 1);
                        updateStars(stars, m.getRating());
                    }
                });

                ratingPanel.add(stars[i]);
            }

            updateStars(stars, m.getRating());
            textPanel.add(ratingPanel);

            JButton addX = new JButton("ADD");
            addX.setFont(new Font("Basketball", Font.PLAIN, 16));
            addX.setBackground(new Color(59, 189, 59));
            addX.setForeground(Color.WHITE);
            addX.addActionListener(e -> {
                cart.addItem(m);
                playSound(soundService.getBeliSound());
                showToast(m.getName() + " added!");
            });

            itemPanel.add(imgLabel, BorderLayout.WEST);
            itemPanel.add(textPanel, BorderLayout.CENTER);
            itemPanel.add(addX, BorderLayout.EAST);

            menuListPanel.add(itemPanel);
        }
        JScrollPane menuScroll = new JScrollPane(menuListPanel);
        menuScroll.getVerticalScrollBar().setUnitIncrement(6);
        menuScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        menuPage.add(menuScroll, BorderLayout.CENTER);      

        JButton back1 = new JButton("Kembali");
        back1.setFont(tombolFont);
        style(back1, Color.GRAY);
        menuPage.add(back1, BorderLayout.SOUTH);

        // === HEADER ATAS UNTUK TOMBOL KERANJANG ===
        JPanel menuHeader = new JPanel(new BorderLayout());
        menuHeader.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JButton cartInMenu = new JButton("Keranjang");
        cartInMenu.setFont(new Font("Basketball", Font.PLAIN, 16));
        cartInMenu.setBackground(new Color(59, 189, 59));
        cartInMenu.setForeground(Color.WHITE);

        // Action buka cart langsung
        cartInMenu.addActionListener(e -> {
            updateCartPanel(cartListPanel);
            layout.show(mainPanel, "cart");
        });

        // Masukkan tombol ke kanan
        menuHeader.add(cartInMenu, BorderLayout.EAST);
        menuPage.add(menuHeader, BorderLayout.NORTH);

        // ================= CART PAGE =================
        JPanel cartPage = new JPanel(new BorderLayout());

        cartListPanel.setLayout(new GridLayout(0, 1, 10, 10));

        JScrollPane cartScroll = new JScrollPane(cartListPanel);
        cartScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        cartScroll.getVerticalScrollBar().setUnitIncrement(6);

        JButton back2 = new JButton("Kembali");
        back2.setFont(tombolFont);
        style(back2, Color.GRAY);

        cartPage.add(cartScroll, BorderLayout.CENTER);
        cartPage.add(back2, BorderLayout.SOUTH);

        // ================= PAYMENT PAGE =================
        JPanel payPage = new JPanel(new GridLayout(5, 1, 10, 10));
        payPage.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

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

        payPage.add(totalLabel);
        payPage.add(cashBtn);
        payPage.add(tfBtn);
        payPage.add(ewBtn);
        payPage.add(back3);

        // ================= PAY DETAIL PAGE =================
        JPanel payDetailPage = new JPanel(new GridLayout(6, 1, 10, 10));
        payDetailPage.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Header metode pembayaran
        JLabel pdMethod = new JLabel("METODE PEMBAYARAN : -", SwingConstants.CENTER);
        pdMethod.setFont(new Font("Basketball", Font.BOLD, 20));
        pdMethod.setForeground(Color.BLACK);

        // Nomor rekening
        JLabel pdNorek = new JLabel("Nomor Rekening : 1234 5678 9876", SwingConstants.CENTER);
        pdNorek.setFont(new Font("Basketball", Font.PLAIN, 16));
        pdNorek.setForeground(Color.BLACK);

        // Total bayar
        JLabel pdTotal = new JLabel("TOTAL BAYAR : Rp 0", SwingConstants.CENTER);
        pdTotal.setFont(new Font("Basketball", Font.BOLD, 20));
        pdTotal.setForeground(new Color(33, 158, 33)); // hijau

        JButton pdUpload = new JButton("Upload Bukti Pembayaran");
        pdUpload.setFont(tombolFont);
        style(pdUpload, green);

        JButton pdPayNow = new JButton("Bayar Sekarang");
        pdPayNow.setFont(tombolFont);
        style(pdPayNow, green);

        JButton pdBack = new JButton("Kembali");
        pdBack.setFont(tombolFont);
        style(pdBack, Color.GRAY);

        payDetailPage.add(pdMethod);
        payDetailPage.add(pdNorek);
        payDetailPage.add(pdTotal);
        payDetailPage.add(pdUpload);
        payDetailPage.add(pdPayNow);
        payDetailPage.add(pdBack);

        // =============== ADD PAGES TO MAIN PANEL ===============
        mainPanel.add(home, "home");
        mainPanel.add(menuPage, "menu");
        mainPanel.add(cartPage, "cart");
        mainPanel.add(payPage, "pay");
        mainPanel.add(payDetailPage, "payDetail"); // <<< pay detail ditambahin DI SINI

        add(mainPanel);

        // ================= ACTIONS =================

        btnMenu.addActionListener(e -> layout.show(mainPanel, "menu"));

        btnCart.addActionListener(e -> {
            updateCartPanel(cartListPanel);
            layout.show(mainPanel, "cart");
        });

        btnPay.addActionListener(e -> {
            if (cart.getItems().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Keranjang masih kosong!",
                        "Tidak Bisa Bayar",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            String input = JOptionPane.showInputDialog(this, "Masukkan jarak rumah ke toko (km):");

            if (input == null || input.isEmpty())
                return;
            
            double km = Double.parseDouble(input);

            if(km < 0) return;

            shippingCost = km * 2000;

            double total = cart.getTotal() + shippingCost;

            totalLabel.setText("<html>Total: Rp " + total + "<br>( + Ongkir Rp " + shippingCost + ")</html>");
    
            layout.show(mainPanel, "pay");

            for (CartItem ci : cart.getItems()) {
                purchaseHistory.add(
                        new PurchaseRecord(
                                ci.getItem().getName(),
                                ci.getItem().getPrice(),
                                ci.getQuantity(),
                                java.time.LocalDate.now().toString()));
            }
        });

        // CASH save receipt
        cashBtn.addActionListener(e -> {
            double total = cart.getTotal() + shippingCost;

            pdMethod.setText("METODE PEMBAYARAN : Cash");
            pdNorek.setText("Pembayaran dilakukan secara tunai.");
            pdTotal.setText("TOTAL BAYAR : Rp " + total);

            layout.show(mainPanel, "payDetail");
        });

        // TF / E-WALLET skrg buka halaman payDetailPage dulu
        tfBtn.addActionListener(e -> {
            double total = cart.getTotal() + shippingCost;

            pdMethod.setText("METODE PEMBAYARAN : Transfer Bank");
            pdNorek.setText("Nomor Rekening : 1234 5678 9876");
            pdTotal.setText("TOTAL BAYAR : Rp " + total);

            layout.show(mainPanel, "payDetail");
        });

        ewBtn.addActionListener(e -> {
            double total = cart.getTotal() + shippingCost;

            pdMethod.setText("METODE PEMBAYARAN : E-Wallet");
            pdNorek.setText("Nomor Rekening : 1234 5678 9876");
            pdTotal.setText("TOTAL BAYAR : Rp " + total);

            layout.show(mainPanel, "payDetail");
        });

        back1.addActionListener(e -> layout.show(mainPanel, "home"));
        back2.addActionListener(e -> layout.show(mainPanel, "home"));
        back3.addActionListener(e -> layout.show(mainPanel, "home"));

        // ================= payDetail buttons' listeners =================
        pdUpload.addActionListener(e -> {
            showReceipt(pdMethod.getText().replace("METODE PEMBAYARAN : ", ""));
        });

        pdPayNow.addActionListener(e -> {
            showReceipt(pdMethod.getText().replace("METODE PEMBAYARAN : ", ""));
        });

        pdBack.addActionListener(e -> layout.show(mainPanel, "pay"));

        playBackgroundMusic(soundService.getBacksound());
    }

    // ======================== STYLE ========================
    private void style(JButton b, Color c) {
        b.setBackground(c);
        b.setForeground(Color.WHITE);
    }

    // ======================== CART PANEL (diubah) ========================
    private void updateCartPanel(JPanel cartListPanel) {
        cartListPanel.removeAll();

        for (CartItem ci : cart.getItems()) { // <-- sekarang iterasi CartItem
            MenuItem m = ci.getItem();

            JPanel itemPanel = new JPanel(new BorderLayout());
            itemPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
            itemPanel.setBackground(new Color(245, 245, 245));
            itemPanel.setPreferredSize(new Dimension(300, 120));

            ImageIcon rawFood = new ImageIcon(m.getImagePath());
            Image scaled = rawFood.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            JLabel imgLabel = new JLabel(new ImageIcon(scaled));
            imgLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            JLabel name = new JLabel(m.getName());
            name.setFont(new Font("Basketball", Font.PLAIN, 18));

            // label harga yang menampilkan qty x price = totalItem
            JLabel price = new JLabel("Rp " + m.getPrice() + " x " + ci.getQuantity() + " = Rp " + ci.getTotalPrice());
            price.setFont(new Font("Arial", Font.BOLD, 14));
            price.setForeground(new Color(37, 174, 37));

            JPanel textPanel = new JPanel(new GridLayout(2, 1));
            textPanel.add(name);
            textPanel.add(price);

            // Spinner untuk ubah quantity
            SpinnerNumberModel model = new SpinnerNumberModel(ci.getQuantity(), 1, 999, 1);
            JSpinner spinner = new JSpinner(model);
            spinner.addChangeListener(e -> {
                int newQty = (int) spinner.getValue();
                ci.setQuantity(newQty);
                price.setText("Rp " + m.getPrice() + " x " + ci.getQuantity() + " = Rp " + ci.getTotalPrice());
                // update displayed total (totalLabel di halaman "pay" hanya diperbarui ketika
                // masuk ke page bayar)
                // jika mau update totalLabel langsung di sini, uncomment baris berikut:
                // totalLabel.setText("Total: Rp " + (cart.getTotal() + shippingCost) + " (+
                // Ongkir Rp " + shippingCost + ")");
            });

            JPanel spinnerPanel = new JPanel();
            spinnerPanel.add(spinner);

            JButton removeBtn = new JButton("Hapus");
            removeBtn.setFont(new Font("Basketball", Font.PLAIN, 14));
            removeBtn.setBackground(Color.RED);
            removeBtn.setForeground(Color.WHITE);
            removeBtn.addActionListener(e -> {
                cart.getItems().remove(ci);
                updateCartPanel(cartListPanel);
            });

            itemPanel.add(imgLabel, BorderLayout.WEST);
            itemPanel.add(textPanel, BorderLayout.CENTER);
            itemPanel.add(spinnerPanel, BorderLayout.EAST);
            itemPanel.add(removeBtn, BorderLayout.SOUTH);

            cartListPanel.add(itemPanel);
        }

        cartListPanel.revalidate();
        cartListPanel.repaint();
    }

    // ======================== PAYMENT / STRUK ========================
    private void showReceipt(String method) {

        // Formatter tanggal (lengkap)
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        java.time.format.DateTimeFormatter fmt = java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        // Hitung total
        double subtotal = cart.getTotal();
        double total = subtotal + shippingCost;

        // Item list (gunakan CartItem dan quantity yang tersimpan)
        StringBuilder itemList = new StringBuilder();
        itemList.append("-----------------------------\n");
        itemList.append("Item Belanja:\n");

        for (CartItem ci : cart.getItems()) {
            String itemName = ci.getItem().getName();
            int qty = ci.getQuantity();
            double priceEach = ci.getItem().getPrice();
            double totalPrice = ci.getTotalPrice();

            itemList.append(itemName).append("\n");
            itemList.append("  ").append(qty)
                    .append(" x Rp ").append(priceEach)
                    .append("   =   Rp ").append(totalPrice).append("\n");
        }

        itemList.append("-----------------------------\n");

        // STRUK FULL
        StringBuilder sb = new StringBuilder();
        sb.append("========= STRUK BELANJA =========\n");
        sb.append("Tanggal           : ").append(now.format(fmt)).append("\n");
        sb.append("Metode Pembayaran : ").append(method).append("\n");
        sb.append(itemList);
        sb.append("Subtotal          : Rp ").append(subtotal).append("\n");
        sb.append("Ongkir            : Rp ").append(shippingCost).append("\n");
        sb.append("TOTAL BAYAR       : Rp ").append(total).append("\n");
        if (proofPath != null) {
            sb.append("Bukti Pembayaran  : ").append(proofPath).append("\n");
        }
        sb.append("=================================\n");
        sb.append("Terima kasih telah berbelanja!\n");
        sb.append("=================================\n");

        JOptionPane.showMessageDialog(
                this,
                sb.toString(),
                "Struk Pembayaran",
                JOptionPane.INFORMATION_MESSAGE);

        saveReceiptToFile(sb.toString());

        // Reset cart & state
        proofPath = null;
        cart.clear();
        shippingCost = 0;
        layout.show(mainPanel, "home");
    }

    // ======================== SAVE RECEIPT TO FOLDER ========================
    private void saveReceiptToFile(String receiptText) {
        try {
            // biar user bebas pilih folder penyimpanan
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Simpan Struk");
            chooser.setSelectedFile(new java.io.File("struk.txt")); // default name

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

    // ======================== SOUND ========================
    private void playSound(String path) {
        try {
            AudioInputStream audio = AudioSystem.getAudioInputStream(new java.io.File(path));
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            clip.start();
        } catch (Exception ex) {
            System.out.println("Sound gagal diputar: " + ex.getMessage());
        }
    }

    private Clip bgmClip;

    private void playBackgroundMusic(String path) {
        try {
            AudioInputStream audio = AudioSystem.getAudioInputStream(new java.io.File(path));
            bgmClip = AudioSystem.getClip();
            bgmClip.open(audio);
            bgmClip.loop(Clip.LOOP_CONTINUOUSLY);
            bgmClip.start();
        } catch (Exception e) {
            System.out.println("BGM gagal diputar: " + e.getMessage());
        }
    }

    // ======================== STARS ========================
    private void updateStars(JLabel[] stars, int rating) {
        for (int i = 0; i < stars.length; i++) {
            if (i < rating) {
                stars[i].setText("★");
                stars[i].setForeground(Color.ORANGE);
            } else {
                stars[i].setText("☆");
                stars[i].setForeground(Color.GRAY);
            }
        }
    }

    public static void main(String[] args) {
        new FoodOrderGUI().setVisible(true);
    }

    // ============ BUTTON GRADIENT ============
    class GradientButton extends JButton {
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

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            int w = getWidth(), h = getHeight();

            GradientPaint gp = new GradientPaint(0, 0, c1, 0, h, c2);
            g2.setPaint(gp);
            g2.fillRoundRect(0, 0, w, h, 20, 20);

            super.paintComponent(g);
        }
    }

    // ======================== TOAST (popup kecil) ========================
    private void showToast(String message) {
        JWindow toast = new JWindow();

        JPanel panel = new JPanel();
        panel.setBackground(new Color(0, 0, 0, 180));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel label = new JLabel(message);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 14));

        panel.add(label);
        toast.add(panel);
        toast.pack();

        // posisi pojok kiri bawah
        int x = this.getX() + 10;
        int y = this.getY() + this.getHeight() - toast.getHeight() - 10;

        toast.setLocation(x, y);
        toast.setVisible(true);

        // hilang otomatis setelah 2.5 detik
        new Timer(2500, e -> toast.dispose()).start();
    }
}