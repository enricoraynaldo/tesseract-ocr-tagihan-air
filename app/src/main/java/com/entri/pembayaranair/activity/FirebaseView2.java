package com.entri.pembayaranair.activity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.entri.pembayaranair.R;

public class FirebaseView2 extends RecyclerView.ViewHolder  {

    TextView tvblok, tvimageurl, tvperiode, tvstatus;
    Button bttes, btvalidasi;

    public FirebaseView2(@NonNull View itemView){
        super(itemView);

        tvblok = itemView.findViewById(R.id.tvblok);
        tvimageurl = itemView.findViewById(R.id.tvimageurl);
        tvperiode = itemView.findViewById(R.id.tvperiode);
        bttes = itemView.findViewById(R.id.bttes);
        btvalidasi = itemView.findViewById(R.id.btvalid);
        tvstatus = itemView.findViewById(R.id.tvstatus);

    }
}


