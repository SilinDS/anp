package sds.auto.plate.base;

/**
 * Created by sds on 04.03.16.
 */

import com.google.gson.annotations.SerializedName;

public class Webdk {

    @SerializedName("dk")
    private String dk;
    @SerializedName("caption")
    private String caption;
    @SerializedName("vin")
    private String vin;
    @SerializedName("body")
    private String body;
    @SerializedName("plate")
    private String plate;
    @SerializedName("startdate")
    private String startdate;
    @SerializedName("enddate")
    private String enddate;
    @SerializedName("oper")
    private String oper;
    @SerializedName("expert")
    private String expert;

    /**
     *
     * @return
     * The dk
     */
    public String getDk() {
        return dk;
    }

    /**
     *
     * @param dk
     * The dk
     */
    public void setDk(String dk) {
        this.dk = dk;
    }

    /**
     *
     * @return
     * The caption
     */
    public String getCaption() {
        return caption;
    }

    /**
     *
     * @param caption
     * The caption
     */
    public void setCaption(String caption) {
        this.caption = caption;
    }

    /**
     *
     * @return
     * The vin
     */
    public String getVin() {
        return vin;
    }

    /**
     *
     * @param vin
     * The vin
     */
    public void setVin(String vin) {
        this.vin = vin;
    }

    /**
     *
     * @return
     * The body
     */
    public String getBody() {
        return body;
    }

    /**
     *
     * @param body
     * The body
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     *
     * @return
     * The plate
     */
    public String getPlate() {
        return plate;
    }

    /**
     *
     * @param plate
     * The plate
     */
    public void setPlate(String plate) {
        this.plate = plate;
    }

    /**
     *
     * @return
     * The startdate
     */
    public String getStartdate() {
        return startdate;
    }

    /**
     *
     * @param startdate
     * The startdate
     */
    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    /**
     *
     * @return
     * The enddate
     */
    public String getEnddate() {
        return enddate;
    }

    /**
     *
     * @param enddate
     * The enddate
     */
    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    /**
     *
     * @return
     * The operator
     */
    public String getOperator() {
        return oper;
    }

    /**
     *
     * @param oper
     * The operator
     */
    public void setOperator(String oper) {
        this.oper = oper;
    }

    /**
     *
     * @return
     * The expert
     */
    public String getExpert() {
        return expert;
    }

    /**
     *
     * @param expert
     * The expert
     */
    public void setExpert(String expert) {
        this.expert = expert;
    }
}

