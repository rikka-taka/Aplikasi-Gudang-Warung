package upi.aplikasidaftarproduk;

import javafx.application.Application;
import javafx.stage.Stage;
import upi.data.DataProduk;

public class AplikasiDaftarProduk extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // Halaman pertama: form tambah produk
        new NavigatorAplikasi(stage, new DataProduk()).bukaForm();
    }

    public static void main(String[] args) {
        launch(args);
    }
}