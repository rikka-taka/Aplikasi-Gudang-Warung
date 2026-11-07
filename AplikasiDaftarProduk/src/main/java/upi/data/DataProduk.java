/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package upi.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import upi.model.Produk;
import upi.model.TambahProduk;

/**
 *
 * @author muama
 */
public class DataProduk {
    private final ObservableList <TambahProduk> daftar = FXCollections.observableArrayList();
    private int urutan = 1;
    
    public ObservableList <TambahProduk> getDaftar(){
        return FXCollections.unmodifiableObservableList(daftar);
    }
    
    public TambahProduk tambah(Produk produk, String uri){
        TambahProduk p;
        p = new TambahProduk(String.format("PR%03d", urutan), produk, uri);
        daftar.add(p);
        urutan++;
        return p;
    }
    
    public void hapus (TambahProduk p){
        daftar.remove(p);
    }
    
}
