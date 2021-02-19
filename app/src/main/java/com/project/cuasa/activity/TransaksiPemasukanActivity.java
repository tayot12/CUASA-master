package com.project.cuasa.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.cuasa.MainActivity;
import com.project.cuasa.R;
import com.project.cuasa.models.data_transaksi;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static android.text.TextUtils.isEmpty;

public class TransaksiPemasukanActivity extends AppCompatActivity implements View.OnClickListener /*AdapterView.OnItemClickListener */ {

    private ImageButton btn_kembali;
    private FirebaseAuth mAuth;
    private TextView datedeparture;
    private EditText Catatan, Nominal, NominalPengeluaran;
    private Button Simpan, btn_Pemasukan;

    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi_pemasukan);

        mAuth = FirebaseAuth.getInstance();

        //Inisialisasi ID Button
        btn_kembali = findViewById(R.id.btn_kembali);
        btn_kembali.setOnClickListener(this);
        btn_Pemasukan = findViewById(R.id.btn_Pemasukan);
        btn_Pemasukan.setOnClickListener(this);
        Simpan = findViewById(R.id.save);
        Simpan.setOnClickListener(this);

        //Inisialisasi ID EditText
        Catatan = findViewById(R.id.etCatatan);
        Nominal = findViewById(R.id.etNominal);
        datedeparture = findViewById(R.id.datedeparture);

        Nominal.addTextChangedListener(simpanTextWatcher);

        myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                TextView tanggal = findViewById(R.id.datedeparture);
                String myFormat = "dd-MM-yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                tanggal.setText(sdf.format(myCalendar.getTime()));
            }
        };
        datedeparture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(TransaksiPemasukanActivity.this, date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    private TextWatcher simpanTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String nominalInput = Nominal.getText().toString().trim();
            Simpan.setEnabled(!nominalInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) { }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_kembali:
                onBackPressed();
                break;
            case R.id.btn_Pemasukan:
                startActivity(new Intent(this, TransaksiPengeluaranActivity.class));
                break;
            case R.id.save:
                Simpan();
                break;
        }
    }

    private void Simpan() {
        //Mendapatkan UserID dari pengguna yang terautentifikasi
        String getUserID = mAuth.getCurrentUser().getUid();

        //Mendapatkan Istance dari Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference getReference;

        //Menyimpan Data yang diinputkan User kedalam Variabel
        Integer getNominalPemasukan = Integer.parseInt((Nominal.getText().toString()));
        Integer getNominalPengeluaran = Integer.valueOf("0");
        String getCatatan = Catatan.getText().toString();
        String getTanggal = datedeparture.getText().toString();

        //Mendapatkan Reference dari Database
        getReference = database.getReference();

        if (getNominalPemasukan.toString().length() < 3){
            Nominal.setError("Nominal harus lebih dari 2 digit!");
            Nominal.requestFocus();
            return;
        }

        if (isEmpty(getNominalPemasukan.toString()) && isEmpty(getNominalPengeluaran.toString()) && isEmpty(getCatatan) && isEmpty(getTanggal)) {
            Toast.makeText(this, "Data Harus Diisi!", Toast.LENGTH_SHORT).show();
        } else {
            getReference.child("User").child(getUserID).child("Transaksi").push()
                    .setValue(new data_transaksi(getNominalPemasukan, getNominalPengeluaran, getCatatan, getTanggal))
                    .addOnSuccessListener(this, new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                            //Peristiwa ini terjadi saat user berhasil menyimpan datanya kedalam Database
                            Nominal.setText("");
                            Catatan.setText("");
                            datedeparture.setText("");
                        }
                    });
            startActivity(new Intent(TransaksiPemasukanActivity.this, TransaksiSuksesActivity.class));
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}













