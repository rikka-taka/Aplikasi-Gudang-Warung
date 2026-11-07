/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package upi.model;

/**
 *
 * @author muama
 */
public class TambahProduk {
    
    private final String kode;
    private final Produk produk;
    private final String gambarUri;

    public TambahProduk(String kode, Produk produk, String gambarUri) {
        if (kode == null || kode.isBlank() || produk == null){
            throw new IllegalArgumentException("Data produk tidak lengkap.");
        }
        this.kode = kode;
        this.produk = produk;
        this.gambarUri = gambarUri;
    }

    public String getKode() {
        return kode;
    }

    public Produk getProduk() {
        return produk;
    }

    public String getGambarUri() {
        return gambarUri;
    }
    
    public String getJenis(){
        if (produk instanceof ProdukMakanan){
            return "Produk Makanan";
        } else{
            return "Produk Minuman";
        }
    }
    
    public String getNamaProduk(){
        return produk.getNamaProduk();
    }
    
    public int getHargaProduk(){
        return produk.getHargaProduk();
    }
    
    
}
