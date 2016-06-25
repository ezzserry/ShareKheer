package net.AWstreams.ShareKhher.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import net.AWstreams.ShareKhher.R;
import net.AWstreams.ShareKhher.activities.BaseActivity;
import net.AWstreams.ShareKhher.models.City;
import net.AWstreams.ShareKhher.utils.ConnectionDetector;
import net.AWstreams.ShareKhher.utils.Constants;
import net.AWstreams.ShareKhher.utils.GPSTracker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 * Created by LENOVO on 07/06/2016.
 */
public class CreateEventFragment extends Fragment implements View.OnClickListener {

    // flag for Internet connection status
    Boolean isInternetPresent = false;
    // Connection detector class
    ConnectionDetector cd;
    ScrollView sScrollview;

    ImageView photo;
    TextView showmap;
    Button btnTake_capture_photo, btnUpload;
    View view;
    String userChoosenTask;
    CheckBox cbDonations, cbVolunteers, cbBoth;
    EditText etTitle, etDescription, etAddress, etPhonenumber;
    TextView etDate, etTime;
    String sTitle, sDescription, sAddress, sDate, sTime, sPhone;

    ProgressDialog loading = null;
    String latitude, longitude, eventLat, eventLng;
    GPSTracker gpsTracker;
    String sType = "";
    String sImagestring = "";
    private int mYear, mMonth, mDay, mHour, mMinute;
    RelativeLayout tiDate, tiTime;

    Spinner spCities, spAreas;
    //maps//
    LinearLayout mainlayout;
    String sSelectedCity, sSelectedArea;
    Typeface typefaceLight, typefaceBold;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Locale locale_ar = new Locale("ar");
        Locale.setDefault(locale_ar);
        Configuration config_ar = new Configuration();
        config_ar.locale = locale_ar;
        getActivity().getBaseContext().getResources().updateConfiguration(config_ar,
                getActivity().getBaseContext().getResources().getDisplayMetrics());

