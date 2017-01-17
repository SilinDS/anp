package sds.auto.plate.utility;


import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Date;

import j.antigate.CapchaBypass;
import sds.auto.plate.base.GibddAnswer;

public class FindGibddCapchaTask extends AsyncTask<String, String, String> {
    public FindGibddCapchaTask( String capcha ) {
        this.capcha = capcha;
    }


    protected String capcha;
    private static final String TAG = LogUtil.makeLogTag(FindGibddCapchaTask.class);
    protected String jsessionid;
    protected boolean error;
    protected String answer = "";

    private static String url_get_capcha = "http://check.gibdd.ru/proxy/captcha.jpg?";


    protected String doInBackground(String... args) {


        HttpURLConnection con  = null;
        try {
            String curTime = String.valueOf( new Date().getTime() );
            con = (HttpURLConnection) new URL( url_get_capcha + curTime ).openConnection();
            con.setRequestProperty("Accept-Encoding", "");
            boolean responded = (con.getResponseCode() == HttpURLConnection.HTTP_OK);
            InputStream in = con.getInputStream();
            File sdPath = Environment.getExternalStorageDirectory();
            final File folder = new File(sdPath.getAbsolutePath() + "/АвтоНомера");
            File file;

            if ( responded ) {
                OutputStream fOut;
                Bitmap result = BitmapFactory.decodeStream(in);
                file = new File(folder, "capcha.jpg" ); // создать уникальное имя
                fOut = new FileOutputStream(file);
                result.compress(Bitmap.CompressFormat.JPEG, 85, fOut); // сохранять картинку в jpeg-формате с 85% сжатия.
                fOut.flush();
                fOut.close();

                jsessionid = "";
                int numHead = con.getHeaderFields().size();
                int i = 0;
                if ( numHead > 0 ) {
                    do {
                        if (con.getHeaderField(i).contains("JSESSIONID")) {
                            String cookieLine = con.getHeaderField(i).replaceAll(";", "=");
                            String temp[] = cookieLine.split("=");
                            jsessionid = temp[1];
                            error = false;
                            LogUtil.logE( TAG, "JSESSIONID : " + jsessionid);
                        }
                    } while (jsessionid.equals("") && i++ < numHead);
                }

                FileInputStream is = new FileInputStream( file );
                //** You may check your antigate.com balance first **/
                //String balance = CapchaUtils.getBalance("YOURAPIKEY");

                capcha =   // "50cf9f65a97e18d4fe5004ec28d1ba55";
                answer = CapchaBypass.CapchaAnswer(is, capcha, null, null, null);
                LogUtil.logE(TAG, "CapchaAnswer - " +  answer );
                error = false;
            } else error = true;
        }
        catch (MalformedURLException e) {    e.printStackTrace();    }
        catch (ProtocolException e) { e.printStackTrace();   }
        catch (UnsupportedEncodingException e) {   e.printStackTrace(); }
        catch (IOException e) {     e.printStackTrace();   }
        catch (InterruptedException e) {   e.printStackTrace();    }
        finally {    con.disconnect();
        }
        return null;
    }
}