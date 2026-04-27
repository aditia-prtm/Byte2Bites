package model;

import java.util.ArrayList;

// ========================= CART (diubah) =========================
public class Cart {
    private ArrayList<CartItem> items = new ArrayList<>();

    // jika item sudah ada, tambah quantity; jika belum, tambahkan CartItem baru
    public void addItem(MenuItem menuItem) {
        for (CartItem ci : items) {
            if (ci.getItem() == menuItem) {
                ci.setQuantity(ci.getQuantity() + 1);
                return;
            }
        }
        items.add(new CartItem(menuItem));
    }

    public ArrayList<CartItem> getItems() {
        return items;
    }

    public double getTotal() {
        double t = 0;
        for (CartItem ci : items)
            t += ci.getTotalPrice();
        return t;
    }

    public void clear() {
        items.clear();
    }
}