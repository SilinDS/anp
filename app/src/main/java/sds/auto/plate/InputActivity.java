package sds.auto.plate;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.devspark.robototextview.widget.RobotoTextView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sds.auto.plate.base.AnpImage;
import sds.auto.plate.base.EaistoTab;
import sds.auto.plate.base.Libsite;
import sds.auto.plate.base.MyPlate;
import sds.auto.plate.base.PlateTab;
import sds.auto.plate.base.Webdk;
import sds.auto.plate.db.tables.EaistoTable;
import sds.auto.plate.db.tables.PlateTable;
import sds.auto.plate.retrofit.converter.LibsiteConvertor;
import sds.auto.plate.retrofit.converter.WebdkConvertor;
import sds.auto.plate.retrofit.service.LibsiteInterface;
import sds.auto.plate.retrofit.service.MyPlateInterface;
import sds.auto.plate.retrofit.service.PlateImageInterface;
import sds.auto.plate.retrofit.service.PlateInterface;
import sds.auto.plate.retrofit.service.WebDkInterface;
import sds.auto.plate.retrofit.service.WebDkStInterface;
import sds.auto.plate.utility.LogUtil;
import sds.auto.plate.utility.PlateNumberUtils;

import static java.lang.Thread.sleep;
import static sds.auto.plate.utility.LogUtil.makeLogTag;

