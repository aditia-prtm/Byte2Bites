package model;

// ========================= RIWAYAT PEMBELIAN =========================
public class PurchaseRecord {
    private String itemName;
    private double price; // harga per item
    private int quantity;
    private String date;

    public PurchaseRecord(String itemName, double price, int quantity, String date) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
        this.date = date;
    }

    public String getItemName() {
        return itemName;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getDate() {
        return date;
    }
}