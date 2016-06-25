package net.AWstreams.ShareKhher.models;

import java.util.ArrayList;

/**
 * Created by LENOVO on 21/06/2016.
 */
public class CitiesList extends ArrayList<City> {


    public ArrayList<City> cairoArrayList = new ArrayList<City>();
    public ArrayList<City> gizaArrayList = new ArrayList<City>();
    public ArrayList<City> alexArrayList = new ArrayList<City>();


    public ArrayList<City> getcairoArrayList() {
        return cairoArrayList;
    }

    public void setcairoArrayList(ArrayList<City> cairoArrayList) {
        this.cairoArrayList = cairoArrayList;

    }

    public ArrayList<City> addcairoCities() {
        cairoArrayList.add(new City("el mokattam", "المقطم"));
        cairoArrayList.add(new City("nasr city", "مدينة نصر"));
        cairoArrayList.add(new City("shoubra", "شبرا"));
        cairoArrayList.add(new City("masr el gdeda", "مصر الجديدة"));
        cairoArrayList.add(new City("helioplis", "هليوبليس"));
        cairoArrayList.add(new City("maadi", "المعادي"));
        cairoArrayList.add(new City("new cairo", "القاهرة الجديدة"));
        cairoArrayList.add(new City("abbasia", "العباسية"));
        cairoArrayList.add(new City("gesr el suiz", "جسر السويس"));
        cairoArrayList.add(new City("tahrir", "التحرير"));
        cairoArrayList.add(new City("hadaek el kobba", "حدائق القبة"));

        return cairoArrayList;
    }

    public ArrayList<City> addalexCities() {
        alexArrayList.add(new City("somouha", "سموحة"));
        alexArrayList.add(new City("sidi gaber", "سيدي جابر"));
        alexArrayList.add(new City("moharram bek", "محرم بك"));
        alexArrayList.add(new City("sporting", "سبورتنج"));
        alexArrayList.add(new City("azarita", "الازاريطة"));
        alexArrayList.add(new City("abo kir", "ابو قير"));

        return alexArrayList;
    }

    public ArrayList<City> addgizaCities() {
        gizaArrayList.add(new City("dokki", "الدقي"));
        gizaArrayList.add(new City("haram", "الهرم"));
        gizaArrayList.add(new City("feisal", "فيصل"));
        gizaArrayList.add(new City("agouza", "العجوزة"));
        gizaArrayList.add(new City("mohandseen", "المهندسين"));
        gizaArrayList.add(new City("talbeya", "الطالبية"));
        return gizaArrayList;
    }

    public int getCairoListsize() {

        return cairoArrayList.size();
    }

    public int getGizaListsize() {

        return gizaArrayList.size();
    }

    public int getAlexListsize() {

        return alexArrayList.size();
    }

    public void setAlexArrayList(ArrayList<City> alexArrayList) {
        this.alexArrayList = alexArrayList;


    }

    public void setGizaArrayList(ArrayList<City> gizaArrayList) {
        this.gizaArrayList = gizaArrayList;

    }

    public ArrayList<City> getAlexArrayList() {
        return alexArrayList;
    }

    public ArrayList<City> getGizaArrayList() {
        return gizaArrayList;
    }
}
