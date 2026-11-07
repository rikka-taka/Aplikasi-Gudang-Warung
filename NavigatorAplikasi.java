package upi.aplikasidaftarproduk;

import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import upi.controller.CatalogController;
import upi.controller.DetailController;
import upi.controller.FormController;
import upi.data.DataProduk;
import upi.model.TambahProduk;

/**
 * Mengatur perpindahan halaman (form, detail, catalog) dan
 * meneruskan data/konteks ke tiap controller.
 */
public class NavigatorAplikasi {

    // Nama file FXML (tanpa .fxml) - harus sama persis dengan nama file, termasuk huruf besar/kecil
    private static final String FXML_FORM = "form";
    private static final String FXML_DETAIL = "Detail";
    private static final String FXML_CATALOG = "Catalog";

    // Folder di dalam src/main/resources yang dicari (berurutan).
    // Cukup tambahkan folder lain di sini jika file Anda berada di tempat berbeda.
    private static final String[] FOLDER = {"/upi/", "/kelas/", "/upi/fxml/", "/upi/view/", "/"};

    private final Stage stage;
    private final DataProduk data;
    private Scene scene;

    public NavigatorAplikasi(Stage stage, DataProduk data) {
        this.stage = stage;
        this.data = data;
    }

    /** Cari resource di semua folder kandidat; null jika tidak ada. */
    private URL cari(String relatif) {
        for (String folder : FOLDER) {
            URL url = getClass().getResource(folder + relatif);
            if (url != null) {
                return url;
            }
        }
        return null;
    }

    /** Membuat FXMLLoader untuk file FXML bernama {@code nama} (tanpa .fxml). */
    public FXMLLoader loader(String nama) throws IOException {
        URL url = cari(nama + ".fxml");
        if (url == null) {
            throw new IOException("File " + nama + ".fxml tidak ditemukan. Folder yang dicari: "
                    + String.join(" ", FOLDER) + " (di dalam src/main/resources)");
        }
        return new FXMLLoader(url);
    }

    private void tampil(Parent root) {
        if (scene == null) {
            scene = new Scene(root, 900, 680);
            URL css = cari("styles/app.css");
            if (css != null) {
                scene.getStylesheets().add(css.toExternalForm());
            } else {
                System.err.println("PERINGATAN: styles/app.css tidak ditemukan, tampilan tanpa stylesheet.");
            }
            stage.setScene(scene);
            stage.setTitle("Aplikasi Daftar Produk");
            stage.setMinWidth(640);
            stage.setMinHeight(480);
        } else {
            scene.setRoot(root);
        }
        stage.show();
    }

    public void bukaForm() throws IOException {
        FXMLLoader l = loader(FXML_FORM);
        Parent root = l.load();
        FormController c = l.getController();
        c.setKonteks(this, data);
        tampil(root);
    }

    public void bukaDetail(TambahProduk p) throws IOException {
        FXMLLoader l = loader(FXML_DETAIL);
        Parent root = l.load();
        DetailController c = l.getController();
        c.setData(this, p);
        tampil(root);
    }

    public void bukaCatalog() throws IOException {
        FXMLLoader l = loader(FXML_CATALOG);
        Parent root = l.load();
        CatalogController c = l.getController();
        c.setKonteks(this, data);
        tampil(root);
    }
}