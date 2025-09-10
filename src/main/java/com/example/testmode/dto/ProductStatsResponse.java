package com.example.testmode.dto;

public class ProductStatsResponse {
    private long conhang;
    private long hethang;
    private long ngungban;
    private long total;

    public ProductStatsResponse() {}

    public ProductStatsResponse(long conhang, long hethang, long ngungban) {
        this.conhang = conhang;
        this.hethang = hethang;
        this.ngungban = ngungban;
        this.total = conhang + hethang + ngungban;
    }

    public long getConhang() {
        return conhang;
    }

    public void setConhang(long conhang) {
        this.conhang = conhang;
    }

    public long getHethang() {
        return hethang;
    }

    public void setHethang(long hethang) {
        this.hethang = hethang;
    }

    public long getNgungban() {
        return ngungban;
    }

    public void setNgungban(long ngungban) {
        this.ngungban = ngungban;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
