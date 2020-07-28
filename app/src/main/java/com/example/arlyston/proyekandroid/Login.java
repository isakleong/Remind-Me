package com.example.arlyston.proyekandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Login extends AppCompatActivity {

    EditText et1,et2;
    TextView tv1;
    public static FirebaseDatabase database;
    public static DatabaseReference pengguna;
    String username,password;
    String Uname,Pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        database = FirebaseDatabase.getInstance();
        pengguna = database.getReference("user");
        et1 = (EditText) findViewById(R.id.user);
        et2 = (EditText) findViewById(R.id.pass);
        tv1 = (TextView) findViewById(R.id.daftar);

        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), SignUp.class);
                startActivity(i);
                finish();
            }
        });
    }

    public void login(View view) {

        if (TextUtils.isEmpty(et2.getText().toString())) {
            et2.setError("required!");
        } else if (TextUtils.isEmpty(et1.getText().toString())) {
            et1.setError("required!");
        } else {

            username = String.valueOf(et1.getText());
            password = String.valueOf(et2.getText());
            pengguna.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User login = dataSnapshot.child(username).getValue(User.class);
                    if (login != null) {
                        Uname = login.getUsername();
                        Pwd = login.getPassword();
                        Log.d("my tag", "ketemu");
                        if (password.equals(Pwd)) {
                            Intent i = new Intent(getBaseContext(), MainActivity.class);
                            i.putExtra("Uname", Uname);
                            startActivity(i);
                            
                        } else {
                            Toast.makeText(Login.this, "Password Salah", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Login.this, "Username Tidak Ditemukan", Toast.LENGTH_SHORT).show();
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d("my tag", "tdk ketemu");
                }


            });
        }
    }



}
