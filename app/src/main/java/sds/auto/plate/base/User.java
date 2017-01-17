package sds.auto.plate.base;

/**
 * Created by sds on 04.03.16.
 */

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("id_user")      private long id_user;
    @SerializedName("latitude")     private double latitude;
    @SerializedName("longitude")    private double longitude;
    @SerializedName("accuracy")     private int accuracy;
    @SerializedName("nickname")     private String nickname;
    @SerializedName("name")         private String name;
    @SerializedName("image")        private String image;
    @SerializedName("lasttime")     private long lasttime;
    @SerializedName("md5")          private int md5;
    @SerializedName("access")       private int access;
    @SerializedName("letters")      private int letters;


    public User() {
    }

    public User(long id_user, double latitude, double longitude, int accuracy, String nickname,
                String name, String image, long lasttime, int md5, int access, int letters) {
        this.id_user = id_user;
        this.latitude = latitude;
        this.longitude = longitude;
        this.accuracy = accuracy;
        this.name = name;
        this.nickname = nickname;
        this.image = image;
        this.lasttime = lasttime;
        this.md5 = md5;
        this.access = access;
        this.letters = letters;

    }

    /**


    public long getId_user() { return id_user;   }
    public int getSubscribe() { return subscribe;   }
    public long getLasttime() { return lasttime;   }
    public int getPort() { return port;   }
    public int getStat() { return stat;   }
    public int getUptime() { return uptime;   }

    public double getLatitude() { return latitude;   }
    public double getLongitude() { return longitude;   }
    public String getIata() { return iata;   }
    public String getName() { return name;   }
    public String getMap() { return map;   }
    public String getImg() { return image;   }

    public void setIdRadar(long value) {   this.id_radar = value;   }
    public void setSubscribe(int value) {   this.subscribe = value;   }
    public void setLasttime(long value) {   this.lasttime = value;   }
    public void setPort(int value) {   this.port = value;   }
    public void setStatus(int value) {   this.stat = value;   }
    public void setUptime(int value) {   this.uptime = value;   }

    public void setLatitude(double value) {   this.latitude = value;   }
    public void setLongitude(double value) {   this.longitude = value;   }
    public void setIata(String value) {   this.iata = value;   }
    public void setName(String value) {   this.name = value;   }
    public void setMap(String value) {   this.map = value;   }
    public void setImg(String value) {   this.image = value;   }
     */

}
