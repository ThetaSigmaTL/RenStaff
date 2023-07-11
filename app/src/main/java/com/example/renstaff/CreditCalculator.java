package com.example.renstaff;
import android.util.JsonReader;

import androidx.appcompat.app.AppCompatActivity;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import io.grpc.internal.JsonParser;


public class CreditCalculator {

    private HashMap <String, Double> creditOptions = null;

    public Credit calculateCredit (int term, BigDecimal goodsSum, boolean insurance, boolean sms,
                                   boolean lawyer, boolean dateChanger, boolean creditHistory){
        BigDecimal creditSum;
        BigDecimal servicesSum;
        return null;
    }
    private HashMap<String, Double> readBankProductsJson()
    {
        try {
            Object object = new JSONParser().parse(new FileReader("bank-products.json"));
            JSONObject jo = (JSONObject) object;
            JSONArray rates  = (JSONArray) jo.get("interestRates");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
