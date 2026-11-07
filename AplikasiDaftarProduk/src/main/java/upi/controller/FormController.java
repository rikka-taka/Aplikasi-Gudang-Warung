package upi.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import upi.aplikasidaftarproduk.NavigatorAplikasi;
import upi.data.DataProduk;
import upi.model.Produk;
import upi.model.ProdukMakanan;
import upi.model.ProdukMinuman;
import upi.model.TambahProduk;

/**
 * Controller untuk form.fxml (form tambah produk).
 */
public class FormController implements Initializable {

    private static final String MAKANAN = "Produk Makanan";
    private static final String MINUMAN = "Produk Minuman";
    private static final long MAKS_UKURAN_GAMBAR = 5L * 1024 * 1024; // 5 MB

    @FXML
    private TextField namaField;
    @FXML
    private ComboBox<String> jenisBox;
    @FXML
    private TextField HargaField;
    @FXML
    private ImageView gambarView;
    @FXML
    private Label pesanLabel;

    private NavigatorAplikasi navigator;
    private DataProduk data;
    private String gambarUri; // null jika belum memilih gambar

    /** Dipanggil oleh NavigatorAplikasi setelah FXML dimuat. */
    public void setKonteks(NavigatorAplikasi navigator, DataProduk data) {
        this.navigator = navigator;
        this.data = data;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        jenisBox.getItems().setAll(MAKANAN, MINUMAN);
        jenisBox.setPromptText("Pilih jenis produk");
        pesanLabel.setText("");
    }

    @FXML
    private void bukaCatalog(ActionEvent event) {
        try {
            navigator.bukaCatalog();
        } catch (IOException e) {
            pesanLabel.setText("Gagal membuka catalog: " + e.getMessage());
        }
    }

    @FXML
    private void pilihGambar(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Pilih Gambar Produk");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Gambar PNG/JPEG", "*.png", "*.jpg", "*.jpeg"));

        Window window = gambarView.getScene().getWindow();
        File file = chooser.showOpenDialog(window);
        if (file == null) {
            return; // dibatalkan
        }

        String nama = file.getName().toLowerCase();
        if (!(nama.endsWith(".png") || nama.endsWith(".jpg") || nama.endsWith(".jpeg"))) {
            pesanLabel.setText("File harus berformat PNG atau JPEG.");
            return;
        }
        if (file.length() > MAKS_UKURAN_GAMBAR) {
            pesanLabel.setText("Ukuran gambar maksimal 5 MB.");
            return;
        }

        Image gambar = new Image(file.toURI().toString());
        if (gambar.isError()) {
            pesanLabel.setText("Gambar tidak dapat dibaca.");
            return;
        }
        gambarUri = file.toURI().toString();
        gambarView.setImage(gambar);
        pesanLabel.setText("");
    }

    @FXML
    private void tambahProduk(ActionEvent event) {
        String nama = namaField.getText();
        String jenis = jenisBox.getValue();

        if (nama == null || nama.isBlank()) {
            pesanLabel.setText("Nama produk wajib diisi.");
            return;
        }
        if (jenis == null) {
            pesanLabel.setText("Jenis produk wajib dipilih.");
            return;
        }

        int harga;
        try {
            // Terima format seperti "15000", "15.000", atau "Rp 15.000"
            String bersih = HargaField.getText() == null ? ""
                    : HargaField.getText().replace("Rp", "").replace("rp", "")
                            .replace(".", "").replace(" ", "").trim();
            harga = Integer.parseInt(bersih);
        } catch (NumberFormatException e) {
            pesanLabel.setText("Harga harus berupa angka bulat.");
            return;
        }

        try {
            Produk produk = MAKANAN.equals(jenis)
                    ? new ProdukMakanan(nama, harga)
                    : new ProdukMinuman(nama, harga);
            TambahProduk baru = data.tambah(produk, gambarUri);
            navigator.bukaDetail(baru);
        } catch (IllegalArgumentException e) {
            // pesan validasi dari kelas Produk
            pesanLabel.setText(e.getMessage());
        } catch (IOException e) {
            pesanLabel.setText("Gagal membuka halaman detail: " + e.getMessage());
        }
    }
}