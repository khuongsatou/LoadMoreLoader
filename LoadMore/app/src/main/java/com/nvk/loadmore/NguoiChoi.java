package com.nvk.loadmore;

public class NguoiChoi {
    private int id;
    private String tenDangNhap;
    private int diemCaoNhat;

    public NguoiChoi() {
    }

    public NguoiChoi(int id, String tenDangNhap, int diemCaoNhat) {
        this.id = id;
        this.tenDangNhap = tenDangNhap;
        this.diemCaoNhat = diemCaoNhat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public int getDiemCaoNhat() {
        return diemCaoNhat;
    }

    public void setDiemCaoNhat(int diemCaoNhat) {
        this.diemCaoNhat = diemCaoNhat;
    }
}
