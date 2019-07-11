package com.example.roomfinder.views;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.roomfinder.R;
import com.example.roomfinder.model.Room;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;

public class CreateRoomActivity extends AppCompatActivity {
    private EditText edtTitle;
    private EditText edtRoomType;
    private EditText edtRoomRentPrice;

    private TextInputLayout titleTIL;
    private TextInputLayout typeTIL;
    private TextInputLayout rentPriceTIL;

    private ImageView roomImage;
    private Button chooseRoomImage;
    private Button addRoomAd;

    private File roomImageFile;
    private Toolbar toolbar;
    private KProgressHUD hud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_room);
        Nammu.init(getApplicationContext());

        findView();
        init();

    }

    private void findView() {
        toolbar = findViewById(R.id.toolbar);
        chooseRoomImage = findViewById(R.id.chooseImageButton);
        roomImage = findViewById(R.id.roomImage);

        edtTitle = findViewById(R.id.edtTitle);
        edtRoomRentPrice = findViewById(R.id.edtPrice);
        edtRoomType = findViewById(R.id.edtRoomType);

        titleTIL = findViewById(R.id.titleTIL);
        typeTIL = findViewById(R.id.roomTypeTIL);
        rentPriceTIL = findViewById(R.id.roomRentTIL);

        addRoomAd = findViewById(R.id.addPostButton);
    }

    private void init() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Nammu.askForPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionCallback() {
                @Override
                public void permissionGranted() {
                    //Nothing, this sample saves to Public gallery so it needs permission
                }

                @Override
                public void permissionRefused() {

                }
            });
        }
        EasyImage.configuration(this).setImagesFolderName(getString(R.string.app_name));

        chooseRoomImage.setOnClickListener(view -> EasyImage.openChooserWithGallery(this, "Room Image", 1000));


        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Loading...")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);

        addRoomAd.setOnClickListener(view -> {
            titleTIL.setError(null);
            typeTIL.setError(null);
            rentPriceTIL.setError(null);

            String title = edtTitle.getText().toString().trim();
            String type = edtRoomType.getText().toString().trim();
            String price = edtRoomRentPrice.getText().toString().trim();

            if (title == null || title.isEmpty()) {
                titleTIL.setError("Required");
                return;
            }

            if (type == null || type.isEmpty()) {
                typeTIL.setError("Required");
                return;
            }

            if (price == null || price.isEmpty()) {
                rentPriceTIL.setError("Required");
                return;
            }

            Room room = new Room();
            room.setName(title);
            room.setPrice(price);
            room.setType(type);
            room.setImage(roomImageFile.getName());

            hud.show();
            if (roomImage == null) {
                addRoom(room);
                return;
            }

            try {
                InputStream stream = new FileInputStream(roomImageFile);
                UploadTask uploadTask = FirebaseStorage.getInstance().getReference(roomImageFile.getName()).putStream(stream);
                uploadTask.addOnFailureListener(exception -> {
                    Toast.makeText(getApplicationContext(), "There an problem creating room post", Toast.LENGTH_SHORT).show();
                    hud.dismiss();
                }).addOnSuccessListener(taskSnapshot -> addRoom(room));

            } catch (FileNotFoundException e) {
                Toast.makeText(getApplicationContext(), "There an problem creating room post", Toast.LENGTH_SHORT).show();
                hud.dismiss();
            }

        });
    }

    private void addRoom(Room room) {
        DatabaseReference postsRef = FirebaseDatabase.getInstance()
                .getReference().child("Rooms");

        DatabaseReference newPostRef = postsRef.push();
        newPostRef.setValue(room)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) finish();
                    else
                        Toast.makeText(getApplicationContext(), "There an problem creating room post", Toast.LENGTH_SHORT).show();
                    hud.dismiss();
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Nammu.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                Toast.makeText(CreateRoomActivity.this, "Something went wrong while picking image! try again later.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                switch (type) {
                    case 1000:
                        roomImageFile = imageFile;
                        Glide.with(CreateRoomActivity.this)
                                .load(imageFile)
                                .centerCrop()
                                .into(roomImage);
                        break;
                }
            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                //Cancel handling, you might wanna remove taken photo if it was canceled
                if (source == EasyImage.ImageSource.CAMERA) {
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(CreateRoomActivity.this);
                    if (photoFile != null) photoFile.delete();
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EasyImage.clearConfiguration(this);
    }
}
