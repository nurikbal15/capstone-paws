package com.dicoding.pawscapstone.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "peliharaan")
public class Peliharaan {
    @PrimaryKey(autoGenerate = true)
    private int peliharaanId;

    private String nama;
    private String jenisKelamin;
    private String userId;
    private String jenisPeliharaan;
    private String fotoPeliharaan;
    private String ras;
    private String tanggalLahir;

    // Getters and Setters
    public int getPeliharaanId() {
        return peliharaanId;
    }

    public void setPeliharaanId(int peliharaanId) {
        this.peliharaanId = peliharaanId;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getJenisKelamin() {
        return jenisKelamin;
    }

    public void setJenisKelamin(String jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getJenisPeliharaan() {
        return jenisPeliharaan;
    }

    public void setJenisPeliharaan(String jenisPeliharaan) {
        this.jenisPeliharaan = jenisPeliharaan;
    }

    public String getFotoPeliharaan() {
        return fotoPeliharaan;
    }

    public void setFotoPeliharaan(String fotoPeliharaan) {
        this.fotoPeliharaan = fotoPeliharaan;
    }

    public String getRas() {
        return ras;
    }

    public void setRas(String ras) {
        this.ras = ras;
    }

    public String getTanggalLahir() {
        return tanggalLahir;
    }

    public void setTanggalLahir(String tanggalLahir) {
        this.tanggalLahir = tanggalLahir;
    }
}