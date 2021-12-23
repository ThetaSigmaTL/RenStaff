package com.example.renstaff;

import androidx.appcompat.app.AppCompatActivity;


import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity{
    TextView textField;
    Button cancelButton;
    Button okButton;
    final int MENU_COLOR_RED = 1;
    final int MENU_COLOR_GREEN = 2;
    final int MENU_COLOR_BLUE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //sssd
//      textField =(TextView) findViewById(R.id.trainView);
//        registerForContextMenu(textField);


    }

//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        switch (v.getId()){
//            case R.id.trainView:
//                menu.add(0, MENU_COLOR_RED, 0,"Red");
//                menu.add(0, MENU_COLOR_GREEN, 0, "Green");
//                menu.add(0, MENU_COLOR_BLUE, 0, "Blue");
//                break;
//
//        }
//        super.onCreateContextMenu(menu, v, menuInfo);
//    }



}

