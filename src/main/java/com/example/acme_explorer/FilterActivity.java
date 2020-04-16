package com.example.acme_explorer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FilterActivity extends AppCompatActivity {

    public Calendar calendar = Calendar.getInstance();
    private ImageView startDateIcon, endDateIcon;
    private TextView textViewStartDate, textViewEndDate;
    private EditText minPrice, maxPrice;
    private Button saveAndExit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        Intent intent = getIntent();

        textViewStartDate = findViewById(R.id.textViewStartDate);
        textViewEndDate = findViewById(R.id.textViewEndDate);
        minPrice = (EditText)findViewById(R.id.editTextMinPrice);
        maxPrice = (EditText)findViewById(R.id.editTextMaxPrice);

        minPrice.setText(intent.getStringExtra("filter_minPrice"));
        maxPrice.setText(intent.getStringExtra("filter_maxPrice"));

        setStartDate(intent.getStringExtra("filter_startDate"));
        setEndDate(intent.getStringExtra("filter_endDate"));

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

        saveAndExit = findViewById(R.id.buttonSaveExit);
        saveAndExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("filter_minPrice", getMinPrice());
                intent.putExtra("filter_maxPrice", getMaxPrice());
                intent.putExtra("filter_startDate",getStartDate());
                intent.putExtra("filter_endDate",getEndDate());
                setResult(RESULT_OK,intent);
                finish();
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

    public Long getMinPrice() {
        if (minPrice.getText().length() <= 0){
            return Long.valueOf(0);
        } else {
            return Long.valueOf(String.valueOf(minPrice.getText()));
        }
    }

    public Long getMaxPrice() {
        if (maxPrice.getText().length() <= 0){
            return Long.valueOf(0);
        } else {
            return Long.valueOf(String.valueOf(maxPrice.getText()));
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
