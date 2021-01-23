package com.example.pokedex;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static java.lang.String.valueOf;

public class MainActivity extends AppCompatActivity{

    public static Activity act;
    public static TextView txtDisplay;
    public static ImageView imgPok;
    public static TextView idDisplay;
    public static String pickedType="";
    public static ImageView [] imgType;
    public static String pokeTemp;
    public static ArrayList<String>ids=new ArrayList<String>();
    public static ArrayList<String>pokemonsOfType=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        act = this;
        imgType = new ImageView[2];

        txtDisplay = findViewById(R.id.txtDisplay);
        idDisplay = findViewById(R.id.idTextView);
        imgPok = findViewById(R.id.imgPok);
        imgType[0] = findViewById(R.id.imgType0);
        imgType[1] = findViewById(R.id.imgType1);


        String pokSearch = "1";
        for(int i=0; i<898; i++){
            ids.add(valueOf(i+1));
        }
        fetchData process = new fetchData(pokSearch, false);
        process.execute();
        Button btnRight = findViewById(R.id.btnRight);
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    goToNext();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Button btnUp = findViewById(R.id.btnUp);
        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    goToPrevious();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Button btnDown = findViewById(R.id.btnDown);
        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    goToNext();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Button btnLeft = findViewById(R.id.btnLeft);
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    goToPrevious();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        ImageButton btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showTxtSearch();
            }
        });

        ImageButton btnTypes = findViewById(R.id.btnTypes);
        btnTypes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String [] types = getResources().getStringArray(R.array.types);
                AlertDialog.Builder pickTypeDialog = new AlertDialog.Builder(MainActivity.this);
                pickTypeDialog.setTitle("Pick a type");
                pickTypeDialog.setItems(types, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pickedType=types[which];
                        searchPokemon(pickedType, true);
                        searchPokemon(ids.get(0), false);
                    }
                });
                pickTypeDialog.show();
            }

        });

    }

    private void goToNext() throws JSONException {
        String pokSearch = fetchData.getName();
        int index;
        String pokSearchId = fetchData.getId();
        boolean found = false;
        if(ids.size()==898) {
            int id=Integer.parseInt(pokSearchId);
            id++;
            if (id ==899)
                id = 1;
            pokSearch=valueOf(id);
        }
        else
            for(int i=0; i<ids.size()&&!found; i++){
                if(ids.get(i).equals(pokSearch))
                {
                    found = true;
                    index = i+1;
                    if(index==ids.size())
                        index=0;
                    pokSearch = ids.get(index);
                }
            }

        searchPokemon(pokSearch, false);
    }

    public static String getPickedType(){
        return pickedType;
    }

    private void goToPrevious() throws JSONException {
        String pokSearch = fetchData.getName();
        int index;
        boolean found = false;
        if(ids.size()==898){
            index=Integer.parseInt(fetchData.getId())-1;
            if(index==1)
                index=898;
            pokSearch=ids.get(index-1);
        }
        else
            for(int i=0; i<ids.size()&&!found; i++){
                if(ids.get(i).equals(pokSearch))
                {
                    found = true;
                    index = i-1;
                    if(index<0)
                        index=ids.size()-1;
                    pokSearch = ids.get(index);
                }
            }
        searchPokemon(pokSearch, false);
    }



    public void showTxtSearch(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Search a Pokemon");

        final EditText input = new EditText(this);
        input.setHint("Pokemon");
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                searchPokemon(input.getText().toString(), false);
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });
        alert.show();
    }

    public void searchPokemon(String pok, boolean isType){
        fetchData process = new fetchData(pok, isType);
        process.execute();
    }


    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}