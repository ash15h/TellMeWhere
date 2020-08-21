package com.example.tellmewhere;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

public class CountryDataSource {

    public static final String COUNTRY_KEY = "country";
    public static final float MINIMUM_CONFIDENCE_LEVEL = 0.4f;
    public static final String DEFAULT_COUNTRY_NAME = "Madagascar";
    public static final double DEFAULT_COUNTRY_LATITUDE = 21.044295;
    public static final double DEFAULT_COUNTRY_LONGITUDE = 46.151819;
    public static final String DEFAULT_MESSAGE = "This is the Republic of Madagascar!";

    private Hashtable<String,String> countries_and_messages;

    public CountryDataSource(Hashtable<String,String> countries_and_messages)
    {
        this.countries_and_messages = countries_and_messages;
    }

    public String minimumConfidenceLevelOfUserWords(ArrayList<String> user_words,float[] confidence_levels)
    {
        if(user_words==null || confidence_levels==null)
        {
            return DEFAULT_COUNTRY_NAME;
        }

        int number_of_user_words = user_words.size();

        Enumeration<String> countries;

        for(int index = 0;index<number_of_user_words && index<confidence_levels.length;index++)
        {
            if(confidence_levels[index]<MINIMUM_CONFIDENCE_LEVEL)
            {
                break;
            }
            String accepted_user_word = user_words.get(index);

            countries = countries_and_messages.keys();

            while(countries.hasMoreElements())
            {
                String selected_country = countries.nextElement();

                if(accepted_user_word.equalsIgnoreCase(selected_country))
                {
                    return accepted_user_word;
                }
            }
        }
        return DEFAULT_COUNTRY_NAME;
    }

    public String getCountryInfo(String country)
    {
        return countries_and_messages.get(country);
    }

}
