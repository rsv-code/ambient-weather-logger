package com.lehman.ambientweatherlogger;

import com.lehman.ambientweatherjava.AmbientWeather;
import com.lehman.ambientweatherjava.DataRecord;
import com.lehman.ambientweatherjava.HttpStatusException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.io.IOException;

public class AmbientWeatherLogger {
    private String appKey = "";
    private String apiKey = "";
    private String[] macList = new String[0];

    private AmbientWeather aw;

    private SessionFactory factory;

    public AmbientWeatherLogger(String AppKey, String ApiKey, String[] MacList, String MysqlHost, String MysqlDatabase, String MysqlUserName, String MysqlPassword) {
        this.appKey = AppKey;
        this.apiKey = ApiKey;
        this.macList = MacList;

        this.aw = new AmbientWeather(this.appKey, this.apiKey);

        this.factory = HibernateUtil.getSessionFactory(MysqlHost, MysqlDatabase, MysqlUserName, MysqlPassword);
    }

    public void collectLastRecord() throws InterruptedException, HttpStatusException, IOException {
        System.out.println("Getting the last record for device: '" + this.macList[0] + "'");
        DataRecord[] records = this.aw.queryDeviceData(this.macList[0]);

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
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            e.printStackTrace();
        }
    }
}
