package net.AWstreams.ShareKhher.utils;

import net.AWstreams.ShareKhher.models.City;

import java.util.ArrayList;

/**
 * Created by LENOVO on 01/06/2016.
 */
public class Constants {
    public static String URL_BASE_SERVICE = "http://178.62.88.11:3000/v1/";
    public static String REGISTER_SERVICE = URL_BASE_SERVICE + "auth/signup/";
    public static String Sign_in_SERVICE = URL_BASE_SERVICE + "auth/signin/";
    public static String ADD_EVENT_SERVICE = URL_BASE_SERVICE + "events";
    public static String GET_ALL_EVENTS = URL_BASE_SERVICE + "events?access_token=";
    public static String GET_MY_EVENTS = URL_BASE_SERVICE + "events?user_id=%s&access_token=%s";
    public static String GET_FILTER_EVENTS = URL_BASE_SERVICE + "events?type=%s&access_token=%s";
    public static String GET_FILTER_EVENTS_LOCATION = URL_BASE_SERVICE + "events?area=%s&access_token=%s&type=%s";
    public static String GET_PIC = "http://178.62.88.11:3000/v1/events/file/";


    public static String Facebook_Username = "fb_username";
    public static String User_Token = "token";
    public static String User_name = "username";
    public static String User_email = "useremail";
    public static String User_phone = "phone";

    public static String KEY_ACCESS_TOKEN = "access_token";
    public static String KEY_EVENT_TITLE = "title";
    public static String KEY_EVENT_DESCRIPTION = "description";
    public static String KEY_TYPE = "type";
    public static String KEY_EVENT_ADDRESS = "address";
    public static String KEY_EVENT_LNG = "lng";
    public static String KEY_EVENT_LAT = "lat";
    public static String KEY_USER_ID = "user_id";
    public static String KEY_COVER = "cover";
    public static String KEY_Date = "date";
    public static String KEY_Time = "time";
    public static String KEY_CITY = "city";
    public static String KEY_AREA = "area";
    public static String KEY_PHONE = "phone";


    public static String isLoggedin = "isLogged";
    public static String FILTER_TYPE = "type";
    public static String FILTER_LOCATION = "area";

    public static String FONT_REGULAR_AR = "GE_SS_Text_Medium.otf";
    public static String FONT_BOLD_AR = "GE_SS_Two_Bold.otf";




}
