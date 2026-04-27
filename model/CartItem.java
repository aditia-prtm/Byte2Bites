package model;

// ========================= CART ITEM (baru) =========================
public class CartItem {
    private MenuItem item;
    private int quantity;

    public CartItem(MenuItem item) {
        this.item = item;
        this.quantity = 1;
    }

    public MenuItem getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int q) {
        quantity = q;
    }

    public double getTotalPrice() {
        return item.getPrice() * quantity;
    }
}