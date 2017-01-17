package sds.auto.plate.base;

/**
 * Created by sds on 04.03.16.
 */

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AnpImage {

    @SerializedName("error")
    private Integer error;
    @SerializedName("region")
    private String region;
    @SerializedName("informer")
    private String informer;
    @SerializedName("cars")
    private List<Car> cars = null;

    public Integer getError() {
        return error;
    }

    public void setError(Integer error) {
        this.error = error;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getInformer() {
        return informer;
    }

    public void setInformer(String informer) {
        this.informer = informer;
    }

    public List<Car> getCars() {
        return cars;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }

    public class Car {

        @SerializedName("make")
        private String make;
        @SerializedName("model")
        private String model;
        @SerializedName("date")
        private String date;
        @SerializedName("photo")
        private Photo photo;

        public String getMake() {
            return make;
        }

        public void setMake(String make) {
            this.make = make;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public Photo getPhoto() {
            return photo;
        }

        public void setPhoto(Photo photo) {
            this.photo = photo;
        }

    }

    public class Photo {

        @SerializedName("link")
        private String link;
        @SerializedName("small")
        private String small;
        @SerializedName("medium")
        private String medium;
        @SerializedName("original")
        private String original;

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getSmall() {
            return small;
        }

        public void setSmall(String small) {
            this.small = small;
        }

        public String getMedium() {
            return medium;
        }

        public void setMedium(String medium) {
            this.medium = medium;
        }

        public String getOriginal() {
            return original;
        }

        public void setOriginal(String original) {
            this.original = original;
        }

    }
}