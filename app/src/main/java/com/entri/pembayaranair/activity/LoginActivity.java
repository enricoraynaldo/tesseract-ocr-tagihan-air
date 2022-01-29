package com.entri.pembayaranair.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.entri.pembayaranair.R;
import com.entri.pembayaranair.adapter.AlertDialogManager;
import com.entri.pembayaranair.session.SessionManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class LoginActivity extends AppCompatActivity {

    TextInputLayout logUsername, logPassword;
    Button btnLogin, btnRegister, btnGanti, btnRegadm;
    AlertDialogManager alert = new AlertDialogManager();
    SessionManager session;

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        session = new SessionManager(getApplicationContext());

        logUsername = findViewById(R.id.username);
        logPassword = findViewById(R.id.password);

        btnLogin = findViewById(R.id.masuk);
        btnGanti = findViewById(R.id.ganti);
        btnRegister = findViewById(R.id.ke_daftar);
        btnRegadm = findViewById(R.id.reg_adm);

        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (!validateUsername() | !validatePassword()) {
                    return;
                } else {
                    isUser();
                }
            }
        });

    }

    private Boolean validateUsername() {
        String val = logUsername.getEditText().getText().toString();
        if (val.isEmpty()) {
            logUsername.setError("Username tidak boleh kosong");
            return false;
        } else {
            logUsername.setError(null);
            logUsername.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword() {
        String val = logPassword.getEditText().getText().toString();
        if (val.isEmpty()) {
            logPassword.setError("Password tidak boleh kosong");
            return false;
        } else {
            logPassword.setError(null);
            logPassword.setErrorEnabled(false);
            return true;
        }
    }

    private void isUser() {
        final String userEnteredUsername = logUsername.getEditText().getText().toString().trim();
        final String userEnteredPassword = logPassword.getEditText().getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("tb_admin");

        Query checkUser = reference.orderByChild("username").equalTo(userEnteredUsername);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    logUsername.setError(null);
                    logUsername.setErrorEnabled(false);

                    String passwordFromDB = snapshot.child(userEnteredUsername).child("password").getValue(String.class);
                    if (userEnteredPassword.equals(passwordFromDB)) {

                        logUsername.setError(null);
                        logUsername.setErrorEnabled(false);

                        String nama_adminFromDB = snapshot.child(userEnteredUsername).child("nama_admin").getValue(String.class);
                        final String usernameFromDB = snapshot.child(userEnteredUsername).child("username").getValue(String.class);
                        String no_adminFromDB = snapshot.child(userEnteredUsername).child("no_admin").getValue(String.class);

                        FirebaseInstanceId.getInstance().getInstanceId()
                                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                        if (task.isSuccessful()) {
                                            String token = task.getResult().getToken();
                                            DatabaseReference r = FirebaseDatabase.getInstance().getReference("id_token_admin");
                                            r.child(usernameFromDB).child("token").setValue(token);
                                            r.child(usernameFromDB).child("username").setValue(usernameFromDB);

                                        }
                                    }
                                });

                        session.createLoginSession(usernameFromDB);
                        finish();

                        Intent i = new Intent(getApplicationContext(), MainActivity.class);

                        i.putExtra("nama_admin", nama_adminFromDB);
                        i.putExtra("username", usernameFromDB);
                        i.putExtra("no_admin", no_adminFromDB);

                        startActivity(i);

                    } else {
                        logPassword.setError("Wrong Password");
                        logPassword.requestFocus();
                    }
                } else {
                    logUsername.setError("Akun tidak ditemukan");
                    logUsername.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public static void setWindowFlag(Activity activity, final int bits, boolean on) {

        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage("Apakah Anda ingin keluar dari aplikasi?");
        builder.setCancelable(true);
        builder.setNegativeButton(getString(R.string.batal), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton(getString(R.string.keluar), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}