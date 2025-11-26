package gcu.awarno300.currency;

import android.util.Log;

import java.util.Locale;

public class CurrencyExchange {
    private final String category; //Converted from
    private final String foreign; //Converted to
    private final float exchangeRate;

    public CurrencyExchange(String input)
    {

        // Split items into their own section
        String[] items = input.split(",");

        //Extract just the converted items coin code
        String[] category = items[0].split("/");
        this.category = category[0].substring(category[0].length()-4, category[0].length()-1);
        this.foreign = category[1].substring(category[1].length()-4, category[1].length()-1);

        //Extract the rate from the items list
        String[] description = items[4].split("=");
        String[] rates = description[1].split(" ");
        this.exchangeRate = Float.parseFloat(rates[1]);
    }

    public float exchange(float in)
    {
        return in*exchangeRate;
    }

    public String getCategory()
    {
        return this.category;
    }

    public String getForeign()
    {
        return this.foreign;
    }

    public float getRate()
    {
        return this.exchangeRate;
    }


}
