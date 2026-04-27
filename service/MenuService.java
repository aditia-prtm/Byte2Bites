package service;
import model.*;
import java.util.ArrayList;

// ========================= SERVICE =========================
public class MenuService {
    private ArrayList<MenuItem> menu = new ArrayList<>();

    public MenuService() {
        menu.add(new MenuItem("Gado-Gado Teknik", 10000,
                "resources/image/gadogado.jpg"));
        menu.add(
                new MenuItem("Mie Ayam Teknik", 10000, "resources/image/mieayam.jpg"));
        menu.add(new MenuItem("Ayam Geprek FE", 10000,
                "resources/image/ayamgeprek.png"));
        menu.add(new MenuItem("SuSuSu", 4000, "resources/image/sususu.png"));
        menu.add(new MenuItem("Donut FKIP", 10000, "resources/image/donut.jpg"));
        menu.add(new MenuItem("Angin FASILKOM", 999999, "resources/image/ngin.png"));
    }

    public ArrayList<MenuItem> getMenu() {
        return menu;
    }
}