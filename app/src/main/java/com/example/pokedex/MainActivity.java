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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

    public static Activity act;
    public static TextView txtDisplay;
    public static ImageView imgPok;


    public static ImageView [] imgType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        act = this;
        imgType = new ImageView[2];

        txtDisplay = findViewById(R.id.txtDisplay);
        imgPok = findViewById(R.id.imgPok);
        imgType[0] = findViewById(R.id.imgType0);
        imgType[1] = findViewById(R.id.imgType1);

        String pokSearch = "1";
        fetchData process = new fetchData(pokSearch);
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
                String [] types = new String[fetchData.getAllExistingTypes().size()];
                for(int i=0; i<fetchData.getAllExistingTypes().size(); i++)
                    types[i] = fetchData.getAllExistingTypes().get(i);
                int size = fetchData.getAllExistingTypes().size();
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Pick a color");
                builder.setItems(types, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // the user clicked on colors[which
                        // ]
                    }
                });
                builder.show();
            }
        });

    }

    private void goToNext() throws JSONException {
        String pokSearch = fetchData.getId();
        int id=Integer.parseInt(pokSearch)+1;
        if(id >898)
            id=1;
        searchPokemon(Integer.toString(id));

    }

    private void goToPrevious() throws JSONException {
        String pokSearch = fetchData.getId();
        int id=Integer.parseInt(pokSearch)-1;
        if(id <=0)
            id=898;
        searchPokemon(Integer.toString(id));
    }



    public void showTxtSearch(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Search a Pokemon");

        final EditText input = new EditText(this);
        input.setHint("Pokemon");
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                searchPokemon(input.getText().toString());
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });
        alert.show();
    }

    public void searchPokemon(String pok){
        fetchData process = new fetchData(pok);
        process.execute();
    }
}