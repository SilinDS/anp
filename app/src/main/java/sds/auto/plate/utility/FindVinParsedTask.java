package sds.auto.plate.utility;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;


public class FindVinParsedTask extends AsyncTask<String, String, String> {
    public FindVinParsedTask( String vin  ) {
        this.vin = vin;

    }

    private static final String TAG = LogUtil.makeLogTag(FindVinParsedTask.class);

    protected String result = "";
    protected String vin;
    protected String parse = "";

    @Override
    protected String doInBackground(String... strings) {

        try {

            // current.setVin(  "JMBSRCY2ABU011125"  );
            URL url = new URL( "http://pogazam.ru/vin/?vin=" + vin );

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(is, "windows-1251"));
            StringBuilder responseStrBuilder = new StringBuilder();

            String inputStr;
            while ((inputStr = streamReader.readLine()) != null)
                responseStrBuilder.append(inputStr);

            result = responseStrBuilder.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if ( !result.equals("") ) {

            int ind1 = result.indexOf("Идентификационный номер");
            int ind2 = result.indexOf("Покупаете автомобиль и хотите быть уверенным");

            if ( ind1 > 0 && ind2 > 0 )  {

                String findBlock1 = result.substring(ind1 , ind2 - 536 );
                String temp1[] = findBlock1.split("b>");

                for (int i = 0; i < temp1.length; i = i+2) {
                    if (temp1[i].length() > 1)
                        temp1[i] = temp1[i].substring(0, temp1[i].length() - 2);
                    else
                        temp1[i] = "";
                }
                for (int i = 1; i < temp1.length; i = i+2) {
                    if (temp1[i].length() > 2)
                        temp1[i] = temp1[i].substring(4, temp1[i].length() - 14);

                    else
                        temp1[i] = "";
                }

                for (int i = 1; i < temp1.length/2; i ++) {
                    parse += "|" + temp1[i*2] + ":" +  temp1[i*2+1];
                }

                result = "isdata";

            }  else{
                result = "nodata";
            }

        }

        return null;
    }



}

