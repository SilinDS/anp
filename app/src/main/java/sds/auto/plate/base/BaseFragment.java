package sds.auto.plate.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import org.solovyev.android.checkout.ActivityCheckout;
import org.solovyev.android.checkout.Inventory;

import javax.annotation.Nonnull;


/**
 * The base class for all fragment classes.
 *
 * Created by Andreas Schrade on 14.12.2015.
 */
public class BaseFragment extends Fragment {

    @Nonnull
    protected ActivityCheckout checkout;
    protected Inventory inventory;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
   //     checkout = ((BaseActivity) activity).getCheckout();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
  //      inventory = checkout.makeInventory();
    }

}