        view = inflater.inflate(R.layout.fragment_create_an_event, container, false);
        setRetainInstance(true);
        initViews();
        btnTake_capture_photo.setOnClickListener(this);
        view.setFocusableInTouchMode(true);
        view.requestFocus();

        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        // Toast.makeText(getActivity(), "Back Pressed",
                        // Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getActivity(), BaseActivity.class);
                        startActivity(i);
                        getActivity().finish();
                        return true;
                    }
                }
                return false;
            }
        });


        return view;
    }

    private void initViews() {

        mainlayout = (LinearLayout) view.findViewById(R.id.mainlayout);
//        showmap = (TextView) view.findViewById(R.id.address_et);
//        showmap.setOnClickListener(this);
        spCities = (Spinner) view.findViewById(R.id.cities_spinner);
        spAreas = (Spinner) view.findViewById(R.id.areas_spinner);

        spCities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                               @Override
                                               public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                   String sp1 = String.valueOf(spCities.getSelectedItem());
//                                                   Toast.makeText(getActivity(), sp1, Toast.LENGTH_SHORT).show();
                                                   if (sp1.contentEquals("القاهرة")) {
                                                       sSelectedCity = "cairo";
                                                       ArrayList<City> cityArrayList = new ArrayList<>();

                                                       cityArrayList.add(new City("el mokattam", "المقطم"));
                                                       cityArrayList.add(new City("nasr city", "مدينة نصر"));
                                                       cityArrayList.add(new City("shoubra", "شبرا"));
                                                       cityArrayList.add(new City("masr el gdeda", "مصر الجديدة"));
                                                       cityArrayList.add(new City("helioplis", "هليوبليس"));
                                                       cityArrayList.add(new City("maadi", "المعادي"));
                                                       cityArrayList.add(new City("new cairo", "القاهرة الجديدة"));
                                                       cityArrayList.add(new City("abbasia", "العباسية"));
                                                       cityArrayList.add(new City("gesr el suiz", "جسر السويس"));
                                                       cityArrayList.add(new City("tahrir", "التحرير"));
                                                       cityArrayList.add(new City("hadaek el kobba", "حدائق القبة"));
                                                       //fill data in spinner
                                                       ArrayAdapter<City> adapter = new ArrayAdapter<City>(getActivity(), android.R.layout.simple_spinner_dropdown_item, cityArrayList);
                                                       adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                       adapter.notifyDataSetChanged();
                                                       spAreas.setAdapter(adapter);

                                                   }
                                                   if (sp1.contentEquals("الجيزة")) {
                                                       sSelectedCity = "giza";
                                                       ArrayList<City> cityArrayList = new ArrayList<>();
                                                       cityArrayList.add(new City("dokki", "الدقي"));
                                                       cityArrayList.add(new City("haram", "الهرم"));
                                                       cityArrayList.add(new City("feisal", "فيصل"));
                                                       cityArrayList.add(new City("agouza", "العجوزة"));
                                                       cityArrayList.add(new City("mohandseen", "المهندسين"));
                                                       cityArrayList.add(new City("talbeya", "الطالبية"));

                                                       ArrayAdapter<City> adapter = new ArrayAdapter<City>(getActivity(), android.R.layout.simple_spinner_dropdown_item, cityArrayList);
                                                       adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                       adapter.notifyDataSetChanged();
                                                       spAreas.setAdapter(adapter);
                                                   }
                                                   if (sp1.contentEquals("الاسكندرية")) {
                                                       sSelectedCity = "alex";
                                                       ArrayList<City> cityArrayList = new ArrayList<>();
                                                       cityArrayList.add(new City("somouha", "سموحة"));
                                                       cityArrayList.add(new City("sidi gaber", "سيدي جابر"));
                                                       cityArrayList.add(new City("moharram bek", "محرم بك"));
                                                       cityArrayList.add(new City("sporting", "سبورتنج"));
                                                       cityArrayList.add(new City("azarita", "الازاريطة"));
                                                       cityArrayList.add(new City("abo kir", "ابو قير"));


                                                       ArrayAdapter<City> adapter = new ArrayAdapter<City>(getActivity(), android.R.layout.simple_spinner_dropdown_item, cityArrayList);
                                                       adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                       adapter.notifyDataSetChanged();
                                                       spAreas.setAdapter(adapter);
                                                   }

                                               }

                                               @Override
                                               public void onNothingSelected(AdapterView<?> parent) {

                                               }
                                           }
        );
        spAreas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                City city = (City) parent.getSelectedItem();
                Toast.makeText(getActivity(), "arabic: " + city.getArabicCity() + ",  english : " + city.getEnglishCity(), Toast.LENGTH_SHORT).show();
                sSelectedArea = city.getEnglishCity();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sScrollview = (ScrollView) view.findViewById(R.id.scrollView_sv);

        loading = new ProgressDialog(getActivity());
        loading.setCancelable(false);
        loading.setMessage("جاري التحميل");
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        photo = (ImageView) view.findViewById(R.id.uploaded_iv);
        btnTake_capture_photo = (Button) view.findViewById(R.id.capture_take_photo_btn);
        btnUpload = (Button) view.findViewById(R.id.uploadevent_btn);
        btnUpload.setOnClickListener(this);

        etTitle = (EditText) view.findViewById(R.id.eventname_et);
        etDescription = (EditText) view.findViewById(R.id.eventdesc_et);
        etAddress = (EditText) view.findViewById(R.id.address_et);
        etPhonenumber = (EditText) view.findViewById(R.id.phone_et);
        etDate = (TextView) view.findViewById(R.id.date_et);
        etTime = (TextView) view.findViewById(R.id.time_et);
        typefaceLight = Typeface.createFromAsset(getActivity().getAssets(), Constants.FONT_REGULAR_AR);
        typefaceBold = Typeface.createFromAsset(getActivity().getAssets(), Constants.FONT_BOLD_AR);

