package com.entri.pembayaranair.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.entri.pembayaranair.R;
import com.entri.pembayaranair.session.SessionManager;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class DataStatusActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    DatabaseReference reference;
    private FirebaseRecyclerOptions<StatusData> options;
    private FirebaseRecyclerAdapter<StatusData, FirebaseView2> adapter;
    SessionManager session;
    TextView tvstatus;

    Button btvalidasi, bttes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_status);


        session = new SessionManager(getApplicationContext());

        HashMap<String, String> user = session.getUserDetails();
        btvalidasi = findViewById(R.id.btvalid);
        recyclerView = findViewById(R.id.recyclerview);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        reference = FirebaseDatabase.getInstance().getReference().child("bukti_pembayaran");
        reference.keepSynced(true);

        options = new FirebaseRecyclerOptions.Builder<StatusData>().setQuery(reference, StatusData.class).build();
        adapter = new FirebaseRecyclerAdapter<StatusData, FirebaseView2>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final FirebaseView2 holder, int position, @NonNull final StatusData model) {
                holder.tvblok.setText(model.getBlok());
                holder.tvperiode.setText(model.getPeriode());

                final String inURL = model.getImageUrl();

                holder.bttes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openWebURL(inURL);
                    }
                });

                final String blok = model.getBlok();
                final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("data_pelanggan");


                ref.child(blok).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String stat = snapshot.child("status").getValue(String.class);
                        holder.tvstatus.setText(stat);

                        if (snapshot.child("status").getValue().equals("Belum")) {
                            holder.btvalidasi.setVisibility(View.VISIBLE);
                            holder.btvalidasi.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    AlertDialog alertDialog = new AlertDialog.Builder(DataStatusActivity.this)
                                            .setTitle("Anda yakin ingin validasi data?")
                                            .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    final String blok = model.getBlok();
                                                    final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("data_pelanggan");
                                                    ref.child(blok).addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            String status = "Sudah";
                                                            String totalbiaya = "0";
                                                            ref.child(blok).child("status").setValue(status);
                                                            ref.child(blok).child("totalbiaya").setValue(totalbiaya);
                                                            holder.btvalidasi.setEnabled(false);

                                                            final String periode = snapshot.child("periodeterakhir").getValue(String.class);
                                                            DatabaseReference r = FirebaseDatabase.getInstance().getReference("data_tagihan");

                                                            r.child(periode).child(blok).addValueEventListener(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                    String mawal = snapshot.child("mawal").getValue(String.class);
                                                                    String makhir = snapshot.child("makhir").getValue(String.class);
                                                                    String total_tagihan = snapshot.child("total_tagihan").getValue(String.class);

                                                                    DatabaseReference r0 = FirebaseDatabase.getInstance().getReference("data_transaksi");
                                                                    r0.child(blok).child(periode).child("mawal").setValue(mawal);
                                                                    r0.child(blok).child(periode).child("makhir").setValue(makhir);
                                                                    r0.child(blok).child(periode).child("total_tagihan").setValue(total_tagihan);
                                                                    r0.child(blok).child(periode).child("blok").setValue(blok);
                                                                    r0.child(blok).child(periode).child("periode").setValue(periode);

                                                                    DatabaseReference rr = FirebaseDatabase.getInstance().getReference("data_pembayaran");
                                                                    rr.child(periode).child(blok).child("mawal").setValue(mawal);
                                                                    rr.child(periode).child(blok).child("makhir").setValue(makhir);
                                                                    rr.child(periode).child(blok).child("total_tagihan").setValue(total_tagihan);
                                                                    rr.child(periode).child(blok).child("blok").setValue(blok);
                                                                    rr.child(periode).child(blok).child("periode").setValue(periode);

                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                }
                                                            });
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });

                                                    Toast.makeText(DataStatusActivity.this, "Data berhasil divalidasi!", Toast.LENGTH_LONG).show();
                                                }
                                            })
                                            .setNegativeButton("Tidak", null)
                                            .create();
                                    alertDialog.show();
                                }
                            });

                        }
                        else {
                            holder.btvalidasi.setVisibility(View.GONE);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @NonNull
            @Override
            public FirebaseView2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_layout2, parent, false);
                return new FirebaseView2(v);
            }
        };

        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    ;
    //setupToolbar();

    public void openWebURL(String inURL) {
        Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse(inURL));

        startActivity(browse);
    }
}


  /*  private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.tbHistory);
        toolbar.setTitle("Database Pelanggan");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

