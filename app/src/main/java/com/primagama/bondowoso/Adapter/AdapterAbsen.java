package com.primagama.bondowoso.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.primagama.bondowoso.Model.Modelabsen;

import com.primagama.bondowoso.R;

import java.util.List;

public class AdapterAbsen extends RecyclerView.Adapter<AdapterAbsen.Holder>{

    private List<Modelabsen> mListData;
    private Context mContext;

    public AdapterAbsen(List<Modelabsen> mListData, Context mContext){
        this.mListData = mListData;
        this.mContext = mContext;
    }

    @Override
    public AdapterAbsen.Holder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.list_absen, null);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(AdapterAbsen.Holder holder, int position){
        Modelabsen model = mListData.get(position);

        holder.hari.setText(model.getHari());
        holder.jam_datang.setText(model.getJam_datang());
        holder.jam_pulang.setText(model.getJam_pulang());
        holder.keterangan.setText(model.getKeterangan());
        holder.tanggal.setText(model.getTanggal());
    }

    @Override
    public int getItemCount(){
        return mListData.size();

    }

    public static class Holder extends RecyclerView.ViewHolder{
        public TextView hari, jam_datang, jam_pulang, keterangan, tanggal;

        public Holder(View itemView){
            super(itemView);

            hari = (TextView) itemView.findViewById(R.id.hari);
            jam_datang = (TextView) itemView.findViewById(R.id.jam_datang);
            jam_pulang = (TextView) itemView.findViewById(R.id.jam_pulang);
            keterangan = (TextView) itemView.findViewById(R.id.keterangan);
            tanggal = (TextView) itemView.findViewById(R.id.tanggal);

        }
    }
}