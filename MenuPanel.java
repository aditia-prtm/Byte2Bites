
import model.Cart;
import model.MenuItem;
import service.MenuService;
import service.SoundService;

import javax.swing.*;

import java.awt.*;
import java.io.File;

public class MenuPanel extends JPanel {

    private final FoodOrderApp app;
    private final Cart cart;
    private final SoundService soundService;

    public MenuPanel(FoodOrderApp app, MenuService menuService, Cart cart, SoundService soundService) {
        this.app = app;
        this.cart = cart;
        this.soundService = soundService;

        setLayout(new BorderLayout());

        JPanel menuListPanel = new JPanel();
        menuListPanel.setLayout(new GridLayout(0, 1, 10, 10));

        for (MenuItem m : menuService.getMenu()) {
            JPanel itemPanel = createMenuItemPanel(m);
            menuListPanel.add(itemPanel);
        }

        JScrollPane menuScroll = new JScrollPane(menuListPanel);
        menuScroll.getVerticalScrollBar().setUnitIncrement(6);
        menuScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Header dengan tombol Keranjang
        JPanel menuHeader = new JPanel(new BorderLayout());
        menuHeader.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JButton cartInMenu = new JButton("Keranjang");
        cartInMenu.setFont(new Font("Basketball", Font.PLAIN, 16));
        cartInMenu.setBackground(new Color(59, 189, 59));
        cartInMenu.setForeground(Color.WHITE);
        cartInMenu.addActionListener(e -> app.showPanel("cart"));

        menuHeader.add(cartInMenu, BorderLayout.EAST);

        // Tombol Kembali
        JButton back1 = new JButton("Kembali");
        back1.setFont(new Font("Basketball", Font.PLAIN, 18));
        style(back1, Color.GRAY);
        back1.addActionListener(e -> app.showPanel("home"));

        add(menuHeader, BorderLayout.NORTH);
        add(menuScroll, BorderLayout.CENTER);
        add(back1, BorderLayout.SOUTH);
    }

    private JPanel createMenuItemPanel(MenuItem m) {
        JPanel itemPanel = new JPanel(new BorderLayout());
        itemPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        itemPanel.setBackground(new Color(245, 245, 245));
        itemPanel.setPreferredSize(new Dimension(300, 100));

        // Image
        ImageIcon rawFood = new ImageIcon(m.getImagePath());
        Image scaled = rawFood.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        JLabel imgLabel = new JLabel(new ImageIcon(scaled));
        imgLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Name & Price
        JLabel name = new JLabel(m.getName());
        name.setFont(new Font("Basketball", Font.PLAIN, 18));

        JLabel price = new JLabel("Rp " + m.getPrice());
        price.setFont(new Font("Arial", Font.BOLD, 14));
        price.setForeground(new Color(29, 200, 29));

        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.add(name);
        textPanel.add(price);

        // Rating Stars
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

        // Add Button
        JButton addBtn = new JButton("ADD");
        addBtn.setFont(new Font("Basketball", Font.PLAIN, 16));
        addBtn.setBackground(new Color(59, 189, 59));
        addBtn.setForeground(Color.WHITE);
        addBtn.addActionListener(e -> {
            cart.addItem(m);
            soundService.playSound(soundService.getBeliSound());
            showToast(m.getName() + " added!");
        });

        itemPanel.add(imgLabel, BorderLayout.WEST);
        itemPanel.add(textPanel, BorderLayout.CENTER);
        itemPanel.add(addBtn, BorderLayout.EAST);

        return itemPanel;
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

    private void style(JButton b, Color c) {
        b.setBackground(c);
        b.setForeground(Color.WHITE);
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