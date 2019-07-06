package com.primagama.bondowoso.Model;

public class ModelJadwal {
    String Hari, Mapel, Tentor, Ruang, Jam;

    public ModelJadwal(String hari, String mapel, String tentor, String ruang, String jam){
        this.Hari = hari;
        this.Tentor = tentor;
        this.Mapel = mapel;
        this.Ruang = ruang;
        this.Jam = jam;
    }

    public ModelJadwal(){

    }

    public  String getHari(){
        return Hari;
    }

    public void setHari(String hari){
        Hari = hari;
    }

    public String getMapel(){
        return Mapel;
    }

    public void setMapel(String mapel){
        Mapel = mapel;
    }

    public String getTentor(){
        return Tentor;
    }

    public void setTentor(String tentor){
        Tentor = tentor;
    }

    public String getRuang(){
        return Ruang;
    }

    public void setRuang(String ruang){
        Ruang = ruang;
    }

    public String getJam(){
        return Jam;
    }

    public void setJam(String jam){
        Jam = jam;
    }
}
