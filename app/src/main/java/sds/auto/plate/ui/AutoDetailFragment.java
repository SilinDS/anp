package sds.auto.plate.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import org.solovyev.android.checkout.Sku;

import static sds.auto.plate.R.drawable.nocar;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sds.auto.plate.R;
import sds.auto.plate.base.BaseActivity;
import sds.auto.plate.base.BaseFragment;
import sds.auto.plate.base.GibddAnswer;
import sds.auto.plate.base.GibddTab;
import sds.auto.plate.base.PlateTab;
import sds.auto.plate.db.tables.GibddTable;
import sds.auto.plate.db.tables.PlateTable;
import sds.auto.plate.retrofit.service.GibddInterface;
import sds.auto.plate.utility.FindGibddCapchaTask;
import sds.auto.plate.utility.FindGibddInfoTask;
import sds.auto.plate.utility.FindVinParsedTask;
import sds.auto.plate.utility.LogUtil;
import sds.auto.plate.utility.PlateNumberUtils;

public class AutoDetailFragment extends BaseFragment {

    public static final String ARG_ITEM_ID = "item_id";
    LinearLayout workArea;
    ImageView backdropImg;
    CollapsingToolbarLayout collapsingToolbar;
    Context context;
    static Activity main;
    AutoDetailFragment fragment;
    private FragmentActivity myContext;
    static SharedPreferences sp;

    private static final String TAG = "AutoDetailFragment";
    private static final String TAG_USER_MD5 = "md5";
    private static final String TAG_CAPCHA = "bypass";

    static String user_md5, capcha;

    private PlateTab auto;
    private GibddTab gibdd;
    private PlateTable dbAuto;
    private GibddTable dbGibdd;

    String tempVin, gibddToken, gibddID;

    Sku skuVin;
    String priceVin = "...";
    String priceGibdd = "...";

    String tokenVin = "";
    Button butPayVin, butRegister, butDTP, butWanted, butRestricted;
    String plateEng;
    public MaterialDialog mDialog;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        main = getActivity();
     //   checkout.createPurchaseFlow(new PurchaseListener());
//        inventory = checkout.makeInventory();
   //     reloadInventory();

