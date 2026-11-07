/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package upi.model;

/**
 *
 * @author muama
 */
public abstract class Produk {
    private final String namaProduk;
    private final int hargaProduk;

    public Produk(String namaProduk, int hargaProduk) {
        if (namaProduk == null || namaProduk.isBlank()) throw new IllegalArgumentException("nama Wajib diisi");
        if (hargaProduk <= 0) throw new IllegalArgumentException ("Harga harus lebih dari nol");
        this.namaProduk = namaProduk.trim();
        this.hargaProduk = hargaProduk;
    }

    public String getNamaProduk() {
        return namaProduk;
    }

    public int getHargaProduk() {
        return hargaProduk;
    }
    
   
    
    
    
}
