package com.example.qthjen.webservicedemo;

public class SinhVien {

    private int id;
    private String hoTen;
    private String namSinh;
    private String diaChi;

    public SinhVien(int id, String hoTen, String namSinh, String diaChi) {
        this.diaChi = diaChi;
        this.id = id;
        this.hoTen = hoTen;
        this.namSinh = namSinh;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNamSinh() {
        return namSinh;
    }

    public void setNamSinh(String namSinh) {
        this.namSinh = namSinh;
    }

}
