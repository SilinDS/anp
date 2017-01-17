package sds.auto.plate.utility;


import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;

import j.antigate.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sds.auto.plate.R;
import sds.auto.plate.base.AnpImage;
import sds.auto.plate.base.GibddAnswer;
import sds.auto.plate.retrofit.service.GibddInterface;

public class FindGibddInfoTask extends AsyncTask<String, String, String> {
    public FindGibddInfoTask(String vin, int goal, String capcha ) {
        this.vin = vin;
        this.goal = goal;
        this.capcha = capcha;
    }


    protected String vin, capcha;
    protected int goal;
    private static final String TAG = LogUtil.makeLogTag(FindGibddInfoTask.class);
    protected String JSESSIONID;
    protected boolean error;
    protected String answer="";

    private static String url_get_capcha = "http://check.gibdd.ru/proxy/captcha.jpg?";

    protected  GibddAnswer answerGibdd = null;

    protected String doInBackground(String... args) {

        try {
            String curTime = String.valueOf( new Date().getTime() );
            HttpURLConnection con  =(HttpURLConnection) new URL( url_get_capcha + curTime ).openConnection();
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

                int numHead = con.getHeaderFields().size();
                int i = 0;
                JSESSIONID = "";
                if ( numHead > 0 ) {
                    do {
                        if (con.getHeaderField(i).contains("JSESSIONID")) {
                            String cookieLine = con.getHeaderField(i).replaceAll(";", "=");
                            String temp[] = cookieLine.split("=");
                            JSESSIONID = temp[1];
                            error = false;
                            LogUtil.logE( TAG, "JSESSIONID : " + JSESSIONID);
                        }
                    } while (JSESSIONID.equals("") && i++ < numHead);
                }
                con.disconnect();
                FileInputStream is = new FileInputStream( file );
                //** You may check your antigate.com balance first **/
                //String balance = CapchaUtils.getBalance("YOURAPIKEY");

                String key = "50cf9f65a97e18d4fe5004ec28d1ba55";
                answer = CapchaBypass.CapchaAnswer(is, key, null, null, null);
                LogUtil.logE(TAG, "CapchaAnswer - " +  answer );

                error = false;

            } else error = true;




        }
        catch (MalformedURLException e) {    e.printStackTrace();    }
        catch (ProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }   catch (IOException e) {

         //   is = urlConnection.getErrorStream();

            if(e != null) {
                e.printStackTrace();
            } else {
                Log.i("ERROR", "Error: I/O Error");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
   //         urlConnection.disconnect();
        }
        return null;
    }



}