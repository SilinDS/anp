package sds.auto.plate;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.devspark.robototextview.widget.RobotoTextView;
import java.util.UUID;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sds.auto.plate.base.Device;
import sds.auto.plate.retrofit.service.DeviceInterface;
import sds.auto.plate.utility.CheckNetwork;
import sds.auto.plate.utility.LogUtil;

import static sds.auto.plate.utility.LogUtil.makeLogTag;
public class InfoActivity extends Activity  {
    private static final String TAG = makeLogTag(InfoActivity.class);
    private static final String DEVICE_STORAGE = "storage";
    private static final String DEVICE_MD5 = "md5";
    private static final String ACCESS = "access";
    private static final String ANPKEY = "anpKey";
    private static final String SOURCE = "source";
    private static final String CAPCHA = "bypass";
    private Context context;
    // Storage Permissions variables
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static boolean accessStorage;
    private  Activity activity;
    private  SharedPreferences sp;
    private static SharedPreferences.Editor spe;
    private String resultText;
    private RobotoTextView internet, eaisto, avtonomer, gibdd, result;
    private Button close;
    private CheckNetwork network;
    private static DeviceInterface myDeviceApi;
    private int errorCode;
    private boolean progress, hidecheck;

    private static final String SETTINGS_HIDECHECK = "hidecheck";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progress = true;
        try {
            context = getApplicationContext();
            sp = PreferenceManager.getDefaultSharedPreferences(context);
            hidecheck = sp.getBoolean(SETTINGS_HIDECHECK, false);

            activity = this;
            setContentView(R.layout.activity_info);
            resultText = "";
            internet = (RobotoTextView) findViewById(R.id.internet);
            eaisto = (RobotoTextView) findViewById(R.id.eaisto);
            avtonomer = (RobotoTextView) findViewById(R.id.avtonomer);
            gibdd = (RobotoTextView) findViewById(R.id.gibdd);
            result  = (RobotoTextView) findViewById(R.id.result);
            close = (Button) findViewById(R.id.close);
            //  проверка систем
            checkStorage();
            checkInternet();
                //  дальнейшие проверки запускаются автоматически
        } catch (Exception e) {
            Log.w(getClass().getName(), e.toString());
        }
    }
    private void checkInternet() {
        network = new CheckNetwork();
        myDeviceApi = App.getMyApi().create(DeviceInterface.class); //Создаем объект, при помощи которого будем выполнять запросы
        LogUtil.logE(TAG, "регистрируем аппарат и проверяем наличие интернета");
        network.isNetworkAvailable(checkUPID, 5000, getString(R.string.server3qr) +
                getString(R.string.myFileOnServer) );
    }
    Handler checkUPID = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what != 1) { // code if not connected
                //Произошла ошибка
                spe = sp.edit(); //  готовимся менять настройки
                spe.putString( DEVICE_MD5, "");
                spe.putInt( ACCESS, -1 );
                spe.putString( ANPKEY, "" );
                spe.putString( CAPCHA, "" );
                spe.putString( SOURCE, "" );
                spe.commit();
                internet.setText( getString(R.string.internetInfoNo) );
                eaisto.setText( getString(R.string.internetNoAccess) );
                avtonomer.setText( getString(R.string.internetNoAccess) );
                gibdd.setText( getString(R.string.internetNoAccess) );
                LogUtil.logE(TAG, "Интернета НЕТ");
                errorCode = 1;
                stopCheck();  //  отменяем дальнейшие проверки
            } else { // code if connected
                getDeviceData();
            }
        }
    };
    private void getDeviceData() {
        LogUtil.logE(TAG, "регистрируем аппарат");
        myDeviceApi.getData( getUniquePsuedoID() ).enqueue(new Callback<Device>() {
            @Override
            public void onResponse(Call<Device> call, Response<Device> response) {
                //Данные успешно пришли, но надо проверить response.body() на null
                if ( response.body() != null ) {
                    spe = sp.edit(); //  готовимся менять настройки
                    spe.putString( DEVICE_MD5, response.body().getMd5());
                    spe.putInt( ACCESS, response.body().getAccess() );
                    spe.putString( ANPKEY, response.body().getAnpKey() );
                    spe.putString( CAPCHA, response.body().getCapchaBypass() );
                    spe.putString( SOURCE, response.body().getSource() );
                    spe.commit();
                    internet.setText( getString(R.string.internetInfoYes) );
                    errorCode = 0;
                    LogUtil.logE(TAG, "Интернет есть, данные получены");
                     //  продолжаем проверки
                    if ( response.body().getAccess() > -1)   {  //доступ разрешен
                        switch (response.body().getSource()) {  // проверка ЕАИСТО
                            case "lib":
                                checkEaisto(getString(R.string.libsite));
                                break;
                            case "web":
                                checkEaisto(getString(R.string.webdk)); // проверка ЕАИСТО
                                break;
                            default:   //доступ закрыт, для профилактики
                                internet.setText(getString(R.string.noUserAccess));
                                LogUtil.logE(TAG, "Пользователь ограничен, для профилактики !");
                                errorCode = 3;
                                eaisto.setText(getString(R.string.internetNoAccess));
                                checkImageAnp();   // пропускаем проверку ЕАИСТО
                                break;
                        }
                    }  else {   //доступ закрыт!
                        internet.setText( getString(R.string.noUserAccessAll) );

                        LogUtil.logE(TAG, "Пользователь отключен !");
                        errorCode = 4;
                        eaisto.setText( getString(R.string.internetNoAccess) );
                        avtonomer.setText( getString(R.string.internetNoAccess) );
                        gibdd.setText( getString(R.string.internetNoAccess) );
                        stopCheck();  //  отменяем дальнейшие проверки
                    }
                }
            }
            @Override
            public void onFailure(Call<Device> call, Throwable t) {
                //Произошла ошибка
                spe = sp.edit(); //  готовимся менять настройки
                spe.putString( DEVICE_MD5, "");
                spe.putInt( ACCESS, -1 );
                spe.putString( ANPKEY, "" );
                spe.putString( CAPCHA, "" );
                spe.putString( SOURCE, "" );
                spe.commit();
                internet.setText( getString(R.string.internetInfoFeil) );
                errorCode = 2;
                LogUtil.logE(TAG, "Интернет есть, данные НЕ получены");
                stopCheck();  //  отменяем дальнейшие проверки
            }
        });
    }
    private void checkGibdd( ) {
        LogUtil.logE(TAG, "проверяем наличие источника ЕАИСТО");
        network.isNetworkAvailable(checkGibdd, 10000, getString(R.string.gibdd) );
    }
    Handler checkGibdd = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what != 1) {         // code if not connected
                //Произошла ошибка
                gibdd.setText( getString(R.string.gibddFeil) );
                LogUtil.logE(TAG, "gibdd.ru НЕ готов");
                errorCode += 100;
                stopCheck();  //  отменяем дальнейшие проверки
            } else { // code if connected
                gibdd.setText( getString(R.string.gibddOk) );
                LogUtil.logE(TAG, "gibdd.ru готов");
                stopCheck();  //  завершаем проверки
            }
        }
    };
    private void checkImageAnp() {
        LogUtil.logE(TAG, "проверяем доступ к avto-nomer.ru");
        network.isNetworkAvailable(checkAvtoNomer, 10000, getString(R.string.avtonomer) );
    }
    Handler checkAvtoNomer = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what != 1) { // code if not connected
                //Произошла ошибка
                avtonomer.setText( getString(R.string.avtonomerFeil) );
                LogUtil.logE(TAG, "avto-nomer.ru НЕ готов");
                errorCode += 10;
                checkGibdd();  //  продолжаем проверки
            } else { // code if connected
                avtonomer.setText( getString(R.string.avtonomerOk) );
                LogUtil.logE(TAG, "avto-nomer.ru готов");
                checkGibdd();  //  продолжаем проверки
            }
        }
    };
    Handler checkEaisto = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what != 1) { // code if not connected
                //Произошла ошибка
                eaisto.setText( getString(R.string.eaistoFeil) );
                LogUtil.logE(TAG, "Источник ЕАИСТО НЕ готов");
                errorCode = 5;
                checkImageAnp();  //  продолжаем проверки
            } else { // code if connected
                eaisto.setText( getString(R.string.eaistoOk) );
                LogUtil.logE(TAG, "Источник ЕАИСТО готов");
                checkImageAnp();  //  продолжаем проверки
            }
        }
    };
    private void setCloseButtion ( int status ) {
        close.setEnabled( true );
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        switch ( status )  {
            case 0:  // просто ззакрыть
                close.setText(getString(R.string.butContinue));
                intent.putExtra("result", 0 );
                if ( hidecheck )    finish();
                else
                    close.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {  finish();  }
                    });
                break;
            case 1:   //   просто ззакрыть, но показать проблему
                close.setText(getString(R.string.butContinue));
                intent.putExtra("result", 0 );
                close.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {  finish();  }
                });
                break;
            case 2:   //   завершить приложение
                close.setText(getString(R.string.butClose));
                intent.putExtra("result", 1 );
                close.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {  finish();  }
                });
                break;
        }
    }
    private void stopCheck() {
        String message = "";
        boolean problem = false;
        if ( errorCode  > 99 ) {   //   проблема с сайтом ГИБДД
            message += getString(R.string.addGibddProblem);
            errorCode -= 100;
            problem = true;
        }
        if ( errorCode  > 9  ) {   //   проблема с картинками
            message += getString(R.string.addImageProblem);
            errorCode -= 10;
            problem = true;
        }
        switch ( errorCode ) {
            case 0:     //   все ок
                progress = false;
                result.setText( getString(R.string.checkResultOnline) + message);
                if ( problem ) setCloseButtion ( 1 );
                else setCloseButtion ( 0 );
                break;
            case 1:     //   нет интернета
                progress = false;
                result.setText( getString(R.string.checkResultOffline));
                setCloseButtion ( 1 );
                break;
            case 2:     //   интернет есть, сбой на моем серве
                result.setText( getString(R.string.checkResultClose));
                setCloseButtion ( 2 );
                break;
            case 3:     //   юзер ограничен
                progress = false;
                result.setText( getString(R.string.checkResultAccessDenied) +
                        getString(R.string.addSmallProblem) + message);
                setCloseButtion ( 1 );
                break;
            case 4:     //   юзер отключен
                result.setText( getString(R.string.checkResultClose) +
                        getString(R.string.addBigProblem));
                setCloseButtion ( 2 );
                break;
            case 5:     //   нет еаисто
                progress = false;
                result.setText( getString(R.string.checkResultEaistoProblem) + message);
                setCloseButtion ( 1 );
                break;
        }


    }
    private void checkEaisto( String url ) {
        LogUtil.logE(TAG, "проверяем наличие источника ЕАИСТО");
        network.isNetworkAvailable(checkEaisto, 10000, url );
    }
    private void checkStorage() {
        LogUtil.logE(TAG, "check accessStorage");
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            accessStorage = false;
            requestPermissionWithRationale();
        } else accessStorage = true;
        spe = sp.edit(); //  готовимся менять настройки
        if ( accessStorage )  spe.putInt( DEVICE_STORAGE, 1 );
        else spe.putInt( DEVICE_STORAGE, 0 );
        spe.commit();
    }
    public static String getUniquePsuedoID() {
        // If all else fails, if the user does have lower than API 9 (lower
        // than Gingerbread), has reset their device or 'Secure.ANDROID_ID'
        // returns 'null', then simply the ID returned will be solely based
        // off their Android device information. This is where the collisions
        // can happen.
        // Thanks http://www.pocketmagic.net/?p=1662!
        // Try not to use DISPLAY, HOST or ID - these items could change.
        // If there are collisions, there will be overlapping data
        String m_szDevIDShort = "35" + (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10) + (Build.CPU_ABI.length() % 10) + (Build.DEVICE.length() % 10) + (Build.MANUFACTURER.length() % 10) + (Build.MODEL.length() % 10) + (Build.PRODUCT.length() % 10);

        // Thanks to @Roman SL!
        // http://stackoverflow.com/a/4789483/950427
        // Only devices with API >= 9 have android.os.Build.SERIAL
        // http://developer.android.com/reference/android/os/Build.html#SERIAL
        // If a user upgrades software or roots their device, there will be a duplicate entry
        String serial;
        try {
            serial = Build.class.getField("SERIAL").get(null).toString();

            // Go ahead and return the serial for api => 9
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            // String needs to be initialized
            serial = "serial"; // some value
        }

        // Thanks @Joe!
        // http://stackoverflow.com/a/2853253/950427
        // Finally, combine the values we have found by using the UUID class to create a unique identifier
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }
    public void verifyStoragePermissions(Activity activity) {
        // Check if we have read or write permission
        int writePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        } else {
            accessStorage = true;
            spe = sp.edit(); //  готовимся менять настройки
            spe.putInt( DEVICE_STORAGE, 1 );
            spe.commit();
            LogUtil.logE(TAG, "Получен доступ к Storage");
        }
    }
    public void requestPermissionWithRationale() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {

            final View content = findViewById(R.id.samples_fabLocation);
            Snackbar.make(content, getString(R.string.accesMessage), Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.access), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            verifyStoragePermissions( activity );
                        }
                    })
                    .show();
        } else {
            verifyStoragePermissions( activity );
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_EXTERNAL_STORAGE ) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                recreate();
            } else
                finish();

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @Override
    public void onBackPressed() {
        // Закрываем Navigation Drawer по нажатию системной кнопки "Назад" если он открыт
        if (!progress) {  //  если проверка закончена
            super.onBackPressed();
        }
    }
}