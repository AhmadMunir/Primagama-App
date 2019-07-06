package com.primagama.bondowoso.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.primagama.bondowoso.Model.Modelreqjadwal;
import com.primagama.bondowoso.R;

import java.util.List;

public class AdapterRequestjadwal extends RecyclerView.Adapter<AdapterRequestjadwal.Holder>{

    private List<Modelreqjadwal> mListData;
    private Context mContext;

    public AdapterRequestjadwal(List<Modelreqjadwal> mListData, Context mContext){
        this.mListData = mListData;
        this.mContext = mContext;
    }

    @Override
    public AdapterRequestjadwal.Holder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.list_reqjadwal, null);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(AdapterRequestjadwal.Holder holder, int position){
        Modelreqjadwal model = mListData.get(position);

        holder.id_mapel.setText(model.getId_reqmapel());
        holder.nama_mapel.setText(model.getNama_reqmapel());
        holder.total_req.setText(model.getTotal());
    }

    @Override
    public int getItemCount(){
        return mListData.size();

    }

    public static class Holder extends RecyclerView.ViewHolder{
        public TextView nama_mapel, total_req, id_mapel;

        public Holder(View itemView){
            super(itemView);

            id_mapel = (TextView) itemView.findViewById(R.id.id_reqmapel);
            nama_mapel = (TextView) itemView.findViewById(R.id.nama_reqmapel);
            total_req = (TextView) itemView.findViewById(R.id.total);

        }
    }
}