package sds.auto.plate.base;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class GibddTab {
    @SerializedName("id_gibdd")         private long idGibdd;
    @SerializedName("id_plate")         private long idPlate;
    @SerializedName("dateRegister")     private long dateRegister;
    @SerializedName("dateAccidents")    private long dateAccidents;
    @SerializedName("dateWanted")       private long dateWanted;
    @SerializedName("dateRestrict")     private long dateRestrict;
    @SerializedName("category")         private String category;
    @SerializedName("color")            private String color;
    @SerializedName("engineNumber")     private String engineNumber;
    @SerializedName("engineVolume")     private String engineVolume;
    @SerializedName("model")            private String model;
    @SerializedName("power")            private String power;
    @SerializedName("type")             private String type;
    @SerializedName("year")             private int year;
    @SerializedName("ownership")        private String ownership;
    @SerializedName("accidents")        private String accidents;
    @SerializedName("wanted")           private String wanted;
    @SerializedName("restrict")         private String restrict;

    public GibddTab ( long idPlate  ) {

        this ( (long) 0, idPlate,
                (long) 0, (long) 0, (long) 0, (long) 0,
                "", "", "",  "",  "",  "", "",  0, "",  "", "",  "" );
    }

    public GibddTab(long idGibdd, long idPlate,
                    long dateRegister,  long dateAccidents,  long dateWanted,  long dateRestrict,
                    String category, String color,
                    String engineNumber, String engineVolume, String model, String power,
                    String type, int year, String ownership, String accidents,
                    String wanted, String restrict ) {

        this.idGibdd = idGibdd;
        this.idPlate = idPlate;
        this.dateRegister = dateRegister;
        this.dateAccidents = dateAccidents;
        this.dateWanted = dateWanted;
        this.dateRestrict = dateRestrict;

        this.category = category;
        this.color = color;
        this.engineNumber = engineNumber;
        this.engineVolume = engineVolume;
        this.model = model;
        this.power = power;
        this.type = type;
        this.year = year;
        this.ownership = ownership;
        this.accidents = accidents;
        this.wanted = wanted;
        this.restrict = restrict;
    }

    public long getIdGibdd()     { return idGibdd;   }
    public long getIdPlate()     { return idPlate;   }
    public long getDateRegister()       { return dateRegister;   }
    public long getDateAccidents()       { return dateAccidents;   }
    public long getDateWanted()       { return dateWanted;   }
    public long getDateRestrict()       { return dateRestrict;   }
    public int getYear()        { return year;   }
    public String getPower()     { return power;   }
    public String getCategory()         { return category;   }
    public String getColor()            { return color;   }
    public String getEngineNumber()     { return engineNumber;   }
    public String getEngineVolume()     { return engineVolume;   }
    public String getModel()            { return model;   }
    public String getType()             { return type;   }
    public String getOwnership()        { return ownership;   }
    public String getAccidents()        { return accidents;   }
    public String getWanted()           { return wanted;   }
    public String getRestrict()         { return restrict;   }
    public void setIdGibdd(long value)     {   this.idGibdd = value;   }
    public void setIdPlate(int value)      {   this.idPlate = value;   }
    public void setDateRegister (long value)     {   this.dateRegister = value;   }
    public void setDateAccidents (long value)     {   this.dateAccidents = value;   }
    public void setDateWanted (long value)     {   this.dateWanted = value;   }
    public void setDateRestrict (long value)     {   this.dateRestrict = value;   }
    public void setYear(int value)      {   this.year = value;   }
    public void setPower(String value)   {   this.power = value;   }

    public void setCategory(String value)       {   this.category = value;   }
    public void setColor(String value)          {   this.color = value;   }
    public void setEngineNumber(String value)   {   this.engineNumber = value;   }
    public void setEngineVolume(String value)   {   this.engineVolume = value;   }
    public void setModel(String value)          {   this.model = value;   }
    public void setType(String value)           {   this.type = value;   }
    public void setOwnership(String value)      {   this.ownership = value;   }
    public void setAccidents(String value)      {   this.accidents = value;   }
    public void setWanted(String value)         {   this.wanted = value;   }
    public void setRestrict(String value)       {   this.restrict = value;   }



}

