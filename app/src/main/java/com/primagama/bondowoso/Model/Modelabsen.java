package com.primagama.bondowoso.Model;

public class Modelabsen {
    String hari, jam_datang, jam_pulang, keterangan, tanggal;

    public Modelabsen(String hari, String jam_datang, String jam_pulang, String keterangan, String tanggal){
        this.hari = hari;
        this.keterangan = keterangan;
        this.jam_datang = jam_datang;
        this.jam_pulang = jam_pulang;
        this.tanggal = tanggal;
    }

    public Modelabsen(){

    }

    public String getHari() {
        return hari;
    }

    public void setHari(String hari) {
        this.hari = hari;
    }

    public String getJam_datang() {
        return jam_datang;
    }

    public void setJam_datang(String jam_datang) {
        this.jam_datang = jam_datang;
    }

    public String getJam_pulang() {
        return jam_pulang;
    }

    public void setJam_pulang(String jam_pulang) {
        this.jam_pulang = jam_pulang;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }
}
