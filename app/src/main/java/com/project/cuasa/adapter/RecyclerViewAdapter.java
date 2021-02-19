package com.project.cuasa.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.project.cuasa.R;
import com.project.cuasa.activity.UpdateTransaksi;
import com.project.cuasa.MainActivity;
import com.project.cuasa.models.data_transaksi;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

//Class Adapter ini Digunakan Untuk Mengatur Bagaimana Data akan Ditampilkan
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    //Deklarasi Variable
    private ArrayList<data_transaksi> listTransaksi;
    private Context context;


    //ViewHolder Digunakan Untuk Menyimpan Referensi Dari View-View
    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView TanggalPemasukan, CatatanPemasukan, Pemasukan, Pengeluaran;
        private LinearLayout ListItem;

        ViewHolder(View itemView) {
            super(itemView);
            //Menginisialisasi View-View yang terpasang pada layout RecyclerView kita
            TanggalPemasukan = itemView.findViewById(R.id.tanggal);
            CatatanPemasukan = itemView.findViewById(R.id.catatan);
            Pemasukan = itemView.findViewById(R.id.pemasukan);
            Pengeluaran = itemView.findViewById(R.id.pengeluaran);
            ListItem = itemView.findViewById(R.id.list_item);
        }
    }


    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View V = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_design, parent, false);
        return new ViewHolder(V);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        Locale localeID = new Locale("in", "ID");
        final NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

        //Mengambil Nilai/Value yenag terdapat pada RecyclerView berdasarkan Posisi Tertentu
        final String Tanggal = listTransaksi.get(position).getTanggal();
        final String Catatan = listTransaksi.get(position).getCatatan();
        final Integer Pemasukan = listTransaksi.get(position).getNominalPemasukan();
        final Integer Pengeluaran = listTransaksi.get(position).getNominalPengeluaran();

        //Memasukan Nilai/Value kedalam View (TextView: NIM, Nama, Jurusan)
        holder.TanggalPemasukan.setText(Tanggal);
        holder.CatatanPemasukan.setText(Catatan);
        holder.Pemasukan.setText(formatRupiah(Double.valueOf(Pemasukan)));
        holder.Pengeluaran.setText(formatRupiah(Double.valueOf(Pengeluaran)));

        holder.ListItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                final String[] action = {"Ubah Transaksi", "Hapus Transaksi"};
                final AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setItems(action,  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        switch (i){
                            case 0:
                        /*
                          Berpindah Activity pada halaman layout updateData
                          dan mengambil data pada listMahasiswa, berdasarkan posisinya
                          untuk dikirim pada activity selanjutnya
                        */
                                Bundle bundle = new Bundle();
                                bundle.putString("dataTanggal", listTransaksi.get(position).getTanggal());
                                bundle.putString("dataCatatan", listTransaksi.get(position).getCatatan());
                                bundle.putString("dataPemasukan", String.valueOf(listTransaksi.get(position).getNominalPemasukan()));
                                bundle.putString("dataPengeluaran", String.valueOf(listTransaksi.get(position).getNominalPengeluaran()));
                                bundle.putString("getPrimaryKey", listTransaksi.get(position).getKey());
                                Intent intent = new Intent(v.getContext(), UpdateTransaksi.class);
                                intent.putExtras(bundle);
                                context.startActivity(intent);
                                break;
                            case 1:
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());

                                // set pesan dari dialog
                                alertDialogBuilder
                                        .setMessage("Yakin untuk menghapus transaksi?")
                                        .setIcon(R.mipmap.ic_launcher)
                                        .setCancelable(false)
                                        .setPositiveButton("Ya",new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,int id) {
                                                // jika tombol diklik, maka akan menutup activity ini
                                                RecyclerViewAdapter.this.listener.onDeleteData(listTransaksi.get(position), position);;
                                            }
                                        })
                                        .setNegativeButton("Tidak",new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                // jika tombol ini diklik, akan menutup dialog
                                                // dan tidak terjadi apa2
                                                dialog.cancel();
                                            }
                                        });

                                // membuat alert dialog dari builder
                                AlertDialog alertDialog = alertDialogBuilder.create();

                                // menampilkan alert dialog
                                alertDialog.show();
                                break;
                        }
                    }
                });
                alert.create();
                alert.show();
                return true;
            }
        });
    }


    @Override
    public int getItemCount() {
        //Menghitung Ukuran/Jumlah Data Yang Akan Ditampilkan Pada RecyclerView
        return listTransaksi.size();
    }

    private String formatRupiah(double parseDouble) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(parseDouble);
    }

    //Membuat Interfece
    public interface dataListener{
        void onDeleteData(data_transaksi data, int position);
    }

    //Deklarasi objek dari Interfece
    dataListener listener;

    //Membuat Konstruktor, untuk menerima input dari Database
    public RecyclerViewAdapter(ArrayList listTransaksi, Context context) {
        this.listTransaksi = listTransaksi;
        this.context = context;
        listener = (MainActivity)context;
    }

}
