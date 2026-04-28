# Byte2Bites - Food Ordering System 🍔

Byte2Bites adalah aplikasi berbasis Java Desktop (Swing) yang dirancang untuk mempermudah proses pemesanan makanan. Aplikasi ini menggunakan arsitektur terpisah antara model, view, dan service untuk menjaga kode tetap modular dan profesional.

## Prasyarat

Sebelum menjalankan aplikasi, pastikan perangkat Anda sudah memiliki:
- **Java Development Kit (JDK)**: Versi 17 atau terbaru (Sangat direkomendasikan JDK 21+).
- **Editor**: [Visual Studio Code](https://code.visualstudio.com/).

## Extensions VS Code Wajib
Agar VS Code dapat mengenali struktur package dan menjalankan program dengan benar, Anda **WAJIB** menginstal ekstensi berikut dari Marketplace:

1. **Extension Pack for Java** (oleh Microsoft): Paket utama untuk dukungan bahasa Java, IntelliSense, dan debugging.
2. **Debugger for Java**: Untuk menjalankan dan mencari error pada kode.
3. **Project Manager for Java**: Untuk mengelola struktur folder `main`, `view`, `model`, dll.

---

## Struktur Project
Pastikan struktur folder Anda tetap seperti ini (Jangan pindahkan file ke root agar tidak merusak package):
```text
Byte2Bites/
├── main/       # Root aplikasi (FoodOrderApp.java)
├── model/      # Struktur data (Cart, MenuItem, dll)
├── view/       # Komponen UI (CartPanel, HomePanel, dll)
├── service/    # Logika pendukung (Sound, Icon, Menu)
├── resources/  # Asset Gambar & Audio
└── font/       # Font kustom
```

## Cara Menjalankan 

- 1. Cara Terminal (Manual & Paling Aman)
Pastikan kamu membuka terminal (PowerShell/CMD) di folder niga (root folder proyek), bukan di dalam folder main.
Ketikkan perintah ini secara berurutan:

```
PowerShell
# Langkah 1: Bersihkan file .class lama agar tidak bentrok
Get-ChildItem -Include *.class -Recurse | Remove-Item

# Langkah 2: Kompilasi semua folder sekaligus
javac main/FoodOrderApp.java view/*.java model/*.java service/*.java

# Langkah 3: Jalankan aplikasi menggunakan nama package
java main.FoodOrderApp
```

- 2. Cara VS Code (Otomatis)
Jika kamu sudah menginstall Extension Pack for Java, VS Code akan mengurus semuanya di belakang layar.

```
# Langkah 1: Buka file main/FoodOrderApp.java.

# Langkah 2: Tunggu sebentar sampai muncul tulisan kecil Run | Debug tepat di atas baris public static void main(String[] args).

# Langkah 3: Klik Run.
```