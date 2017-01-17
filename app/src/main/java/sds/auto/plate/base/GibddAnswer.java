package sds.auto.plate.base;

/**
 * Created by sds on 04.03.16.
 */

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GibddAnswer {

    @SerializedName("RequestResult")
    private RequestResult requestResult;
    @SerializedName("vin")
    private String vin;
    @SerializedName("message")
    private String message;
    @SerializedName("status")
    private int status;

    //  общее для всех проверок
    public RequestResult getRequestResult() {   return requestResult;    }
    public void setRequestResult(RequestResult requestResult) {
        this.requestResult = requestResult;
    }
    public String getVin() {        return vin;    }
    public void setVin(String vin) {        this.vin = vin;    }
    public String getMessage() {        return message;    }
    public void setMessage(String message) {        this.message = message;    }
    public int getStatus() {        return status;    }
    public void setStatus(int status) {        this.status = status;    }

    // только для 1ой - регистрационные данные
    @SerializedName("regnum")
    private String regnum;
    public String getRegnum() {        return regnum;    }
    public void setRegnum(String regnum) {        this.regnum = regnum;    }


    public class RequestResult {

        // для 1ой - регистрационные данные
        @SerializedName("ownershipPeriods")
        private OwnershipPeriods ownershipPeriods;
        @SerializedName("vehiclePassport")
        private VehiclePassport vehiclePassport;
        @SerializedName("vehicle")
        private Vehicle vehicle;

        public OwnershipPeriods getOwnershipPeriods() {   return ownershipPeriods;      }
        public void setOwnershipPeriods(OwnershipPeriods ownershipPeriods) {
            this.ownershipPeriods = ownershipPeriods;
        }
        public VehiclePassport getVehiclePassport() {    return vehiclePassport;       }
        public void setVehiclePassport(VehiclePassport vehiclePassport) {
            this.vehiclePassport = vehiclePassport;
        }

        public class VehiclePassport {
            @SerializedName("number")
            private String number;
            @SerializedName("issue")
            private String issue;

            public String getNumber() {      return number;         }
            public void setNumber(String number) { this.number = number;   }
            public String getIssue() {   return issue;      }
            public void setIssue(String issue) {  this.issue = issue;  }
        }

        public Vehicle getVehicle() {            return vehicle;        }
        public void setVehicle(Vehicle vehicle) {         this.vehicle = vehicle;        }

        // для 2ой - ДТП
        @SerializedName("errorDescription")
        private String errorDescription;
        @SerializedName("statusCode")
        private int statusCode;
        @SerializedName("Accidents")
        private List<Accident> accidents = null;

        public String getErrorDescription() {            return errorDescription;        }
        public void setErrorDescription(String errorDescription) {
            this.errorDescription = errorDescription;
        }
        public int getStatusCode() {            return statusCode;        }
        public void setStatusCode(int statusCode) {  this.statusCode = statusCode;    }
        public List<Accident> getAccidents() {   return accidents;        }
        public void setAccidents(List<Accident> accidents) {  this.accidents = accidents;    }

        // для 3eй и 4ой - розыск и ограничения
        @SerializedName("records")
        private List<Record> records = null;
        @SerializedName("count")
        private int count;
        @SerializedName("error")
        private int error;

        public List<Record> getRecords() {       return records;        }
        public void setRecords(List<Record> records) {       this.records = records;       }
        public int getCount() {  return count;     }
        public void setCount(int count) {   this.count = count;      }
        public int getError() {        return error;       }
        public void setError(int error) {        this.error = error;      }
    }    // end RequestResult


    public class Record {

        //  для розыска
        @SerializedName("w_rec")
        private int wRec;
        public int getWRec() { return wRec;     }
        public void setWRec(int wRec) { this.wRec = wRec;        }
        @SerializedName("w_reg_inic")
        private String wRegInic;
        @SerializedName("w_user")
        private String wUser;
        @SerializedName("w_model")
        private String wModel;
        @SerializedName("w_data_pu")
        private String wDataPu;
        @SerializedName("w_god_vyp")
        private int wGodVyp;
        @SerializedName("w_vid_uch")
        private String wVidUch;
        @SerializedName("w_un_gic")
        private String wUnGic;

        public String getWRegInic() {    return wRegInic;       }
        public void setWRegInic(String wRegInic) {   this.wRegInic = wRegInic;      }
        public String getWUser() {      return wUser;      }
        public void setWUser(String wUser) {     this.wUser = wUser;       }
        public String getWModel() {      return wModel;        }
        public void setWModel(String wModel) {       this.wModel = wModel;        }
        public String getWDataPu() {            return wDataPu;        }
        public void setWDataPu(String wDataPu) {  this.wDataPu = wDataPu;        }
        public int getWGodVyp() {      return wGodVyp;        }
        public void setWGodVyp(int wGodVyp) {     this.wGodVyp = wGodVyp;       }
        public String getWVidUch() {    return wVidUch;       }
        public void setWVidUch(String wVidUch) {  this.wVidUch = wVidUch;       }
        public String getWUnGic() {            return wUnGic;        }
        public void setWUnGic(String wUnGic) {        this.wUnGic = wUnGic;        }


        //  для ограничений
        @SerializedName("regname")
        private String regname;
        @SerializedName("gid")
        private String gid;
        @SerializedName("tsyear")
        private int tsyear;
        @SerializedName("dateadd")
        private String dateadd;
        @SerializedName("regid")
        private int regid;
        @SerializedName("divtype")
        private int divtype;
        @SerializedName("dateogr")
        private String dateogr;
        @SerializedName("ogrkod")
        private int ogrkod;
        @SerializedName("divid")
        private int divid;
        @SerializedName("tsmodel")
        private String tsmodel;

        public String getRegname() {            return regname;        }
        public void setRegname(String regname) {  this.regname = regname;     }
        public String getGid() {         return gid;        }
        public void setGid(String gid) {         this.gid = gid;       }
        public int getTsyear() {return tsyear;    }
        public void setTsyear(int tsyear) {   this.tsyear = tsyear;       }
        public String getDateadd() {        return dateadd;       }
        public void setDateadd(String dateadd) {  this.dateadd = dateadd;     }
        public int getRegid() {          return regid;       }
        public void setRegid(int regid) {        this.regid = regid;       }
        public int getDivtype() {         return divtype;        }
        public void setDivtype(int divtype) {     this.divtype = divtype;      }
        public String getDateogr() {          return dateogr;        }
        public void setDateogr(String dateogr) {   this.dateogr = dateogr;     }
        public int getOgrkod() {       return ogrkod;        }
        public void setOgrkod(int ogrkod) {     this.ogrkod = ogrkod;       }
        public int getDivid() {         return divid;      }
        public void setDivid(int divid) {     this.divid = divid;       }
        public String getTsmodel() {          return tsmodel;      }
        public void setTsmodel(String tsmodel) {      this.tsmodel = tsmodel;       }

    }

    public class OwnershipPeriod {
        @SerializedName("simplePersonType")
        private String simplePersonType;
        @SerializedName("from")
        private String from;
        @SerializedName("to")
        private String to;
        public String getSimplePersonType() {     return simplePersonType;       }
        public void setSimplePersonType(String simplePersonType) {
            this.simplePersonType = simplePersonType;
        }
        public String getFrom() {   return from;      }
        public void setFrom(String from) {   this.from = from;       }
        public String getTo() {   return to;    }
        public void setTo(String to) {     this.to = to;      }
    }

    public class OwnershipPeriods {
        @SerializedName("ownershipPeriod")
        private List<OwnershipPeriod> ownershipPeriod = null;
        public List<OwnershipPeriod> getOwnershipPeriod() {   return ownershipPeriod;       }
        public void setOwnershipPeriod(List<OwnershipPeriod> ownershipPeriod) {
            this.ownershipPeriod = ownershipPeriod;
        }
    }


    public class Vehicle {
        @SerializedName("engineVolume")
        private String engineVolume;
        @SerializedName("color")
        private String color;
        @SerializedName("bodyNumber")
        private String bodyNumber;
        @SerializedName("year")
        private int year;
        @SerializedName("engineNumber")
        private String engineNumber;
        @SerializedName("vin")
        private String vin;
        @SerializedName("model")
        private String model;
        @SerializedName("category")
        private String category;
        @SerializedName("type")
        private String type;
        @SerializedName("powerHp")
        private String powerHp;
        @SerializedName("powerKwt")
        private String powerKwt;

        public String getEngineVolume() {   return engineVolume;      }
        public void setEngineVolume(String engineVolume) {   this.engineVolume = engineVolume;   }
        public String getColor() {    return color;       }
        public void setColor(String color) {   this.color = color;       }
        public String getBodyNumber() {    return bodyNumber;       }
        public void setBodyNumber(String bodyNumber) {  this.bodyNumber = bodyNumber;      }
        public int getYear() {     return year;      }
        public void setYear (int year) {    this.year = year;       }
        public String getEngineNumber() {   return engineNumber;       }
        public void setEngineNumber(String engineNumber) {   this.engineNumber = engineNumber;    }
        public String getVin() {   return vin;      }
        public void setVin(String vin) {    this.vin = vin;     }
        public String getModel() {   return model;      }
        public void setModel(String model) {     this.model = model;       }
        public String getCategory() {     return category;       }
        public void setCategory(String category) {       this.category = category;       }
        public String getType() {    return type;     }
        public void setType(String type) {       this.type = type;       }
        public String getPowerHp() {   return powerHp;       }
        public void setPowerHp(String powerHp) {    this.powerHp = powerHp;      }
        public String getPowerKwt() {    return powerKwt;        }
        public void setPowerKwt(String powerKwt) {   this.powerKwt = powerKwt;      }

    }

    public class Accident {
        @SerializedName("AccidentDateTime")
        private String accidentDateTime;
        @SerializedName("VehicleModel")
        private String vehicleModel;
        @SerializedName("VehicleDamageState")
        private String vehicleDamageState;
        @SerializedName("RegionName")
        private String regionName;
        @SerializedName("AccidentNumber")
        private String accidentNumber;
        @SerializedName("AccidentType")
        private String accidentType;
        @SerializedName("VehicleMark")
        private String vehicleMark;
        @SerializedName("DamagePoints")
        private List<String> damagePoints = null;
        @SerializedName("VehicleYear")
        private int vehicleYear;

        public String getAccidentDateTime() {    return accidentDateTime;        }
        public void setAccidentDateTime(String accidentDateTime) {
            this.accidentDateTime = accidentDateTime;
        }
        public String getVehicleModel() {            return vehicleModel;        }
        public void setVehicleModel(String vehicleModel) {
            this.vehicleModel = vehicleModel;
        }
        public String getVehicleDamageState() {    return vehicleDamageState;      }
        public void setVehicleDamageState(String vehicleDamageState) {
            this.vehicleDamageState = vehicleDamageState;
        }
        public String getRegionName() {        return regionName;        }
        public void setRegionName(String regionName) {   this.regionName = regionName;     }
        public String getAccidentNumber() {   return accidentNumber;        }
        public void setAccidentNumber(String accidentNumber) {
            this.accidentNumber = accidentNumber;
        }
        public String getAccidentType() {  return accidentType;    }
        public void setAccidentType(String accidentType) {
            this.accidentType = accidentType;
        }
        public String getVehicleMark() {       return vehicleMark;       }
        public void setVehicleMark(String vehicleMark) {
            this.vehicleMark = vehicleMark;
        }
        public List<String> getDamagePoints() {  return damagePoints;       }
        public void setDamagePoints(List<String> damagePoints) {
            this.damagePoints = damagePoints;
        }
        public int getVehicleYear() {    return vehicleYear;      }
        public void setVehicleYear(int vehicleYear) {
            this.vehicleYear = vehicleYear;
        }

    }

}   // end GIBDDANSWER









