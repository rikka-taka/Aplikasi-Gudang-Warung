package upi.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import upi.aplikasidaftarproduk.NavigatorAplikasi;
import upi.model.TambahProduk;

/**
 * Controller untuk Detail.fxml (detail produk yang baru ditambahkan).
 */
public class DetailController implements Initializable {

    @FXML
    private Label kodeLabel;
    @FXML
    private Label namaLabel;
    @FXML
    private Label jenisLabel;
    @FXML
    private Label hargaLabel;
    @FXML
    private ImageView gambarView;
    @FXML
    private Label pesanLabel;

    private NavigatorAplikasi navigator;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pesanLabel.setText("");
    }

    /** Dipanggil oleh NavigatorAplikasi setelah FXML dimuat. */
    public void setData(NavigatorAplikasi navigator, TambahProduk p) {
        this.navigator = navigator;

        kodeLabel.setText(p.getKode());
        namaLabel.setText(p.getNamaProduk());
        jenisLabel.setText(p.getJenis());
        hargaLabel.setText(formatRupiah(p.getHargaProduk()));

        if (p.getGambarUri() != null) {
            Image gambar = new Image(p.getGambarUri());
            gambarView.setImage(gambar.isError() ? null : gambar);
        } else {
            gambarView.setImage(null);
        }
        pesanLabel.setText("Produk berhasil ditambahkan.");
    }

    static String formatRupiah(int harga) {
        return String.format("Rp %,d", harga).replace(',', '.');
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
    private void tambahLagi(ActionEvent event) {
        try {
            navigator.bukaForm();
        } catch (IOException e) {
            pesanLabel.setText("Gagal membuka form: " + e.getMessage());
        }
    }
}