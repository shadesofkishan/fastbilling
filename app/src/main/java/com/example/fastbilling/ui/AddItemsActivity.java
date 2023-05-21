package com.example.fastbilling.ui;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fastbilling.R;
import com.example.fastbilling.db.DBHandler;
import com.example.fastbilling.model.BillingItemModel;
import com.example.fastbilling.model.ItemsModel;
import com.example.fastbilling.services.CaptureAct;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddItemsActivity extends AppCompatActivity {

    EditText title, desc, price;
    TextView imei,imgNameTextView;
    RelativeLayout scanBtn, submitBtn;
    private Uri imageUri = null;
    private RelativeLayout uploadImage;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private File photoFile;
    private ArrayList<BillingItemModel> billingModels = new ArrayList<>();
    private String imgPath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_items);
        getSupportActionBar().hide();

        title = findViewById(R.id.title_edittext);
        desc = findViewById(R.id.descr_edittext);
        price = findViewById(R.id.price_edittext);
        imei = findViewById(R.id.imeiResult);
        imgNameTextView = findViewById(R.id.imgNameTextView);

        scanBtn = findViewById(R.id.scanitemButton);
        submitBtn = findViewById(R.id.additemButton);
        uploadImage = findViewById(R.id.uploadimgbtn);

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dispatchTakePictureIntent();

            }
        });


            submitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String gTitle, gDesc,gPrice,gImei,imgname;
                    gTitle = title.getText().toString();
                    gDesc = desc.getText().toString();
                    gPrice = price.getText().toString();
                    gImei = imei.getText().toString();
                    imgname = imgNameTextView.getText().toString();
                    ArrayList<ItemsModel> modalData = new ArrayList<>();

                    Cursor cursor=new DBHandler(AddItemsActivity.this).getData(gImei);
                    if (cursor != null) {
                        while (cursor.moveToNext()) {
                            ItemsModel model = new ItemsModel(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4),cursor.getString(5),cursor.getString(6));
                            modalData.add(model);
                        }
                    }

                    if (title.getText().toString().isEmpty())
                    {
                        Toast.makeText(getApplicationContext(),"Please enter Title !",Toast.LENGTH_SHORT).show();

                    }
                    else if (desc.getText().toString().isEmpty())
                    {
                        Toast.makeText(getApplicationContext(),"Please enter Description !",Toast.LENGTH_SHORT).show();
                    }
                    else if (price.getText().toString().isEmpty())
                    {
                        Toast.makeText(getApplicationContext(),"Please enter Price !",Toast.LENGTH_SHORT).show();
                    }
                    else if (imei.getText().toString().equals("---IMEI---"))
                    {
                        Toast.makeText(getApplicationContext(),"Please scan your Item !",Toast.LENGTH_SHORT).show();
                    }
                    else if (!modalData.isEmpty())
                    {
                        Toast.makeText(getApplicationContext(),"You have already scanned this item !",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        processInsert(gTitle,gDesc,gPrice,gImei,imgname,imgPath);
                    }

                }
            });

        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanCode();
            }
        });

    }

    private void processInsert(String ttl, String dsc, String pric, String imi, String imgName, String imgPath) {

        String res=new DBHandler(this).additems(ttl,dsc,pric,imi,imgName,imgPath);
        Toast.makeText(getApplicationContext(),res,Toast.LENGTH_LONG).show();
        title.setText("");
        desc.setText("");
        price.setText("");
        imei.setText("---IMEI---");
        imgNameTextView.setText("");
    }

    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result ->
    {
        if (result.getContents() != null)
        {


            AlertDialog.Builder builder = new AlertDialog.Builder(AddItemsActivity.this);
            builder.setTitle("Result");
            builder.setMessage(result.getContents());
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    imei.setText(result.getContents().toString());
                }
            }).show();

            builder.setNegativeButton("Try again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
        }
    });


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            try {
                createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private void createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        photoFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            ArrayList<ItemsModel> modalData = new ArrayList<>();

            if (photoFile != null)
            {
                imgNameTextView.setText(photoFile.getName());
                imgPath = photoFile.getPath();

            }

        }
    }

}