package com.lehman.ambientweatherlogger;

public class Main {

    /**
     * Main entry point for ambient-weather-logger. You will need to provide some
     * Java VM options, see examples below.
     *
     * -DappKey=c2978e...cd246d
     * -DapiKey=0e613c...2389b7
     * -DdeviceMac='EC:...:22,EC:...:21'
     */
    public static void main(String[] args) {
        String appKey = System.getProperty("appKey");
        String apiKey = System.getProperty("apiKey");
        String deviceMac = System.getProperty("apiKey");

        // Check for appKey and apiKey VM options.
        if (appKey == null || appKey.equals("")) {
            System.err.println("Error, appKey VM option can't be blank. (-DappKey=123456)");
            System.exit(1);
        }
        if (apiKey == null || apiKey.equals("")) {
            System.err.println("Error, apiKey VM option can't be blank. (-DapiKey=123456)");
            System.exit(1);
        }

        String[] macList = new String[0];
        if (deviceMac != null && !deviceMac.equals("")) {
            macList = deviceMac.split(",");
        }

        AmbientWeatherLogger logger = new AmbientWeatherLogger(appKey, apiKey, macList);
    }
}
