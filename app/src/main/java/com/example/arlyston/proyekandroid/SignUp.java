package com.example.arlyston.proyekandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    public static FirebaseDatabase database;
    public static DatabaseReference pengguna;

    EditText et1,et2;
    String username,password;
    TextView tv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        database = FirebaseDatabase.getInstance();
        pengguna = database.getReference("user");
        et1 = (EditText) findViewById(R.id.user);
        et2 = (EditText) findViewById(R.id.pass);

        tv1 = (TextView) findViewById(R.id.login);

        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), Login.class);
                startActivity(i);
                finish();
            }
        });
    }

    public void signup(View view){

        if( TextUtils.isEmpty(et2.getText().toString())){et2.setError( "required!" );}
        else if( TextUtils.isEmpty(et1.getText().toString())){et1.setError( "required!" );}
        else {
            username = String.valueOf(et1.getText());
            password = String.valueOf(et2.getText());

            final User baru = new User(username, password);//buat user baru,assign value =0
            pengguna.child(username).setValue(baru);

            Intent i = new Intent(getBaseContext(), Login.class);///////////////////////////////////////////
            startActivity(i);
            finish();
        }
    }
}
