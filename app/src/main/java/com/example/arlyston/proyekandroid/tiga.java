package com.example.arlyston.proyekandroid;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Arlyston on 05/11/2018.
 */

public class tiga extends AppCompatActivity{

    public Button button;
    public EditText et3;
    String time,nama,times,priority,dateTemp;
    Date tanggal;
    FirebaseDatabase database;
    DatabaseReference users;

    Calendar myCalendar = Calendar.getInstance();
    EditText et1,et2;
    RadioGroup group;
    RadioButton prior;
    int maxID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tiga);

        button = (Button) findViewById(R.id.but1);
        group = (RadioGroup) findViewById(R.id.priority);
        et3 = (EditText) findViewById(R.id.judul);
        et1= (EditText) findViewById(R.id.date);
        et2= (EditText) findViewById(R.id.time);



        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        et1.setOnClickListener(new View.OnClickListener() {//date

            @Override
            public void onClick(View v) {
                new DatePickerDialog(tiga.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        et2.setOnClickListener(new View.OnClickListener() {//time
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(tiga.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        DecimalFormat df = new DecimalFormat("00");

                        times = String.valueOf(df.format(hourOfDay)) + ":" + String.valueOf(df.format(minutes));
                        et2.setText(times);
                    }
                }, 0, 0, false);
                timePickerDialog.show();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {///////settttt
            @Override
            public void onClick(View v) {

                int selectedId = group.getCheckedRadioButtonId();
                prior = (RadioButton) findViewById(selectedId);
                priority=String.valueOf(prior.getText());
                nama = String.valueOf(et3.getText());
                dateTemp = String.valueOf(et1.getText() + " " + et2.getText());
                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy HH:mm");//string to date


                        try {
                            tanggal = formatter.parse(dateTemp);
//            System.out.println(date1);
//            System.out.println(formatter.format(date1));

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                time = String.valueOf(et2.getText());

//                maxID = 0;

//                if (MainActivity.mActivity.size()>0){
//                    for(int i=0; i<MainActivity.mActivity.size(); i++){
//                        if (MainActivity.mActivity.get(i).getId() > maxID)
//                            maxID = MainActivity.mActivity.get(i).getId();
//                    }
//                }

                database = FirebaseDatabase.getInstance();
                users = database.getReference("notif");

                if( TextUtils.isEmpty(et3.getText().toString())){et3.setError( "required!" );}
                else if( TextUtils.isEmpty(et1.getText().toString())){et1.setError( "required!" );}
                else if( TextUtils.isEmpty(et2.getText().toString())){et2.setError( "required!" );}
                else{
                    Toast.makeText(tiga.this,MainActivity.countBig+"",Toast.LENGTH_SHORT).show();
                    Log.d("my home","jhakhakjds");
                    final Activity user1 = new Activity(MainActivity.countBig + 1, nama, tanggal, priority, MainActivity.UName);//buat user baru,assign value =0
                    MainActivity.mActivity.addLast(user1);

                    users.child(String.valueOf(MainActivity.countBig+1)).setValue(user1); //max id setting ulang
                    MainActivity.countBig++;
                    onBackPressed();
                }
            }
        });
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        et1.setText(sdf.format(myCalendar.getTime()));
    }
}
