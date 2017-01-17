package sds.auto.plate;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import sds.auto.plate.base.BaseActivity;
import sds.auto.plate.base.PlateTab;
import sds.auto.plate.db.tables.PlateTable;
import sds.auto.plate.ui.AutoDetailFragment;
import sds.auto.plate.ui.FragmentListHolder;
import sds.auto.plate.utility.LogUtil;

import static sds.auto.plate.utility.LogUtil.makeLogTag;

public class MainActivity extends BaseActivity  implements FragmentListHolder.Callback {
    private Activity activity;
    public static Context context;
    private static final String TAG = makeLogTag(MainActivity.class);
    private SharedPreferences sp;
    private SharedPreferences.Editor spe;
    private int terms;
    private static final String DEVICE_MD5 = "md5";
    private static final String TERMS = "terms";
    private static final String SETTINGS_HIDECHECK = "hidecheck";
    private boolean twoPaneMode;
    public static FloatingActionButton scanButton;
    public PlateTable dbAuto;
    private List<PlateTab> cars;
    final int REQUEST_CODE_INFO = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.logE(TAG, "create");
        context = MainActivity.this;
        activity = this;
        activityCode = 1;

        loadPref();
        if (terms > 0) {
            //  создаем список номеров
            dbAuto = new PlateTable( context ) ;
            cars = dbAuto.getAll( 0 );
            setContentView(R.layout.activity_main);
            LogUtil.logE(TAG, "setContentView");

            if (  false )  {  //isTwoPaneLayoutUsed()) {  разобраться, когда для планшетов буду делать
                twoPaneMode = true;
                LogUtil.logD("TEST", "TWO POANE TASDFES");
                enableActiveItemState();
            }
            if (savedInstanceState == null && twoPaneMode) {
                setupDetailFragment();
            }
            // проверка подключений
            LogUtil.logE(TAG, "запускаем проверку сервисов");
            Intent infoIntent = new Intent(this, InfoActivity.class);
       //     startActivityForResult(infoIntent, REQUEST_CODE_INFO);
            scanButton = (FloatingActionButton) findViewById( R.id.fab );
            scanButton.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addNewAuto() ;
                }
            } );

        } else {
            new MaterialDialog.Builder(this)
                    .iconRes(R.mipmap.ic_launcher)
                    .limitIconToDefaultSize() // limits the displayed icon size to 48dp
                    .backgroundColorRes(R.color.material_drawer_background)
                    .titleColorRes(R.color.material_drawer_primary_text)
                    .title(R.string.item_terms)
                    .contentColorRes(R.color.colorPrimary)
                    .content(readRawTextFile(R.raw.terms))
                    .positiveText(R.string.agree)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @SuppressLint("CommitPrefEdits")
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            spe = sp.edit(); //  готовимся менять настройки
                            spe.putInt(TERMS, 1);
                            spe.commit();
                            recreate();
                        }
                    })
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            spe = sp.edit(); //  готовимся менять настройки
                            spe.putInt(TERMS, 0);
                            spe.commit();
                            finish();
                        }
                    })
                    .negativeText(R.string.disagree)
                    .cancelable(false)
                    .show();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerResult.setOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                if (drawerItem != null) {
                    Intent intent = null;
                    if (drawerItem.getIdentifier() == 1) {
                        // сами себя не будем запускать
                    } else if (drawerItem.getIdentifier() == 2) {
                        intent = new Intent( activity, InfoActivity.class);
                    }

                    if (intent != null) {
                        MainActivity.this.startActivity(intent);
                    }
                }
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // запишем в лог значения requestCode и resultCode
        LogUtil.logE(TAG, "requestCode = " + requestCode + ", resultCode = " + resultCode);
        // если пришло ОК
        if (resultCode == RESULT_OK) {

            if (data.getIntExtra("result", 1) == 1 )  finish();  //  выкидываем, если что не так
            if (  cars.size() == 0 )  addNewAuto();
        } else {
            //         разобраться, а какой еще может быть resultCode ?
          //  finish();
        }
    }
    private void setupDetailFragment() {
        if ( cars.size() > 0 ) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            AutoDetailFragment adf = new AutoDetailFragment();
            // Подготавливаем аргументы
            Bundle args = new Bundle();
            args.putString(AutoDetailFragment.ARG_ITEM_ID,
                    String.valueOf( cars.get(0).getIdPlate() ));
            adf.setArguments(args);
            ft.replace(R.id.article_detail_container, adf, "AutoDetailFragment");
            ft.addToBackStack(null);
            ft.commit();
        }
    }
    public void addNewAuto() {
        Intent inputIntent = new Intent(activity, InputActivity.class);
        startActivity(inputIntent);
        scanButton.hide();
    }
    @Override
    public boolean providesActivityToolbar() {
        return false;
    }
    private void enableActiveItemState() {
    }
    public String readRawTextFile(int resId) {
        InputStream inputStream = context.getResources().openRawResource(resId);
        InputStreamReader inputreader;
        StringBuilder text;
        try {
            inputreader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader buffreader = new BufferedReader(inputreader);
            String line;
            text= new StringBuilder();
            while ((line = buffreader.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            return null;
        }
        return text.toString();
    }
    private void loadPref() {
        LogUtil.logE(TAG, "load preferences");
        sp = PreferenceManager.getDefaultSharedPreferences(context);
        if (!sp.contains(DEVICE_MD5) || !sp.contains(TERMS)) {
            spe = sp.edit(); //  готовимся менять настройки
            spe.putString(DEVICE_MD5, "");
            spe.putInt(TERMS, 0);
            spe.putBoolean( SETTINGS_HIDECHECK, false );
            spe.commit();
        }
        terms = sp.getInt(TERMS, -1);

        spe = sp.edit(); //  для новых настроек, потом - убрать
        spe.putBoolean( SETTINGS_HIDECHECK, true );
        spe.commit();
    }
    @Override
    public void onItemSelected(String id) {
        if (twoPaneMode) {
            LogUtil.logE(TAG, "используем две панели");
            // Show the quote detail information by replacing the DetailFragment via transaction.
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            AutoDetailFragment adf = new AutoDetailFragment();
            // Подготавливаем аргументы
            Bundle args = new Bundle();
            args.putString(AutoDetailFragment.ARG_ITEM_ID, id );
            adf.setArguments(args);
            ft.replace(R.id.article_detail_container, adf, "AutoDetailFragment");
            ft.addToBackStack(null);
            ft.commit();
        } else {
            LogUtil.logE(TAG, "используем одна панель");
            // Start the detail activity in single pane mode.
            Intent detailIntent = new Intent(activity, AutoDetailActivity.class);
            detailIntent.putExtra(AutoDetailFragment.ARG_ITEM_ID, id);
            detailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(detailIntent);
        }
    }
    @Override
    public void onBackPressed() {
        // Закрываем Navigation Drawer по нажатию системной кнопки "Назад" если он открыт
        if (drawerResult.isDrawerOpen()) {
            drawerResult.closeDrawer();
        } else  {
            new MaterialDialog.Builder(this)
                    .iconRes(R.mipmap.ic_launcher)
                    .limitIconToDefaultSize() // limits the displayed icon size to 48dp
                    .backgroundColorRes(R.color.material_drawer_background)
                    .titleColorRes(R.color.material_drawer_primary_text)
                    .title(R.string.dialog_title_exit)
                    .contentColorRes(R.color.colorPrimary)
                    .content(R.string.dialog_text_exit)
                    .positiveText(R.string.yes)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            MainActivity.super.onBackPressed();
                        }
                    })
                    .negativeText(R.string.no)
                    .show();
        }
    }
}