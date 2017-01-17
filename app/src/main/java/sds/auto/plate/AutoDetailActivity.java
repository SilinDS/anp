package sds.auto.plate;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import sds.auto.plate.base.BaseActivity;
import sds.auto.plate.ui.AutoDetailFragment;

/**
 * This wrapper is only used in single pan mode (= on smartphones)
 * Created by Andreas Schrade on 14.12.2015.
 */
public class AutoDetailActivity extends BaseActivity {


    private static Activity context;

    AutoDetailFragment adf;

    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        activity = this;
        context = AutoDetailActivity.this;
        activityCode = 2;

        // Show the Up button in the action bar.
///        if (getSupportActionBar() != null) {
    //        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  //      }

    //    AutoDetailFragment fragment =  AutoDetailFragment.newInstance(
   //             getIntent().getStringExtra(AutoDetailFragment.ARG_ITEM_ID));

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        adf = new AutoDetailFragment();

        // Подготавливаем аргументы
        Bundle args = new Bundle();
        args.putString(AutoDetailFragment.ARG_ITEM_ID,
                getIntent().getStringExtra(AutoDetailFragment.ARG_ITEM_ID) );
        adf.setArguments(args);
        ft.replace(R.id.article_detail_container, adf, "AutoDetailFragment");
        ft.addToBackStack(null);
        ft.commit();



   //     getFragmentManager().beginTransaction().replace(R.id.article_detail_container, fragment).commit();

        if ( Build.VERSION.SDK_INT> Build.VERSION_CODES.LOLLIPOP_MR1 )  {

            verifyStoragePermissions( this );

        }  else access = true;
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
                        finish();
                    } else if (drawerItem.getIdentifier() == 2) {
                        intent = new Intent( activity, InfoActivity.class);
                    }

                    if (intent != null) {
                        AutoDetailActivity.this.startActivity(intent);
                    }
                }
                return false;
            }
        });
    }

    public static void verifyStoragePermissions(Activity activity) {
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
        }
    }

    // Storage Permissions variables
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    boolean access = false;

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    context.recreate();
                } else {
                    finish();
                }
            }
        }
    }

    @Override
    public boolean providesActivityToolbar() {
        return true;
    }


    @Override
    public void onBackPressed() {
        // Закрываем Navigation Drawer по нажатию системной кнопки "Назад" если он открыт
        if (drawerResult.isDrawerOpen()) {
            drawerResult.closeDrawer();
        } else if ( adf == null ) {


            new AlertDialog.Builder(this)
                    .setTitle("Требуется подтверждение.")
                    .setMessage("Вы действительно хотите выйти из приложения?")
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    }).show();


        } else {
            adf = null;
            AutoDetailActivity.super.onBackPressed();
            finish();
        }

    }


}