//        etTitle.setTypeface(typefaceLight);
//        etDescription.setTypeface(typefaceLight);
//        etAddress.setTypeface(typefaceLight);
//        etPhonenumber.setTypeface(typefaceLight);
//        etDate.setTypeface(typefaceLight);
//        etTime.setTypeface(typefaceLight);
        btnTake_capture_photo.setTypeface(typefaceBold);
        btnUpload.setTypeface(typefaceBold);

        etDate.setOnClickListener(this);
        etTime.setOnClickListener(this);

        tiDate = (RelativeLayout) view.findViewById(R.id.date_form);
        tiTime = (RelativeLayout) view.findViewById(R.id.time_form);
        etDate.setOnClickListener(new View.OnClickListener()

                                  {
                                      @Override
                                      public void onClick(View v) {
                                          Calendar c = Calendar.getInstance();
                                          mYear = c.get(Calendar.YEAR);
                                          mMonth = c.get(Calendar.MONTH);
                                          mDay = c.get(Calendar.DAY_OF_MONTH);


                                          DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                                                  new DatePickerDialog.OnDateSetListener() {

                                                      @Override
                                                      public void onDateSet(DatePicker view, int year,
                                                                            int monthOfYear, int dayOfMonth) {

                                                          etDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                                          etDate.setError(null);
                                                      }
                                                  }, mYear, mMonth, mDay);
                                          datePickerDialog.show();
                                      }
                                  }

        );
        etTime.setOnClickListener(new View.OnClickListener()

                                  {
                                      @Override
                                      public void onClick(View v) {
                                          Calendar c = Calendar.getInstance();
                                          mHour = c.get(Calendar.HOUR_OF_DAY);
                                          mMinute = c.get(Calendar.MINUTE);

                                          // Launch Time Picker Dialog
                                          TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                                                  new TimePickerDialog.OnTimeSetListener() {

                                                      @Override
                                                      public void onTimeSet(TimePicker view, int hourOfDay,
                                                                            int minute) {

                                                          etTime.setText(hourOfDay + ":" + minute);
                                                          etTime.setError(null);
                                                      }
                                                  }, mHour, mMinute, false);
                                          timePickerDialog.show();
                                      }
                                  }

        );

        cbDonations = (CheckBox) view.findViewById(R.id.donations_cb);
        cbVolunteers = (CheckBox) view.findViewById(R.id.volunteers_cb);
        cbBoth = (CheckBox) view.findViewById(R.id.both_cb);
        cbBoth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()

                                          {
                                              @Override
                                              public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                  if (cbBoth.isChecked()) {
                                                      cbDonations.setChecked(false);
                                                      cbVolunteers.setChecked(false);
                                                      sType = "Both";
                                                  }
                                              }
                                          }

        );
        cbVolunteers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()

                                                {

                                                    @Override
                                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                        if (cbVolunteers.isChecked()) {
                                                            cbBoth.setChecked(false);
                                                            sType = "Volunteering";

                                                        }
                                                    }
                                                }

        );
        cbDonations.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()

                                               {

                                                   @Override
                                                   public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                                                       if (cbDonations.isChecked()) {
                                                           cbBoth.setChecked(false);
                                                           sType = "Donation";

                                                       }
                                                   }
                                               }

        );

    }


    private void selectImage() {

        final CharSequence[] items = {"التقط صورة", "اختر من المعرض",
                "إلغاء"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("إضافة صورة!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, final int item) {
                if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
                    Dexter.checkPermission(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse response) {
                            if (items[item].equals("التقط صورة")) {
                                userChoosenTask = "Take Photo";
                                cameraIntent();
                            } else if (items[item].equals("اختر من المعرض")) {
                                userChoosenTask = "Choose from Library";
                                galleryIntent();
                            } else if (items[item].equals("إلغاء")) {
                                dialog.dismiss();
                            }
                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse response) {
                            Toast.makeText(getActivity(), "لقد قمت بالغاء المحاوله", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                        }
                    }, Manifest.permission.CAMERA);
                } else {
                    if (items[item].equals("التقط صورة")) {
                        userChoosenTask = "Take Photo";
                        cameraIntent();
                    } else if (items[item].equals("اختر من المعرض")) {
                        userChoosenTask = "Choose from Library";
                        galleryIntent();
                    } else if (items[item].equals("إلغاء")) {
                        dialog.dismiss();
                    }
                }
            }
        });

        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), 2);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 2)
                onSelectFromGalleryResult(data);
            else if (requestCode == 1)
                onCaptureImageResult(data);

        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        sImagestring = getStringImage(thumbnail);
        photo.setImageBitmap(thumbnail);
        photo.setVisibility(View.VISIBLE);
    }

    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), data.getData());
                sImagestring = getStringImage(bm);
                photo.setImageBitmap(bm);
                photo.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.capture_take_photo_btn:
                selectImage();
                break;
            case R.id.uploadevent_btn:
                cd = new ConnectionDetector(getActivity());
                isInternetPresent = cd.isConnectingToInternet();
                gpsTracker = new GPSTracker(getActivity());

                if (isInternetPresent) {
                    if (eventValidation()) {
                        if (cbVolunteers.isChecked() && cbDonations.isChecked()) {
                            cbBoth.setChecked(true);
                            cbDonations.setChecked(false);
                            cbVolunteers.setChecked(false);
                            sType = "Both";
                        }
                        final CharSequence[] items = {"الحالي", "موقع اخر"};
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("اختيار الموقع");
                        builder.setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, final int item) {
                                if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
                                    Dexter.checkPermissions(new MultiplePermissionsListener() {
                                        @Override
                                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                                            if (items[item].equals("الحالي")) {
                                                if (gpsTracker.canGetLocation()) {
                                                    eventLat = String.valueOf(gpsTracker.getLatitude());
                                                    eventLng = String.valueOf(gpsTracker.getLongitude());
                                                    uploadEvent(eventLat, eventLng);
                                                } else
                                                    gpsTracker.showSettingsAlert();
                                            } else if (items[item].equals("موقع اخر")) {
                                                eventLat = "0.0";
                                                eventLng = "0.0";
                                                uploadEvent(eventLat, eventLng);
                                            }
                                        }

                                        @Override
                                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                                        }
                                    }, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION);
                                } else {
                                    if (items[item].equals("الحالي")) {
                                        if (gpsTracker.canGetLocation()) {
                                            eventLat = String.valueOf(gpsTracker.getLatitude());
                                            eventLng = String.valueOf(gpsTracker.getLongitude());
                                            uploadEvent(eventLat, eventLng);
                                        } else
                                            gpsTracker.showSettingsAlert();
                                    } else if (items[item].equals("موقع اخر")) {
                                        eventLat = "0.0";
                                        eventLng = "0.0";
                                        uploadEvent(eventLat, eventLng);
                                    }
                                }
                            }
                        });
                        builder.show();

                    }
                } else
                    Snackbar.make(sScrollview, "لا يوجد اتصال بالانترنت", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                break;

        }
    }


    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private boolean eventValidation() {

        sTitle = etTitle.getText().toString();
        sDescription = etDescription.getText().toString();
        sAddress = etAddress.getText().toString();
        sPhone = etPhonenumber.getText().toString();
        sDate = etDate.getText().toString();
        sTime = etTime.getText().toString();
        Boolean validationFlag = true;

        if (sTitle.isEmpty()) {
            etTitle.setError("ادخل اسم الحدث");
            validationFlag = false;
        }
        if (sDescription.isEmpty()) {
            etDescription.setError("ادخل وصف الحدث");

            validationFlag = false;
        }
        if (sAddress.isEmpty()) {
            etAddress.setError("ادخل مكان الحدث");
            validationFlag = false;

        }
        if (sDate.isEmpty()) {
            etDate.setError("اختر تاريخ الحدث");
            validationFlag = false;

        }
        if (sTime.isEmpty()) {
            etTime.setError("اختر وقت الحدث");
            validationFlag = false;

        }
        if (!cbVolunteers.isChecked() && !cbDonations.isChecked() && !cbBoth.isChecked()) {
            Toast.makeText(getActivity(), "اختر نوع الحدث", Toast.LENGTH_LONG).show();
            validationFlag = false;

        }

        if (!isValidPhoneNumber(sPhone)) {
            etPhonenumber.setError("ادخل رقم الهاتف");

            validationFlag = false;
        }
        return validationFlag;

    }

    private void uploadEvent(String eventLat, String eventLng) {
        loading.show();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String Token = prefs.getString(Constants.User_Token, null);
        String User_ID = prefs.getString(Constants.KEY_USER_ID, null);

        String url = Constants.ADD_EVENT_SERVICE;
        Map<String, String> params = new HashMap();
        params.put(Constants.KEY_ACCESS_TOKEN, Token);
        params.put(Constants.KEY_EVENT_TITLE, sTitle);
        params.put(Constants.KEY_EVENT_DESCRIPTION, sDescription);
        params.put(Constants.KEY_EVENT_ADDRESS, sAddress);
        params.put(Constants.KEY_EVENT_LAT, eventLat);
        params.put(Constants.KEY_EVENT_LNG, eventLng);
        params.put(Constants.KEY_TYPE, sType);
        params.put(Constants.KEY_USER_ID, User_ID);
        params.put(Constants.KEY_COVER, sImagestring);
        params.put(Constants.KEY_Date, sDate);
        params.put(Constants.KEY_Time, sTime);
        params.put(Constants.KEY_CITY, sSelectedCity);
        params.put(Constants.KEY_AREA, sSelectedArea);
        params.put(Constants.KEY_PHONE, sPhone);


        JSONObject parameters = new JSONObject(params);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status_code = response.getString("code");
                            if (status_code.equals("CREATED")) {
                                loading.dismiss();
                                Intent intent = new Intent(getActivity(), BaseActivity.class);
                                getActivity().startActivity(intent);
                                getActivity().finish();
                            } else {
                                loading.dismiss();
                                Snackbar.make(sScrollview, "حدث خطأ", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Snackbar.make(sScrollview, "حدث خطأ", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();

                            loading.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Snackbar.make(sScrollview, "حدث خطأ", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        loading.dismiss();
                    }
                }
        ) {
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(getActivity()).add(request);


    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber.isEmpty() || phoneNumber.length() != 11 || !phoneNumber.startsWith("01")) {
            return false;
        } else
            return true;

    }

}