package com.entri.pembayaranair.activity;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.entri.pembayaranair.R;

public class FirebaseView extends RecyclerView.ViewHolder {

    TextView tvblok, tvname, tvusername;

    public FirebaseView(@NonNull View itemView){
        super(itemView);

        tvblok = itemView.findViewById(R.id.tvblok);
        tvname = itemView.findViewById(R.id.tvname);
        tvusername = itemView.findViewById(R.id.tvusername);
    }
}
