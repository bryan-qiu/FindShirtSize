package com.example.bryan.findshirtsize;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class MyActivity extends AppCompatActivity {

    private static String logtag = "App";
    private static int TAKE_PICTURE = 1;
    private Uri imageUri;
    public final static String EXTRA_MESSAGE = "com.example.bryan.findshirtsize.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        Button cameraButton = (Button)findViewById(R.id.button_camera);
        cameraButton.setOnClickListener(cameraListener);
    }

    private View.OnClickListener cameraListener = new View.OnClickListener() {
        public void onClick(View v) {
            takePhoto(v);
        }
    };

    private void takePhoto(View v) {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        File photo = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"pic.jpg"); //currently overrides file
        imageUri = Uri.fromFile(photo);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri); // store camera output to where we put the file
        startActivityForResult(intent,TAKE_PICTURE);
    }

    @Override
    // override what happens when we use the camera
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        // if user clicks the ok button
        if (resultCode == Activity.RESULT_OK) {
            Uri selectedImage = imageUri;
            // notifies other applications of the content
            getContentResolver().notifyChange(selectedImage, null);

            ImageView imageView = (ImageView)findViewById(R.id.image_camera);
            ContentResolver cr = getContentResolver();

            Bitmap bitmap;

            try {
                /*bitmap = MediaStore.Images.Media.getBitmap(cr, imageUri);
                imageView.setImageBitmap(bitmap);
                Toast.makeText(MyActivity.this, selectedImage.toString(), Toast.LENGTH_LONG).show();*/
                sendMessage("hi");
            } catch(Exception e) {
                Log.e(logtag, e.toString());
            }
        }
    }

    public void sendMessage(String s) {
        Intent intent = new Intent(this, ResultActivity.class);
        String message = s;
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my, menu);
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
