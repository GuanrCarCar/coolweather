package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

public class Daily_Forecast {
    public Astro astro;

    private class Astro {
        public String mr;//月升时间

        public String ms;//月落时间

        public String sr;//日升时间

        public String ss;//日落时间
    }
    @SerializedName("cond")
    public More more;
    public class More{

        @SerializedName("txt_d")
        public String info;//白天天气状况描述

        @SerializedName("txt_n")
        public String night_info;//晚间天气状况描述
    }
    @SerializedName("date")
    public String date;//预报日期

    public String pcpn;//降水量

    public String pop;//降水概率

    public String pres;//大气压强

    public String uv;//紫外线强度指数

    public String vis;//能见度

    public String hum;//相对湿度

    @SerializedName("tmp")
    public Temperature temperature;
    public class Temperature{

        public String max;//最高温度

        public String min;//最低温度

    }

    public Wind wind;
    public class Wind{

        public String dir;//风向

        public String sc;//风力

        public String spd;//风速
    }


}
