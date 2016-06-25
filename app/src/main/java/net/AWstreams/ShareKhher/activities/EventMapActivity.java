//package net.AWstreams.ShareKhher.activities;
//
//import android.content.Context;
//import android.content.Intent;
//import android.location.Address;
//import android.location.Geocoder;
//import android.os.AsyncTask;
//import android.support.design.widget.Snackbar;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.Display;
//import android.view.Gravity;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.LinearLayout;
//import android.widget.PopupWindow;
//import android.widget.Toast;
//
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.BitmapDescriptorFactory;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MarkerOptions;
//
//import net.AWstreams.ShareKhher.R;
//import net.AWstreams.ShareKhher.fragments.CreateEventFragment;
//import net.AWstreams.ShareKhher.utils.ConnectionDetector;
//import net.AWstreams.ShareKhher.utils.GPSTracker;
//
//import java.io.IOException;
//import java.util.List;
//
//public class EventMapActivity extends AppCompatActivity {
//    GPSTracker gpsTracker;
//    EditText etSearch;
//    // flag for Internet connection status
//    Boolean isInternetPresent = false;
//    // Connection detector class
//    ConnectionDetector cd;
//    GoogleMap googleMap;
//    LinearLayout mainlayout;
//    MarkerOptions markerOptions;
//    String eventLat, eventLng;
//    String SearchAddress = "";
//    LatLng latLng;
//    Button btnSave;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_events_map);
//        btnSave = (Button) findViewById(R.id.close_map);
//        btnSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(!SearchAddress.equals("")) {
//                    Intent intent = new Intent();
//                    intent.putExtra("lat", eventLat);
//                    intent.putExtra("lng", eventLng);
//                    intent.putExtra("address", SearchAddress);
//                    setResult(RESULT_OK, intent);
////                    startActivity(intent);
//                    finish();
//                }
//                else
//                    Toast.makeText(getApplicationContext(),"ادخل العنوان",Toast.LENGTH_LONG).show();
//            }
//        });
//        gpsTracker = new GPSTracker(this);
//        double latitude = gpsTracker.getLatitude();
//        double longitude = gpsTracker.getLongitude();
//        showMap(latitude, longitude);
//    }
//
//    private void showMap(double latitude, double longitude) {
//
//        try {
//            // Loading map
//            initMap();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        // create marker
//        final MarkerOptions marker = new MarkerOptions().position(
//                new LatLng((latitude), (longitude)))
//                .title("Your location");
//
//        // ROSE color icon
//        marker.icon(BitmapDescriptorFactory
//                .defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
//
//        // adding marker
//        googleMap.addMarker(marker);
//
//        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
//                Double.valueOf(latitude), Double.valueOf(longitude)), 10.0f));
//        Toast.makeText(this, "موقعك الان", Toast.LENGTH_LONG).show();
//
//
//        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(LatLng latLng) {
//                // Creating a marker
//                MarkerOptions markerOptions = new MarkerOptions();
//
//                // Setting the position for the marker
//                markerOptions.position(latLng);
//
//                // Setting the title for the marker.
//                // This will be displayed on taping the marker
//                markerOptions.title(latLng.latitude + " : " + latLng.longitude);
//
//                // Clears the previously touched position
//                googleMap.clear();
//
//                // Animating to the touched position
//                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
//
//                // Placing a marker on the touched position
//                googleMap.addMarker(markerOptions);
//                eventLat = String.valueOf(latLng.latitude);
//                eventLng = String.valueOf(latLng.longitude);
//            }
//        });
//
//        etSearch = (EditText) findViewById(R.id.search_et);
//        etSearch.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                // If the event is a key-down event on the "enter" button
//                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
//                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
//                    cd = new ConnectionDetector(getApplicationContext());
//                    isInternetPresent = cd.isConnectingToInternet();
//
//                    if (isInternetPresent) {
//
//                        if (!etSearch.getText().toString().isEmpty()) {
//                            String query = etSearch.getText().toString();
//                            // Getting user input location
//
//                            if (query != null && !query.equals("")) {
//                                new GeocoderTask().execute(query);
//                                SearchAddress = query;
//
//                            }
//                            return true;
//                        } else
//                            Snackbar.make(mainlayout, "ادخل العنوان", Snackbar.LENGTH_LONG)
//                                    .setAction("Action", null).show();
//                    } else
//                        Snackbar.make(mainlayout, "No internet connection ", Snackbar.LENGTH_LONG)
//                                .setAction("Action", null).show();
//                }
//                return false;
//            }
//        });
//
//    }
//
//    private void initMap() {
//        if (googleMap == null) {
//            googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(
//                    R.id.map)).getMap();
//
//            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//            // check if map is created successfully or not
//            if (googleMap == null) {
//                Toast.makeText(getApplicationContext(),
//                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
//                        .show();
//            }
//
//        }
//    }
//
//
//    private class GeocoderTask extends AsyncTask<String, Void, List<Address>> {
//
//        @Override
//        protected List<Address> doInBackground(String... locationName) {
//            // Creating an instance of <a href="#">Geocoder</a> class
//            Geocoder geocoder = new Geocoder(getApplicationContext());
//            List<Address> addresses = null;
//
//            try {
//                // Getting a maximum of 3 Address that matches the input text
//                addresses = geocoder.getFromLocationName(locationName[0], 3);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return addresses;
//        }
//
//        @Override
//        protected void onPostExecute(List<Address> addresses) {
//
//            if (addresses == null || addresses.size() == 0) {
//                Toast.makeText(getApplicationContext(), "No Location found", Toast.LENGTH_SHORT).show();
//            }
//
//            // Clears all the existing markers on the <a href="#">map</a>
//            googleMap.clear();
//
//            // Adding Markers on Google <a href="#">Map</a> for each matching address
//            for (int i = 0; i < addresses.size(); i++) {
//
//                Address address = (Address) addresses.get(i);
//
//                // Creating an instance of GeoPoint, to display in Google <a href="#">Map</a>
//                latLng = new LatLng(address.getLatitude(), address.getLongitude());
//
//                String addressText = String.format("%s, %s",
//                        address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
//                        address.getCountryName());
//
//                markerOptions = new MarkerOptions();
//                markerOptions.position(latLng);
//                markerOptions.title(addressText);
//
//                googleMap.addMarker(markerOptions);
//
//                // Locate the first location
//                if (i == 0)
//                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
//            }
//        }
//    }
//
//
//}
