package com.primagama.bondowoso.Profile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.primagama.bondowoso.Adapter.AdapterAbsen;
import com.primagama.bondowoso.Adapter.AdapterPembayaran;
import com.primagama.bondowoso.Controller;
import com.primagama.bondowoso.Model.ModelPembayaran;
import com.primagama.bondowoso.Model.Modelabsen;
import com.primagama.bondowoso.R;
import com.primagama.bondowoso.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.primagama.bondowoso.Login.TAG_FOTO;
import static com.primagama.bondowoso.Login.TAG_IDSISWA;

public class Pembayaran extends Fragment {
    private Context context;

    String tag_json_obj = "json_obj_req";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    private String url = Server.URL + "api/ortu/ortu/getPembayaran/";

    public static final String my_shared_preferences = "my_shared_preferences";
    public static final String session_status = "session_status";
    private static final String TAG_USER = "user";
    SharedPreferences sharedpreferences;
    Boolean session = false;
    String id_siswa, username, kelas, id_kelas, user, id_program,fp;
    public static final String TAG_IDPROGRAM = "id_program";
    public static final String TAG_IDMAPEL = "id_mapel";
    public static final String TAG_MAPEL = "nama_mapel";
    public static final String TAG_ID = "id";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_KELAS = "kelas";
    public static final String TAG_IDKELAS = "id_kelas";

    public TextView no, waktu, jumlah_bayar, sisa, admin;

    private ArrayList<String> bayar;

    private RecyclerView recyclerViewabayar;
    private AdapterPembayaran mAdapter;
    private ProgressDialog mProgressDialog;
    private List<ModelPembayaran> mListData;

    private JSONArray result;

    public TextView takse;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pembayaran, container, false);

        context = this.getActivity();

        sharedpreferences = this.getActivity().getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        id_siswa = sharedpreferences.getString(TAG_IDSISWA, null);
        username = sharedpreferences.getString(TAG_USERNAME, null);
        kelas = sharedpreferences.getString(TAG_KELAS, null);
        id_kelas = sharedpreferences.getString(TAG_IDKELAS, null);
        user = sharedpreferences.getString(TAG_USER, null);
        id_program = sharedpreferences.getString(TAG_IDPROGRAM, null);
        fp = sharedpreferences.getString(TAG_FOTO, null);

        Log.d("Profil siswa siswa", "id :"+id_siswa+", id_prog: "+id_program+", username: "+username
                +", id_kelas: "+id_kelas+", kelas: "+kelas+", foto:" +fp);

        recyclerViewabayar = (RecyclerView) view.findViewById(R.id.pembayaran);
        mListData = new ArrayList<>();

        getbayar();

        takse = (TextView) view.findViewById(R.id.takde);

        return view;
    }

    public void getbayar(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Pembayaran", response.toString());
                iniData(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR", error.getMessage());
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("id_siswa", id_siswa);

                return params;
            }
        };
        Controller.getInstance().addToRequestQueue(stringRequest, tag_json_obj);
    }
    public void iniData(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            int success = jsonObject.getInt(TAG_SUCCESS);
            Log.d("SUCCESS", Integer.toString(success));
            if (success == 1){
                JSONArray jsonArray = jsonObject.getJSONArray("pembayaran");
                for (int i=0; i<jsonArray.length(); i++){
                    JSONObject pembayaran = jsonArray.getJSONObject(i);

                    String no = pembayaran.getString("no");
                    String jml_bayar = pembayaran.getString("jumlah_bayar");
                    String waktu = pembayaran.getString("waktu");
                    String sisa = pembayaran.getString("sisa");
                    String admin = pembayaran.getString("admin");

                    ModelPembayaran modelPembayaran = new ModelPembayaran();
                    modelPembayaran.setAdmin(admin);
                    modelPembayaran.setJumlah_bayar(jml_bayar);
                    modelPembayaran.setNo(no);
                    modelPembayaran.setWaktu(waktu);
                    modelPembayaran.setSisa(sisa);

                    mListData.add(modelPembayaran);

                    mAdapter = new AdapterPembayaran(mListData, context);
                    mAdapter.notifyDataSetChanged();

                    recyclerViewabayar.setLayoutManager(new LinearLayoutManager(context));
                    recyclerViewabayar.setItemAnimator(new DefaultItemAnimator());
                    recyclerViewabayar.setAdapter(mAdapter);
                }
            }else {
                takse.setText("Data Pembayaran Belum ada");
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}
