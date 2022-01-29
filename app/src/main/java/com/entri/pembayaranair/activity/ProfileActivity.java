package com.entri.pembayaranair.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.entri.pembayaranair.R;
import com.entri.pembayaranair.session.SessionManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {

    protected Cursor cursor;
    TextView lblName, lblUsername, lblNohp;
    SessionManager session;
    String username;
    Button tentang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        session = new SessionManager(getApplicationContext());

        HashMap<String, String> user = session.getUserDetails();
        username = user.get(SessionManager.KEY_EMAIL);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("tb_admin");
        Query checkUser = reference.orderByChild("username").equalTo(username);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String nama_adminFromDB = snapshot.child(username).child("nama_admin").getValue(String.class);
                String usernameFromDB = snapshot.child(username).child("username").getValue(String.class);
                String no_adminFromDB = snapshot.child(username).child("no_admin").getValue(String.class);

                lblName = findViewById(R.id.lblName);
                lblUsername = findViewById(R.id.lblUsername);
                lblNohp = findViewById(R.id.lblNohp);

                lblName.setText(nama_adminFromDB);
                lblUsername.setText(username);
                lblNohp.setText(no_adminFromDB);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });


        //showAllUserData();

        aboutUs();

    }

    private void aboutUs(){
        tentang = (Button) findViewById(R.id.bt_tentang);
        tentang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ttg = new Intent(ProfileActivity.this, AboutUsActivity.class);
                startActivity(ttg);
            }
        });
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.tbProfile);
        toolbar.setTitle("Identitas Admin");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}