package com.example.arlyston.proyekandroid;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;

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

public class Edit extends AppCompatActivity {
    public Button button;
    public EditText et1,et2,et3;
    int mPosition;
    String mTitle,mDate,mTime,mPriority;
    private String title,time,times,priorit,tedaTemp;
    private Date teda;
    RadioGroup group;
    RadioButton lo,med,hi,groupbtn;
    FirebaseDatabase database;
    DatabaseReference users;
    Calendar myCalendar = Calendar.getInstance();
    private String userna;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit);

        button = (Button) findViewById(R.id.but1);
        et1 = (EditText) findViewById(R.id.judul);
        et2 = (EditText) findViewById(R.id.tanggal);
        et3 = (EditText) findViewById(R.id.jam);
        group = (RadioGroup) findViewById(R.id.priority);
        lo = (RadioButton) findViewById(R.id.low);
        med = (RadioButton) findViewById(R.id.medium);
        hi = (RadioButton) findViewById(R.id.high);

        database = FirebaseDatabase.getInstance();
        users = database.getReference("notif");

        Bundle extras = getIntent().getExtras();
        if(extras !=null)
        {
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
            SimpleDateFormat formatter2 = new SimpleDateFormat("MM/dd/yy");
            mPosition = extras.getInt("mPosition");
            mTitle = extras.getString("mTitle");
            mDate = formatter2.format(extras.get("mDate"));
            mPriority = extras.getString("mPriority");
            mTime = formatter.format(extras.get("mDate"));
            //int selectedId = group.getCheckedRadioButtonId();
            //groupbtn = (RadioButton) findViewById(selectedId);

            et1.setText(mTitle);
            et2.setText(mDate);
            et3.setText(mTime);

            if(mPriority.equals("Low Priority")){
                lo.setChecked(true);
            }
            else if(mPriority.equals("Medium Priority")){
                med.setChecked(true);
            }
            else if(mPriority.equals("High Priority")){
                hi.setChecked(true);
            }

        }

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

        et2.setOnClickListener(new View.OnClickListener() {//date

            @Override
            public void onClick(View v) {
                new DatePickerDialog(Edit.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        et3.setOnClickListener(new View.OnClickListener() {//time
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(Edit.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        DecimalFormat df = new DecimalFormat("00");

                        times = String.valueOf(df.format(hourOfDay)) + ":" + String.valueOf(df.format(minutes));
                        et3.setText(times);
                    }
                }, 0, 0, false);
                timePickerDialog.show();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                title = String.valueOf(et1.getText());
                tedaTemp = String.valueOf(et2.getText()+ " " + et3.getText());
                //time = String.valueOf(et3.getText());

                        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy HH:mm");//string to date

                        try {
                            teda = formatter.parse(tedaTemp);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }



                group = (RadioGroup) findViewById(R.id.prior);
                int selectedId = group.getCheckedRadioButtonId();
                groupbtn = (RadioButton) findViewById(selectedId);
                priorit = String.valueOf(groupbtn.getText());
                userna = MainActivity.UName;





                if( TextUtils.isEmpty(et1.getText().toString())){et1.setError( "Required!" );}
                else if( TextUtils.isEmpty(et2.getText().toString())){et2.setError( "Required!" );}
                else if( TextUtils.isEmpty(et3.getText().toString())){et3.setError( "Required!" );}
                else{
                    final Activity user1 = new Activity(mPosition+1, title, teda,priorit,userna);
                    users.child(String.valueOf(mPosition+1)).setValue(user1);
                    onBackPressed();
                }

                MainActivity.mActivity.set(mPosition, new Activity(mPosition,title,teda,priorit,userna));
                MainActivity.mAdapter.notifyDataSetChanged();
                onBackPressed();



            }
        });

    }
    private void updateLabel() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        et2.setText(sdf.format(myCalendar.getTime()));
    }

}
