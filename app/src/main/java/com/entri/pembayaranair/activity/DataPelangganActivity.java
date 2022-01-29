package com.entri.pembayaranair.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.entri.pembayaranair.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DataPelangganActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    DatabaseReference reference;
    private FirebaseRecyclerOptions<UserData> options;
    private FirebaseRecyclerAdapter<UserData,FirebaseView> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_pelanggan);

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        reference = FirebaseDatabase.getInstance().getReference().child("tb_user");
        reference.keepSynced(true);

        options = new FirebaseRecyclerOptions.Builder<UserData>().setQuery(reference,UserData.class).build();
        adapter = new FirebaseRecyclerAdapter<UserData, FirebaseView>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FirebaseView holder, int position, @NonNull UserData model) {
                holder.tvblok.setText(model.getBlok());
                holder.tvname.setText(model.getName());
                holder.tvusername.setText(model.getUsername());
            }

            @NonNull
            @Override
            public FirebaseView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_layout,parent,false);
                return new FirebaseView(v);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
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

