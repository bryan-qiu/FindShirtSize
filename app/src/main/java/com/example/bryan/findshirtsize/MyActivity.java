package com.example.bryan.findshirtsize;

import android.annotation.TargetApi;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import cz.msebera.android.httpclient.Header;

public class MyActivity extends AppCompatActivity {
    private Uri imageUri;
    public final static String EXTRA_MESSAGE = "com.example.bryan.findshirtsize.MESSAGE";

    private static final String BASE_URL = "http://192.168.56.1/AndroidFileUpload/fileUpload.php";
    private AsyncHttpClient mClient;
    private AsyncHttpResponseHandler mResponseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        Button cameraButton = (Button) findViewById(R.id.button_camera);
        cameraButton.setOnClickListener(cameraListener);

        mClient = new AsyncHttpClient();
        mResponseHandler = new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Toast.makeText(MyActivity.this, "YAYYY", Toast.LENGTH_SHORT).show();
                Log.e("hihihi", "IT WORKED");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(MyActivity.this, "DIDN'T WORK :(", Toast.LENGTH_SHORT).show();
                Log.e("hihihi", "IT WORKED jk");
            }
        };
    }

    private void uploadImage() {
        File file = new File(getImageRealPathFromUri(imageUri));
        RequestParams params = new RequestParams();

        try {
            params.put("image", file);
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

        CursorLoader cursorLoader = new CursorLoader(
                this,
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
        openImages();
//        Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//        imageUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
//        i.putExtra(MediaStore.EXTRA_OUTPUT, imageUri); // set the image file name
//        startActivityForResult(i, TAKE_PICTURE);
    }

    @Override
    // override what happens when we use the camera
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        InputStream stream = null;
        if (requestCode == 1 && resultCode == RESULT_OK) {
            try {
                stream = getContentResolver().openInputStream(intent.getData());
                imageUri = intent.getData();
                uploadImage();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void openImages() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, 1);
    }

    private void submitImage() {
        File myFile = new File(getImageRealPathFromUri(imageUri));
        RequestParams params = new RequestParams();
        try {
            params.put("file", myFile, getMimeType(myFile.getAbsolutePath()));
            AsyncHttpClient client = new AsyncHttpClient();
            client.post(BASE_URL, params, mResponseHandler);
        } catch (FileNotFoundException e) {
            Toast.makeText(this, "Sorry, " + myFile.getName() + " cannot be found.", Toast.LENGTH_SHORT).show();
        }
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
