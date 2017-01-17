package sds.auto.plate.base;

/**
 * Created by sds on 04.03.16.
 */

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class MyPlate {

    @SerializedName("id_plate")      private long id_plate;
    @SerializedName("plate")   private String plate;
    @SerializedName("vin")   private String vin;
    @SerializedName("caption")   private String caption;
    @SerializedName("note")   private String note;

    public MyPlate(long idPlate, String number, String vin, String caption,  String note) {
        this.id_plate = idPlate;
        this.plate = number;
        this.vin = vin;
        this.caption = caption;
        this.note = note;
    }


    public long getIdPlate() {  return id_plate;   }
    public void setIdPlate(long idPlate) { this.id_plate = idPlate;   }
    public String getNumber() {      return plate;   }
    public void setNumber(String plate) {      this.plate = plate;   }
    public String getVin() {        return vin;    }
    public void setVin(String vin) {        this.vin = vin;    }
    public String getCaption() {        return caption;    }
    public void setCaption(String caption) {        this.caption = caption;    }

    public String getNote() {        return note;    }

}

