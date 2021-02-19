package com.project.cuasa.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.project.cuasa.MainActivity;
import com.project.cuasa.R;

public class TransaksiSuksesActivity extends AppCompatActivity implements View.OnClickListener {

    private Button  buttonTransaksi, btn_Home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi_sukses);

        buttonTransaksi = (Button) findViewById(R.id.buttonTransaksi);
        buttonTransaksi.setOnClickListener(this);

        btn_Home = (Button) findViewById(R.id.buttonHome);
        btn_Home.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonTransaksi:
                startActivity(new Intent(this, TransaksiPemasukanActivity.class));
                break;
            case R.id.buttonHome:
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }
}