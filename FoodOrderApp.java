import model.Cart;
import model.PurchaseRecord;
import service.IconService;
import service.MenuService;
import service.SoundService;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class FoodOrderApp extends JFrame {

    private CardLayout cardLayout = new CardLayout();
    private JPanel mainPanel = new JPanel(cardLayout);

    // Services & Model
    private final MenuService menuService = new MenuService();
    private final IconService iconService = new IconService();
    private final SoundService soundService = new SoundService();

    private final Cart cart = new Cart();
    private final ArrayList<PurchaseRecord> purchaseHistory = new ArrayList<>();

    private double shippingCost = 0;
    private String proofPath = null;

    // Panels (Hanya dideklarasikan SEKALI)
    private HomePanel homePanel;
    private MenuPanel menuPanel;
    private CartPanel cartPanel;
    private PaymentPanel paymentPanel;
    private PaymentDetailPanel paymentDetailPanel;

    public FoodOrderApp() {
        setTitle("BYTE2BITES - Project Kelompok 2");
        setSize(750, 520);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        initializePanels();
        add(mainPanel);

        soundService.playBackgroundMusic(soundService.getBacksound());

        setVisible(true);
    }

    // Tambahkan variabel ini di bagian deklarasi panel
    private LoadingPanel loadingPanel;

    private void initializePanels() {
        homePanel = new HomePanel(this);
        menuPanel = new MenuPanel(this, menuService, cart, soundService);
        cartPanel = new CartPanel(this, cart, soundService);
        paymentPanel = new PaymentPanel(this, cart);
        paymentDetailPanel = new PaymentDetailPanel(this, cart);
        
        // Tambahkan Loading Panel
        loadingPanel = new LoadingPanel();

        mainPanel.add(homePanel, "home");
        mainPanel.add(menuPanel, "menu");
        mainPanel.add(cartPanel, "cart");
        mainPanel.add(paymentPanel, "pay");
        mainPanel.add(paymentDetailPanel, "payDetail");
        mainPanel.add(loadingPanel, "loading");   // ← Tambahkan ini
    }

    // Method untuk menampilkan loading
    public void showLoading(String message) {
        loadingPanel.setLoadingText(message);
        cardLayout.show(mainPanel, "loading");
    }

    public void hideLoading() {
        cardLayout.show(mainPanel, "home");   // atau panel sebelumnya
    }

    public void showPanel(String panelName) {
        if (panelName.equals("cart")) {
            cartPanel.refreshCart();
        }
        cardLayout.show(mainPanel, panelName);
    }

    // ==================== GETTER & SETTER ====================

    public Cart getCart() {
        return cart;
    }

    public ArrayList<PurchaseRecord> getPurchaseHistory() {
        return purchaseHistory;
    }

    public double getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(double shippingCost) {
        this.shippingCost = shippingCost;
    }

    public String getProofPath() {
        return proofPath;
    }

    public void setProofPath(String proofPath) {
        this.proofPath = proofPath;
    }

    public void addToPurchaseHistory(PurchaseRecord record) {
        purchaseHistory.add(record);
    }

    public void resetAfterPayment() {
        proofPath = null;
        shippingCost = 0;
        cart.clear();
        showPanel("home");
    }

    public SoundService getSoundService() {
        return soundService;
    }

    public IconService getIconService() {
        return iconService;
    }

    public PaymentDetailPanel getPaymentDetailPanel() {
        return paymentDetailPanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FoodOrderApp());
    }
}