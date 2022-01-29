package com.entri.pembayaranair.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.entri.pembayaranair.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DataTagihanActivity extends AppCompatActivity {

    Button btCari;
    TextInputLayout cekBulan;

    private RecyclerView recyclerView;


    DatabaseReference reference;
    private FirebaseRecyclerOptions<TagihanData> options;
    private FirebaseRecyclerAdapter<TagihanData,FirebaseView1> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_tagihan);
        btCari = findViewById(R.id.bt_cari);
        cekBulan = findViewById(R.id.cek_bulan);

        btCari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cekBulan.getEditText().length()>0) {
                    String bulan = cekBulan.getEditText().getText().toString().trim();
                    recyclerView = findViewById(R.id.recyclerview);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(DataTagihanActivity.this));

                    reference = FirebaseDatabase.getInstance().getReference().child("data_tagihan").child(bulan);
                    reference.keepSynced(true);

                    options = new FirebaseRecyclerOptions.Builder<TagihanData>().setQuery(reference,TagihanData.class).build();
                    adapter = new FirebaseRecyclerAdapter<TagihanData, FirebaseView1>(options) {
                        @Override
                        protected void onBindViewHolder(@NonNull FirebaseView1 holder, int position, @NonNull TagihanData model) {
                            holder.tvblok.setText(model.getBlok());
                            holder.tvname.setText(model.getName());
                            holder.tvmawal.setText(model.getMawal());
                            holder.tvmakhir.setText(model.getMakhir());
                            holder.tvperiode.setText(model.getPeriode());
                            holder.tvtotal_tagihan.setText(model.getTotal_tagihan());
                        }

                        @NonNull
                        @Override
                        public FirebaseView1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_layout1,parent,false);
                            return new FirebaseView1(v);

                        }
                    };
                    adapter.startListening();
                    recyclerView.setAdapter(adapter);

                }

            }
        });

    };

    //setupToolbar();
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

