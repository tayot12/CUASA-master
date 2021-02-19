package com.project.cuasa;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.cuasa.activity.ProfileActivity;
import com.project.cuasa.activity.TransaksiPemasukanActivity;
import com.project.cuasa.adapter.RecyclerViewAdapter;
import com.project.cuasa.models.data_transaksi;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, RecyclerViewAdapter.dataListener {

    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;

    //Deklarasi Variable untuk RecyclerView
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    //Deklarasi Variable Database Reference dan ArrayList dengan Parameter Class Model kita.
    private DatabaseReference reference;
    private ArrayList<data_transaksi> dataTransaksi;

    private FirebaseAuth auth;
    private Button tambahTransaksi;
    private ImageView profile;
    private TextView totalPemasukan, totalPengeluaran, totalSelisih;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan);

        profile = (ImageView) findViewById(R.id.profile);
        profile.setOnClickListener(this);

        tambahTransaksi = (Button) findViewById(R.id.tambahTransaksi);
        tambahTransaksi.setOnClickListener(this);


        totalPemasukan = (TextView) findViewById(R.id.totalpemasukan);
        totalPengeluaran = (TextView) findViewById(R.id.totalpengeluaran);
        totalSelisih = (TextView) findViewById(R.id.totalselisih);


        recyclerView = findViewById(R.id.datalist);
        auth = FirebaseAuth.getInstance();

        MyRecyclerView();
        GetData();

        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("User").child(auth.getUid()).child("Transaksi")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int sum=0;
                        int sum1=0;

                        for (DataSnapshot data : snapshot.getChildren()){

                            Map<String,Object> map = (Map<String, Object>) data.getValue();
                            Object total = map.get("nominalPemasukan");
                            Object total1 = map.get("nominalPengeluaran");
                            int pemasukanValue = Integer.parseInt(String.valueOf(total));
                            sum += pemasukanValue;
                            int pengeluaranValue = Integer.parseInt(String.valueOf(total1));
                            sum1 += pengeluaranValue;

                            int selisih = sum - sum1;


                            totalPemasukan.setText(formatRupiah(Double.valueOf(sum)));
                            totalPengeluaran.setText(formatRupiah(Double.valueOf(sum1)));
                            totalSelisih.setText(formatRupiah(Double.valueOf(selisih)));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private String formatRupiah(double parseDouble) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(parseDouble);
    }

    //Berisi baris kode untuk mengambil data dari Database dan menampilkannya kedalam Adapter
    private void GetData(){
        Toast.makeText(getApplicationContext(), "Tekan dan tahan item transaksi untuk Update atau Delete transaksi", Toast.LENGTH_SHORT).show();
        //Mendapatkan Referensi Database
        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("User").child(auth.getUid()).child("Transaksi")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                //Inisialisasi ArrayList
                dataTransaksi = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    //Mapping data pada DataSnapshot ke dalam objek mahasiswa
                    data_transaksi data = snapshot.getValue(data_transaksi.class);

                    //Mengambil Primary Key, digunakan untuk proses Update dan Delete
                    data.setKey(snapshot.getKey());
                    dataTransaksi.add(data);
                }

                //Inisialisasi Adapter dan data Mahasiswa dalam bentuk Array
                adapter = new RecyclerViewAdapter(dataTransaksi, MainActivity.this);

                if (dataTransaksi.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Data Tidak Berhasil Dimuat, Silahkan Tambah Transaksi terlebih dahulu!", Toast.LENGTH_LONG).show();
                } else {
                    //Memasang Adapter pada RecyclerView
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Data Gagal Dimuat", Toast.LENGTH_LONG).show();
                Log.e("MyListActivity", databaseError.getDetails() + " " + databaseError.getMessage());
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profile:
                startActivity(new Intent(this, ProfileActivity.class));
                break;

            case R.id.tambahTransaksi:
                startActivity(new Intent(this, TransaksiPemasukanActivity.class));
                break;
        }
    }

    //Methode yang berisi kumpulan baris kode untuk mengatur RecyclerView
    private void MyRecyclerView(){
        //Menggunakan Layout Manager, Dan Membuat List Secara Vertical
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        //Membuat Underline pada Setiap Item Didalam List
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.line));
        recyclerView.addItemDecoration(itemDecoration);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onDeleteData(data_transaksi data, int position) {
        String userID = auth.getUid();
        if(reference != null){
            reference.child("User")
                    .child(userID)
                    .child("Transaksi")
                    .child(data.getKey())
                    .removeValue()
                    .addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                            Toast.makeText(MainActivity.this, "Data Berhasil Dihapus", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    @Override
    public void onBackPressed() {
            if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
            }else { Toast.makeText(getBaseContext(), "Tekan Back Sekali lagi untuk Keluar", Toast.LENGTH_SHORT).show();
            }
            mBackPressed = System.currentTimeMillis();
        }
}
