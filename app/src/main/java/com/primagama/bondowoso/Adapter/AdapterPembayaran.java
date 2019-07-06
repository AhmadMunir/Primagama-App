package com.primagama.bondowoso.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.primagama.bondowoso.Model.ModelPembayaran;
import com.primagama.bondowoso.Model.Modelabsen;
import com.primagama.bondowoso.R;

import java.util.List;

public class AdapterPembayaran extends RecyclerView.Adapter<AdapterPembayaran.Holder>{

    private List<ModelPembayaran> mListData;
    private Context mContext;

    public AdapterPembayaran(List<ModelPembayaran> mListData, Context mContext){
        this.mListData = mListData;
        this.mContext = mContext;
    }

    @Override
    public AdapterPembayaran.Holder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.list_bayar, null);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(AdapterPembayaran.Holder holder, int position){
        ModelPembayaran model = mListData.get(position);

        holder.no.setText(model.getNo());
        holder.jumlah_bayar.setText(model.getJumlah_bayar());
        holder.sisa.setText(model.getSisa());
        holder.admin.setText(model.getAdmin());
        holder.waktu.setText(model.getWaktu());
    }

    @Override
    public int getItemCount(){
        return mListData.size();

    }

    public static class Holder extends RecyclerView.ViewHolder{
        public TextView no, waktu, jumlah_bayar, sisa, admin;

        public Holder(View itemView){
            super(itemView);

            no = (TextView) itemView.findViewById(R.id.itungan);
            waktu = (TextView) itemView.findViewById(R.id.waktu);
            jumlah_bayar = (TextView) itemView.findViewById(R.id.jml);
            sisa = (TextView) itemView.findViewById(R.id.sisa);
            admin = (TextView) itemView.findViewById(R.id.admin);

        }
    }
}