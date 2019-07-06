package com.primagama.bondowoso.Model;

public class Modelnilai {
    String nama_mapel, to1, to2, to3, to4,to5;

    public Modelnilai(String nama_mapel, String to1, String to2, String to3, String to4, String to5){
        this.nama_mapel = nama_mapel;
        this.to1 = to1;
        this.to2 = to2;
        this.to3 = to3;
        this.to4 = to4;
        this.to5 = to5;
    }
    public Modelnilai(){

    }

    public String getNama_mapel() {
        return nama_mapel;
    }

    public void setNama_mapel(String nama_mapel) {
        this.nama_mapel = nama_mapel;
    }

    public String getTo1() {
        return to1;
    }

    public void setTo1(String to1) {
        this.to1 = to1;
    }

    public String getTo2() {
        return to2;
    }

    public void setTo2(String to2) {
        this.to2 = to2;
    }

    public String getTo3() {
        return to3;
    }

    public void setTo3(String to3) {
        this.to3 = to3;
    }

    public String getTo4() {
        return to4;
    }

    public void setTo4(String to4) {
        this.to4 = to4;
    }

    public String getTo5() {
        return to5;
    }

    public void setTo5(String to5) {
        this.to5 = to5;
    }
}
