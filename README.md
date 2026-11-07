# Aplikasi Daftar Produk

Aplikasi desktop berbasis **JavaFX** untuk mencatat dan menampilkan daftar produk toko (makanan dan minuman). Pengguna dapat menambah produk lengkap dengan gambar, melihat detailnya, lalu menelusuri seluruh produk dalam tampilan katalog berbentuk kartu yang bisa dicari, difilter, dan dihapus.

## Fitur

- **Tambah produk**: isi nama, jenis (makanan/minuman), harga, dan gambar produk.
- **Validasi input**:
  - Nama wajib diisi.
  - Jenis produk wajib dipilih.
  - Harga harus berupa angka bulat lebih dari nol. Format `15000`, `15.000`, dan `Rp 15.000` diterima.
  - Gambar harus PNG atau JPEG, maksimal 5 MB, dan harus bisa dibaca.
- **Kode produk otomatis** dengan format `PR001`, `PR002`, dan seterusnya.
- **Halaman detail** yang menampilkan kode, nama, jenis, harga, dan gambar produk.
- **Katalog** berupa kartu produk di dalam area yang bisa di-scroll:
  - Cari berdasarkan kode atau nama (tekan Enter atau tombol *Cari*).
  - Filter berdasarkan jenis produk.
  - Klik kartu untuk memilih, klik dua kali untuk membuka detail.
  - Hapus produk dengan dialog konfirmasi.
- Harga ditampilkan dalam format rupiah, misalnya `Rp 15.000`.

## Teknologi

| Komponen | Keterangan |
|---|---|
| Bahasa | Java |
| UI | JavaFX (FXML + CSS) |
| Arsitektur | MVC sederhana |
| Penyimpanan | In-memory (`ObservableList`), belum ada persistensi |

