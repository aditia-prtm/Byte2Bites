package main;

import model.Cart;
import model.PurchaseRecord;
import service.IconService;
import service.MenuService;
import service.SoundService;
import view.CartPanel;
import view.HomePanel;
import view.LoadingPanel;
import view.MenuPanel;
import view.PaymentDetailPanel;
import view.PaymentPanel;

import javax.swing.*;

import java.awt.*;
import java.util.ArrayList;

public class FoodOrderApp extends JFrame {

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel mainPanel = new JPanel(cardLayout);

    // Services & Model
    private final MenuService menuService;
    private final IconService iconService;
    private final SoundService soundService;
    private final Cart cart;
    private final ArrayList<PurchaseRecord> purchaseHistory;

    // Panels
    private HomePanel homePanel;
    private MenuPanel menuPanel;
    private CartPanel cartPanel;
    private PaymentPanel paymentPanel;
    private PaymentDetailPanel paymentDetailPanel;
    private LoadingPanel loadingPanel;

    public FoodOrderApp() {
        this.menuService = new MenuService();
        this.iconService = new IconService();
        this.soundService = new SoundService();
        this.cart = new Cart();
        this.purchaseHistory = new ArrayList<>();

        setTitle("BYTE2BITES - Project Kelompok 2");
        setSize(600, 520);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        initializePanels();
        add(mainPanel);

        soundService.playBackgroundMusic(soundService.getBacksound());
        setVisible(true);
    }

    private void initializePanels() {
        // Inject dependencies
        homePanel = new HomePanel(this);
        menuPanel = new MenuPanel(this, menuService, cart, soundService);
        cartPanel = new CartPanel(this, cart, soundService);
        paymentPanel = new PaymentPanel(this, cart);
        paymentDetailPanel = new PaymentDetailPanel(this, cart);
        loadingPanel = new LoadingPanel();

        // Register panels ke CardLayout
        mainPanel.add(homePanel, "home");
        mainPanel.add(menuPanel, "menu");
        mainPanel.add(cartPanel, "cart");
        mainPanel.add(paymentPanel, "pay");
        mainPanel.add(paymentDetailPanel, "payDetail");
        mainPanel.add(loadingPanel, "loading");
    }

    // ==================== NAVIGATION METHODS ====================
    public void showPanel(String panelName) {
        if ("cart".equals(panelName)) {
            cartPanel.refreshCart();
        }
        cardLayout.show(mainPanel, panelName);
    }

    public void showLoading(String message) {
        loadingPanel.setLoadingText(message);
        cardLayout.show(mainPanel, "loading");
    }

    public void hideLoading() {
        cardLayout.show(mainPanel, "home");
    }

    // ==================== GETTERS ====================
    public Cart getCart() { return cart; }
    public ArrayList<PurchaseRecord> getPurchaseHistory() { return purchaseHistory; }
    public IconService getIconService() { return iconService; }
    public SoundService getSoundService() { return soundService; }
    public PaymentDetailPanel getPaymentDetailPanel() { return paymentDetailPanel; }

    // ==================== STATE MANAGEMENT ====================
    public double getShippingCost() {
        // Bisa di-refactor ke ShippingService nanti
        return paymentPanel != null ? paymentPanel.getShippingCost() : 0;
    }

    public void setShippingCost(double shippingCost) {
        if (paymentPanel != null) {
            paymentPanel.setShippingCost(shippingCost);
        }
    }

    public void addToPurchaseHistory(PurchaseRecord record) {
        purchaseHistory.add(record);
    }

    public void resetAfterPayment() {
        cart.clear();
        setShippingCost(0);
        showPanel("home");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FoodOrderApp());
    }
}