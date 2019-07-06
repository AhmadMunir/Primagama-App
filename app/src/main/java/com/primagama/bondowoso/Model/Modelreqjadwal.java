package com.primagama.bondowoso.Model;

public class Modelreqjadwal {
    String id_reqmapel, nama_reqmapel, total;

    public Modelreqjadwal(String id_reqmapel, String nama_reqmapel, String total){
        this.id_reqmapel = id_reqmapel;
        this.nama_reqmapel = nama_reqmapel;
        this.total = total;
    }

    public Modelreqjadwal(){

    }

    public String getId_reqmapel() {
        return id_reqmapel;
    }

    public void setId_reqmapel(String id_reqmapel) {
        this.id_reqmapel = id_reqmapel;
    }

    public String getNama_reqmapel() {
        return nama_reqmapel;
    }

    public void setNama_reqmapel(String nama_reqmapel) {
        this.nama_reqmapel = nama_reqmapel;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
