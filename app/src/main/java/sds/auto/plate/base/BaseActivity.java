package sds.auto.plate.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;

import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import sds.auto.plate.R;

import static sds.auto.plate.utility.LogUtil.logD;
import static sds.auto.plate.utility.LogUtil.makeLogTag;

public abstract class BaseActivity extends AppCompatActivity {
    private static final String TAG = makeLogTag(BaseActivity.class);
    protected Drawer drawerResult = null;
    protected int activityCode;

 //   @Nonnull
 //   protected final ActivityCheckout checkout = Checkout.forActivity(this, Application.get().getBilling());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
  //      checkout.start();
    }

    @Override
    protected void onDestroy() {
  //      checkout.stop();
        super.onDestroy();
    }

 //   @Nonnull
 //   public ActivityCheckout getCheckout() {
 //       return checkout;
    //  }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
   //     checkout.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setupNavDrawer();
    }

    private void setupNavDrawer() {
        // Инициализируем Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        activityCode = 0;

        if ( toolbar != null ) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            // иконки могут понадобиться:
            //  car, truck, email? email-open, eye - глаз, favorite, globe (глобус)
            // key, power, refresh-sync, storage (список), help
            //  camera, card-sd, keyboard-hide, mic, view-list-alt


            drawerResult = new DrawerBuilder()
                    .withActivity(this)
                    .withToolbar(toolbar)
                    .withActionBarDrawerToggle(true)
             //       .withHeader(R.layout.drawer_header)
                    .addDrawerItems(
                            new PrimaryDrawerItem().withName(R.string.drawer_item_list)
                                    .withIcon(MaterialDesignIconic.Icon.gmi_view_list)
                                    .withBadge("99").withIdentifier(1),
                            new PrimaryDrawerItem().withName(R.string.drawer_item_check)
                                    .withIcon(MaterialDesignIconic.Icon.gmi_refresh)
                                    .withIdentifier(2),
                          //  new PrimaryDrawerItem().withName(R.string.drawer_item_settings).withIcon(MaterialDesignIconic.Icon.gmi_account),
                    //        new PrimaryDrawerItem().withName(R.string.drawer_item_custom).withIcon(FontAwesome.Icon.faw_eye).withBadge("6").withIdentifier(2),
                     //       new SectionDrawerItem().withName(R.string.drawer_item_settings) //,
                            new SecondaryDrawerItem().withName(R.string.drawer_item_settings)
                                    .withIcon(MaterialDesignIconic.Icon.gmi_settings)
                                    .withIdentifier(9)//,
                   //         new SecondaryDrawerItem().withName(R.string.drawer_item_help).withIcon(FontAwesome.Icon.faw_cog),
                    //        new SecondaryDrawerItem().withName(R.string.drawer_item_open_source).withIcon(FontAwesome.Icon.faw_question),
                   //         new DividerDrawerItem(),
                     //       new SecondaryDrawerItem().withName(R.string.drawer_item_contact).withIcon(FontAwesome.Icon.faw_github).withBadge("12+").withIdentifier(1)
                    )

                    .withOnDrawerListener(new Drawer.OnDrawerListener() {
                        @Override
                        public void onDrawerOpened(View drawerView) {
                            // Скрываем клавиатуру при открытии Navigation Drawer
                            InputMethodManager inputMethodManager = (InputMethodManager) BaseActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(BaseActivity.this.getCurrentFocus().getWindowToken(), 0);
                        }
                        @Override
                        public void onDrawerClosed(View drawerView) {
                        }
                        @Override
                        public void onDrawerSlide(View drawerView, float slideOffset) {
                        }
                    })
                    .withOnDrawerItemLongClickListener(new Drawer.OnDrawerItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(View view, int position, IDrawerItem drawerItem) {
                            return false;
                        }
                        // Обработка длинного клика, например, только для SecondaryDrawerItem
                        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                            if (drawerItem instanceof SecondaryDrawerItem) {
                                //        Toast.makeText(MainActivity.this, MainActivity.this.getString(((SecondaryDrawerItem) drawerItem).getNameRes()), Toast.LENGTH_SHORT).show();
                            }
                            return false;
                        }
                    })
                    .build();


            logD(TAG, "navigation drawer setup finished");
        }

    }


    // Заглушка, работа с меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    //    getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // Заглушка, работа с меню
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
   //     if (id == R.id.action_settings) {
   //         return true;
  //      }

        return super.onOptionsItemSelected(item);
    }

    public abstract boolean providesActivityToolbar();

    public void setToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}