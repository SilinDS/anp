package sds.auto.plate;

import android.app.Activity;
import android.app.Application;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import org.solovyev.android.checkout.Billing;
import org.solovyev.android.checkout.Cache;
import org.solovyev.android.checkout.Checkout;
import org.solovyev.android.checkout.Inventory;
import org.solovyev.android.checkout.RobotmediaDatabase;
import org.solovyev.android.checkout.RobotmediaInventory;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import retrofit2.Retrofit;
import sds.auto.plate.retrofit.converter.MyJsonConverter;
import sds.auto.plate.retrofit.service.UslugaInterface;
import sds.auto.plate.retrofit.service.UslugaStInterface;

import static android.content.Intent.ACTION_VIEW;


public class App extends Application {


 //   private static MyPlateInterface myPlateApi;


    private static UslugaInterface UslugaApi;
    private static UslugaStInterface UslugaStApi;

    private static Retrofit retrofit3qr;

  //  private Retrofit retrofitLibsite,
   //         retrofitUsluga,retrofitUslugaSt, retrofitAnpPlate, retrofitAnpImage,
   //         retrofitGibdd;


    @Override
    public void onCreate() {
        super.onCreate();
        File sdPath = Environment.getExternalStorageDirectory();
        final File folder = new File(sdPath.getAbsolutePath() + "/" + getString(R.string.app_name));
        if (!folder.exists()) {
            folder.mkdir();
        }

        retrofit3qr = new Retrofit.Builder()
                .baseUrl("http://3qr.ru/") //Базовая часть адреса
             //   .addConverterFactory(GsonConverterFactory.create()) //Конвертер, необходимый для преобразования JSON'а в объекты
                .addConverterFactory(MyJsonConverter.create()) //Конвертер, необходимый для преобразования JSON'а в объекты
                .build();
           //   myPlateApi = retrofit3qr.create(MyPlateInterface.class); //Создаем объект, при помощи которого будем выполнять запросы


  //      retrofitUslugaSt = new Retrofit.Builder()
    //            .baseUrl("http://avto-yslyga.ru/") //Базовая часть адреса
          //      .addConverterFactory(WebdkConvertor.create()) //Конвертер, необходимый для преобразования JSON'а в объекты
   //             .client(okHttpClient)
   //             .build();
   //     UslugaStApi = retrofitUslugaSt.create(UslugaStInterface.class); //Создаем объект, при помощи которого будем выполнять запросы

  //      retrofitUsluga = new Retrofit.Builder()
    //            .baseUrl("http://avto-yslyga.ru/") //Базовая часть адреса
  //              .addConverterFactory(UslugaConvertor.create()) //Конвертер, необходимый для преобразования JSON'а в объекты
    //            .client(okHttpClient)
   //             .build();
   //     UslugaApi = retrofitUsluga.create(UslugaInterface.class); //Создаем объект, при помощи которого будем выполнять запросы



    }



    public static Retrofit getMyApi() {   return retrofit3qr;      }

    @Nonnull
    public static final List<String> IN_APPS = new ArrayList<>();
    @Nonnull
    public static final List<String> SUBSCRIPTIONS = new ArrayList<>();
    @Nonnull
    static final String MAIL = "sds3qr@gmail.com";
    @Nonnull
    private static Application instance;

    static {
        IN_APPS.addAll(Arrays.asList("sds.testpay", "sds.auto.vin", "sds.auto.gibdd"));
        SUBSCRIPTIONS.add("sub_01");
    }

    /**
     * For better performance billing class should be used as singleton
     */
    @Nonnull
    private final Billing billing = new Billing(this, new Billing.DefaultConfiguration() {
        @Nonnull
        @Override
        public String getPublicKey() {
            // Note that this is not plain public key but public key encoded with CheckoutApplication.x() method where
            // key = MAIL. As symmetric ciphering is used in CheckoutApplication.x() the same method is used for both
            // ciphering and deciphering. Additionally result of the ciphering is converted to Base64 string => for
            // deciphering with need to convert it back. Generally, x(fromBase64(toBase64(x(PK, salt))), salt) == PK
            // To cipher use CheckoutApplication.toX(), to decipher - CheckoutApplication.fromX().
            // Note that you also can use plain public key and just write `return "Your public key"` but this
            // is not recommended by Google, see http://developer.android.com/google/play/billing/billing_best_practices.html#key
            // Also consider using your own ciphering/deciphering algorithm.
            final String s = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAyST0bTIos6QCfPI8KPKAvd79OtF6sERZQVyINZwPjqKh2aQlAgEwk4x7xyuOOqHgfaKFjH7Erlxm/lbZJmyPPf2b7infdzJDaL8AP7KB3avdk4q3CFyMPY4ItJgg141yX5Km/mWw4JybPRVpkPC4EMm4NI56tQZbqYqpBAGKdxrk+d+ADIVyJjZuHMRLuuWbx3ZtW9itu15sFikEssCIvEs25i4nvL2BSoD1ueSYsVyDkklUuDa9W/cEJL+4A+94zgUx8zd4STmAzNqjkuz8b6UXz5uWFGSabGi+Ke6iLN/w9vSg05NLQKo0CrMb3bT3/QyHBttClXtJFwwzKbSeYQIDAQAB";
            return s;
                    //fromX(s, MAIL);
        }

        @Nullable
        @Override
        public Cache getCache() {
            return Billing.newCache();
        }

        @Nullable
        @Override
        public Inventory getFallbackInventory(@Nonnull Checkout checkout, @Nonnull Executor onLoadExecutor) {
            if (RobotmediaDatabase.exists(billing.getContext())) {
                return new RobotmediaInventory(checkout, onLoadExecutor);
            } else {
                return null;
            }
        }
    });
    public App() {
        instance = this;
    }

    /**
     * Method deciphers previously ciphered message
     *
     * @param message ciphered message
     * @param salt    salt which was used for ciphering
     * @return deciphered message
     */
    @Nonnull
    static String fromX(@Nonnull String message, @Nonnull String salt) {
        return x(new String(Base64.decode(message, 0)), salt);
    }

    /**
     * Method ciphers message. Later {@link #fromX} method might be used for deciphering
     *
     * @param message message to be ciphered
     * @param salt    salt to be used for ciphering
     * @return ciphered message
     */
    @Nonnull
    static String toX(@Nonnull String message, @Nonnull String salt) {
        return new String(Base64.encode(x(message, salt).getBytes(), 0));
    }

    /**
     * Symmetric algorithm used for ciphering/deciphering. Note that in your application you
     * probably want to modify
     * algorithm used for ciphering/deciphering.
     *
     * @param message message
     * @param salt    salt
     * @return ciphered/deciphered message
     */
    @Nonnull
    static String x(@Nonnull String message, @Nonnull String salt) {
        final char[] m = message.toCharArray();
        final char[] s = salt.toCharArray();

        final int ml = m.length;
        final int sl = s.length;
        final char[] result = new char[ml];

        for (int i = 0; i < ml; i++) {
            result[i] = (char) (m[i] ^ s[i % sl]);
        }
        return new String(result);
    }

    @Nonnull
    public static Application get() {
        return instance;
    }

    static boolean openUri(@Nonnull Activity activity, @Nonnull String uri) {
        try {
            activity.startActivity(new Intent(ACTION_VIEW, Uri.parse(uri)));
            return true;
        } catch (ActivityNotFoundException e) {
            Log.e("Checkout", e.getMessage(), e);
        }
        return false;
    }

    @Nonnull
    public Billing getBilling() {
        return billing;
    }
}
