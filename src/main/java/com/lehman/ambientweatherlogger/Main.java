/*
 * Copyright 2020 Austin Lehman
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lehman.ambientweatherlogger;

import com.lehman.ambientweatherjava.HttpStatusException;

import java.io.IOException;

public class Main {

    /**
     * Main entry point for ambient-weather-logger. You will need to provide some
     * Java VM options, see examples below.
     *
     * -DappKey=c2978e...cd246d
     * -DapiKey=0e613c...2389b7
     * -DdeviceMac='EC:...:22,EC:...:21'
     */
    public static void main(String[] args) throws InterruptedException, HttpStatusException, IOException {
        String appKey = System.getProperty("appKey");
        String apiKey = System.getProperty("apiKey");
        String deviceMac = System.getProperty("deviceMac");
        String mysqlHost = System.getProperty("mysqlHost");
        String mysqlDatabase = System.getProperty("mysqlDatabase");
        String mysqlUserName = System.getProperty("mysqlUserName");
        String mysqlPassword = System.getProperty("mysqlPassword");

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

        if (mysqlHost == null || mysqlHost.equals("")) {
            mysqlHost = "localhost";
        }

        if (mysqlDatabase == null || mysqlDatabase.equals("")) {
            mysqlDatabase = "ambient_weather";
        }

        if (mysqlUserName == null || mysqlUserName.equals("")) {
            mysqlUserName = "root";
        }

        if (mysqlPassword == null || mysqlPassword.equals("")) {
            mysqlPassword = "root";
        }

        AmbientWeatherLogger logger = new AmbientWeatherLogger(appKey, apiKey, macList, mysqlHost, mysqlDatabase, mysqlUserName, mysqlPassword);
        logger.collectDeviceInfo();
        logger.collectLastRecord();
    }
}
