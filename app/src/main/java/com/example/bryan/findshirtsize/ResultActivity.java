package com.example.bryan.findshirtsize;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResultActivity extends AppCompatActivity {

    //uh.... this is only temporary.... supposed to be using database ^^
    double num [][] = {{35, 37, 40, 43, 46,
            34, 36, 38, 40, 42,
            30, 34, 38, 42, 46,
            34, 36, 38, 40, 43,
            30, 32, 34, 36, 38,
            32.5, 36.5, 38.5, 43,
            31.875, 35, 38.25, 41.375, 44.5,
            39.4, 41, 42.6, 45.6, 48.8,
            35, 37.5, 41, 44, 48.5,
            31, 34, 37, 40, 44, 48,
            34, 38, 42, 46, 50,
            31.5, 34.5, 37.75, 41, 44,
            34, 36, 38, 41, 45,
            32, 35, 38, 41, 44,
            34, 36, 39, 42, 45,
            35, 37, 39, 41, 43,
            34, 38, 42, 46, 50},
            {0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0,
                    28, 30, 32, 33, 36,
                    0, 0, 0, 0, 0,
                    23, 25, 27, 29, 31,
                    27, 32, 34, 39,
                    26, 29.125, 32.25, 35.375, 38.5,
                    29, 32, 35, 38, 43,
                    27, 30, 32, 35, 39, 43,
                    28, 30, 34, 38, 42,
                    26.75, 30, 33, 36.25, 39.25,
                    28, 29, 32, 34, 36,
                    26, 29, 32, 35, 38,
                    27, 29, 31, 33, 35,
                    28, 31, 35, 40, 43},
            {0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0,
                    33, 35, 37, 39, 41,
                    0, 0, 0, 0, 0,
                    33.5, 36.625, 39.75, 42.875, 46,
                    0, 0, 0, 0, 0,
                    35, 37.5, 41, 44, 47,
                    32, 35, 37, 40, 44, 48,
                    0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0}};


    String size [][] = {{"American Eagle", "American Eagle", "American Eagle", "American Eagle", "American Eagle",
            "Hollister", "Hollister", "Hollister", "Hollister", "Hollister",
            "American Apparel", "American Apparel", "American Apparel", "American Apparel", "American Apparel",
            "Abercrombie and Fitch", "Abercrombie and Fitch", "Abercrombie and Fitch", "Abercrombie and Fitch", "Abercrombie and Fitch",
            "Bench", "Bench", "Bench", "Bench", "Bench",
            "Gildan", "Gildan", "Gildan", "Gildan",
            "Levi's", "Levi's", "Levi's", "Levi's", "Levi's",
            "Jack and Jones", "Jack and Jones", "Jack and Jones", "Jack and Jones", "Jack and Jones",
            "Nike", "Nike", "Nike", "Nike", "Nike",
            "Adidas", "Adidas", "Adidas", "Adidas", "Adidas", "Adidas",
            "Under Armour", "Under Armour", "Under Armour", "Under Armour", "Under Armour",
            "H & M", "H & M", "H & M", "H & M", "H & M",
            "Banana Republic", "Banana Republic", "Banana Republic", "Banana Republic", "Banana Republic",
            "J Crew", "J Crew", "J Crew", "J Crew", "J Crew",
            "Gap", "Gap", "Gap", "Gap", "Gap",
            "Club Monaco", "Club Monaco", "Club Monaco", "Club Monaco", "Club Monaco",
            "Ralph Lauren", "Ralph Lauren", "Ralph Lauren", "Ralph Lauren", "Ralph Lauren"},
            {"XS", "S", "M", "L", "XL",
                    "XS", "S", "M", "L", "XL",
                    "XS", "S", "M", "L", "XL",
                    "XS", "S", "M", "L", "XL",
                    "XS", "S", "M", "L", "XL",
                    "S", "M", "L", "XL",
                    "XS", "S", "M", "L", "XL",
                    "S", "M", "L", "XL", "XXL",
                    "S", "M", "L", "XL", "XXL",
                    "XS", "S", "M", "L", "XL", "2XL",
                    "S", "M", "L", "XL", "XXL",
                    "XS", "S", "M", "L", "XL",
                    "XS", "S", "M", "L", "XL",
                    "XS", "S", "M", "L", "XL",
                    "XS", "S", "M", "L", "XL",
                    "XS", "S", "M", "L", "XL",
                    "S", "M", "L", "XL", "XXL"}};



    private ArrayAdapter<String> listAdapter ;

    DatabaseAdapter db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Intent intent = getIntent();
        //String message = intent.getStringExtra(MyActivity.EXTRA_MESSAGE);

        int chest_size = intent.getIntExtra(InputActivity.CHEST_SIZE,0);
        int waist_size = intent.getIntExtra(InputActivity.WAIST_SIZE,0);
        int hips_size = intent.getIntExtra(InputActivity.HIPS_SIZE,0);


        // Create the text view
        /*TextView textView = new TextView(this);
        textView.setTextSize(40);
        if (chest_size == 33)
            textView.setText("hi");

        // Set the text view as the activity layout
        setContentView(textView);*/

        // Find the ListView resource.
        ListView mainListView = (ListView) findViewById(R.id.list );

        //list view example
        /*String[] planets = new String[] { "Mercury", "Venus", "Earth", "Mars",
                "Jupiter", "Saturn", "Uranus", "Neptune"};
        ArrayList<String> planetList = new ArrayList<String>();
        planetList.addAll( Arrays.asList(planets) );*/


        listAdapter = new ArrayAdapter<String>(this,R.layout.simplerow);


        for (int i = 0; i < 70; i++) {
            if (((double)chest_size >= num[0][i] && (double)chest_size < num[0][i+1] ||
                    (double)chest_size >= num[0][i] && num[0][i] > num[0][i+1])
                &&
                    ((double)waist_size >= num[1][i] && (double)waist_size < num[1][i+1] ||
                            (double)waist_size >= num[1][i] && num[1][i] > num[1][i+1])) {
                listAdapter.add(size[0][i] + " " + size[1][i]);
            }
        }

        mainListView.setAdapter( listAdapter );

        // fully implement database later
      /*  db = new DatabaseAdapter(this);
        db.createShirt(38,"American Eagle","L");


        List<String> s = db.searchShirts(38);
        for (int i = 0; i < s.size(); i++) {
            Log.i("Database", "Loop through search results: " + s.get(i));
            //Toast.makeText(ResultActivity.this,s.get(i),Toast.LENGTH_LONG);
        }*/
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}
