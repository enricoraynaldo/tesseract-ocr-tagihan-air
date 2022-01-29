package com.entri.pembayaranair.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.entri.pembayaranair.R;
import com.entri.pembayaranair.adapter.AlertDialogManager;
import com.entri.pembayaranair.session.SessionManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    AlertDialogManager alert = new AlertDialogManager();
    SessionManager session;
    Button btnLogout;

    TextView lblName, lblUsername, lblNohp;

    String username, name, nohp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = new SessionManager(getApplicationContext());
        session.checkLogin();

        lblName = findViewById(R.id.lblName);
        lblUsername = findViewById(R.id.lblUsername);
        lblNohp = findViewById(R.id.lblNohp);

        HashMap<String, String> user = session.getUserDetails();
        username = user.get(SessionManager.KEY_EMAIL);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("tb_admin");
        Query checkUser = reference.orderByChild("username").equalTo(username);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    name = snapshot.child(username).child("nama_admin").getValue(String.class);
                    nohp = snapshot.child(username).child("no_admin").getValue(String.class);

                    lblName.setText(name);
                    lblUsername.setText(username);
                    lblNohp.setText(nohp);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnLogout = findViewById(R.id.out);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Anda yakin ingin keluar ?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                session.logoutUser();
                            }
                        })
                        .setNegativeButton("Tidak", null)
                        .create();
                dialog.show();
            }
        });
    }

    public void profileMenu(View v) {

        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
    }

    public void validasiPembayaran(View v) {
        Intent i = new Intent(this, DataStatusActivity.class);
        startActivity(i);
    }

    public void updatePelanggan(View v) {
        Intent i = new Intent(this, UpdatePelangganActivity.class);
        startActivity(i);
    }

    public void showData(View v) {
        Intent i = new Intent(this, DataPelangganActivity.class);
        startActivity(i);
    }

}
