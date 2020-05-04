package com.example.acme_explorer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.acme_explorer.entity.Trip;
import com.example.acme_explorer.services.FirestoreService;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;

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
    private EditText etTitle, etImageURL, etDescription, etPrice;

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


        saveButton = findViewById(R.id.formTrip_save_button);


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
                            Float.parseFloat(String.valueOf(etPrice.getText()))
                    );

                    FirestoreService.getServiceInstance().saveTrip(trip, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            if (databaseError == null) {
                                Log.i("MasterITS", "Trip insertado");
                                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                startActivity(intent);
                            } else {
                                Log.e("MasterITS", "Error al insertar Trip : " + databaseError.getMessage());
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
}
