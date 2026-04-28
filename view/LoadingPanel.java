package view;


import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;

import main.FoodOrderApp;

public class LoadingPanel extends JPanel {

    private JLabel loadingLabel;
    private JProgressBar progressBar;
    private Timer timer;
    private int progress = 0;

    public LoadingPanel() {
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);

        JLabel logo = new JLabel("BYTE2BITES", SwingConstants.CENTER);
        logo.setFont(new Font("Angels", Font.BOLD, 28));
        logo.setForeground(new Color(33, 158, 33));

        loadingLabel = new JLabel("Sedang memproses", SwingConstants.CENTER);
        loadingLabel.setFont(new Font("Basketball", Font.PLAIN, 18));
        loadingLabel.setForeground(new Color(59, 68, 75));

        progressBar = new JProgressBar(0, 100);
        progressBar.setPreferredSize(new Dimension(300, 20));
        progressBar.setStringPainted(true);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 0, 15, 0);

        gbc.gridy = 0; centerPanel.add(logo, gbc);
        gbc.gridy = 1; centerPanel.add(loadingLabel, gbc);
        gbc.gridy = 2; 
        gbc.insets = new Insets(20, 0, 10, 0);
        centerPanel.add(progressBar, gbc);

        add(centerPanel, BorderLayout.CENTER);
        startLoadingAnimation();
    }

    private void startLoadingAnimation() {
        progress = 0;
        progressBar.setValue(0);

        timer = new Timer(50, (ActionEvent e) -> {
            progress += 2;
            if (progress > 100) {
                progress = 100;
                timer.stop();
            }
            progressBar.setValue(progress);

            int dots = (progress / 10) % 4;
            loadingLabel.setText("Sedang memproses" + ".".repeat(dots));
        });
        timer.start();
    }

    public void setLoadingText(String text) {
        loadingLabel.setText(text);
        startLoadingAnimation(); // restart animasi
    }

    public void stopLoading() {
        if (timer != null) timer.stop();
    }
}