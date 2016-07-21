package com.together.stand;

import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.internal.view.menu.ListMenuItemView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.sql.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class ShowList extends ListActivity {

    ListView lv;
    ArrayAdapter arrayAdapter;
    String name[];
   // int diff[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println(LoggedIn.list.length);
        System.out.println(LoggedIn.list[0].pincode);
        name = new String[LoggedIn.list.length];
        //diff = new int[LoggedIn.list.length];
       /* for(int i=0; i<LoggedIn.list.length; ++i){
           //pincode[i] = new String();
            pincode[i] = LoggedIn.list[i].pincode;
           // diff[i] = Math.abs(Integer.parseInt(pincode[i]) - Integer.parseInt(LoggedIn.reqPin));
        }*/
      //  Arrays.sort(pincode, new PinCompare());
        Arrays.sort(LoggedIn.list, new ListCompare());
        for(int i=0; i<LoggedIn.list.length; ++i){
            name[i] = LoggedIn.list[i].name;
        }
        arrayAdapter = new ArrayAdapter(ShowList.this, android.R.layout.simple_list_item_1, name);
        //setContentView(R.layout.volunteer_list);
        setListAdapter(arrayAdapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int i, long id) {
        super.onListItemClick(l, v, i, id);
        String myInfo = LoggedIn.list[i].name + "\n" + LoggedIn.list[i].phone
                        + "\n" + LoggedIn.list[i].address +
                        "\nPIN: " + LoggedIn.list[i].pincode + "\n" +
                        LoggedIn.list[i].blood_group + LoggedIn.list[i].rh_factor;
        AlertDialog info = new AlertDialog.Builder(ShowList.this).create();
        info.setTitle("Volunteer Info");
        info.setMessage(myInfo);
        info.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        info.show();
    }

    class ListCompare implements Comparator<Volunteer>{

        @Override
        public int compare(Volunteer v1, Volunteer v2) {
            int first, second;
            int relative;
            first = Integer.parseInt(v1.pincode);
            second = Integer.parseInt(v2.pincode);
            int req = Integer.parseInt(LoggedIn.reqPin);
            relative = Math.abs(first - req) - Math.abs(second - req);
            return relative;
        }
    }
}
