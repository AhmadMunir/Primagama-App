package com.primagama.bondowoso.Model;

public class ModelPembayaran {
    String no;
    String waktu, jumlah_bayar, sisa, admin;

    public ModelPembayaran(String no, String waktu, String jumlah_bayar, String sisa, String admin){
        this.no=no;
        this.waktu=waktu;
        this.jumlah_bayar=jumlah_bayar;
        this.sisa=sisa;
        this.admin=admin;
    }

    public ModelPembayaran(){

    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public String getJumlah_bayar() {
        return jumlah_bayar;
    }

    public void setJumlah_bayar(String jumlah_bayar) {
        this.jumlah_bayar = jumlah_bayar;
    }

    public String getSisa() {
        return sisa;
    }

    public void setSisa(String sisa) {
        this.sisa = sisa;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }
}
