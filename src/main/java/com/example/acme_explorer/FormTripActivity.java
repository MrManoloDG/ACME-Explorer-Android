package com.example.acme_explorer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.acme_explorer.entity.Trip;
import com.example.acme_explorer.services.FirestoreService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FormTripActivity extends AppCompatActivity {

    public Calendar calendar = Calendar.getInstance();
    private ImageView startDateIcon, endDateIcon;
    private TextView textViewStartDate, textViewEndDate;
    private Button saveButton;
    private EditText etTitle, etImageURL, etDescription, etPrice, etLatitude, etLongitude;


    private static final int CAMERA_PERMISSION_REQUEST = 0x512;
    private static final int WRITE_EXTERNAL_PERMISSION_REQUEST = 0x513;
    private static final int TAKE_PHOTO_CODE = 0x514 ;

    private Button takePictureButton;
    private ImageView takePictureImageView;

    private String urlImage;

    private String file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_trip);

        textViewStartDate = findViewById(R.id.textViewStartDate);
        textViewEndDate = findViewById(R.id.textViewEndDate);

        etTitle = findViewById(R.id.formTrip_title);
        etImageURL = findViewById(R.id.formTrip_imageURL);
        etDescription =findViewById(R.id.formTrip_description);
        etPrice = findViewById(R.id.formTrip_price);
        etLatitude = findViewById(R.id.formTrip_latitude);
        etLongitude = findViewById(R.id.formTrip_longitude);


        saveButton = findViewById(R.id.formTrip_save_button);

        takePictureButton = findViewById(R.id.take_picture_button);
        takePictureImageView = findViewById(R.id.take_picture_image);


        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });

        etImageURL.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Glide.with(FormTripActivity.this)
                        .load(etImageURL.getText())
                        .placeholder(R.drawable.ic_launcher_background)
                        .centerCrop()
                        .into(takePictureImageView);
            }
        });

        startDateIcon = findViewById(R.id.imageViewStartDate);
        startDateIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date(v, R.id.textViewStartDate);
            }
        });
        endDateIcon = findViewById(R.id.imageViewEndDate);
        endDateIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date(v, R.id.textViewEndDate);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(getStartDate() == Long.valueOf(0)|| getEndDate() == Long.valueOf(0) || String.valueOf(etTitle.getText()) == "" ){
                    Snackbar.make(v , getString(R.string.date_title_empty_error), Snackbar.LENGTH_LONG).show();
                } else {
                    Trip trip = new Trip(
                            0,
                            String.valueOf(etTitle.getText()),
                            String.valueOf(etDescription.getText()),
                            String.valueOf(etImageURL.getText()),
                            new Date(getStartDate()),
                            new Date(getEndDate()),
                            Float.parseFloat(String.valueOf(etPrice.getText())),
                            Float.parseFloat(String.valueOf(etLatitude.getText())),
                            Float.parseFloat(String.valueOf(etLongitude.getText()))
                    );

                    FirestoreService.getServiceInstance().saveTrip(trip, new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()) {
                                Log.i("MasterITS", "Trip insertado");
                                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                startActivity(intent);
                            } else {
                                Log.e("MasterITS", "Error al insertar Trip ");
                            }
                        }
                    });
                }
            }
        });


    }


    public void setStartDate(String value) {
        System.out.println("**************" + value);
        if(!value.equals("0")) {
            Date d = new Date();
            d.setTime(Long.valueOf(value));
            String pattern = "dd/MM/yyyy";
            DateFormat df = new SimpleDateFormat(pattern);
            textViewStartDate.setText(df.format(d));
        } else {
            textViewStartDate.setText("dd/mm/yyyy");
        }
    }

    public void setEndDate(String value) {
        if(!value.equals("0")) {
            Date d = new Date();
            d.setTime(Long.valueOf(value));
            String pattern = "dd/MM/yyyy";
            DateFormat df = new SimpleDateFormat(pattern);
            textViewEndDate.setText(df.format(d));
        } else {
            textViewEndDate.setText("dd/mm/yyyy");
        }
    }

    public Long getStartDate(){
        if( (String) textViewStartDate.getText() == "dd/mm/yyyy"){
            return Long.valueOf(0);
        } else {
            try {
                return new SimpleDateFormat("dd/MM/yyyy").parse((String) textViewStartDate.getText()).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
                return Long.valueOf(0);
            }
        }
    }

    public Long getEndDate(){
        if((String) textViewEndDate.getText() == "dd/mm/yyyy"){
            return Long.valueOf(0);
        } else {
            try {
                return new SimpleDateFormat("dd/MM/yyyy").parse((String) textViewEndDate.getText()).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
                return Long.valueOf(0);
            }
        }
    }

    public void date(View view,int textViewID) {
        int yy=calendar.get(Calendar.YEAR);
        int mm=calendar.get(Calendar.MONTH);
        int dd=calendar.get(Calendar.DAY_OF_MONTH);
        final TextView textViewDate = findViewById(textViewID);
        DatePickerDialog dialog=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                textViewDate.setText(day+"/"+(month+1)+"/"+year);
            }
        },yy,mm,dd);
        dialog.show();
    }


    private void takePicture() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,  Manifest.permission.CAMERA)){
                Snackbar.make(takePictureButton, getString(R.string.take_picture_camara_rationale), BaseTransientBottomBar.LENGTH_LONG).setAction(getString(R.string.take_picture_camara_rationale_ok), click -> {
                    ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST);
                });
            } else {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST);
            }
        } else {
            // Permiso concedido

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,  Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    Snackbar.make(takePictureButton, getString(R.string.take_picture_camara_rationale), BaseTransientBottomBar.LENGTH_LONG).setAction(getString(R.string.take_picture_camara_rationale_ok), click -> {
                        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_PERMISSION_REQUEST);
                    });
                } else {
                    ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_PERMISSION_REQUEST);
                }
            } else {

                // Eliminar politica de privacidad de archivos android
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());

                String dir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/masterits";
                File newFile = new File(dir);
                newFile.mkdirs();

                file = dir + "/" + Calendar.getInstance().getTimeInMillis() + ".jpg";
                File newFilePicture = new File(file);
                try {
                    newFilePicture.createNewFile();
                } catch (Exception ignore) {
                }

                Uri outputFileDir = Uri.fromFile(newFilePicture);
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileDir);
                startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TAKE_PHOTO_CODE && resultCode == RESULT_OK) {
            File filePicture = new File(file);

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReference().child("users").child(FirebaseAuth.getInstance().getUid()).child(filePicture.getName());
            UploadTask uploadTask = storageReference.putFile(Uri.fromFile(filePicture));
            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        Log.i("MasterITS", "Firebase Storage: Completed  " + task.getResult().getTotalByteCount());

                        storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if(task.isSuccessful()) {
                                    urlImage = task.getResult().toString();
                                    etImageURL.setText(task.getResult().toString());
                                    Glide.with(FormTripActivity.this)
                                            .load(task.getResult())
                                            .placeholder(R.drawable.ic_launcher_background)
                                            .centerCrop()
                                            .into(takePictureImageView);
                                    //takePictureImageView.setImageURI( task.getResult());
                                }
                            }
                        });
                    }
                }
            });

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i("MasterITS", "Firebase Storage error: " + e.getMessage() );
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_PERMISSION_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
                    takePicture();
                } else {
                    Toast.makeText(this, R.string.camera_not_granted , Toast.LENGTH_SHORT).show();
                }
                break;

            case WRITE_EXTERNAL_PERMISSION_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
                    takePicture();
                } else {
                    Toast.makeText(this, R.string.write_external_not_granted , Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
