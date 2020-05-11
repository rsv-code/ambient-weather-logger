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

/**
 * This class holds the device information. It's normalized from
 * the data returned in the listUserDevices call and includes that
 * data which I find most useful.
 */
public class DbDevice {
    /**
     * A String with the MAC address of the device. This is the primary key.
     */
    private String macAddress = "";

    /**
     * A String with the given name of the device.
     */
    private String name = "";

    /**
     * A String with the device location.
     */
    private String location = "";

    /**
     * A double with the coordinate longitude of the device.
     */
    private double longitude;

    /**
     * A double with the coordinate latitude of the device.
     */
    private double latitude;

    /**
     * A String with the address of the device.
     */
    private String address = "";

    /**
     * A String with the address location of the device.
     * This appears to be the City.
     */
    private String addressLocation = "";

    /**
     * A double with the elevation in feet above sea level.
     */
    private double elevation;

    public DbDevice() { }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressLocation() {
        return addressLocation;
    }

    public void setAddressLocation(String addressLocation) {
        this.addressLocation = addressLocation;
    }

    public double getElevation() {
        return elevation;
    }

    public void setElevation(double elevation) {
        this.elevation = elevation;
    }
}