> File FXML pada proyek ini ditulis dengan namespace JavaFX 26. Gunakan JavaFX dan JDK dengan versi yang saling kompatibel (lihat [openjfx.io](https://openjfx.io) untuk persyaratan JDK tiap versi JavaFX).

## Struktur Proyek

```
src/main/
├── java/upi/
│   ├── aplikasidaftarproduk/
│   │   ├── AplikasiDaftarProduk.java   # Kelas Application (entry point JavaFX)
│   │   ├── Launcher.java               # Entry point alternatif untuk jar/shaded jar
│   │   └── NavigatorAplikasi.java      # Perpindahan halaman + pemuatan FXML
│   ├── controller/
│   │   ├── FormController.java         # Form tambah produk
│   │   ├── DetailController.java       # Halaman detail produk
│   │   ├── CatalogController.java      # Katalog: cari, filter, pilih, hapus
│   │   └── KartuCatalogController.java # Satu kartu produk di katalog
│   ├── data/
│   │   └── DataProduk.java             # Penyimpanan produk + pembuat kode
│   └── model/
│       ├── Produk.java                 # Kelas abstract (nama, harga, validasi)
│       ├── ProdukMakanan.java
│       ├── ProdukMinuman.java
│       └── TambahProduk.java           # Produk terdaftar (kode, produk, gambar)
└── resources/
    ├── form.fxml
    ├── Detail.fxml
    ├── Catalog.fxml
    ├── Kartu-Catalog.fxml
    └── styles/app.css
```

### Alur Aplikasi

```
Form ──simpan──▶ Detail ──Lihat Catalog──▶ Catalog
 ▲                  │                         │
 └──Tambah Produk───┘◀──────double-click───────┤
 ▲                                             │
 └────────────────── Tambah ───────────────────┘
```

## tampilan Aplikasi:
<img width="1127" height="872" alt="image" src="https://github.com/user-attachments/assets/4609cc52-0801-4a43-80fc-39aa4f642986" />
<img width="1137" height="887" alt="image" src="https://github.com/user-attachments/assets/7236b5e2-1604-4984-91fa-da4b80e46614" />
<img width="1127" height="887" alt="image" src="https://github.com/user-attachments/assets/97f0498c-4577-4ed3-90a7-987f3e11caf3" />



Aplikasi dibuka di halaman **Form**. `NavigatorAplikasi` memegang satu `Scene` dan hanya mengganti root-nya saat berpindah halaman, sehingga stylesheet dan ukuran jendela tetap konsisten. Satu objek `DataProduk` dibagikan ke semua controller.

## Cara Menjalankan

### Prasyarat

- JDK yang kompatibel dengan versi JavaFX yang dipakai
- [JavaFX SDK](https://openjfx.io) (atau dependensi JavaFX lewat Maven/Gradle)

### Lewat IDE (NetBeans, IntelliJ, Eclipse)

1. Buat proyek Java/JavaFX (Maven atau Gradle) dengan struktur seperti di atas.
2. Tambahkan dependensi `javafx-controls` dan `javafx-fxml`.
3. Jalankan kelas `upi.aplikasidaftarproduk.AplikasiDaftarProduk`.

### Lewat command line

Ganti `PATH_TO_FX` dengan lokasi folder `lib` dari JavaFX SDK.

```bash
# Kompilasi
javac --module-path PATH_TO_FX --add-modules javafx.controls,javafx.fxml \
      -d out $(find src/main/java -name "*.java")

# Salin resource (FXML dan CSS) ke folder output
cp -r src/main/resources/* out/

# Jalankan
java --module-path PATH_TO_FX --add-modules javafx.controls,javafx.fxml \
     -cp out upi.aplikasidaftarproduk.AplikasiDaftarProduk
```

Di Windows, ganti `$(find ...)` dengan daftar file `.java` secara manual, atau gunakan IDE/Maven.

> **Catatan:** `Launcher` disediakan sebagai entry point untuk pembuatan jar yang dijalankan tanpa module path. Kelas utama yang diperpanjang dari `Application` tidak dapat dijalankan langsung dari jar biasa pada beberapa konfigurasi.

## Cara Penggunaan

1. **Tambah produk**: isi nama, pilih jenis, isi harga, lalu pilih gambar (opsional). Klik *Simpan dan Lihat Detail*.
2. **Lihat detail**: halaman detail muncul setelah produk tersimpan. Klik *Tambah Produk Lain* untuk kembali ke form, atau *Lihat Catalog* untuk membuka katalog.
3. **Telusuri katalog**: ketik kode atau nama produk di kolom pencarian, atau pilih filter jenis. Klik *Reset* untuk menghapus semua filter.
4. **Hapus produk**: klik kartu untuk memilihnya, lalu klik *Hapus* dan konfirmasi.

## Batasan Saat Ini

- Data hanya disimpan di memori, jadi **semua produk hilang saat aplikasi ditutup**.
- Belum ada fitur **edit** produk (baru Create, Read, Delete).
- Gambar disimpan sebagai referensi ke file lokal, bukan disalin ke dalam aplikasi. Jika file dipindah atau dihapus, gambar tidak akan tampil.
- Belum ada pengecekan nama produk kembar.

## Masalah yang Diketahui

- `CatalogController` memuat kartu dengan nama `KartuCatalog`, sedangkan file resource bernama `Kartu-Catalog.fxml`. Akibatnya katalog gagal memuat kartu saat sudah ada produk.
  **Solusi:** ganti nama file menjadi `KartuCatalog.fxml`, atau ubah konstanta `FXML_KARTU` di `CatalogController` menjadi `"Kartu-Catalog"`.
- Halaman detail selalu menampilkan pesan "Produk berhasil ditambahkan", termasuk saat dibuka dari katalog.

## Rencana Pengembangan

- [ ] Persistensi data (JSON, CSV, atau SQLite)
- [ ] Fitur edit produk
- [ ] Unit test untuk validasi model dan `DataProduk`
- [ ] Pemuatan gambar dengan ukuran terbatas agar hemat memori
- [ ] Memindahkan jenis produk ke `enum` atau method abstract di `Produk`
- [ ] Format rupiah berbasis `NumberFormat` dengan locale Indonesia

## Lisensi

Tentukan lisensi proyek ini (misalnya MIT) dan tambahkan file `LICENSE`.

## Pengembang

MUAMAD ASIF MU'AFA
