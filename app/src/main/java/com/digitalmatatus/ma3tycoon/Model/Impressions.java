package com.digitalmatatus.ma3tycoon.Model;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

/**
 * Created by stephineosoro on 04/07/2017.
 */

//Setting up impressions to be sent to the server
public class Impressions extends DataSupport {
    @Column(unique = true)
    private long id;

    private String impression;

    private String date;

    private String sessionEnd;

    private String sessionStart;


    public String getSessionStart() {
        return sessionStart;
    }

    public void setSessionStart(String sessionStart) {
        this.sessionStart = sessionStart;
    }

    public String getSessionEnd() {
        return sessionEnd;
    }

    public void setSessionEnd(String sessionEnd) {
        this.sessionEnd = sessionEnd;
    }




  /*  public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }*/

    public String getImpression() {
        return impression;
    }

    public void setImpression(String impression) {
        this.impression = impression;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
