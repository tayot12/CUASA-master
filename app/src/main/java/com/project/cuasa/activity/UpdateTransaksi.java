package com.project.cuasa.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.cuasa.R;
import com.project.cuasa.models.data_transaksi;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class UpdateTransaksi extends AppCompatActivity {

    //Deklarasi Variable
    private EditText nomPemasukanBaru, nomPengeluaranBaru, catatanBaru, tanggalBaru;
    private Button update;
    private DatabaseReference database;
    private FirebaseAuth auth;
    private String cekCatatan, cekTanggal;
    private Integer cekNominalPemasukan, cekNominalPengeluaran;

    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_transaksi);


        nomPemasukanBaru = findViewById(R.id.newNominalPemasukan);
        nomPengeluaranBaru = findViewById(R.id.newNominalPengeluaran);
        catatanBaru = findViewById(R.id.newCatatan);
        tanggalBaru = findViewById(R.id.newTanggal);
        update = findViewById(R.id.update);

        myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                TextView tanggal = findViewById(R.id.newTanggal);
                String myFormat = "dd-MM-yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                tanggal.setText(sdf.format(myCalendar.getTime()));
            }
        };
        tanggalBaru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(UpdateTransaksi.this, date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        //Mendapatkan Instance autentikasi dan Referensi dari Database
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();

        getData();
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Mendapatkan Data Mahasiswa yang akan dicek
                cekNominalPemasukan = Integer.valueOf(nomPemasukanBaru.getText().toString());
                cekNominalPengeluaran = Integer.valueOf(nomPengeluaranBaru.getText().toString());
                cekCatatan = catatanBaru.getText().toString();
                cekTanggal = tanggalBaru.getText().toString();


                if (cekNominalPemasukan.toString().isEmpty()){
                    nomPemasukanBaru.setError("Nominal harus diisi!");
                    nomPemasukanBaru.requestFocus();
                    return;
                }
                if (cekNominalPengeluaran.toString().isEmpty()){
                    nomPengeluaranBaru.setError("Nominal harus diisi!");
                    nomPengeluaranBaru.requestFocus();
                    return;
                }
                //Mengecek agar tidak ada data yang kosong, saat proses update
                if(isEmpty(String.valueOf(cekNominalPemasukan)) || isEmpty(String.valueOf(cekNominalPengeluaran)) || isEmpty(cekCatatan) || isEmpty(cekTanggal)){
                    Toast.makeText(UpdateTransaksi.this, "Data tidak boleh ada yang kosong", Toast.LENGTH_SHORT).show();
                }else {
                    /*
                      Menjalankan proses update data.
                      Method Setter digunakan untuk mendapakan data baru yang diinputkan User.
                    */
                    data_transaksi setNilai = new data_transaksi();
                    setNilai.setNominalPemasukan(Integer.valueOf(nomPemasukanBaru.getText().toString()));
                    setNilai.setNominalPengeluaran(Integer.valueOf(nomPengeluaranBaru.getText().toString()));
                    setNilai.setCatatan(catatanBaru.getText().toString());
                    setNilai.setTanggal(tanggalBaru.getText().toString());
                    updateTransaksi(setNilai);
                }
            }
        });
    }

    // Mengecek apakah ada data yang kosong, sebelum diupdate
    private boolean isEmpty(String s){
        return TextUtils.isEmpty(s);
    }

    //Menampilkan data yang akan di update
    private void getData(){

        final String getNominalPemasukan = getIntent().getExtras().getString("dataPemasukan");
        final String getNominalPengeluaran = getIntent().getExtras().getString("dataPengeluaran");
        final String getCatatan = getIntent().getExtras().getString("dataCatatan");
        final String getTanggal = getIntent().getExtras().getString("dataTanggal");

        nomPemasukanBaru.addTextChangedListener(TextWatcher);
        nomPengeluaranBaru.addTextChangedListener(TextWatcher1);


        nomPemasukanBaru.setText(getNominalPemasukan);
        nomPengeluaranBaru.setText(getNominalPengeluaran);
        catatanBaru.setText(getCatatan);
        tanggalBaru.setText(getTanggal);
    }

    //Proses Update data yang sudah ditentukan
    private void updateTransaksi(data_transaksi data){
        String userID = auth.getUid();
        String getKey = getIntent().getExtras().getString("getPrimaryKey");
        database.child("User")
                .child(userID)
                .child("Transaksi")
                .child(getKey)
                .setValue(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        nomPemasukanBaru.setText("");
                        nomPengeluaranBaru.setText("");
                        catatanBaru.setText("");
                        tanggalBaru.setText("");
                        Toast.makeText(UpdateTransaksi.this, "Data Berhasil diubah", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }

    private TextWatcher TextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String pemasukanInput = nomPemasukanBaru.getText().toString().trim();

            nomPengeluaranBaru.setEnabled(pemasukanInput.length() == 1);

            update.setEnabled(!pemasukanInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private TextWatcher TextWatcher1 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String pengeluaranInput = nomPengeluaranBaru.getText().toString().trim();

            nomPemasukanBaru.setEnabled(pengeluaranInput.length() == 1);

            update.setEnabled(!pengeluaranInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }

    };
}