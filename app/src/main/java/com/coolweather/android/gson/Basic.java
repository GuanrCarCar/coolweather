package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

public class Basic {
    @SerializedName("city")
    public String cityName;//城市名

    @SerializedName("id")
    public String weatherId;//城市对应的天气ID

    @SerializedName("lat")
    public String cityLat;//城市的经度

    @SerializedName("lon")
    public String cityLon;//城市的纬度

    public Updata updata;

    public class Updata {
        @SerializedName("loc")
        public String updatatime;//接口更新时间
    }
}
