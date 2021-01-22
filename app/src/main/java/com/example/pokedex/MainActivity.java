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

public class MainActivity extends AppCompatActivity{

    public static Activity act;
    public static TextView txtDisplay;
    public static ImageView imgPok;
    public static TextView idDisplay;
    public static String pickedType="";
    public static ImageView [] imgType;
    public static ArrayList<Integer>ids=new ArrayList<Integer>();
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
        for(int i=0; i<898; i++)
            ids.add(i+1);
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
                    }
                });
                pickTypeDialog.show();
            }
        });

    }

    private void goToNext() throws JSONException {
        String pokSearch = fetchData.getId();
        int id=Integer.parseInt(pokSearch);
        if(ids.size()==898) {
            id++;
            if (id ==899)
                id = 1;
        }
        else
        {
            int index;
            boolean found = false;
            for(int i=0; i<ids.size()&&!found; i++){
                if(ids.get(i).equals(pokSearch))
                {
                    found = true;
                    index = i;
                    if(index==ids.size()-1)
                        index=1;
                    id = ids.get(index-1);
                }
            }
        }
        searchPokemon(Integer.toString(id), false);
    }

    public static String getPickedType(){
        return pickedType;
    }

    private void goToPrevious() throws JSONException {
        String pokSearch = fetchData.getId();
        int id=Integer.parseInt(pokSearch);
        if(ids.size()==898) {
            id--;
            if (id <= 0)
                id = 898;
        }
        else
        {
            int index;
            boolean found = false;
            for(int i=0; i<ids.size()&&!found; i++){
                if(ids.get(i).equals(pokSearch))
                {
                    found = true;
                    index = i;
                    if(index<0)
                        index=ids.size();
                    id = ids.get(index-1);
                }
            }
        }
        searchPokemon(Integer.toString(id), false);
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
}