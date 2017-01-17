package sds.auto.plate.base;

/**
 * Created by sds on 04.03.16.
 */

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class EaistoTab {

    @SerializedName("id")    private long id;
    @SerializedName("idPlate")    private long idPlate;
    @SerializedName("timedate")    private long timedate;
    @SerializedName("dk")    private String dk;
    @SerializedName("caption")    private String caption;
    @SerializedName("vin")    private String vin;
    @SerializedName("frame")    private String frame;
    @SerializedName("body")    private String body;
    @SerializedName("plate")    private String plate;
    @SerializedName("startdate")    private String startdate;
    @SerializedName("enddate")    private String enddate;
    @SerializedName("operator")    private String operator;
    @SerializedName("expert")    private String expert;

    public EaistoTab( long idPlate, String caption, String vin,
                     String body, String plate) {
        this( (long)0, idPlate, new Date().getTime(), "", caption, vin, body, "",
                plate, "", "", "", "" );

    }
    public EaistoTab(long id, long idPlate, long timedate, String dk, String caption, String vin,
                     String body, String frame, String plate, String startdate, String enddate,
                     String operator, String expert) {

        this.id = id;
        this.idPlate = idPlate;
        this.timedate = timedate;
        this.dk = dk;
        this.caption = caption;
        this.body = body;
        this.frame = frame;
        this.plate = plate;
        this.startdate = startdate;
        this.enddate = enddate;
        this.expert = expert;
        this.vin = vin;
        this.expert = expert;
        this.operator = operator;
    }

    public long getId() {
        return id;
    }
    public void setId(long idPlate) {
        this.id = idPlate;
    }

    public long getIdPlate() {
        return idPlate;
    }
    public void setIdPlate(long idPlate) {
        this.idPlate = idPlate;
    }

    public long getTimedate() {  return timedate;   }
    public void setTimedate(long timedate) { this.idPlate = timedate;   }

    public String getDk() {
        return dk;
    }
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
    public void setBody(String body) {
        this.body = body;
    }

    public String getFrame() {
        return frame;
    }
    public void setFrame(String frame) {
        this.frame = frame;
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
        return operator;
    }

    /**
     *
     * @param operator
     * The operator
     */
    public void setOperator(String operator) {
        this.operator = operator;
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

