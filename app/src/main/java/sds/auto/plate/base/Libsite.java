package  sds.auto.plate.base;

/**
 * Created by sds on 04.03.16.
 */

import com.google.gson.annotations.SerializedName;

public class Libsite {
     
    @SerializedName("Id")

private String id;
        @SerializedName("IsArchive")            private String isArchive;
        @SerializedName("BodyNumber")           private String bodyNumber;
        @SerializedName("DateOfDiagnosis")      private String dateOfDiagnosis;
        @SerializedName("Form")                 private Expert.Form form;
        @SerializedName("Name")                 private String name;
        @SerializedName("RegistrationNumber")   private String registrationNumber;
        @SerializedName("TestResult")           private String testResult;
        @SerializedName("Values")               private String values;
        @SerializedName("Vehicle")              private Expert.Vehicle vehicle;
        @SerializedName("VehicleCategory")      private String vehicleCategory;
        @SerializedName("VehicleCategory2")     private String vehicleCategory2;
        @SerializedName("Vin")                  private String vin;
        @SerializedName("FrameNumber")          private String frameNumber;
        @SerializedName("Expert")               private Expert expert;
        @SerializedName("Operator")             private Expert.Operator operator;

        public Libsite(String id, String isArchive, String bodyNumber, String dateOfDiagnosis,
                       Expert.Form form, String name, String registrationNumber,
                       String testResult, String values, Expert.Vehicle vehicle,
                       String vehicleCategory, String vehicleCategory2, String vin, String frameNumber,
                       Expert expert, Expert.Operator operator) {
            this.id = id;
            this.isArchive = isArchive;
            this.bodyNumber = bodyNumber;
            this.dateOfDiagnosis = dateOfDiagnosis;
            this.form = form;
            this.name = name;
            this.registrationNumber = registrationNumber;
            this.testResult = testResult;
            this.values = values;
            this.vehicle = vehicle;
            this.vehicleCategory = vehicleCategory;
            this.vehicleCategory2 = vehicleCategory2;
            this.vin = vin;
            this.frameNumber = frameNumber;
            this.expert = expert;
            this.operator = operator;
        }

        public String getId() {           return id;        }
        public void setId(String id) {           this.id = id;        }
        public String getIsArchive() {            return isArchive;        }
        public void setIsArchive(String isArchive) {            this.isArchive = isArchive;        }
        public String getBodyNumber() {            return bodyNumber;        }
        public void setBodyNumber(String bodyNumber) {            this.bodyNumber = bodyNumber;        }
        public String getDateOfDiagnosis() {            return dateOfDiagnosis;        }
        public void setDateOfDiagnosis(String dateOfDiagnosis) {  this.dateOfDiagnosis = dateOfDiagnosis;    }
        public Expert.Form getForm() {         return form;      }
        public void setForm(Expert.Form form) {       this.form = form;      }
        public String getName() {            return name;        }
        public void setName(String name) {            this.name = name;        }
        public String getRegistrationNumber() {            return registrationNumber;        }
        public void setRegistrationNumber(String registrationNumber) {
            this.registrationNumber = registrationNumber;
        }
        public String getTestResult() {          return testResult;        }
        public void setTestResult(String testResult) {         this.testResult = testResult;        }
        public String getValues() {          return values;        }
        public void setValues(String values) {         this.values = values;        }
        public Expert.Vehicle getVehicle() {            return vehicle;        }
        public void setVehicle(Expert.Vehicle vehicle) {          this.vehicle = vehicle;        }
        public String getVehicleCategory() {           return vehicleCategory;        }
        public void setVehicleCategory(String vehicleCategory) { this.vehicleCategory = vehicleCategory;    }
        public String getVehicleCategory2() {         return vehicleCategory2;      }
        public void setVehicleCategory2(String vehicleCategory2) {  this.vehicleCategory2 = vehicleCategory2;  }
        public String getVin() {         return vin;       }
        public void setVin(String vin) {           this.vin = vin;       }
        public String getFrameNumber() {            return frameNumber;        }
        public void setFrameNumber(String frameNumber) {            this.frameNumber = frameNumber;        }
        public Expert getExpert() {            return expert;        }
        public void setExpert(Expert expert) {            this.expert = expert;        }
        public Expert.Operator getOperator() {           return operator;        }
        public void setOperator(Expert.Operator operator) {          this.operator = operator;       }

    public class Expert {
        @SerializedName("Name")         private String name;
        @SerializedName("FName")        private String fName;
        @SerializedName("MName")        private String mName;

        public Expert(String name, String fName, String mName) {
            this.name = name;
            this.fName = fName;
            this.mName = mName;
        }

        public String getName() {           return name;        }
        public void setName(String name) {            this.name = name;        }
        public String getFName() {            return fName;        }
        public void setFName(String fName) {            this.fName = fName;        }
        public String getMName() {            return mName;        }
        public void setMName(String mName) {            this.mName = mName;        }

        public class Form {
            @SerializedName("Duplicate")        private String duplicate;
            @SerializedName("Number")           private String number;
            @SerializedName("Series")           private String series;
            @SerializedName("Type")             private String type;
            @SerializedName("Validity")         private String validity;

            public Form(String duplicate, String number, String series, String type, String validity) {
                this.duplicate = duplicate;
                this.number = number;
                this.series = series;
                this.type = type;
                this.validity = validity;
            }


            public String getDuplicate() {              return duplicate;            }
            public void setDuplicate(String duplicate) {             this.duplicate = duplicate;            }
            public String getNumber() {               return number;            }
            public void setNumber(String number) {                this.number = number;            }
            public String getSeries() {                return series;            }
            public void setSeries(String series) {                this.series = series;            }
            public String getType() {                return type;            }
            public void setType(String type) {                this.type = type;            }
            public String getValidity() {                return validity;            }
            public void setValidity(String validity) {                this.validity = validity;      }

        }


        public class Operator {

            @SerializedName("FullName")            private String fullName;
            @SerializedName("ShortName")           private String shortName;

            public Operator(String fullName, String shortName) {
                this.fullName = fullName;
                this.shortName = shortName;
            }

            public String getFullName() {         return fullName;            }
            public void setFullName(String fullName) {     this.fullName = fullName;         }
            public String getShortName() {             return shortName;            }
            public void setShortName(String shortName) {        this.shortName = shortName;         }
        }


        public class Vehicle {

            @SerializedName("Make")             private String make;
            @SerializedName("Model")            private String model;

            public Vehicle(String make, String model) {
                this.make = make;
                this.model = model;
            }

            public String getMake() {              return make;            }
            public void setMake(String make) {       this.make = make;           }
            public String getModel() {             return model;           }
            public void setModel(String model) {    this.model = model;         }

        }

    }

}

