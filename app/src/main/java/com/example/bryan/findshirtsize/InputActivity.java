package com.example.bryan.findshirtsize;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class InputActivity extends AppCompatActivity {

    public final static String CHEST_SIZE = "com.example.bryan.findshirtsize.CHEST_SIZE";
    public final static String WAIST_SIZE = "com.example.bryan.findshirtsize.WAIST_SIZE";
    public final static String HIPS_SIZE = "com.example.bryan.findshirtsize.HIPS_SIZE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        setContentView(R.layout.activity_input);
    }

    public void sendData(View v) {
        EditText chest = (EditText) findViewById(R.id.chest_size);
        int chest_size = 0;

        // doesn't work... fix it later
        if (chest.getText().toString() != "") {
            chest_size = Integer.parseInt(chest.getText().toString());
        }

        EditText waist = (EditText) findViewById(R.id.waist_size);
        int waist_size = 0;

        if (waist.getText().toString() != "") {
            waist_size = Integer.parseInt(waist.getText().toString());
        }

        EditText hips = (EditText) findViewById(R.id.hips_size);
        int hips_size = 0;

        if (hips.getText().toString() != "") {
            hips_size = Integer.parseInt(hips.getText().toString());
        }

        Intent intent = new Intent(this,ResultActivity.class);
        intent.putExtra(CHEST_SIZE,chest_size);
        intent.putExtra(WAIST_SIZE,waist_size);
        intent.putExtra(HIPS_SIZE,hips_size);

        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_input, menu);
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
    }
}
