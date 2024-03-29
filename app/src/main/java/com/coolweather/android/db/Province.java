package com.coolweather.android.db;

import org.litepal.crud.LitePalSupport;
import org.litepal.exceptions.DataSupportException;

public class Province extends LitePalSupport {
    //省类
private int id;
private String provinceName;
private int provinceCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }
}
