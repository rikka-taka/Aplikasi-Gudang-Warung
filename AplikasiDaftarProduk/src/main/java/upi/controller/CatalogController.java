package upi.controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import upi.aplikasidaftarproduk.NavigatorAplikasi;
import upi.data.DataProduk;
import upi.model.TambahProduk;

/**
 * Controller untuk Catalog.fxml.
 * Setiap produk ditampilkan sebagai kartu yang dimuat dari KartuCatalog.fxml,
 * ditampung di TilePane di dalam ScrollPane (bisa di-scroll sesuai jumlah data).
 */
public class CatalogController implements Initializable {

    private static final String SEMUA = "Semua";
    private static final String FXML_KARTU = "KartuCatalog";
    private static final String CLASS_TERPILIH = "kartu-terpilih";

    @FXML
    private TextField cariField;
    @FXML
    private ComboBox<String> filterJenisProduk;
    @FXML
    private TilePane daftarPane;

    private NavigatorAplikasi navigator;
    private DataProduk data;
    private TambahProduk terpilih;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        filterJenisProduk.getItems().setAll(SEMUA, "Produk Makanan", "Produk Minuman");
        filterJenisProduk.setValue(SEMUA);
        filterJenisProduk.setOnAction(e -> refresh());
    }

    /** Dipanggil oleh NavigatorAplikasi setelah FXML dimuat. */
    public void setKonteks(NavigatorAplikasi navigator, DataProduk data) {
        this.navigator = navigator;
        this.data = data;
        refresh();
    }

    // ---------------------------------------------------------------- aksi

    @FXML
    private void cari(ActionEvent event) {
        refresh();
    }

    @FXML
    private void resetPencarian(ActionEvent event) {
        cariField.clear();
        filterJenisProduk.setValue(SEMUA);
        refresh();
    }

    @FXML
    private void tambah(ActionEvent event) {
        try {
            navigator.bukaForm();
        } catch (IOException e) {
            info(Alert.AlertType.ERROR, "Gagal membuka form: " + e.getMessage());
        }
    }

    @FXML
    private void detail(ActionEvent event) {
        bukaDetailTerpilih();
    }

    @FXML
    private void hapus(ActionEvent event) {
        if (terpilih == null) {
            info(Alert.AlertType.INFORMATION, "Pilih produk terlebih dahulu (klik kartu produk).");
            return;
        }
        Alert konfirmasi = new Alert(Alert.AlertType.CONFIRMATION,
                "Hapus produk \"" + terpilih.getNamaProduk() + "\"?",
                ButtonType.YES, ButtonType.NO);
        konfirmasi.setHeaderText(null);
        Optional<ButtonType> hasil = konfirmasi.showAndWait();
        if (hasil.isPresent() && hasil.get() == ButtonType.YES) {
            data.hapus(terpilih);
            terpilih = null;
            refresh();
        }
    }

    // ------------------------------------------------------------ helper

    private void bukaDetailTerpilih() {
        if (terpilih == null) {
            info(Alert.AlertType.INFORMATION, "Pilih produk terlebih dahulu (klik kartu produk).");
            return;
        }
        try {
            navigator.bukaDetail(terpilih);
        } catch (IOException e) {
            info(Alert.AlertType.ERROR, "Gagal membuka detail: " + e.getMessage());
        }
    }

    /** Terapkan pencarian + filter, lalu bangun ulang kartu untuk SEMUA hasil. */
    private void refresh() {
        if (data == null) {
            return;
        }
        String kata = cariField.getText() == null ? "" : cariField.getText().trim().toLowerCase();
        String jenis = filterJenisProduk.getValue();

        List<TambahProduk> hasil = data.getDaftar().stream()
                .filter(p -> kata.isEmpty()
                        || p.getKode().toLowerCase().contains(kata)
                        || p.getNamaProduk().toLowerCase().contains(kata))
                .filter(p -> jenis == null || SEMUA.equals(jenis) || p.getJenis().equals(jenis))
                .collect(Collectors.toList());

        // Pilihan dilepas jika produk tidak lagi tampil
        if (terpilih != null && !hasil.contains(terpilih)) {
            terpilih = null;
        }

        daftarPane.getChildren().clear();
        if (hasil.isEmpty()) {
            Label kosong = new Label("Tidak ada produk untuk ditampilkan.");
            kosong.getStyleClass().add("muted");
            kosong.setPadding(new Insets(10));
            daftarPane.getChildren().add(kosong);
            return;
        }
        try {
            for (TambahProduk p : hasil) {
                daftarPane.getChildren().add(buatKartu(p));
            }
        } catch (IOException e) {
            daftarPane.getChildren().clear();
            info(Alert.AlertType.ERROR, "Gagal memuat kartu produk: " + e.getMessage());
            return;
        }
        tandaiTerpilih();
    }

    /** Muat KartuCatalog.fxml, isi dengan data produk, dan pasang klik. */
    private VBox buatKartu(TambahProduk p) throws IOException {
        FXMLLoader loader = navigator.loader(FXML_KARTU);
        VBox kartu = loader.load();
        KartuCatalogController c = loader.getController();
        c.setData(p);

        kartu.setUserData(p);
        // Klik = pilih, klik dua kali = buka detail
        kartu.setOnMouseClicked(e -> {
            terpilih = p;
            tandaiTerpilih();
            if (e.getClickCount() == 2) {
                bukaDetailTerpilih();
            }
        });
        return kartu;
    }

    /** Beri/lepas style class "kartu-terpilih" pada kartu sesuai pilihan. */
    private void tandaiTerpilih() {
        for (Node n : daftarPane.getChildren()) {
            if (n.getUserData() == null) {
                continue;
            }
            if (n.getUserData() == terpilih) {
                if (!n.getStyleClass().contains(CLASS_TERPILIH)) {
                    n.getStyleClass().add(CLASS_TERPILIH);
                }
            } else {
                n.getStyleClass().remove(CLASS_TERPILIH);
            }
        }
    }

    private void info(Alert.AlertType tipe, String pesan) {
        Alert alert = new Alert(tipe, pesan);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}