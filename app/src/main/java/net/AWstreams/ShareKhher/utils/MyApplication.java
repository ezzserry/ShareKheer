package net.AWstreams.ShareKhher.utils;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Build;
import android.support.multidex.MultiDexApplication;
import android.text.style.TypefaceSpan;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.karumi.dexter.Dexter;

import java.util.Locale;

/**
 * Created by LENOVO on 29/05/2016.
 */
public class MyApplication extends MultiDexApplication {
    private Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize the SDK before executing any other operations,
//        FacebookSdk.sdkInitialize(getApplicationContext());
//        AppEventsLogger.activateApp(this);
        if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            Dexter.initialize(getApplicationContext());
        }
        Locale locale_ar = new Locale("ar");
        Locale.setDefault(locale_ar);
        Configuration config_ar = new Configuration();
        config_ar.locale = locale_ar;
        getBaseContext().getResources().updateConfiguration(config_ar,
                getBaseContext().getResources().getDisplayMetrics());
//        tfRegular = Typeface.createFromAsset(mContext.getAssets(), Constants.FONT_REGULAR);
//        tfBold = Typeface.createFromAsset(mContext.getAssets(), Constants.FONT_Bold);
//        TypefaceUtil.overrideBold(mContext, "SERIF", Constants.FONT_REGULAR_AR);

    }
}
