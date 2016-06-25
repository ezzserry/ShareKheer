package net.AWstreams.ShareKhher.models;

/**
 * Created by LENOVO on 21/06/2016.
 */
public class City {

    String arabicCity;
    String englishCity;

    public City(String englishCity, String arabicCity) {
        this.englishCity = englishCity;
        this.arabicCity = arabicCity;
    }
    public City (String arabicCity){
        this.arabicCity = arabicCity;
    }


    public String getArabicCity() {
        return arabicCity;
    }

    public void setArabicCity(String arabicCity) {
        this.arabicCity = arabicCity;
    }

    public String getEnglishCity() {
        return englishCity;
    }

    public void setEnglishCity(String englishCity) {
        this.englishCity = englishCity;
    }

    //to display object as a string in spinner
    public String toString() {
        return arabicCity;
    }

}
