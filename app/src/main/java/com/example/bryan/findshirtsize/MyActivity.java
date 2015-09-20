package com.example.bryan.findshirtsize;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;

import cz.msebera.android.httpclient.Header;

public class MyActivity extends AppCompatActivity {

    private static String logtag = "App";
    private static int TAKE_PICTURE = 1;
    private Uri imageUri;
    public final static String EXTRA_MESSAGE = "com.example.bryan.findshirtsize.MESSAGE";

    private static final String BASE_URL = "http://192.168.56.1/AndroidFileUpload/fileUpload.php";
    private AsyncHttpClient mClient;
    private AsyncHttpResponseHandler mResponseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        Button cameraButton = (Button)findViewById(R.id.button_camera);
        cameraButton.setOnClickListener(cameraListener);

        mClient = new AsyncHttpClient();
        mResponseHandler = new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Toast.makeText(MyActivity.this, "YAYYY", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(MyActivity.this, "DIDN'T WORK :(", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void uploadImage() {
        File file = new File(getImageRealPathFromUri(imageUri));
        RequestParams params = new RequestParams();

        try {
            params.put("file", file, getMimeType(file.getAbsolutePath()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        mClient.post(BASE_URL, params, mResponseHandler);
    }

    public String getImageRealPathFromUri(Uri uri) {
        String filePath = "";
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return getImageRealPathFromUriCompat(uri);
        } else {
            String wholeID = DocumentsContract.getDocumentId(uri);

            // Split at colon, use second item in the array
            String id = wholeID.split(":")[1];

            String[] column = {MediaStore.Images.Media.DATA};

            // where id is equal to
            String sel = MediaStore.Images.Media._ID + "=?";

            Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    column, sel, new String[]{id}, null);

            int columnIndex = cursor.getColumnIndex(column[0]);

            if (cursor.moveToFirst()) {
                filePath = cursor.getString(columnIndex);
            }
            cursor.close();
            return filePath;
        }
    }

    // And to convert the image URI to the direct file system path of the image file
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public String getImageRealPathFromUriCompat(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        String result = null;

        CursorLoader cursorLoader = new CursorLoader(this,
                contentUri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        if (cursor != null) {
            int column_index =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            result = cursor.getString(column_index);
        }
        return result;
    }

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
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
            //imageUri = intent.getData();

            try {
//                mBitmap = MediaStore.Images.Media.getBitmap(cr, imageUri);
                //uploadImage();
//                imageView.setImageBitmap(bitmap);
//                Toast.makeText(MyActivity.this, selectedImage.toString(), Toast.LENGTH_LONG).show();
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