public class InputActivity extends Activity implements OnTouchListener, OnClickListener,
        OnFocusChangeListener {
    private EditText mEt; // Edit Text boxes
    private Button mBSpace, mBdone, mBack, mBChange;
    private RelativeLayout mLayout, mKLayout, mLayoutNoK;

    private String mUpper = "upper", mLower = "lower";
    private int w, mWindowWidth;
    private String sL[] = { "А", "В", "Е", "К", "М", "Н",
                        "О" , "Р" , "С" , "Т" , "У" , "Х"};

    private Button mB[] = new Button[12];

    private Button mN[] = new Button[10];

    private static final String TAG = makeLogTag(InputActivity.class);

    String currentNumber;
    PlateTab plateTab;
    EaistoTab eaisto;
    public Target target;
    Context context;
    PlateTable plateTable;
    EaistoTable eaistoTable;
    File folder;
    TextView top;
    ImageView plateImage;
    public MaterialDialog mDialog;
    boolean finishImage, finishPlate;
    RobotoTextView inputTxt, infImg, infPlate;
    MyPlate myPlate;

    private static final String TAG_DEVICE_MD5 = "md5";
    private static final String TAG_ANPKEY = "anpKey";
    private static final String TAG_SOURCE = "source";
    private static final String TAG_ALTER = "alter";

    SharedPreferences sp;
    String md5, anpkey;

    private Retrofit retrofitWebDk, retrofitWebDkSt, retrofitAnpPlate,
            retrofitAnpImage, retrofitLibsite;

    private  MyPlateInterface myPlateApi;
    private  WebDkInterface webdkApi;
    private  WebDkStInterface webdkStApi;
    private  PlateInterface plateApi;
    private  PlateImageInterface plateImageApi;

    private static LibsiteInterface libsiteApi;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {

            context = getApplicationContext();
            plateTable = new PlateTable( context );
            eaistoTable = new EaistoTable( context );

            LogUtil.logE(TAG, "load preferences");
            sp = PreferenceManager.getDefaultSharedPreferences(context);
            if ( sp.contains(TAG_DEVICE_MD5) ) {
                md5  = sp.getString(TAG_DEVICE_MD5, "");
            }
            if ( sp.contains(TAG_ANPKEY) ) {
                anpkey  = sp.getString(TAG_ANPKEY, "");
            }

            // получаем путь к SD
            File sdPath = Environment.getExternalStorageDirectory();
            // добавляем свой каталог к пути
            folder = new File( sdPath.getAbsolutePath() + "/" +
                    context.getResources().getString(R.string.app_name));


            setContentView(R.layout.activity_input);
            // adjusting key regarding window sizes
            setKeys();


            mEt = (EditText) findViewById(R.id.xEt);
            mEt.setOnTouchListener(this);
            mEt.setOnFocusChangeListener(this);
            mEt.setOnClickListener(this);

            mLayout = (RelativeLayout) findViewById(R.id.xK1);
       //     mLayoutNoK = (RelativeLayout) findViewById(R.id.xNoK);
            mKLayout = (RelativeLayout) findViewById(R.id.xKeyBoard);

            plateImage = (ImageView) findViewById(R.id.plateImage);
            infImg = (RobotoTextView) findViewById(R.id.infoImg);
            infPlate = (RobotoTextView) findViewById(R.id.infoPlate);
            inputTxt = (RobotoTextView) findViewById(R.id.input);

            changeSmallLetters();
            changeSmallTags();

            hideDefaultKeyboard();
            enableKeyboard();

        } catch (Exception e) {
            Log.w(getClass().getName(), e.toString());
        }


        mDialog = new MaterialDialog.Builder( context )
                .iconRes(R.mipmap.ic_launcher)
                .limitIconToDefaultSize() // limits the displayed icon size to 48dp
                .backgroundColorRes(R.color.material_drawer_background)
                .contentColorRes(R.color.colorPrimary)
                .content(R.string.find_auto_photo)
                .progress(true, 0)
                .progressIndeterminateStyle(true)
                .cancelable(false)
                .build();

        myPlateApi = App.getMyApi().create(MyPlateInterface.class); //Создаем объект, при помощи которого будем выполнять запросы

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();

        //http://avto-nomer.ru/mobile/api_ru_inf.php?nomer=c001at22
        retrofitAnpPlate = new Retrofit.Builder()
                .baseUrl("http://avto-nomer.ru/") //Базовая часть адреса
                .client(okHttpClient)
                .build();
        plateApi = retrofitAnpPlate.create(PlateInterface.class); //Создаем объект, при помощи которого будем выполнять запросы

        retrofitAnpImage = new Retrofit.Builder()
                .baseUrl("http://avto-nomer.ru/") //Базовая часть адреса
                .addConverterFactory(GsonConverterFactory.create() ) //Конвертер, необходимый для преобразования JSON'а в объекты
                .build();
        //    http://avto-nomer.ru/mobile/api_ru_photo1.php?nomer=a001aa77&key=Gc%J82X9
        plateImageApi = retrofitAnpImage.create(PlateImageInterface.class); //Создаем объект, при помощи которого будем выполнять запросы


    }

    private void getPlateImage(final String num) {
        //   infImg.setText(R.string.findImage);
        finishImage = false;
        plateApi.getData( num.toLowerCase() )
                .enqueue(new Callback <ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.body() != null) {
                            String answer = null;
                            try {
                                answer = response.body().string();
                                Picasso.with( context ).load( answer )
                                        .into( getPlateImageSource( num + ".png" ) );

                                //                infImg.setText(R.string.findImageOK);
                            } catch (IOException e) {
                                e.printStackTrace();
                                // закрыть mDialog
                                //mDialog.dismiss();
                                finishImage = true;
                            } finally {

                            }
                        } else {
                            infImg.setText(R.string.findImageFeil);
                            finishImage = true;
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        LogUtil.logE(TAG, "error" );
                        // закрыть mDialog
                        //    mDialog.dismiss();
                        infImg.setText(R.string.findImageFeil);

                        finishImage = true;
                    }
                } );
    }
    private void getANPImage(final String num) {

        plateImageApi.getData( num.toLowerCase() ,  anpkey)  //"Gc%J82X9" )
                .enqueue(new Callback <AnpImage>() {
                    @Override
                    public void onResponse(Call<AnpImage> call, Response<AnpImage> response) {

                        if (response.body() != null) {
                            String photoName = "";
                            if ( response.body().getError() == 0 &&
                                    response.body().getCars().get(0) != null ) {
                                photoName = response.body().getCars().get(0).getPhoto().getOriginal();
                                LogUtil.logE(TAG, "photoName " + photoName );
                                infImg.setText(R.string.findImageFind);

                                Picasso.with( context ).load( photoName )
                                        .into( getAnpImageSource( num + ".jpg" ) );
                            } else {
                                infImg.setText(R.string.findImageFeil);
                                finishImage = true;
                                finishActivity();
                            }
                        } else {
                            infImg.setText(R.string.findImageFeil);
                            finishImage = true;
                            finishActivity();
                        }

                    }
                    @Override
                    public void onFailure(Call<AnpImage> call, Throwable t) {
                        LogUtil.logE(TAG, "error" );
                        finishImage = true;
                        // закрыть mDialog
                    //    mDialog.dismiss();
                        infImg.setText(R.string.findImageFeil);
                    }
                } );
    }
    Target getAnpImageSource (final String filename ) {
        return target = new Target(  ) {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {

                File file = new File( folder + "/" + filename );
                try
                {
                    file.createNewFile();
                    FileOutputStream ostream = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 75, ostream);
                    ostream.close();

                    loadAnpImageFromCard (  file  );
                }
                catch (Exception e)    {
                    infImg.setText(R.string.findImageFeil);
                    e.printStackTrace();
                    finishImage = true;
                    finishActivity();
                }
            }
            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                // закрыть mDialog
               // mDialog.dismiss();
                infImg.setText(R.string.findImageFeil);
                finishImage = true;
                finishActivity();
            }
            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable)  {        }
        };
    }
    Target getPlateImageSource (final String filename ) {
        return target = new Target(  ) {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                File file = new File( folder + "/" + filename );
                try
                {
                    file.createNewFile();
                    FileOutputStream ostream = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 75, ostream);
                    ostream.close();
                    loadPlateImageFromCard (  file  );
                }
                catch (Exception e)    {
                    e.printStackTrace();
                    finishImage = true;

                }
            }
            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                // закрыть mDialog
                // mDialog.dismiss();
                finishImage = true;
            }
            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable)  {        }
        };
    }
    public void getWebDKDetail (final String number, String phpsesid, String token ) {
//    "Cookie: PHPSESSID=ll9931k5v7drl1lm37pmhrr1i0",
        webdkApi.getData("PHPSESSID=" + phpsesid, number, token)
                .enqueue(new Callback<Webdk>() {
                    //  App.getLibsiteApi().getData( number ).enqueue(new Callback<Libsite>() {
                    @Override
                    public void onResponse(Call<Webdk> call, Response<Webdk> response) {
                        //Данные успешно пришли, но надо проверить response.body() на null
                        if (response.body() != null) {
                            String dk = response.body().getDk();
                            String caption = response.body().getCaption();
                            String vin = response.body().getVin();
                            String body = response.body().getBody();
                            String frame = body;
                            String plate = response.body().getPlate();
                            String startdate = response.body().getStartdate();
                            String enddate  = response.body().getEnddate();
                            String operator = response.body().getOperator();
                            String expert = response.body().getExpert();
                            eaisto = new EaistoTab ( (long)0, (long)0, new Date().getTime(),
                                    dk, caption, vin, body, frame, plate, startdate, enddate,
                                    operator, expert  );

                            if (vin.equals("")) {
                                if ( !body.equals("") )
                                    vin = body;
                                else if ( !frame.equals("") )
                                    vin = frame;
                            }
                            if ( !vin.equals("") ) {
                                getMyNewPlateApi(number, vin, caption);
                                LogUtil.logE(TAG, "response.body().getCaption " + caption );
                            }  else  {
                                LogUtil.logE(TAG, "vin так и не нашли");
                                finishPlate = true;
                                finishActivity();
                                infPlate.setText(R.string.findInfoFeil);
                            }}  else {
                            finishPlate = true;
                            finishActivity();
                            infPlate.setText(R.string.findInfoFeil);
                        }
                    }

                    @Override
                    public void onFailure(Call<Webdk> call, Throwable t) {
                        //Произошла ошибка
                        LogUtil.logE(TAG, "Error");
                        finishPlate = true;
                        finishActivity();
                        infPlate.setText(R.string.findInfoFeil);
                    }

                });
    }
    public void getEaistoData (final String number ) {
        infPlate.setText(R.string.findMyInfo);
        myPlateApi.getData( md5, number, "", "", "1"  )  // просто ищем, вдруг - есть
                .enqueue(new Callback <MyPlate>() {
                    @Override
                    public void onResponse(Call<MyPlate> call, Response<MyPlate> response) {
                        if (response.body() != null) {
                            LogUtil.logE(TAG, "vin - " + response.body().getVin());

                            if (response.body().getVin().equals("")) { // нет в моей базе

                                String source = sp.getString(TAG_SOURCE, "");
                                if ( source.equals("lib"))   getStartLibsite( number );
                                else if ( source.equals("web"))   getStartWebDk( number );
                                else  {
                                    finishPlate = true;
                                    finishActivity();
                                    LogUtil.logE(TAG, "не правильный источник " );
                                    infPlate.setText(R.string.findInfoFeil);
                                }

                            } else {

                                String vin = response.body().getVin();
                                String body = "";
                                int isVin = 1;
                                if (  vin.length() < 17 ) { isVin = 2; body = vin;  vin = ""; }

                                LogUtil.logE(TAG, "vin = " + vin );
                                LogUtil.logE(TAG, "body = " + body );
                                int  status = -1;
                                if ( isVin > 0 ) status = 0;  // номер проверен, про криминал - нет данных

                                plateTab = new  PlateTab( response.body().getIdPlate(),
                                        response.body().getNumber(),
                                        isVin, vin, response.body().getCaption(), status );

                                infPlate.setText(R.string.findInfoOK);

                                plateTable.add ( plateTab );

                                finishPlate = true;
                                finishActivity();
                            }

                        } else  {
                            finishPlate = true;
                            finishActivity();
                        }
                    }
                    @Override
                    public void onFailure(Call<MyPlate> call, Throwable t) {
                        LogUtil.logE(TAG, "error" );
                        finishPlate = true;
                        finishActivity();
                        infPlate.setText(R.string.findInfoFeil);
                    }
                } );
    }

    private void getStartLibsite(final String number) {
        retrofitLibsite = new Retrofit.Builder()
                .baseUrl(getString(R.string.libsite)) //Базовая часть адреса
                .addConverterFactory(LibsiteConvertor.create()) //Конвертер, необходимый для преобразования JSON'а в объекты
                .build();
        libsiteApi = retrofitLibsite.create(LibsiteInterface.class); //Создаем объект, при помощи которого будем выполнять запросы

        libsiteApi.getData( number )
                .enqueue(new Callback <Libsite>() {
                    @Override
                    public void onResponse(Call<Libsite> call, Response<Libsite> response) {
                        if (response.body() != null) {

                            String dk = response.body().getForm().getNumber();
                            String caption = response.body().getVehicle().getMake() + " " +
                                    response.body().getVehicle().getModel();
                            String vin = response.body().getVin();
                            String body = response.body().getBodyNumber();
                            String frame = response.body().getFrameNumber();
                            String plate = response.body().getRegistrationNumber();
                            String startdate = response.body().getDateOfDiagnosis();
                            String enddate  = response.body().getForm().getValidity();
                            String operator = response.body().getOperator().getShortName();
                            String expert = response.body().getExpert().getName() + " " +
                                    response.body().getExpert().getFName() + " " +
                                    response.body().getExpert().getMName();
                            eaisto = new EaistoTab ( (long)0, (long)0, new Date().getTime(),
                                    dk, caption, vin, body, frame, plate, startdate, enddate,
                                    operator, expert  );
                            //  addPlate( eaisto );
                            if (vin.equals("")) {
                                if ( !body.equals("") )
                                    vin = body;
                                else if ( !frame.equals("") )
                                    vin = frame;
                            }
                            if ( !vin.equals("") ) {
                                getMyNewPlateApi(number, vin, caption);

                                LogUtil.logE(TAG, "response.body().getCaption " + caption );

                            }  else  {
                                LogUtil.logE(TAG, "vin так и не нашли");
                                finishPlate = true;
                                finishActivity();
                                infPlate.setText(R.string.findInfoFeil);
                            }

                        }  else {  ///   забанили адрес
                            finishPlate = true;
                            finishActivity();
                            LogUtil.logE(TAG, "response.body() = null" );
                            infPlate.setText(R.string.findInfoFeil);
                        }
                    }
                    @Override
                    public void onFailure(Call<Libsite> call, Throwable t) {
                        LogUtil.logE(TAG, "error libsite" );
                        infPlate.setText(R.string.findInfoFeil);
                        finishPlate = true;
                        finishActivity();
                    }
                } );
    }


    /**
    private void getStartUsluga(final String num) {

        infPlate.setText(R.string.findInfo);
        webdkStApi.getData()
                .enqueue(new Callback <ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.body() != null) {
                            String answer = null;
                            try {
                                answer = response.body().string();
                            } catch (IOException e) {
                                finishPlate = true;
                                finishActivity();
                                e.printStackTrace();
                            }
                            int numKey = answer.indexOf("<input type=\"hidden\" id=\"key\" name=\"key\" value=\"");
                            String key = answer.substring(numKey + 48, numKey +  48 + 32);
                            LogUtil.logE(TAG, "key = " + key);
                            getUslugaDetail(num, key);
                        }  else {  ///   забанили адрес
                            infPlate.setText(R.string.findInfoFeil);
                            LogUtil.logE(TAG, "response.body() = null" );
                            finishPlate = true;
                            finishActivity();
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        LogUtil.logE(TAG, "error" );

                        infPlate.setText(R.string.findInfoFeil);
                    }
                } );
    }

    public void getUslugaDetail (final String numb, String key ) {
        App.getUslugaApi().getData(numb, key)
                .enqueue(new Callback<Webdk>() {
                    //  App.getLibsiteApi().getData( number ).enqueue(new Callback<Libsite>() {
                    @Override
                    public void onResponse(Call<Webdk> call, Response<Webdk> response) {
                        //Данные успешно пришли, но надо проверить response.body() на null
                        if (response.body() != null) {
                            eaisto = new Eaisto( response.body().getDk(), response.body().getCaption(),
                                    response.body().getVin(), response.body().getBody(),
                                    response.body().getPlate(), response.body().getStartdate(),
                                    response.body().getEnddate(), response.body().getOperator(),
                                    response.body().getExpert() );

                            if ( eaisto.getExpert() == null ) eaisto.setExpert("");
                            infPlate.setText(R.string.findInfoOK);

                            String vinNumber = response.body().getVin();
                            if (vinNumber.equals("")) vinNumber = response.body().getBody();

                            getMyNewPlateApi ( numb, vinNumber,
                                    response.body().getCaption() );

                           // addPlate( eaisto );
                            LogUtil.logE(TAG, "response.body().getCaption " + response.body().getCaption());


                        } else {
                            infPlate.setText(R.string.findInfoFeil);
                            finishPlate = true;
                            finishActivity();
                        }
                    }
                    @Override
                    public void onFailure(Call<Webdk> call, Throwable t) {
                        //Произошла ошибка
                        LogUtil.logE(TAG, "Error");
                        infPlate.setText(R.string.findInfoFeil);
                        finishPlate = false;
                    }
                });
    }

     */


    private void getMyNewPlateApi ( String plate, String vin, String caption) {
        infPlate.setText(R.string.findMyInfo);
        LogUtil.logE(TAG, "eaisto vin - "+ vin );

        myPlateApi.getData( md5, plate, vin, caption, "1"  )  // отправляем ко мне
                .enqueue(new Callback <MyPlate>() {
                    @Override
                    public void onResponse(Call<MyPlate> call, Response<MyPlate> response) {
                        if (response.body() != null) {
                            LogUtil.logE(TAG, "id_plate NEW - " +  response.body().getNote() );
                            int isVin = 1;
                            String vin = response.body().getVin();
                            if ( vin.length() < 17 )  isVin = 2;
                            else if ( vin.length() < 7 )  isVin = 0;

                            plateTab = new  PlateTab(  response.body().getIdPlate(),
                                    response.body().getNumber(), isVin,
                                    vin, response.body().getCaption(), 0 );
                            plateTable.add( plateTab );

                            eaisto.setIdPlate( response.body().getIdPlate() );
                            eaistoTable.add( eaisto );
                        }

                        finishPlate = true;
                        finishActivity();

                    }
                    @Override
                    public void onFailure(Call<MyPlate> call, Throwable t) {
                        LogUtil.logE(TAG, "error MyNewPlateApi" );
                        finishPlate = true;
                        finishActivity();
                        infPlate.setText(R.string.findInfoFeil);
                    }
                } );
    }

    private void finishActivity() {

        if ( finishImage && finishPlate ) {
            MainActivity.scanButton.show();
            finish();
        }
     }


    private void getStartWebDk(final String number) {
        retrofitWebDkSt = new Retrofit.Builder()
                .baseUrl("http://web-dk.ru/") //Базовая часть адреса
                //         .addConverterFactory(WebdkStConvertor.create()) //Конвертер, необходимый для преобразования JSON'а в объекты
                .build();
        webdkStApi = retrofitWebDkSt.create(WebDkStInterface.class); //Создаем объект, при помощи которого будем выполнять запросы
        retrofitWebDk = new Retrofit.Builder()
                .baseUrl("http://web-dk.ru/") //Базовая часть адреса
                .addConverterFactory(WebdkConvertor.create()) //Конвертер, необходимый для преобразования JSON'а в объекты
                .build();
        webdkApi = retrofitWebDk.create(WebDkInterface.class); //Создаем объект, при помощи которого будем выполнять запросы

        webdkStApi.getData()
                .enqueue(new Callback <ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.body() != null) {
                            Headers headers = response.headers();
                            String c = headers.get("Set-Cookie");
                            String phpsesid = c.substring(c.indexOf("=") + 1, c.indexOf(";"));
                            String answer = null;
                            try {
                                answer = response.body().string();
                            } catch (IOException e) {
                                finishPlate = true;
                                finishActivity();
                                e.printStackTrace();
                            }
                            int numToken = answer.indexOf("&token=");
                            String token = answer.substring(numToken + 7, numToken + 7 + 64);
                            LogUtil.logE(TAG, "token = " + token);
                            LogUtil.logE(TAG, "phpsesid = " + phpsesid);
                            getWebDKDetail (number, phpsesid, token);
                        }  else {  ///   забанили адрес
                            finishPlate = true;
                            finishActivity();
                            LogUtil.logE(TAG, "response.body() = null" );
                            infPlate.setText(R.string.findInfoFeil);
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        LogUtil.logE(TAG, "error" );
                        infPlate.setText(R.string.findInfoFeil);
                        finishPlate = true;
                        finishActivity();
                    }
                } );

    }

    private void setKeys() {
        mWindowWidth = getWindowManager().getDefaultDisplay().getWidth(); // getting
        // window
        // height
        // getting ids from xml files
        mB[0] = (Button) findViewById(R.id.xA);
        mB[1] = (Button) findViewById(R.id.xB);
        mB[2] = (Button) findViewById(R.id.xC);
        mB[3] = (Button) findViewById(R.id.xD);
        mB[4] = (Button) findViewById(R.id.xE);
        mB[5] = (Button) findViewById(R.id.xF);
        mB[6] = (Button) findViewById(R.id.xG);
        mB[7] = (Button) findViewById(R.id.xH);
        mB[8] = (Button) findViewById(R.id.xJ);
        mB[9] = (Button) findViewById(R.id.xK);
        mB[10] = (Button) findViewById(R.id.xL);
        mB[11] = (Button) findViewById(R.id.xM);

        mN[0] = (Button) findViewById(R.id.x0);
        mN[1] = (Button) findViewById(R.id.x1);
        mN[2] = (Button) findViewById(R.id.x2);
        mN[3] = (Button) findViewById(R.id.x3);
        mN[4] = (Button) findViewById(R.id.x4);
        mN[5] = (Button) findViewById(R.id.x5);
        mN[6] = (Button) findViewById(R.id.x6);
        mN[7] = (Button) findViewById(R.id.x7);
        mN[8] = (Button) findViewById(R.id.x8);
        mN[9] = (Button) findViewById(R.id.x9);


        mBdone = (Button) findViewById(R.id.xDone);
        mBChange = (Button) findViewById(R.id.xChange);
        mBack = (Button) findViewById(R.id.xBack);
        for (int i = 0; i < mB.length; i++)
            mB[i].setOnClickListener(this);
        for (int i = 0; i < mN.length; i++)
            mN[i].setOnClickListener(this);

        mBdone.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mBChange.setOnClickListener(this);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v == mEt) {
            hideDefaultKeyboard();
            enableKeyboard();
        }
        return true;
    }
    @Override
    public void onClick(View v) {
        if (v == mBChange) {
            if (mBChange.getTag().equals(mUpper)) {
                changeSmallLetters();
                changeSmallTags();
            } else if (mBChange.getTag().equals(mLower)) {
                changeCapitalLetters();
            }
        }  else if (v == mBdone) {

            currentNumber = PlateNumberUtils.convertForBD(  mEt.getText().toString() );
            if ( currentNumber.length() == 8 ||
                    currentNumber.length() == 9 ) {
                disableKeyboard();
                findInfo ( );  // начинаем поиск
          //      setClosedBut ();
            }

        } else if (v == mBack) {
            isBack(v);
        } else
            addText(v);
    }

    private void findInfo() {
        //  начинаем искать
        // 1 поиск и загрузка картинки номера
        infImg.setText(R.string.findImage);

        File filePlate = new File( folder + "/" + currentNumber + ".png" );
        if ( filePlate.exists() )
            loadPlateImageFromCard (  filePlate  );
        else
            getPlateImage( currentNumber );


        // поиск инфлормации
        finishPlate = false;
        getEaistoData( currentNumber );

    }

    private void loadPlateImageFromCard(File file) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap load = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        plateImage.setImageBitmap( load );
        inputTxt.setText(R.string.inputOK);
        mEt.setVisibility(View.INVISIBLE);
        plateImage.setVisibility(View.VISIBLE);

        // 2 поиск и загрузка картинки номера
        File fileImage = new File( folder + "/" + currentNumber + ".jpg" );
        if ( fileImage.exists() )
            loadAnpImageFromCard (  fileImage  );
        else
            getANPImage( currentNumber );
    }



    private void loadAnpImageFromCard(File file) {
  //      BitmapFactory.Options options = new BitmapFactory.Options();
  //      Bitmap load = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
  //      plateImage.setImageBitmap( load );
        finishImage = true;
           infImg.setText(R.string.findImageOK);

        finishActivity();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

    }

    private void addText(View v) {
            String b = "";
            b = (String) v.getTag();
            if (b != null) {
                // adding text in Edittext
                mEt.append(b);
            }
    }

    private void isBack(View v) {

            CharSequence cc = mEt.getText();
            if (cc != null && cc.length() > 0) {
                {
                    mEt.setText("");
                    mEt.append(cc.subSequence(0, cc.length() - 1));
                }

            }

    }
    private void changeSmallLetters() {
        mBChange.setVisibility(Button.VISIBLE);
        for (int i = 0; i < sL.length; i++)
            mB[i].setText(sL[i]);
    }
    private void changeSmallTags() {
        for (int i = 0; i < sL.length; i++)
            mB[i].setTag(sL[i].toLowerCase());
        mBChange.setTag("lower");
    }
    private void changeCapitalLetters() {
        mBChange.setVisibility(Button.VISIBLE);
// ставим дефолтную клаву
        mBChange.setTag("upper");

    }

    @Override
    public void onBackPressed() {
        MainActivity.scanButton.show();
        super.onBackPressed();
    }

    // enabling customized keyboard
    private void enableKeyboard() {

        mLayout.setVisibility(RelativeLayout.VISIBLE);
        mKLayout.setVisibility(RelativeLayout.VISIBLE);

    //    mLayoutNoK.setVisibility( RelativeLayout.INVISIBLE);

    }

    // Disable customized keyboard
    private void disableKeyboard() {
        mLayout.setVisibility(RelativeLayout.INVISIBLE);
        mKLayout.setVisibility(RelativeLayout.INVISIBLE);

   //     mLayoutNoK.setVisibility(RelativeLayout.VISIBLE);
    }

    private void hideDefaultKeyboard() {
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }


}