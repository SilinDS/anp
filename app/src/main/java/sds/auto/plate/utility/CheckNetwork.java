package sds.auto.plate.utility;


import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.net.HttpURLConnection;
import java.net.URL;


public class CheckNetwork {

    private static final String TAG = LogUtil.makeLogTag(CheckNetwork.class);

    public void isNetworkAvailable(final Handler handler, final int timeout, final String ur) {
        // ask fo message '0' (not connected) or '1' (connected) on 'handler'
        // the answer must be send before before within the 'timeout' (in milliseconds)

        new Thread() {
            private boolean responded = false;
            @Override
            public void run() {
                // set 'responded' to TRUE if is able to connect with google mobile (responds fast)
                new Thread() {
                    @Override
                    public void run() {


                        try {
                            HttpURLConnection.setFollowRedirects(false);
                            // note : you may also need
                            //        HttpURLConnection.setInstanceFollowRedirects(false)
                            HttpURLConnection con  =(HttpURLConnection) new URL( ur ).openConnection();

                            con.setRequestProperty("Accept-Encoding", "");
                            con.setRequestMethod("HEAD");

                            responded = (con.getResponseCode() == HttpURLConnection.HTTP_OK);
                        }

                        catch (Exception e) {

                            Log.e("http", "exception", e );
                        }

                    }
                }.start();

                try {
                    int waited = 0;
                    while(!responded && (waited < timeout)) {
                        sleep(100);
                        if(!responded ) {
                            waited += 100;
                        }
                    }
                }
                catch(InterruptedException e) {
                    LogUtil.logE(TAG, "Thread", e);
                } // do nothing
                finally {
                    if (!responded) { handler.sendEmptyMessage(0); }
                    else { handler.sendEmptyMessage(1); }
                }
            }
        }.start();
    }

    //  EXAMPLE FOR USE^
    // create handler
    // isNetworkAvailable(h,2000); // get the answser within 2000 ms

    Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if (msg.what != 1) { // code if not connected

            } else { // code if connected

            }
        }
    };


}