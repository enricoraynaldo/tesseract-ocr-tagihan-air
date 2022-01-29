package com.entri.pembayaranair.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.entri.pembayaranair.R;
import com.entri.pembayaranair.session.SessionManager;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;

import static com.entri.pembayaranair.R.id.tampil_nama;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;


public class UpdatePelangganActivity extends AppCompatActivity {

    protected Cursor cursor;
    SessionManager session;
    String email, periode, periodesekarang;
    Button btKembali, btData, btUpdmeter, btTagihan, btAmbilfoto;
    int idtagihann;
    TextInputLayout cekBlok, updMeter;
    TextView tampilnama,tampilblok, tampilmawal, tampilbulan, tampilbulansekarang, updPeriode;
    ImageView imgView;
    TextView updmeters;
    DatePickerDialog dpTanggal;
    Calendar newCalendar = Calendar.getInstance();

    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 400;
    private static final int IMAGE_PICK_GALLERY_CODE = 1000;
    private static final int IMAGE_PICK_CAMERA_CODE = 1001;

    String cameraPermission[];
    String storagePermission[];

    Uri image_uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updpelanggan);

        cameraPermission = new String[]{Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};

        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        session = new SessionManager(getApplicationContext());

        HashMap<String, String> user = session.getUserDetails();
        email = user.get(SessionManager.KEY_EMAIL);

        imgView = (ImageView) findViewById(R.id.imgview);
        updmeters = (TextView) findViewById(R.id.upd_meters);

        ambilfoto();
        cekdata();
        kembali();
        masukkan();
        tagihan();
        OpenCVLoader.initDebug();
    }

    private void ambilfoto() {
        btAmbilfoto = (Button) findViewById(R.id.bt_ambilfoto);

        btAmbilfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] items = {" Camera", " Gallery"};

                AlertDialog.Builder dialog = new AlertDialog.Builder(UpdatePelangganActivity.this);

                dialog.setTitle("Select Image");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        if (which == 0)
                        {
                            if(!checkCameraPermission())
                            {
                                requestCameraPermission();
                            }
                            else
                            {
                                pickCamera();
                            }
                        }
                        if (which == 1)
                        {
                            if (!checkStoragePermission())
                            {
                                requestStoragePermission();
                            }
                            else
                            {
                                pickGallery();
                            }
                        }
                    }

                });
                dialog.create().show();
            }
        });
    }

    private void pickGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_PICK_GALLERY_CODE);
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this,storagePermission,STORAGE_REQUEST_CODE);
    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void pickCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"Newpic");
        values.put(MediaStore.Images.Media.DESCRIPTION,"Image To Text");

        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(cameraIntent,IMAGE_PICK_CAMERA_CODE);
    }

    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermission, CAMERA_REQUEST_CODE);
    }

    public void cekdata() {
        cekBlok = (TextInputLayout) findViewById(R.id.cek_blok);
        btData = (Button) findViewById(R.id.bt_data);

        btData.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (cekBlok.getEditText().length() > 0 ) {
                    final String blok = cekBlok.getEditText().getText().toString().trim();
                    final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("data_pelanggan");
                    Query checkUser = reference.orderByChild("blok").equalTo(blok);
                    checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){

                                String blok = cekBlok.getEditText().getText().toString();

                                String name = snapshot.child(blok).child("name").getValue(String.class);
                                String mawal = snapshot.child(blok).child("makhir").getValue(String.class);
                                String periodeterakhir = snapshot.child(blok).child("periodeterakhir").getValue(String.class);

                                if (periodeterakhir.equals("Juni 2020")) {
                                    periodesekarang = "Juli 2020";
                                }
                                else if (periodeterakhir.equals("Juli 2020")) {
                                    periodesekarang = "Agustus 2020";
                                }
                                else if (periodeterakhir.equals("Agustus 2020")) {
                                    periodesekarang = "September 2020";
                                }
                                else if (periodeterakhir.equals("September 2020")) {
                                    periodesekarang = "Oktober 2020";
                                }
                                else if (periodeterakhir.equals("Oktober 2020")) {
                                    periodesekarang = "November 2020";
                                }
                                else if (periodeterakhir.equals("November 2020")) {
                                    periodesekarang = "Desember 2020";
                                }
                                else if (periodeterakhir.equals("Desember 2020")) {
                                    periodesekarang = "Januari 2021";
                                }
                                else if (periodeterakhir.equals("Januari 2021")) {
                                    periodesekarang = "Februari 2021";
                                }
                                else if (periodeterakhir.equals("Februari 2021")) {
                                    periodesekarang = "Maret 2021";
                                }
                                else if (periodeterakhir.equals("Maret 2021")) {
                                    periodesekarang = "April 2021";
                                }
                                else if (periodeterakhir.equals("April 2021")) {
                                    periodesekarang = "Mei 2021";
                                }
                                else if (periodeterakhir.equals("Mei 2021")) {
                                    periodesekarang = "Juni 2021";
                                }
                                else if (periodeterakhir.equals("Juni 2021")) {
                                    periodesekarang = "Juli 2021";
                                }
                                else if (periodeterakhir.equals("Juli 2021")) {
                                    periodesekarang = "Agustus 2021";
                                }
                                else if (periodeterakhir.equals("Agustus 2021")) {
                                    periodesekarang = "September 2021";
                                }
                                else if (periodeterakhir.equals("September 2021")) {
                                    periodesekarang = "Oktober 2021";
                                }
                                else if (periodeterakhir.equals("Oktober 2021")) {
                                    periodesekarang = "November 2021";
                                }
                                else if (periodeterakhir.equals("November 2021")) {
                                    periodesekarang = "Desember 2021";
                                }
                                else if (periodeterakhir.equals("Desember 2021")) {
                                    periodesekarang = "Januari 2022";
                                }
                                else if (periodeterakhir.equals("Januari 2022")) {
                                    periodesekarang = "Februari 2022";
                                }
                                else if (periodeterakhir.equals("Februari 2022")) {
                                    periodesekarang = "Maret 2022";
                                }

                                tampilnama = findViewById(tampil_nama);
                                tampilblok = findViewById(R.id.tampil_blok);
                                tampilmawal = findViewById(R.id.tampil_mawal);
                                tampilbulan = findViewById(R.id.tampil_bulan);
                                tampilbulansekarang = findViewById(R.id.upd_periode);

                                tampilnama.setText(name);
                                tampilblok.setText(blok);
                                tampilmawal.setText(mawal);
                                tampilbulan.setText(periodeterakhir);
                                tampilbulansekarang.setText(periodesekarang);


                            }
                            else{
                                Toast.makeText(UpdatePelangganActivity.this, "Nomor tidak terdaftar!", Toast.LENGTH_LONG).show();

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }
                else {
                    Toast.makeText(getApplication(), "Form Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    public void kembali(){
        btKembali = (Button) findViewById(R.id.btKembali);

        btKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent kembali = new Intent(UpdatePelangganActivity.this, MainActivity.class);
                startActivity(kembali);
            }
        });
    }

    public void tagihan(){
        btTagihan = (Button) findViewById(R.id.btTagihan);

        btTagihan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tagihan = new Intent(UpdatePelangganActivity.this, DataTagihanActivity.class);
                startActivity(tagihan);
            }
        });
    }

    public void masukkan(){
        btUpdmeter = (Button) findViewById(R.id.bt_updmeter);

        btUpdmeter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cekBlok.getEditText().length() > 0 && updmeters.length() > 0) {
                    String inupdmeter = updmeters.getText().toString();
                    final int nilaiupdmeter = Integer.valueOf(inupdmeter);
                    final String blok = cekBlok.getEditText().getText().toString().trim();
                    final String makhir = updmeters.getText().toString().trim();
                    final DatabaseReference r = FirebaseDatabase.getInstance().getReference("biaya_sekarang");
                    r.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            final String pot = snapshot.child("pot").getValue(String.class);
                            final String badmin = snapshot.child("badmin").getValue(String.class);
                            final String tarifkub = snapshot.child("tarifkub").getValue(String.class);

                            final int npot = Integer.parseInt(pot);
                            final int nbadmin = Integer.parseInt(badmin);
                            final int ntarifkub = Integer.parseInt(tarifkub);

                            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("data_pelanggan");
                            Query checkUser = reference.orderByChild("blok").equalTo(blok);
                            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull final DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        String makhir = snapshot.child(blok).child("makhir").getValue(String.class);
                                        int nilaimakhir = Integer.parseInt(makhir);
                                        if (nilaiupdmeter >= nilaimakhir) {
                                            AlertDialog dialog = new AlertDialog.Builder(UpdatePelangganActivity.this)
                                                    .setTitle("Anda yakin ingin update data ?")
                                                    .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int which) {
                                                /*Random random = new Random();
                                                int idtag = random.nextInt(999999);
                                                if (idtag<=0)
                                                {
                                                    idtagihann = idtag*(-1);
                                                }
                                                else
                                                {
                                                    idtagihann = idtag;
                                                }*/

                                                            String mawal = snapshot.child(blok).child("makhir").getValue(String.class);
                                                            String makhir = updmeters.getText().toString();
                                                            String blok = cekBlok.getEditText().getText().toString().trim();
                                                            String name = snapshot.child(blok).child("name").getValue(String.class);

                                                            int nilaimawal = Integer.parseInt(mawal);
                                                            int nilaimakhir = Integer.parseInt(makhir);
                                                            int totm = (nilaimakhir - nilaimawal);

                                                            int npot2 = ((((totm * ntarifkub) + nbadmin) * npot)/ 100);
                                                            int ntagihan = ((totm * ntarifkub) + nbadmin);
                                                            int ntotalbiaya = ntagihan-npot2;

                                                            String totalm = String.valueOf(totm);
                                                            String tarifkub = String.valueOf(ntarifkub);
                                                            String pot = String.valueOf(npot2);
                                                            String badmin = String.valueOf(nbadmin);
                                                            String isitagihan = String.valueOf(ntagihan);
                                                            final String totalbiaya = String.valueOf(ntotalbiaya);
                                                            String periodeterakhir = snapshot.child(blok).child("periodeterakhir").getValue(String.class);

                                                            if (periodeterakhir.equals("Juni 2020")) {
                                                                periodesekarang = "Juli 2020";
                                                            }
                                                            else if (periodeterakhir.equals("Juli 2020")) {
                                                                periodesekarang = "Agustus 2020";
                                                            }
                                                            else if (periodeterakhir.equals("Agustus 2020")) {
                                                                periodesekarang = "September 2020";
                                                            }
                                                            else if (periodeterakhir.equals("September 2020")) {
                                                                periodesekarang = "Oktober 2020";
                                                            }
                                                            else if (periodeterakhir.equals("Oktober 2020")) {
                                                                periodesekarang = "November 2020";
                                                            }
                                                            else if (periodeterakhir.equals("November 2020")) {
                                                                periodesekarang = "Desember 2020";
                                                            }
                                                            else if (periodeterakhir.equals("Desember 2020")) {
                                                                periodesekarang = "Januari 2021";
                                                            }
                                                            else if (periodeterakhir.equals("Januari 2021")) {
                                                                periodesekarang = "Februari 2021";
                                                            }
                                                            else if (periodeterakhir.equals("Februari 2021")) {
                                                                periodesekarang = "Maret 2021";
                                                            }
                                                            else if (periodeterakhir.equals("Maret 2021")) {
                                                                periodesekarang = "April 2021";
                                                            }
                                                            else if (periodeterakhir.equals("April 2021")) {
                                                                periodesekarang = "Mei 2021";
                                                            }
                                                            else if (periodeterakhir.equals("Mei 2021")) {
                                                                periodesekarang = "Juni 2021";
                                                            }
                                                            else if (periodeterakhir.equals("Juni 2021")) {
                                                                periodesekarang = "Juli 2021";
                                                            }
                                                            else if (periodeterakhir.equals("Juli 2021")) {
                                                                periodesekarang = "Agustus 2021";
                                                            }
                                                            else if (periodeterakhir.equals("Agustus 2021")) {
                                                                periodesekarang = "September 2021";
                                                            }
                                                            else if (periodeterakhir.equals("September 2021")) {
                                                                periodesekarang = "Oktober 2021";
                                                            }
                                                            else if (periodeterakhir.equals("Oktober 2021")) {
                                                                periodesekarang = "November 2021";
                                                            }
                                                            else if (periodeterakhir.equals("November 2021")) {
                                                                periodesekarang = "Desember 2021";
                                                            }
                                                            else if (periodeterakhir.equals("Desember 2021")) {
                                                                periodesekarang = "Januari 2022";
                                                            }
                                                            else if (periodeterakhir.equals("Januari 2022")) {
                                                                periodesekarang = "Februari 2022";
                                                            }
                                                            else if (periodeterakhir.equals("Februari 2022")) {
                                                                periodesekarang = "Maret 2022";
                                                            }

                                                            final String status = "Belum";

                                                            UpdatePHelperClass updateClass = new UpdatePHelperClass(blok, name, mawal, makhir, totalm, tarifkub, badmin, isitagihan, pot, totalbiaya, periodesekarang, status);
                                                            reference.child(blok).setValue(updateClass);

                                                            DatabaseReference rr = FirebaseDatabase.getInstance().getReference("data_tagihan").child(periodesekarang);
                                                            TagihanData up = new TagihanData(blok, name, mawal, makhir, totalbiaya, periodesekarang);
                                                            rr.child(blok).setValue(up);

                                                            Toast.makeText(UpdatePelangganActivity.this, "Data berhasil diupdate!", Toast.LENGTH_LONG).show();
                                                        }
                                                    })
                                                    .setNegativeButton("Tidak", null)
                                                    .create();
                                            dialog.show();
                                        } else {
                                            Toast.makeText(getApplication(), "Nilai update meter tidak boleh lebih kecil!", Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        Toast.makeText(UpdatePelangganActivity.this, "Nomor ID Pelanggan Tidak Ditemukan!", Toast.LENGTH_LONG).show();
                                    }

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


                }
                else {
                    Toast.makeText(getApplication(), "Form Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case CAMERA_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && writeStorageAccepted) {

                        pickCamera();
                    }

                    else {
                        Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
                    }
                }

                break;

            case STORAGE_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean writeStorageAccepted = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    if (writeStorageAccepted) {

                        pickGallery();
                    }
                    else {
                        Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
                    }
                }

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){

            if (requestCode == IMAGE_PICK_GALLERY_CODE) {

                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);
            }

            if (requestCode == IMAGE_PICK_CAMERA_CODE) {

                CropImage.activity(image_uri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);
            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if(resultCode == RESULT_OK){

                Uri resultUri = result.getUri();

                imgView.setImageURI(resultUri);

                BitmapDrawable bitmapDrawable = (BitmapDrawable)imgView.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();

                Bitmap bmgrayscale = toGrayscale(bitmap);


                Mat imageMat = new Mat();
                Utils.bitmapToMat(bmgrayscale, imageMat);
                Imgproc.cvtColor(imageMat, imageMat, Imgproc.COLOR_BGR2GRAY);
                Imgproc.threshold(imageMat, imageMat, 120, 255,Imgproc.THRESH_BINARY);
                Utils.matToBitmap(imageMat, bmgrayscale);
                imgView.setImageBitmap(bmgrayscale);

                TextRecognizer recognizer = new TextRecognizer.Builder(getApplicationContext()).build();

                if (!recognizer.isOperational()) {

                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                }

                else {

                    Frame frame = new Frame.Builder().setBitmap(bmgrayscale).build();
                    SparseArray<TextBlock> items = recognizer.detect(frame);
                    StringBuilder sb = new StringBuilder();

                    for (int i=0; i< items.size(); i++){

                        TextBlock myItems = items.valueAt(i);
                        sb.append(myItems.getValue());
                    }

                    updmeters.setText(sb.toString().replaceAll("(?<!\\S)[^ ](?!\\S)", "").replaceAll("1", "").replaceAll("-","").replaceAll("A", "4")
                            .replaceAll("a","").replaceAll("O","0").replaceAll("o","0").replaceAll("I","").replaceAll("l","").replaceAll("\\.","")
                            .replaceAll("R","8").replaceAll(" ","").replaceAll("g","8").replaceAll("\\$","").replaceAll("\\*","")
                            .replaceAll("/","").replaceAll("V","").replaceAll("L","").replaceAll("S","").replaceAll(":","")
                            .replaceAll("'","").replaceAll("b","").replaceAll("B","8").replaceAll("n","").replaceAll("j","7")
                            .replaceAll("J","7").replaceAll("Z","2").replaceAll("z","2").replaceAll("S","5").replaceAll("s","5")
                            .replaceAll("G","6").replaceAll("c","").replaceAll("C","").replaceAll("d","").replaceAll("D","")
                            .replaceAll("E","").replaceAll("e","").replaceAll("F","").replaceAll("f","").replaceAll("H","")
                            .replaceAll("h","").replaceAll("i","").replaceAll("K","").replaceAll("k","").replaceAll("L","")
                            .replaceAll("M","").replaceAll("N","").replaceAll("P","").replaceAll("p","").replaceAll("Q","")
                            .replaceAll("q","").replaceAll("r","").replaceAll("T","").replaceAll("t","").replaceAll("U","")
                            .replaceAll("u","").replaceAll("V","").replaceAll("v","").replaceAll("W","").replaceAll("w","")
                            .replaceAll("X","").replaceAll("x","").replaceAll("Y","").replaceAll("y",""));
                }
            }

            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){

                Exception error = result.getError();
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public Bitmap toGrayscale(Bitmap bitmap)
    {
        int width, height;
        height = bitmap.getHeight();
        width = bitmap.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bitmap, 0, 0, paint);
        return bmpGrayscale;
    }
}