        context = main.getApplicationContext();
        tempVin = "";
        gibddToken = "";
        gibddID = "";

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            dbAuto = new PlateTable( context );
            auto = dbAuto.get( Long.valueOf( getArguments().getString(ARG_ITEM_ID) ) );
       //     dbGibdd = new GibddTable( context );
       //     gibdd = dbGibdd.get( auto.getIdAuto() );
        }
        setHasOptionsMenu(true);
        fragment = this;

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View rootView = inflater.inflate(R.layout.fragment_auto_detail , container, false);
        collapsingToolbar = (CollapsingToolbarLayout) rootView.findViewById(R.id.collapsing_toolbar);
        backdropImg = (ImageView) rootView.findViewById(R.id.backdrop);
        if (((BaseActivity) getActivity()).providesActivityToolbar()) {
            // No Toolbar present. Set include_toolbar:
            ((BaseActivity) getActivity()).setToolbar((Toolbar) rootView.findViewById(R.id.toolbar));
//            getActivity().getActionBar().setDisplayShowHomeEnabled(false);
        }

       // final PlateNumberUtils plateUtil = new PlateNumberUtils(context);
        sp = PreferenceManager.getDefaultSharedPreferences(context);
        if ( !sp.contains(TAG_USER_MD5)  )  user_md5 = "";
        else user_md5 = sp.getString(TAG_USER_MD5, "");
        if ( sp.contains(TAG_CAPCHA) ) {
            capcha  = sp.getString(TAG_CAPCHA, "");
        }

        if (auto != null) {
            plateEng = PlateNumberUtils.convertForBD(auto.getNumber() );
            loadBackdrop();
            workArea = (LinearLayout) rootView.findViewById(R.id.workArea);
            if ( !auto.getNumber().equals("") )  {   //  если ввели номер или ВИН

                CardView card0 =  (CardView)  rootView.findViewById(R.id.card0);
                setCard0 ( card0 );
                // 1-ой блок, ПРОИЗВЕДЕНО
                CardView cardInfo  = (CardView)  rootView.findViewById(R.id.cardInfo);
                setCardInfo ( cardInfo );

                // 2-ой блок, GIBDD
                CardView cardGibdd  = (CardView)  rootView.findViewById(R.id.cardGibdd);
                setCardGibdd ( cardGibdd );

                 // 2-ой блок, VIN
            //    CardView cardVin = (CardView)  rootView.findViewById(R.id.cardVin);
            //    setCardVin ( cardVin );

            }  // если plate не пустой
        }
        return rootView;
    }

    private void setCardGibdd(CardView cardGibdd) {
        if ( auto.getIsVin() > 0 ) {

            dbGibdd = new GibddTable( context );

            gibdd = dbGibdd.get( auto.getIdPlate() );
            if (  gibdd == null )   gibdd = new GibddTab( auto.getIdPlate() );

            cardGibdd.setVisibility(View.VISIBLE);
            LinearLayout lv2  = new LinearLayout(context);
            lv2.setOrientation(LinearLayout.VERTICAL);
            lv2.setBackgroundColor(context.getResources().getColor(R.color.cardview_light_background));
            LinearLayout.LayoutParams pp =  new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            pp.setMargins(0, 20, 0, 0);
            lv2.setLayoutParams(pp); // из пред.блока
            lv2.addView( addHeader("ГИБДД"));
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd.MM.yy");

            lv2.addView( addPole( context.getResources().getString(R.string.tGibddWanted) ) );
            String wanted = gibdd.getWanted();
            if ( !wanted.equals("") )  { //  если УЖЕ получали данные
                lv2.addView( addPole( gibdd.getWanted() ) );

                String strTime = sdf.format( gibdd.getDateWanted() );
                lv2.addView( addPole( context.getResources().getString(R.string.tActual) +
                        " "  +  strTime ) );
            }
            butWanted = addButton( context.getResources().getColor(R.color.primary),
                    context.getResources().getString(R.string.butGetGibddWanted) );
            butWanted.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {

                    new myFindFibddTask ( auto.getVin(), 3 , capcha ).execute();
                }
            });
            lv2.addView( butWanted);

            /////////////////////////////////////////////////////////////

            lv2.addView( addPole( context.getResources().getString(R.string.tGibddRegister) ) );
            String register = gibdd.getOwnership();
            if ( !register.equals(""))  {  //  если УЖЕ получали данные
                if( !gibdd.getModel().equals("") ) {
                    RelativeLayout data = addData (
                            context.getResources().getString(R.string.tCaption),
                            gibdd.getModel(),  false);
                    lv2.addView( data );
                }
                if( !gibdd.getCategory().equals("") ) {
                    RelativeLayout data = addData (
                            context.getResources().getString(R.string.tCategory),
                            gibdd.getCategory(),  false);
                    lv2.addView( data );
                }
                if( !gibdd.getType().equals("") ) {
                    RelativeLayout data = addData (
                            context.getResources().getString(R.string.tType),
                            gibdd.getType(),  false);
                    lv2.addView( data );
                }
                if( !gibdd.getEngineNumber().equals("") ) {
                    RelativeLayout data = addData (
                            context.getResources().getString(R.string.tEngineNumber),
                            gibdd.getEngineNumber(),  false);
                    lv2.addView( data );
                }
                if( !gibdd.getEngineVolume().equals("") ) {
                    RelativeLayout data = addData (
                            context.getResources().getString(R.string.tEngineVolume),
                            gibdd.getEngineVolume(),  false);
                    lv2.addView( data );
                }
                if( !gibdd.getPower().equals("") ) {
                    RelativeLayout data = addData (
                            context.getResources().getString(R.string.tPower),
                            gibdd.getPower(),  false);
                    lv2.addView( data );
                }
                if( !gibdd.getOwnership().equals("") ) {
                    lv2.addView( addPole( gibdd.getOwnership() ) );
                }
                String strTime = sdf.format( gibdd.getDateRegister() );
                lv2.addView( addPole( context.getResources().getString(R.string.tActual) +
                        " "  +  strTime ) );
            }
            butRegister = addButton( context.getResources().getColor(R.color.primary),
                    context.getResources().getString(R.string.butGetGibddRegistration) ); // + priceGibdd);
            butRegister.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {

                    new myFindFibddTask ( auto.getVin(), 1, capcha ).execute();
                }
            });
            lv2.addView( butRegister);

            /////////////////////////////////////////////////////////////

            lv2.addView( addPole( context.getResources().getString(R.string.tGibddAccident) ) );
            String dtp = gibdd.getAccidents();
            if ( !dtp.equals(""))  { //  если УЖЕ получали данные
                lv2.addView( addPole( gibdd.getAccidents() ) );

                String strTime = sdf.format( gibdd.getDateAccidents() );
                lv2.addView( addPole( context.getResources().getString(R.string.tActual) +
                        " "  +  strTime ) );
            }
            butDTP = addButton( context.getResources().getColor(R.color.primary),
                    context.getResources().getString(R.string.butGetGibddDTP) ); // + priceGibdd);
            butDTP.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    new myFindFibddTask ( auto.getVin(), 2, capcha ).execute();
                }
            });
            lv2.addView( butDTP);
            /////////////////////////////////////////////////////////////

            lv2.addView( addPole( context.getResources().getString(R.string.tGibddRestr) ) );
            String restricted = gibdd.getRestrict();
            if ( !restricted.equals("") )  { //  если УЖЕ получали данные
                lv2.addView( addPole( gibdd.getRestrict() ) );

                String strTime = sdf.format( gibdd.getDateRestrict() );
                lv2.addView( addPole( context.getResources().getString(R.string.tActual) +
                        " "  +  strTime ) );
            }
            butRestricted = addButton( context.getResources().getColor(R.color.primary),
                    context.getResources().getString(R.string.butGetGibddRestricted) ); // + priceGibdd);
            butRestricted.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    new myFindFibddTask ( auto.getVin(), 4 , capcha).execute();
                }
            });
            lv2.addView( butRestricted);

            cardGibdd.addView( lv2 );
        }
    }

    class myFindFibddTask extends FindGibddCapchaTask {
        public myFindFibddTask (String vin, int goal, String capcha ) {
            super( capcha );
            this.vin = vin;
            this.goal = goal;
          }

        protected String vin;
        protected int goal;
        GibddAnswer answerGibdd;
        private String url_get = "http://check.gibdd.ru/proxy/check/auto/";


        // Сначала покажем диалоговое окно прогресса
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new MaterialDialog.Builder(main)
                    .backgroundColorRes(R.color.material_drawer_background)
                    .titleColorRes(R.color.material_drawer_primary_text)
                    .title(main.getResources().getString(R.string.connect))
                    .contentColorRes(R.color.colorPrimary)
                    .content(main.getResources().getString(R.string.addCapcha))
                    .progress(true, 0)
                    .show();
            mDialog.setCancelable(false);
        }
        protected void onPostExecute(String file_url) {

            mDialog.dismiss();
            Retrofit retrofitGibdd = new Retrofit.Builder()
                    .baseUrl("http://http://www.gibdd.ru/") //Базовая часть адреса
                    .addConverterFactory(GsonConverterFactory.create() ) //Конвертер, необходимый для преобразования JSON'а в объекты
                    .build();
            //  http://check.gibdd.ru/proxy/check/auto/history
            GibddInterface gibddApi = retrofitGibdd.create(GibddInterface.class); //Создаем объект, при помощи которого будем выполнять запросы

            if ( !error )  {

                mDialog = new MaterialDialog.Builder(main)
                        .backgroundColorRes(R.color.material_drawer_background)
                        .titleColorRes(R.color.material_drawer_primary_text)
                        .title(main.getResources().getString(R.string.connect))
                        .contentColorRes(R.color.colorPrimary)
                        .content(main.getResources().getString(R.string.addInfo))
                        .progress(true, 0)
                        .show();
                mDialog.setCancelable(false);

                String checkType="";
                String url = url_get;
                switch ( goal )  {
                    case 1: url += "history"; checkType = "history"; break;
                    case 2: url += "dtp"; checkType = "aiusdtp"; break;
                    case 3: url += "wanted"; checkType = "wanted"; break;
                    case 4: url += "restrict"; checkType = "restricted"; break;
                }

                LogUtil.logE(TAG, "gibdd: " + checkType + "  " +  vin );

                gibddApi.getData( "JSESSIONID=" + jsessionid, url, answer, checkType, vin  )
                        .enqueue(new Callback<GibddAnswer>() {
                            @Override
                            public void onResponse(Call<GibddAnswer> call, Response<GibddAnswer> response) {

                                if (response.body() != null) {
                                    if ( response.body().getStatus() == 200 ) {
                                        LogUtil.logE(TAG, "Gibdd OK"  );
                                        answerGibdd = response.body();
                                        LogUtil.logE(TAG, "Gibdd answerGibdd  " + answerGibdd.getVin()  );
                                        switch ( goal ) {
                                            case 1:
                                                updateRegister( answerGibdd );
                                                auto.setColor(gibdd.getColor());
                                                dbAuto.update( auto );
                                                break;
                                            case 2: updateDtp( answerGibdd );  break;
                                            case 3: updateWanted( answerGibdd );  break;
                                            case 4: updateRestrict( answerGibdd );  break;
                                            default: LogUtil.logE(TAG, "не может быть ");
                                        }

                                        if ( gibdd.getIdGibdd() == 0 )  dbGibdd.add( gibdd );
                                        else dbGibdd.update( gibdd );

                                        if ( auto.getYear() == 0 ) {
                                            auto.setYear(gibdd.getYear());
                                            dbAuto.update( auto );
                                        }
                                        LogUtil.logE(TAG, "gibdd -  " + gibdd.getType());
                                        updateUi();
                                    }  else {
                                        LogUtil.logE(TAG, "Gibdd getStatus != 200" );
                                    }
                                } else {
                                    LogUtil.logE(TAG, "Gibdd response.body() = null" );
                                }
                                mDialog.dismiss();

                            }
                            @Override
                            public void onFailure(Call<GibddAnswer> call, Throwable t) {
                                LogUtil.logE(TAG, "error" );
                                mDialog.dismiss();
                            }
                        } );
            }

        }

        private void updateRegister ( GibddAnswer answer ) {
            gibdd.setDateRegister( new Date().getTime() );
            gibdd.setCategory( answer.getRequestResult().getVehicle().getCategory() );
            gibdd.setColor( answer.getRequestResult().getVehicle().getColor() );
            gibdd.setEngineNumber( answer.getRequestResult().getVehicle().getEngineNumber() );
            gibdd.setEngineVolume( answer.getRequestResult().getVehicle().getEngineVolume() );
            gibdd.setModel( answer.getRequestResult().getVehicle().getModel() );

            String pHp = answer.getRequestResult().getVehicle().getPowerHp();
            String pKwt = answer.getRequestResult().getVehicle().getPowerKwt();
            if ( pHp == null ) pHp = " - ";
            if ( pKwt == null ) pKwt = " - ";
            gibdd.setPower( pKwt + "/" + pHp );

            int type = Integer.valueOf( answer.getRequestResult().getVehicle().getType() );
            String typeText;
            switch ( type ) {
                case 1:  typeText = getResources().getString( R.string.type01 ); break;
                case 3:  typeText = getResources().getString( R.string.type03 ); break;
                case 4:  typeText = getResources().getString( R.string.type04 ); break;
                case 9:  typeText = getResources().getString( R.string.type09 ); break;

                case 21:  typeText = getResources().getString( R.string.type21 ); break;
                case 22:  typeText = getResources().getString( R.string.type22 ); break;
                case 23:  typeText = getResources().getString( R.string.type23 ); break;
                case 25:  typeText = getResources().getString( R.string.type25 ); break;
                case 29:  typeText = getResources().getString( R.string.type29 ); break;

                case 43:  typeText = getResources().getString( R.string.type43 ); break;
                case 49:  typeText = getResources().getString( R.string.type49 ); break;

                case 53:  typeText = getResources().getString( R.string.type53 ); break;
                case 55:  typeText = getResources().getString( R.string.type55 ); break;
                case 59:  typeText = getResources().getString( R.string.type59 ); break;

                default: typeText = "";
            }
            gibdd.setType( typeText );
            gibdd.setYear( answer.getRequestResult().getVehicle().getYear() );
            GibddAnswer.OwnershipPeriods listOwner = answer.getRequestResult().getOwnershipPeriods();
            String owner = getResources().getString( R.string.period );
            for (int i = 0; i< listOwner.getOwnershipPeriod().size();  i++ ) {
                GibddAnswer.OwnershipPeriod ownerItem = listOwner.getOwnershipPeriod().get(i);
                String personType = getResources().getString( R.string.natural );
                if ( ownerItem.getSimplePersonType().equals("Legal") )
                    personType = getResources().getString( R.string.legal );
                String toPeriod = getResources().getString( R.string.toLastTime );
                if ( ownerItem.getTo() != null )
                    toPeriod = ownerItem.getTo().substring(0, ownerItem.getTo().length() - 19 );
                String fromPeriod = ownerItem.getFrom().substring(0, ownerItem.getFrom().length() - 19 );
                owner += getResources().getString( R.string.fromP ) +
                        " " + fromPeriod + " " + getResources().getString( R.string.toP )
                        + " " +  toPeriod + personType + "\n";
            }
            gibdd.setOwnership( owner );
            LogUtil.logE(TAG, "тип: " + gibdd.getType() );
        }

        private void updateDtp ( GibddAnswer answer ) {
            String accindent = "";
            gibdd.setDateAccidents( new Date().getTime() );
            if (answer.getRequestResult().getAccidents().size()>0 ) {
                for (int i = 0; i< answer.getRequestResult().getAccidents().size();  i++ ) {
                    GibddAnswer.Accident accItem = answer.getRequestResult().getAccidents().get(i);
                    gibdd.setYear( accItem.getVehicleYear() );
                    accindent += getResources().getString( R.string.accNumber ) +
                            accItem.getAccidentNumber() + "\n" +
                            getResources().getString( R.string.accDate )  +
                            accItem.getAccidentDateTime() + "\n" +
                            getResources().getString( R.string.accType )  +
                            accItem.getAccidentType() + "\n" +
                            getResources().getString( R.string.accName )  +
                            accItem.getRegionName() + "\n" +
                            getResources().getString( R.string.tCaption )  +
                            accItem.getVehicleModel() + "\n" ;
                }
            }
            if ( accindent.equals("")) accindent = getResources().getString( R.string.noAccident );
            gibdd.setAccidents( accindent );
        }

        private void updateWanted ( GibddAnswer answer ) {
            String wanted = "";
            gibdd.setDateWanted ( new Date().getTime() );
            if (answer.getRequestResult().getRecords().size()>0) {
                wanted = getResources().getString( R.string.tWanted );
                List<GibddAnswer.Record> listRecord = answer.getRequestResult().getRecords();
                for (int i = 0; i< listRecord.size();  i++ ) {
                    GibddAnswer.Record wantedItem = listRecord.get(i);
                    gibdd.setYear( wantedItem.getWGodVyp() );
                    wanted += getResources().getString( R.string.tCaption ) +
                            wantedItem.getWModel() + "\n" +
                            getResources().getString( R.string.wantedDate ) +
                            wantedItem.getWDataPu() + "\n" +
                            getResources().getString( R.string.wantedRegion ) +
                            wantedItem.getWRegInic() + "\n" ;
                }
            }
            if ( wanted.equals("")) wanted = getResources().getString( R.string.noWanted );
            gibdd.setWanted( wanted );
        }

        private void updateRestrict ( GibddAnswer answer ) {
            String restrict = "";
            gibdd.setDateRestrict ( new Date().getTime()  );
            if (answer.getRequestResult().getRecords().size()>0) {
                restrict = getResources().getString( R.string.tRestrict );
                List<GibddAnswer.Record> listRecord = answer.getRequestResult().getRecords();
                for (int i = 0; i< listRecord.size();  i++ ) {
                    GibddAnswer.Record restrItem = listRecord.get(i);
                    gibdd.setYear( restrItem.getTsyear() );
                    String typeText = "?";
                    switch ( restrItem.getDivtype() ) {
                        case 2:  typeText = getResources().getString( R.string.restType1 );
                            break;
                        case 6:  typeText = getResources().getString( R.string.restType6 );
                            break;
                    }
                    String codeText = "?";
                    switch ( restrItem.getOgrkod() ) {
                        case 1:  codeText = getResources().getString( R.string.restCode2 );
                            break;
                    }
                    String date = restrItem.getDateadd().substring( 0, restrItem.getDateadd().length() - 8 );
                    restrict += getResources().getString( R.string.tCaption ) + " " +
                            restrItem.getTsmodel() + "\n" +
                            getResources().getString( R.string.restrictRegion ) + " " +
                            restrItem.getRegname() + "\n" +
                            getResources().getString( R.string.restrictDate ) + " " +
                            date + "\n" +
                            getResources().getString( R.string.restrictType ) + " " + typeText +  "\n" +
                            getResources().getString( R.string.restrictCode ) + " " + codeText +  "\n";
                }
            }
            if ( restrict.equals("")) restrict = getResources().getString( R.string.noRestrict );
            gibdd.setRestrict( restrict );
        }


    }

    /**
    class myFindFibddTask extends FindGibddInfoTask {
        public myFindFibddTask (String user_md5, String vin, int goal ) {
            super( user_md5, vin, goal, gibddToken, gibddID );
            this.goal = goal;


        }
        int goal;
        // Сначала покажем диалоговое окно прогресса
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new MaterialDialog.Builder(main)
                    .backgroundColorRes(R.color.material_drawer_background)
                    .titleColorRes(R.color.material_drawer_primary_text)
                    .title(main.getResources().getString(R.string.connect))
                    .contentColorRes(R.color.colorPrimary)
                    .content(main.getResources().getString(R.string.addInfo))
                    .progress(true, 0)
                    .show();
            mDialog.setCancelable(false);
        }
        protected void onPostExecute(String file_url) {

                // закрываем диалоговое окно с индикатором
                mDialog.dismiss();
            }

    }

     */

    private void setCardInfo(CardView cardInfo) {
        if ( auto.getIsVin() == 1 ) {
            cardInfo.setVisibility(View.VISIBLE);
            LinearLayout lv1  = new LinearLayout(context);
            lv1.setOrientation(LinearLayout.VERTICAL);
            lv1.setBackgroundColor(context.getResources().getColor(R.color.cardview_light_background));
            LinearLayout.LayoutParams pp =  new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            pp.setMargins(0, 20, 0, 0);
            lv1.setLayoutParams(pp); // из пред.блока
            lv1.addView( addHeader("ПРОИЗВЕДСТВО"));
            String longParsed = auto.getEquipment().replace("\"", "");
            if ( !longParsed.equals(""))  {
                String[] temp = longParsed.split("\\|");               // разбиваем
                if ( temp.length > 0 ) {  //  если данных достаточно - обрабатываем
                    for (int j=1; j< temp.length; j++ ) {
                        String[] temp1 = temp[j].split(":");               // разбиваем
                        if ( temp1[0].length() + temp1[1].length() > 30)  {
                            lv1.addView( addPole(temp1[0] + " :") );
                            lv1.addView( addData (  " " , temp1[1], false) );
                        } else
                            lv1.addView( addData(temp1[0] + " :", temp1[1], false) );
                    }
                }
            } else {        //  если не получали данные
                Button butInfo = addButton( context.getResources().getColor(R.color.primary),
                        context.getResources().getString(R.string.butGetInfo));
                butInfo.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        new MyFindVinParsedTask ( auto.getVin() ).execute();
                    }
                });
                lv1.addView( butInfo);
            }

            cardInfo.addView( lv1 );
        }
    }
    private void setCardVin(CardView cardVin) {
        if ( auto.getIsVin() > 0 && auto.getVin().equals("") )   { // VIN не куплен
            LinearLayout lv2  = new LinearLayout(context);
            lv2.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams pp =  new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            pp.setMargins(0, 20, 0, 0);
            lv2.setLayoutParams(pp); // из пред.блока
            lv2.setBackgroundColor(context.getResources().getColor(R.color.cardview_light_background));
            lv2.addView(addHeader("VIN"));
            butPayVin = addButton ( context.getResources().getColor(R.color.primary),
                    context.getResources().getString(R.string.butGetVin) + priceVin );
       //     butPayVin.setTextColor(context.getResources().getColor(R.color.primary_light));
            lv2.addView(butPayVin);
            butPayVin.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {

               //     new myFindVin(user_md5, plateEng, 2).execute(); //   покупка ВИН
                }
            });
            cardVin.addView( lv2 );
            cardVin.setVisibility(View.VISIBLE);
        }
    }
    private void setCard0( CardView card0) {

        LinearLayout lv  = new LinearLayout(context);
        lv.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams pp =  new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pp.setMargins(0, 20, 0, 0);
        lv.setLayoutParams(pp);

        lv.setBackgroundColor(context.getResources().getColor(R.color.cardview_light_background));
        // lv.addView( addHeader( "БЕСПЛАТНЫЙ БЛОК" ));

        RelativeLayout plate = addData (context.getResources().getString(R.string.tPlate),
                auto.getNumber(),  true);
        lv.addView( plate );

        if ( auto.getIsVin() > 0 ) {
            RelativeLayout caption = addData (context.getResources().getString(R.string.tCaption), auto.getCaption(), false );
            lv.addView( caption );

            if( auto.getColor().equals("") ) {
                lv.addView( addPole(context.getResources().getString(R.string.tColor)) );
                lv.addView( addData (  " " ,
                            context.getResources().getString(R.string.tNoColor) , false) );
            } else {
                RelativeLayout color = addData (
                        context.getResources().getString(R.string.tColor),
                        auto.getColor()  , false);
                lv.addView( color );

            }

            if( auto.getYear() != 0 ) {
                RelativeLayout year = addData (
                        context.getResources().getString(R.string.tYear),
                        String.valueOf(auto.getYear())  , false);
                lv.addView( year );
            }

            RelativeLayout vinLayout;
            if ( !auto.getVin().equals("")) {
                vinLayout = addData(context.getResources().getString(R.string.tVIN),
                        auto.getVin(), false);
                lv.addView(vinLayout);
            }

        } else { // end isVin

            lv.addView( addSeparator() );
            RelativeLayout noData= addData ("",
                    context.getResources().getString(R.string.notfound), false);
            lv.addView( noData );
        }

        card0.addView( lv );
    }

    @Override
    public void onDestroy() {
//        checkout.destroyPurchaseFlow();
        super.onDestroy();
    }
    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }
    public void updateUi() {
        FragmentManager fragManager = myContext.getSupportFragmentManager();
        final FragmentTransaction ft = fragManager.beginTransaction();
        ft.detach(fragment);
        ft.attach(fragment);
        ft.commit();
    }

    private TextView addHeader (String txt  )  {
        TextView view = new TextView(context);
        view.setText(txt);
        view.setTextColor(context.getResources().getColor(R.color.primary));
        view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        LinearLayout.LayoutParams params =  new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(40, 20, 40, 30);
        view.setLayoutParams(params);
        return view;
    }
    private TextView addPole (String txt  )  {
        TextView view = new TextView(context);
        view.setText(txt);
        view.setTextColor(context.getResources().getColor(R.color.primary));
        view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        LinearLayout.LayoutParams params =  new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //  params.setMargins(40, 7, 40, 7);
        params.setMargins(15, 10, 0, 10);
        view.setLayoutParams(params);
        return view;
    }
    private RelativeLayout addData (String pole, String value, boolean plate  ) {
        RelativeLayout view  = new RelativeLayout(context);
        RelativeLayout.LayoutParams params =  new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 0);
        view.setLayoutParams(params);
        TextView view1 = new TextView(context);
        view1.setText(pole);
        view1.setTextColor(context.getResources().getColor(R.color.primary));
        view1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        RelativeLayout.LayoutParams paramsPole =  new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsPole.setMargins(5, 5, 5, 5);
        paramsPole.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        view1.setLayoutParams(paramsPole);

        TextView view2 = new TextView(context);
        if ( plate ) {   //  если это номер
            view2.setTextColor(context.getResources().getColor(R.color.md_black_1000));
            view2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            SpannableString number = PlateNumberUtils.convertToPlate( value );
            view2.setText(number);
        } else  {
            view2.setTextColor(context.getResources().getColor(R.color.md_black_1000));
            view2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            view2.setText(value);
        }
        RelativeLayout.LayoutParams paramsValue =  new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsValue.setMargins(5, 5, 5, 5);
        paramsValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        view2.setLayoutParams(paramsValue);
        view.addView( view1 );
        view.addView( view2 );
        return view;
    }
    private View addSeparator ( )  {
        View view = new View(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground( context.getResources().getDrawable(R.drawable.separator));
        }
        LinearLayout.LayoutParams params =  new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 1);
        params.setMargins(0, 10, 0, 10);
        view.setLayoutParams(params);
        return view;
    }
    private Button addButton (int color, String txt )  {
        Button view  = new Button(context);
        LinearLayout.LayoutParams params =  new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(40, 20, 40, 20);
        view.setLayoutParams(params);
        view.setBackgroundColor( color );
        view.setText(String.valueOf( txt ));
        return view;
    }
    private void loadBackdrop() {
        //    Glide.with(this).load(radarItem.photoId).centerCrop().into(backdropImg);
        // получаем путь к SD
        File sdPath = Environment.getExternalStorageDirectory();
        // добавляем свой каталог к пути
        final File folder = new File(sdPath.getAbsolutePath() + "/" +
                context.getResources().getString(R.string.app_name));
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        Bitmap bm = null;
        String plate = PlateNumberUtils.convertForBD(auto.getNumber().toUpperCase());
        File image = new File(folder + "/" + plate + ".jpg" );
        collapsingToolbar.setTitleEnabled(true);

        try {
            if (image.exists() ) {
                collapsingToolbar.setTitle( context.getResources().getString(R.string.linkAvtoNomer));
                collapsingToolbar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri address = Uri.parse("http://avto-nomer.ru");
                        Intent openlinkIntent = new Intent(Intent.ACTION_VIEW, address);
                        startActivity(openlinkIntent);
                    }
                });
                backdropImg.setImageBitmap(BitmapFactory.decodeFile( folder + "/" +
                        plate + ".jpg", options));

                LogUtil.logD( TAG, "exists" );
            } else {
                collapsingToolbar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri address = Uri.parse("http://avto-nomer.ru/add");
                        Intent openlinkIntent = new Intent(Intent.ACTION_VIEW, address);
                        startActivity(openlinkIntent);
                    }
                });
                backdropImg.setImageBitmap( BitmapFactory.decodeResource( context.getResources(), nocar,  options));
                LogUtil.logE( TAG, " not exists" );
            }
        } catch ( OutOfMemoryError e ) {
            LogUtil.logD( TAG, "OutOfMemoryError " + e.toString() );
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //     inflater.inflate(R.menu.sample_actions, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //      case R.id.action_getRadar:
            // your logic
            //          return true;
        }
        return super.onOptionsItemSelected(item);
    }




    class MyFindVinParsedTask extends FindVinParsedTask {
        public MyFindVinParsedTask( String vin  ) {
            super( vin );
            mDialog = new MaterialDialog.Builder(main)
                    .backgroundColorRes(R.color.material_drawer_background)
                    .titleColorRes(R.color.material_drawer_primary_text)
                    .title(main.getResources().getString(R.string.connect))
                    .contentColorRes(R.color.colorPrimary)
                    .content(main.getResources().getString(R.string.addInfo))
                    .progress(true, 0)
                    .show();
            mDialog.setCancelable(false);

        }
        protected void onPostExecute(String file_url) {
            // закрываем диалоговое окно с индикатором
            mDialog.dismiss();
            if ( !parse.equals("")) {
                auto.setEquipment (parse);
                dbAuto.saveEquip( auto );
                updateUi();
            }   else  if ( result.equals("nodata")  )  {
                new MaterialDialog.Builder(main)
                        .title(main.getResources().getString(R.string.otvet))
                        .content(main.getResources().getString(R.string.noData))
                        .positiveText(R.string.yes)
                        .show();
            }   else {
                new MaterialDialog.Builder(main)
                        .title(main.getResources().getString(R.string.problem))
                        .content(main.getResources().getString(R.string.sorry))
                        .positiveText(R.string.yes)
                        .show();
            }
        }
    }

    /**
     *
     class myFindVin extends FindVin {
     public myFindVin (String user_md5, String plate, int goal) {
     super( user_md5, plate );
     this.goal = goal;
     }
     int goal;
     // Сначала покажем диалоговое окно прогресса
     @Override
     protected void onPreExecute() {
     super.onPreExecute();
     mDialog = new MaterialDialog.Builder(main)
     .backgroundColorRes(R.color.material_drawer_background)
     .titleColorRes(R.color.material_drawer_primary_text)
     .title(main.getResources().getString(R.string.connect))
     .contentColorRes(R.color.colorPrimary)
     .content(main.getResources().getString(R.string.addInfo))
     .progress(true, 0)
     .show();
     mDialog.setCancelable(false);
     }
     protected void onPostExecute(String file_url) {
     if ( result > 0 ) {
     if ( goal == 1 )  {  // еcли для инфы - ладно
     new  MyFindVinParsedTask  ( vin, user_md5, plate, auto.getNumber() ).execute();
     } else if ( goal == 2 ) {  //   покупка ВИН
     tempVin = vin;
     mDialog.dismiss();
     if ( !vin.contains("*") && !vin.contains("Frame") )   {
     new MyRegBuy ( user_md5, plateEng ).execute();
     }  else  {
     tempVin = "";
     // тут отправка мнесообения о проблеме с номером
     }
     } else if ( goal > 3 && goal < 7 ) {  //   инфа от ГИБДД
     tempVin = vin;
     mDialog.dismiss();
     if ( !vin.contains("*") && !vin.contains("Frame") )   {
     new myFindFibddTask (user_md5, tempVin, goal ).execute();
     }  else  {
     tempVin = "";
     // тут отправка мнесообения о проблеме с номером
     }
     }
     }   else {
     if ( result < 0 ) {  // нельзя смотреть vin
     new MaterialDialog.Builder(main)
     .title(main.getResources().getString(R.string.otvet))
     .content(main.getResources().getString(R.string.denied))
     .positiveText(R.string.ok)
     .show();
     }  else // result =0;
     new MaterialDialog.Builder(main)
     .title(main.getResources().getString(R.string.problem))
     .content(main.getResources().getString(R.string.sorry))
     .positiveText(R.string.ok)
     .show();
     // закрываем диалоговое окно с индикатором
     mDialog.dismiss();
     }
     }
     }


    class MyRegBuy extends RegBuy {
        public MyRegBuy(String user_md5, String plate ) {
            super(user_md5, plate);
        }
        protected void onPostExecute(String file_url) {
            purchase(skuVin);
        }
    }


    // покупки
    private void onProductsReady(@Nonnull Inventory.Products products, boolean canChangeSubs) {
        final Inventory.Product inApp = products.get(IN_APP);
        final Inventory.Product sub = products.get(SUBSCRIPTION);
        if (inApp.supported || sub.supported) {
            if (inApp.supported) {
                addSkus(inApp, canChangeSubs);
            }
            if (sub.supported) {
                addSkus(sub, canChangeSubs);
            }
        }
    }

     */

}
