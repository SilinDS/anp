package sds.auto.plate.ui;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import sds.auto.plate.MainActivity;
import sds.auto.plate.R;
import sds.auto.plate.base.PlateTab;
import sds.auto.plate.db.tables.PlateTable;
import sds.auto.plate.utility.LogUtil;
import sds.auto.plate.utility.PlateNumberUtils;

import static sds.auto.plate.R.drawable.nocarsmall;
import static sds.auto.plate.utility.LogUtil.makeLogTag;

public class FragmentListHolder extends Fragment {

    public FragmentListHolder() {
	}

    static EventAdapter adapter;
    static RecyclerView recyclerView;


    private static final String TAG = makeLogTag(FragmentListHolder.class);

	@Override

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_list , container, false);

        final FragmentActivity c = getActivity();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.cardList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(c);
        recyclerView.setLayoutManager(layoutManager);

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(itemAnimator);


        new Thread(new Runnable() {
            @Override
            public void run() {
                adapter = new EventAdapter ( MainActivity.context );
                c.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter( adapter );
                    }
                });
            }
        }).start();

        return rootView;

	}

    @Override
    public void onResume() {
        super.onResume();
        updateListRecord();
    }

    public void updateListRecord()  {
        adapter = new EventAdapter ( MainActivity.context );
        recyclerView.setAdapter(adapter);
    }



    /**
     * A callback interface. Called whenever a item has been selected.
     */
    public interface Callback {
        void onItemSelected(String id);
    }

    /**
     * A dummy no-op implementation of the Callback interface. Only used when no active Activity is present.
     */
    private static final Callback dummyCallback = new Callback() {
        @Override
        public void onItemSelected(String id) {
        }
    };

    /**
     * onAttach(Context) is not called on pre API 23 versions of Android.
     * onAttach(Activity) is deprecated but still necessary on older devices.
     */
    @TargetApi(23)
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onAttachToContext(context);
    }


    /**
            * Deprecated on API 23 but still necessary for pre API 23 devices.
    */
    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onAttachToContext(activity);
        }
    }

    /**
     * Called when the fragment attaches to the context
     */
    protected void onAttachToContext(Context context) {
        if (!(context instanceof Callback)) {
            throw new IllegalStateException("Activity must implement callback interface.");
        }
        callback = (Callback) context;
    }

    private Callback callback = dummyCallback;

    public class EventAdapter extends RecyclerSwipeAdapter<EventAdapter.ViewHolder> {

        @Override
        public int getSwipeLayoutResourceId(int position) {
            return R.id.swipe;
        }

        private List<PlateTab> records;
        Context context;
        PlateTable ListAllRecord;
        Calendar calendar;


        public EventAdapter(Context context ) {
            this.context = context;
            ListAllRecord =  new PlateTable ( context );
            records = ListAllRecord.getAll( 0 ); // все записи
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.event_card, viewGroup, false);
            return new EventAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

            final PlateTab record = records.get(position);

            // получаем путь к SD
            File sdPath = Environment.getExternalStorageDirectory();
            // добавляем свой каталог к пути
            final File folder = new File(sdPath.getAbsolutePath() + "/" +
                    context.getResources().getString(R.string.app_name));
            if (!folder.exists()) {
                folder.mkdir();
            }

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            Bitmap bm = null;
            String plate = PlateNumberUtils.convertForBD(record.getNumber().toUpperCase());
            File imageJpg = new File(folder + "/" + plate + ".jpg" );
            File imagePng = new File(folder + "/" + plate + ".png" );
            try {

                RoundedBitmapDrawable circularBitmapDrawable ;
                if (imageJpg.exists() ) {
                    circularBitmapDrawable = RoundedBitmapDrawableFactory.create(
                            getActivity().getResources(),
                            BitmapFactory.decodeFile( folder + "/" +
                                    plate + ".jpg", options));
                    LogUtil.logD( TAG, "JPG exists" );
                } else {

                    circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.nocarsmall));
                 //   circularBitmapDrawable.setCornerRadius(Math.max(src.getWidth(), src.getHeight()) / 2.0f);
                    LogUtil.logD( TAG, " not exists" );
                }

                circularBitmapDrawable.setCircular(true);
                viewHolder.icon.setImageDrawable(circularBitmapDrawable);


            } catch ( OutOfMemoryError e ) {
                LogUtil.logD( TAG, "OutOfMemoryError" );
            }


            try {
                if (imagePng.exists() ) {
                    BitmapFactory.Options options1 = new BitmapFactory.Options();
                    Bitmap load = BitmapFactory.decodeFile( folder + "/" +
                            plate + ".png", options1);
                    viewHolder.plateIm.setImageBitmap( load );
                    LogUtil.logD( TAG, "PNG exists" );
                } else {
                    //       viewHolder.icon.setImageBitmap( BitmapFactory.decodeResource( context.getResources(), nocar,  options));

                    LogUtil.logD( TAG, " not exists" );}
            } catch ( OutOfMemoryError e ) {
                LogUtil.logD( TAG, "OutOfMemoryError" );
            }




            SpannableString numb = PlateNumberUtils.convertToPlate( record.getNumber() );
          //  viewHolder.number.setText( numb );
            viewHolder.caption.setText( record.getCaption() );

            calendar = Calendar.getInstance();
         //   viewHolder.date.setText(getDate(record.getTimedate() ));

            viewHolder.deleteButtonListener.setRecord(record);
            viewHolder.allButtonListener.setRecord(record);

            viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
            viewHolder.swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
                @Override
                public void onDoubleClick(SwipeLayout layout, boolean surface) {
                    //         Toast.makeText(context, "DoubleClick", Toast.LENGTH_SHORT).show();
                }
            });

        }

        private String getDate(long d ) {
            //  дата в формате MM/DD/YYYY
            calendar.setTimeInMillis( d );

            String dd = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
            if (dd.length() <2 )  dd = "0" + dd;
            String mm = String.valueOf (calendar.get(Calendar.MONTH ) +1 );
            if (mm.length() <2 )  mm = "0" + mm;

            String yy = String.valueOf(calendar.get(Calendar.YEAR)-2000);
            if (yy.length() <2 )  yy = "0" + yy;

            return (new StringBuilder().append(dd)
                    .append(".").append(mm).append(".")
                    .append(yy).append(" ")).toString();
        }




        @Override
        public int getItemCount() {
            return records.size();
        }

        public void add (PlateTab record) {
            int position = records.indexOf(record);

            ListAllRecord.add( record );

            records.add (position + 1, record);
            notifyItemInserted (position + 1);

        }

        class ViewHolder extends RecyclerView.ViewHolder {

            private TextView number, caption;
            private TextView date;
            private ImageView icon;
            private ImageView plateIm;

            SwipeLayout swipeLayout;
            Button buttonDelete;

            private DeleteButtonListener deleteButtonListener;
            private AllButtonListener allButtonListener;
            private LinearLayout all;

            public ViewHolder(View itemView) {
                super(itemView);

                swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
                buttonDelete = (Button) itemView.findViewById(R.id.delete);

                all = (LinearLayout)  itemView.findViewById(R.id.allline);
                caption = (TextView) itemView.findViewById(R.id.auto_caption);

           //     String custom_font = "fonts/RoadNumbers2.0.ttf";
          //      Typeface typeface = Typeface.createFromAsset(context.getResources().getAssets(), custom_font);
        //        number.setTypeface( typeface );

                icon = (ImageView) itemView.findViewById(R.id.thumbnail);
                plateIm = (ImageView) itemView.findViewById(R.id.plateIm);

                deleteButtonListener = new DeleteButtonListener();
                buttonDelete.setOnClickListener(deleteButtonListener);

                final boolean[] isOpenSwipeLayout = new boolean[1];
                swipeLayout.addSwipeListener( new SimpleSwipeListener() {
                    @Override
                    public void onStartOpen(SwipeLayout layout) {
                        isOpenSwipeLayout[0] = true;
                    }
                    @Override
                    public void onStartClose(SwipeLayout layout) {
                        isOpenSwipeLayout[0] = false;
                    }
                });
                ViewTreeObserver.OnGlobalLayoutListener swipeGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (isOpenSwipeLayout[0]) {
                            // Opens the layout without animation
                            swipeLayout.open(false);
                        }
                    }
                };

                swipeLayout.getViewTreeObserver().addOnGlobalLayoutListener(swipeGlobalLayoutListener);

                allButtonListener = new AllButtonListener();
                all.setOnClickListener(allButtonListener);

            }
        }

        private class AllButtonListener implements View.OnClickListener {
            private PlateTab record;
            public void setRecord(PlateTab record) {
                this.record = record;
            }
            @Override
            public void onClick(View view) {

                callback.onItemSelected( String.valueOf(record.getId()));
            }
        }

        private class DeleteButtonListener implements View.OnClickListener {
            private PlateTab record;

            @Override
            public void onClick(View view) {

                int position = records.indexOf(record);
                long q = ListAllRecord.delete(record.getIdPlate());
                records.remove(position);
                notifyItemRemoved( position );

            }

            public void setRecord(PlateTab record) {
                this.record = record;
            }
        }



    }


}
