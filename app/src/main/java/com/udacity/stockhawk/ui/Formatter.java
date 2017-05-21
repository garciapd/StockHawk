package com.udacity.stockhawk.ui;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by danielgarciaperez on 21/05/2017.
 */

public class Formatter {
    private static DecimalFormat dollarFormatWithPlus;
    private static DecimalFormat dollarFormat;
    private static DecimalFormat percentageFormat;

    public static DecimalFormat dollarFormatWithPlus(){
        if(dollarFormatWithPlus == null){
            dollarFormatWithPlus = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
            dollarFormatWithPlus.setPositivePrefix("+$");
        }

        return dollarFormatWithPlus;
    }

    public static DecimalFormat dollarFormat(){
        if(dollarFormat == null){
            dollarFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
        }

        return dollarFormat;
    }

    public static DecimalFormat percentageFormat(){
        if(percentageFormat == null){
            percentageFormat = (DecimalFormat) NumberFormat.getPercentInstance(Locale.getDefault());
            percentageFormat.setMaximumFractionDigits(2);
            percentageFormat.setMinimumFractionDigits(2);
            percentageFormat.setPositivePrefix("+");
        }

        return percentageFormat;
    }
}

