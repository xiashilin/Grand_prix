package edu.niit.ydkf.grand_prix.module.maps;

import java.io.Serializable;

/**
 * Created by zhangdk on 2016/1/20.
 */
public class info implements Serializable {
    //try
    //经纬度
    private double latitude;
    private double lngtitude;
    //地点图片
    private int imagId;
    private String place;
    //地点名
    private String name;
    //本人到达地点的距离
    private String distance;
    private String objid;


    public info(double latitude, double lngtitude, String name, String place, String distance,
                String objid) {
        this.latitude = latitude;
        this.lngtitude = lngtitude;
        this.place = place;
        this.name = name;
        this.distance = distance;
        this.objid = objid;
    }

    public int getImagId() {
        return imagId;
    }

    public void setImagId(int imagId) {
        this.imagId = imagId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLngtitude() {
        return lngtitude;
    }

    public void setLngtitude(double lngtitude) {
        this.lngtitude = lngtitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }


    public String getObjid() {
        return objid;
    }

    public void setObjid(String objid) {
        this.objid = objid;
    }
}
