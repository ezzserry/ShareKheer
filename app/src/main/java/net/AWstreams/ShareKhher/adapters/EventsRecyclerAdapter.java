package net.AWstreams.ShareKhher.adapters;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;

import net.AWstreams.ShareKhher.R;
import net.AWstreams.ShareKhher.models.CitiesList;
import net.AWstreams.ShareKhher.models.City;
import net.AWstreams.ShareKhher.models.EventItem;
import net.AWstreams.ShareKhher.utils.Constants;
import net.AWstreams.ShareKhher.utils.GPSTracker;

import java.util.ArrayList;

/**
 * Created by Ahmed Ezz on 26/03/2016.
 */
public class EventsRecyclerAdapter extends RecyclerView.Adapter<EventsRecyclerAdapter.CustomViewHolder> {

    private ArrayList<EventItem> eventsList;
    private Context mContext;
    GPSTracker gpsTracker;
    Typeface typefaceLight, typefaceBold;
    CitiesList citiesList;

    public EventsRecyclerAdapter(Context context, ArrayList<EventItem> videoItems) {
        this.eventsList = videoItems;
        this.mContext = context;

    }


    @Override
    public EventsRecyclerAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_event, null);
        typefaceLight = Typeface.createFromAsset(viewGroup.getContext().getAssets(), Constants.FONT_REGULAR_AR);
        typefaceBold = Typeface.createFromAsset(viewGroup.getContext().getAssets(), Constants.FONT_BOLD_AR);

        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final EventsRecyclerAdapter.CustomViewHolder holder, int position) {

        final EventItem eventItem = eventsList.get(position);
        if (eventItem != null) {
            holder.event_nametv.setText(eventItem.getName());
            holder.event_desctv.setText(eventItem.getDesc());
            holder.event_timetv.setText(eventItem.getTime());
            if (eventItem.getType().equals("Both")) {
                holder.event_type.setText("متبرعين و متطوعين");
            } else if (eventItem.getType().equals("Donation"))
                holder.event_type.setText("متبرعين");
            else if ((eventItem.getType().equals("Volunteering")))
                holder.event_type.setText("متطوعين");
            holder.event_datetv.setText(eventItem.getDate());
            holder.event_address.setText(eventItem.getAddress());
            if (eventItem.getCity().equals("cairo")) {
                holder.event_city.setText("القاهرة");
                citiesList = new CitiesList();
                ArrayList<City> arrayListcairo = citiesList.addcairoCities();
                for (int i = 0; i < arrayListcairo.size(); i++) {
                    if (eventItem.getArea().equals(arrayListcairo.get(i).getEnglishCity())) {
                        String city = arrayListcairo.get(i).getArabicCity() + " ,";
                        holder.event_area.setText(city);
                        break;
                    } else
                        holder.event_area.setText(eventItem.getArea());
                }

            } else if (eventItem.getCity().equals("giza")) {
                holder.event_city.setText("الجيزة");
                citiesList = new CitiesList();
                ArrayList<City> arrayListgiza = citiesList.addgizaCities();

                for (int i = 0; i < arrayListgiza.size(); i++) {
                    if (eventItem.getArea().equals(arrayListgiza.get(i).getEnglishCity())) {
                        holder.event_area.setText(arrayListgiza.get(i).getArabicCity() + " ,");
                    }
                }
            } else if (eventItem.getCity().equals("alex")) {
                holder.event_city.setText("الاسكندرية");
                citiesList = new CitiesList();
                ArrayList<City> arrayListalex = citiesList.addalexCities();
                for (int i = 0; i < arrayListalex.size(); i++) {
                    if (eventItem.getArea().equals(arrayListalex.get(i).getEnglishCity())) {
                        holder.event_area.setText(arrayListalex.get(i).getArabicCity() + " ,");
                    }
                }
            }
            Glide.with(mContext)
                    .load(eventItem.getThumbnail())
                    .into(holder.thumbnail);
            Picasso.with(mContext)
                    .load(eventItem.getThumbnail())
                    .into(holder.thumbnail, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            holder.thumbnail.setVisibility(View.VISIBLE);
                            holder.progressBar.setVisibility(View.GONE);

                        }

                        @Override
                        public void onError() {
                            holder.thumbnail.setVisibility(View.VISIBLE);
                            holder.thumbnail.setImageResource(R.mipmap.loadingbg);
                            holder.progressBar.setVisibility(View.GONE);

                        }
                    });

            holder.btnCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String phone_number = eventItem.getPhone();
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone_number));
                    mContext.startActivity(intent);
                }

            });

            holder.event_location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!eventItem.getLat().equals("0.0") && !eventItem.getLat().equals("0.0")) {
                        double latitude = Double.parseDouble(eventItem.getLat());
                        double longitude = Double.parseDouble(eventItem.getLng());
                        gpsTracker = new GPSTracker(mContext);
                        double currentLatitude = gpsTracker.getLatitude();
                        double currentLongitude = gpsTracker.getLongitude();
                        String url = "http://maps.google.com/maps?saddr=" + currentLatitude + "," + currentLongitude + "&daddr=" + latitude + "," + longitude + "&mode=driving";
//                    Uri gmmIntentUri = Uri.parse("geo:" + latitude + "," + longitude);
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        mapIntent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                        mContext.startActivity(mapIntent);
                    } else
                        Toast.makeText(mContext, "لم يتم تحديد موقع النشاط من قبل صاحب النشاط", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView thumbnail;
        protected TextView event_nametv;
        protected TextView event_desctv;
        protected TextView event_timetv;
        protected TextView event_datetv;
        protected TextView event_type;
        protected TextView event_address;
        protected TextView event_city;
        protected TextView event_area;
        protected ImageButton event_location;
        protected ProgressBar progressBar;
        protected ImageButton btnCall;


        public CustomViewHolder(View view) {
            super(view);
            this.progressBar = (ProgressBar) view.findViewById(R.id.loadingpanel);
            this.thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            this.event_address = (TextView) view.findViewById(R.id.eventaddress_tv);
            this.event_nametv = (TextView) view.findViewById(R.id.eventname_tv);
            this.event_desctv = (TextView) view.findViewById(R.id.eventdesc_tv);
            this.event_timetv = (TextView) view.findViewById(R.id.eventtime_tv);
            this.event_datetv = (TextView) view.findViewById(R.id.eventdate_tv);
            this.event_type = (TextView) view.findViewById(R.id.eventtype_tv);
            this.event_city = (TextView) view.findViewById(R.id.eventcity_tv);
            this.event_area = (TextView) view.findViewById(R.id.eventarea_tv);
            this.event_location = (ImageButton) view.findViewById(R.id.route_btn);
            this.btnCall = (ImageButton) view.findViewById(R.id.call_btn);

            this.event_address.setTypeface(typefaceLight);
            this.event_nametv.setTypeface(typefaceBold);
            this.event_desctv.setTypeface(typefaceBold);
            this.event_timetv.setTypeface(typefaceLight);
            this.event_datetv.setTypeface(typefaceLight);
            this.event_type.setTypeface(typefaceLight);
            this.event_city.setTypeface(typefaceLight);
            this.event_area.setTypeface(typefaceLight);


        }
    }
}
