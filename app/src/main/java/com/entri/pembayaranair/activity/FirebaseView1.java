package com.entri.pembayaranair.activity;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.entri.pembayaranair.R;

public class FirebaseView1 extends RecyclerView.ViewHolder {

    TextView tvblok, tvname, tvmawal, tvmakhir, tvperiode, tvtotal_tagihan;

    public FirebaseView1(@NonNull View itemView){
        super(itemView);

        tvblok = itemView.findViewById(R.id.tvblok);
        tvname = itemView.findViewById(R.id.tvname);
        tvmawal = itemView.findViewById(R.id.tvmawal);
        tvmakhir = itemView.findViewById(R.id.tvmakhir);
        tvperiode = itemView.findViewById(R.id.tvperiode);
        tvtotal_tagihan = itemView.findViewById(R.id.tvtotal_tagihan);

    }
}
