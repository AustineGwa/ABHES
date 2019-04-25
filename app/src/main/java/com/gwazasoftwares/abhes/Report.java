package com.gwazasoftwares.abhes;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gwazasoftwares.abhes.models.Emergency;

import java.io.File;
import java.util.HashMap;

public class Report extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference myRef;
    StorageReference mStorage;
    ProgressDialog progressDialog;

    private static final int GALLERY_REQUEST = 1;

   private EditText title, description;
   Button submit;
   ImageButton imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        // Write a message to the database
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("emergencies");
        mStorage = FirebaseStorage.getInstance().getReference();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("upoading data...");
        progressDialog.setCanceledOnTouchOutside(false);

        title = findViewById(R.id.edtitle);
        description = findViewById(R.id.edDesc);
        imageLoader = findViewById(R.id.loadimg);
        submit = findViewById(R.id.btnSubmit);

    }

    @Override
    protected void onStart() {
        super.onStart();


        imageLoader.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Images not supported, feature coming soon  ", Toast.LENGTH_SHORT).show();
                //startGallery();
            }
        });

        submit.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                post();
            }
        });


    }



    private void post() {

        final String myTitle = title.getText().toString().trim();
        final String myDesc = description.getText().toString().trim();


        if (!TextUtils.isEmpty(myTitle) && !TextUtils.isEmpty(myDesc)) {
            progressDialog.show();
            DatabaseReference newPost = myRef.push();
            newPost.child("title").setValue(myTitle);
            newPost.child("description").setValue(myDesc);
            newPost.child("image").setValue(1);

        }else{
            Toast.makeText(getApplicationContext(), "Please fill the form", Toast.LENGTH_SHORT).show();
        }
    }


    public void postEmergency() {
        final String myTitle = title.getText().toString().trim();
        final String myDesc = description.getText().toString().trim();


        if (!TextUtils.isEmpty(myTitle) && !TextUtils.isEmpty(myDesc)) {

            Uri file = Uri.fromFile(new File("path/to/images/rivers.jpg"));
            final StorageReference imagesRef = mStorage.child("emergency_posted_images"+file.getLastPathSegment());
            imagesRef.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUri = imagesRef.getDownloadUrl().getResult();

                    DatabaseReference newPost = myRef.push();
                    newPost.child("title").setValue(myTitle);
                    newPost.child("description").setValue(myDesc);
                    newPost.child("image").setValue(Integer.parseInt(downloadUri.toString()));
                    Toast.makeText(getApplicationContext(), "upload succesfull", Toast.LENGTH_SHORT).show();

                }
            });



        }
    }

    public void startGallery () {

            // Create the Intent for Image Gallery.
            Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, GALLERY_REQUEST);
        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        // Here we need to check if the activity that was triggers was the Image Gallery.
        // If it is the requestCode will match the LOAD_IMAGE_RESULTS value.
        // If the resultCode is RESULT_OK and there is some data we know that an image was picked.
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK && data != null) {
            // Let's read picked image data - its URI
            Uri pickedImage = data.getData();
            // Let's read picked image path using content resolver
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

            // Now we need to set the GUI ImageView data with data read from the picked file.
            //Picasso.with(getBaseContext()).load(imagePath).fit().into(( pic));
           imageLoader.setImageBitmap(BitmapFactory.decodeFile(imagePath));

            // At the end remember to close the cursor or you will end with the RuntimeException!
            cursor.close();
        }
    }


}
