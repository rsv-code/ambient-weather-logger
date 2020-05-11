package com.lehman.ambientweatherlogger;

import com.lehman.ambientweatherjava.DataRecord;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

public class DbDataRecord extends DataRecord {
    private String id;

    public DbDataRecord() { }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
