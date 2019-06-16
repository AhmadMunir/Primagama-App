package com.primagama.bondowoso;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdapterListjadwal extends RecyclerView.Adapter<AdapterListjadwal.Holder>{

    private List<ModelJadwal> mListData;
    private Context mContext;

    public AdapterListjadwal(List<ModelJadwal> mListData, Context mContext){
        this.mListData = mListData;
        this.mContext = mContext;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.list_jadwal, null);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position){
        ModelJadwal model = mListData.get(position);

        holder.hari.setText(model.getHari());
        holder.mapel.setText(model.getMapel());
        holder.tentor.setText(model.getTentor());
        holder.ruang.setText(model.getRuang());
        holder.jam.setText(model.getJam());
    }

    @Override
    public int getItemCount(){
        return mListData.size();

    }

    public static class Holder extends RecyclerView.ViewHolder{
        public TextView hari, mapel, tentor, ruang, jam;

        public Holder(View itemView){
            super(itemView);

            hari = (TextView) itemView.findViewById(R.id.hari);
            mapel = (TextView) itemView.findViewById(R.id.mapel);
            tentor = (TextView) itemView.findViewById(R.id.tentor);
            ruang = (TextView) itemView.findViewById(R.id.ruang);
            jam = (TextView) itemView.findViewById(R.id.jam);
        }
    }
}