package sds.auto.plate.base;

/**
 * Created by sds on 04.03.16.
 */

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class PlateTab {

    @SerializedName("id")      private long id;
    @SerializedName("idPlate")      private long idPlate;
    @SerializedName("number")   private String number;
    @SerializedName("isVin")    private int isVin;
    @SerializedName("vin")   private String vin;
    @SerializedName("timedate")   private long timedate;
    @SerializedName("caption")   private String caption;
    @SerializedName("year")   private int year;
    @SerializedName("color")   private String color;
    @SerializedName("status")   private int status;
    @SerializedName("favorite")   private int favorite;
    @SerializedName("note")        private String note;
    @SerializedName("equipment")        private String equipment;


    public PlateTab(long id,long idPlate, String number, int isVin, String vin,
                    long timedate, String caption, int year, String color,
                    int status, int favorite, String note, String equipment) {
        this.id = id;
        this.idPlate = idPlate;
        this.number = number;
        this.isVin = isVin;
        this.vin = vin;
        this.timedate = timedate;
        this.caption = caption;
        this.year = year;
        this.color = color;
        this.status = status;
        this.favorite = favorite;
        this.note = note;
        this.equipment = equipment;
    }

    public PlateTab ( long idPlate, String number, int isVin, String vin,
                    String caption, int status) {
        this ( (long)0, idPlate, number, isVin , vin, new Date().getTime(), caption,
                0, "", status, 0, "", "" );
    }

    public long getId() {  return id;   }
    public void setId(long id) { this.id = id;   }

    public long getIdPlate() {  return idPlate;   }
    public void setIdPlate(long idPlate) { this.idPlate = idPlate;   }

    public int getIsVin() {     return isVin;  }
    public void setIsVin(int isVin) {     this.isVin = isVin;   }

    public String getNumber() {      return number;   }
    public void setNumber(String number) {      this.number = number;   }
    public String getVin() {        return vin;    }
    public void setVin(String vin) {        this.vin = vin;    }

    public long getTimedate() {  return timedate;   }
    public void setTimedate(long timedate) { this.idPlate = timedate;   }

    public String getCaption() {        return caption;    }
    public void setCaption(String caption) {        this.caption = caption;    }

    public int getYear() {     return year;  }
    public void setYear(int year) {     this.year = year;   }

    public String getColor() {        return color;    }
    public void setColor(String color) {        this.color = color;    }

    public int getStatus() {     return status;  }
    public void setStatus(int status) {     this.status = status;   }

    public int getFavorite() {     return favorite;  }
    public void setFavorite(int favorite) {     this.status = favorite;   }

    public String getNote() {        return note;    }
    public void setNote(String note) {        this.note = note;    }

    public String getEquipment() {        return equipment;    }
    public void setEquipment(String equipment) {        this.equipment = equipment;    }
}

