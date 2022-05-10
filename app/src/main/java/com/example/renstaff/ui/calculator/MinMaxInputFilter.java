package com.example.renstaff.ui.calculator;

import android.text.Spanned;

public class MinMaxInputFilter implements android.text.InputFilter {

    private int mIntMin, mIntMax ;
    public MinMaxInputFilter (int minValue, int maxValue) {
        this.mIntMin = minValue;
        this.mIntMax = maxValue;
    }
    public MinMaxInputFilter (String minValue, String maxValue) {
        this.mIntMin = Integer. parseInt (minValue);
        this.mIntMax = Integer. parseInt (maxValue);
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try {
            String newVal = dest.subSequence(0, dstart)
                    + source.subSequence(start, end).toString()
                    + dest.subSequence(dend, dest.length());
            int input = Integer.parseInt(newVal);
            if (isInRange(mIntMin, mIntMax, input))
                return null;
        } catch (NumberFormatException nfe) { }
        return "";
    }
    private boolean isInRange ( int a, int b, int c) {
        return b > a ? c >= a && c <= b : c >= b && c <= a ;
    }
}
