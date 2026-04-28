package view;


import model.Cart;
import model.MenuItem;
import service.MenuService;
import service.SoundService;

import javax.swing.*;

import main.FoodOrderApp;

import java.awt.*;
import java.io.File;

public class MenuPanel extends JPanel {

    private final FoodOrderApp app;
    private final Cart cart;
    private final SoundService soundService;
    private final MenuService menuService;

    public MenuPanel(FoodOrderApp app, MenuService menuService, Cart cart, SoundService soundService) {
        this.app = app;
        this.menuService = menuService;
        this.cart = cart;
        this.soundService = soundService;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = createHeaderPanel();

        // Menu List
        JPanel menuListPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        for (MenuItem item : menuService.getMenu()) {
            menuListPanel.add(createMenuItemPanel(item));
        }

        JScrollPane scrollPane = new JScrollPane(menuListPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(6);

        // Bottom Back Button
        JButton backButton = createStyledButton("Kembali", Color.GRAY, 18);
        backButton.addActionListener(e -> app.showPanel("home"));

        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(backButton, BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JButton cartButton = createStyledButton("Keranjang", new Color(59, 189, 59), 16);
        cartButton.addActionListener(e -> app.showPanel("cart"));

        header.add(cartButton, BorderLayout.EAST);
        return header;
    }

    private JPanel createMenuItemPanel(MenuItem m) {
        JPanel itemPanel = new JPanel(new BorderLayout());
        itemPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        itemPanel.setBackground(new Color(245, 245, 245));
        itemPanel.setPreferredSize(new Dimension(300, 100));

        // Image
        Image scaled = new ImageIcon(m.getImagePath())
                .getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        JLabel imgLabel = new JLabel(new ImageIcon(scaled));
        imgLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Text
        JLabel nameLabel = new JLabel(m.getName());
        nameLabel.setFont(new Font("Basketball", Font.PLAIN, 18));

        JLabel priceLabel = new JLabel("Rp " + m.getPrice());
        priceLabel.setFont(new Font("Arial", Font.BOLD, 14));
        priceLabel.setForeground(new Color(29, 200, 29));

        JPanel textPanel = new JPanel(new GridLayout(3, 1));
        textPanel.add(nameLabel);
        textPanel.add(priceLabel);
        textPanel.add(createRatingPanel(m));

        // Add Button
        JButton addButton = createStyledButton("ADD", new Color(59, 189, 59), 16);
        addButton.addActionListener(e -> {
            cart.addItem(m);
            soundService.playSound(soundService.getBeliSound());
            showToast(m.getName() + " berhasil ditambahkan!");
        });

        itemPanel.add(imgLabel, BorderLayout.WEST);
        itemPanel.add(textPanel, BorderLayout.CENTER);
        itemPanel.add(addButton, BorderLayout.EAST);

        return itemPanel;
    }

    private JPanel createRatingPanel(MenuItem m) {
        JPanel ratingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        JLabel[] stars = new JLabel[5];

        for (int i = 0; i < 5; i++) {
            final int index = i;
            stars[i] = new JLabel("☆");
            stars[i].setFont(new Font("Dialog", Font.BOLD, 20));
            stars[i].addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    m.setRating(index + 1);
                    updateStars(stars, m.getRating());
                }
            });
            ratingPanel.add(stars[i]);
        }
        updateStars(stars, m.getRating());
        return ratingPanel;
    }

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

    private JButton createStyledButton(String text, Color bg, int size) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Basketball", Font.PLAIN, size));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        return btn;
    }

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

        int x = app.getX() + 10;
        int y = app.getY() + app.getHeight() - toast.getHeight() - 10;
        toast.setLocation(x, y);
        toast.setVisible(true);

        new Timer(2500, e -> toast.dispose()).start();
    }
}