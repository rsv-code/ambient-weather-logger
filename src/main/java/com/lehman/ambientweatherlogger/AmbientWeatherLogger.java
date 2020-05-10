package com.lehman.ambientweatherlogger;

import com.lehman.ambientweatherjava.AmbientWeather;

public class AmbientWeatherLogger {
    private String appKey = "";
    private String apiKey = "";
    private String[] macList = new String[0];

    private AmbientWeather aw;

    public AmbientWeatherLogger(String AppKey, String ApiKey, String[] MacList) {
        this.appKey = AppKey;
        this.apiKey = ApiKey;
        this.macList = MacList;

        this.aw = new AmbientWeather(this.apiKey, this.apiKey);
    }
}
