package sds.auto.plate.base;

/**
 * Created by sds on 04.03.16.
 */

import com.google.gson.annotations.SerializedName;

public class Device {

    @SerializedName("md5")      private String md5;
    @SerializedName("access")   private int access;
    @SerializedName("anpKey")   private String anpKey;
    @SerializedName("capchaBypass")   private String capchaBypass;
    @SerializedName("source")   private String source;
    @SerializedName("alter")   private String alter;


    public Device (String md5, int access, String anpKey, String capchaBypass,
                   String source, String alter) {
        this.md5 = md5;
        this.access = access;
        this.anpKey = anpKey;
        this.capchaBypass = capchaBypass;
        this.source = source;
        this.alter = alter;
    }

    public String getMd5() {  return md5;   }
    public void setMd5(String md5) { this.md5 = md5;   }

    public int getAccess() {     return access;  }
    public void setAccess(int access) {     this.access = access;   }

    public String getAnpKey() {        return anpKey;    }
    public void setAnpKey(String anpKey) {        this.anpKey = anpKey;    }

    public String getCapchaBypass() {        return capchaBypass;    }
    public void setCapchaBypass(String capchaBypass) {        this.capchaBypass = capchaBypass;    }

    public String getSource() {        return source;    }
    public void setSource(String source) {        this.source = source;    }

    public String getAlter() {    return alter;   }
    public void setAlter(String alter) {  this.alter = alter;   }

}

