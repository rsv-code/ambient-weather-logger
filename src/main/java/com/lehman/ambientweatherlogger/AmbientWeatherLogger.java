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

import com.lehman.ambientweatherjava.AmbientWeather;
import com.lehman.ambientweatherjava.DataRecord;
import com.lehman.ambientweatherjava.Device;
import com.lehman.ambientweatherjava.HttpStatusException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.PersistenceException;
import java.io.IOException;

/**
 * Ambient Weather logger class provides the functionality to acquire
 * data from the Ambient Weather API and then log it to a specified
 * MySQL database. It uses Hibernate to handle the DB connection.
 */
public class AmbientWeatherLogger {
    private String appKey = "";
    private String apiKey = "";
    private String[] macList = new String[0];

    private AmbientWeather aw;

    private SessionFactory factory;

    /**
     * Constructor takes a bunch of arguments and initializes the weather logger class.
     * @param AppKey is a String with the Ambient Weather Application Key.
     * @param ApiKey is a String with the Ambient Weather API Key.
     * @param MacList is an array of  Strings with the deivce MAC addresses to collect for.
     * @param MysqlHost is a String with the MySQL server host.
     * @param MysqlDatabase is a String with the MySQL database name to use.
     * @param MysqlUserName is a String with the MySQL username to use.
     * @param MysqlPassword is a String with the MySQL password to use.
     */
    public AmbientWeatherLogger(String AppKey, String ApiKey, String[] MacList, String MysqlHost, String MysqlDatabase, String MysqlUserName, String MysqlPassword) {
        this.appKey = AppKey;
        this.apiKey = ApiKey;
        this.macList = MacList;

        this.aw = new AmbientWeather(this.appKey, this.apiKey);

        this.factory = HibernateUtil.getSessionFactory(MysqlHost, MysqlDatabase, MysqlUserName, MysqlPassword);
    }

    /**
     * Gets the last data record from Ambient Weather and adds it to the
     * datarecord table. It will do this action for every device MAC address
     * in this.macList, and if there are no devices set then it will
     * get a full list of devices from Ambient Weather and iterate over them.
     * @throws InterruptedException
     * @throws HttpStatusException
     * @throws IOException
     */
    public void collectLastRecord() throws InterruptedException, HttpStatusException, IOException {
        String[] localMacList = this.getDeviceList();

        for (String macAddress : localMacList) {
            System.out.println("Getting the last record for device: '" + macAddress + "'");
            DataRecord[] records = this.aw.queryDeviceData(macAddress, 1);

            Transaction tr = null;
            try (Session session = this.factory.openSession()) {
                // start a transaction
                tr = session.beginTransaction();
                // save the record object
                session.save(records[0]);
                // commit transaction
                tr.commit();
                // close the session
                session.close();
            } catch (PersistenceException ex) {
                System.out.println("Duplicate record '" + records[0].getId() + "' found, rolling it back.");
                if (tr != null) { tr.rollback(); }
            } catch (Exception e) {
                if (tr != null) { tr.rollback(); }
                e.printStackTrace();
            }

            Thread.sleep(1000);
        }
    }

    /**
     * Gets all devices information using listUserDevices Ambient Weather call
     * and upserts them into device table.
     * @throws InterruptedException
     * @throws HttpStatusException
     * @throws IOException
     */
    public void collectDeviceInfo() throws InterruptedException, HttpStatusException, IOException {
        // Get list of devices.
        Device[] devices = this.aw.listUsersDevices();
        for(Device awdev : devices) {
            DbDevice dev = this.setDbDevice(awdev);

            System.out.println("Updating device information for device '" + dev.getMacAddress() + "'.");

            Transaction tr = null;
            try (Session session = this.factory.openSession()) {
                // start a transaction
                tr = session.beginTransaction();
                // save the record object
                session.saveOrUpdate(dev);
                // commit transaction
                tr.commit();
                // close the session
                session.close();
            } catch (Exception e) {
                if (tr != null) { tr.rollback(); }
                e.printStackTrace();
            }

            Thread.sleep(1000);
        }
    }

    /**
     * Takes the provided Ambient Weather device structure and
     * creates a DbDevice and returns it.
     * @param awdev is a Device to convert.
     * @return A DbDevice object with the converted data.
     */
    private DbDevice setDbDevice(Device awdev) {
        DbDevice dev = new DbDevice();
        dev.setMacAddress(awdev.getMacAddress());
        dev.setName(awdev.getInfo().getName());
        dev.setLocation(awdev.getInfo().getLocation());
        dev.setLongitude(awdev.getInfo().getCoords().getCoords().getLon());
        dev.setLatitude(awdev.getInfo().getCoords().getCoords().getLat());
        dev.setAddress(awdev.getInfo().getCoords().getAddress());
        dev.setAddressLocation(awdev.getInfo().getCoords().getLocation());
        dev.setElevation(awdev.getInfo().getCoords().getElevation());
        return dev;
    }

    /**
     * Gets the device list. If the macList filter is set with devices, then
     * it just uses that. Otherwise it makes a query to listUserDevices to
     * get a full list of device MAC addresses and returns them.
     * @return An array of Strings with the MAC addresses.
     * @throws InterruptedException
     * @throws HttpStatusException
     * @throws IOException
     */
    private String[] getDeviceList() throws InterruptedException, HttpStatusException, IOException {
        String[] localMacList = this.macList;
        if (localMacList.length == 0) {
            System.out.println("No devices set in 'deviceMac' property, querying for all devices.");
            // Get list of mac addresses from devices.
            Device[] devices = this.aw.listUsersDevices();
            System.out.println("Found " + devices.length + " devices.");
            if (devices.length > 0) {
                localMacList = new String[devices.length];
                for (int i = 0; i < devices.length; i++) {
                    localMacList[i] = devices[i].getMacAddress();
                }
            }
            Thread.sleep(1000);
        }
        return localMacList;
    }
}
