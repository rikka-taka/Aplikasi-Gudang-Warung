package upi.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import upi.model.TambahProduk;

/**
 * Controller untuk KartuCatalog.fxml (satu kartu produk di Catalog).
 */
public class KartuCatalogController {

    private static final double LEBAR_FOTO = 158;
    private static final double TINGGI_FOTO = 128;

    @FXML
    private ImageView fotoView;
    @FXML
    private Label namaLabel;
    @FXML
    private Label jenisLabel;
    @FXML
    private Label hargaLabel;

    /** Isi kartu dengan data produk. */
    public void setData(TambahProduk p) {
        namaLabel.setText(p.getKode() + " - " + p.getNamaProduk());
        jenisLabel.setText(p.getJenis());
        hargaLabel.setText(DetailController.formatRupiah(p.getHargaProduk()));

        if (p.getGambarUri() != null) {
            // Dimuat di latar belakang agar daftar besar tetap lancar
            Image gambar = new Image(p.getGambarUri(), LEBAR_FOTO, TINGGI_FOTO, true, true, true);
            fotoView.setImage(gambar.isError() ? null : gambar);
        } else {
            fotoView.setImage(null);
        }
    }
}