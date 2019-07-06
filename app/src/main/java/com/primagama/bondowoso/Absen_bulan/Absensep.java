package com.primagama.bondowoso.Absen_bulan;

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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.primagama.bondowoso.Adapter.AdapterAbsen;
import com.primagama.bondowoso.Controller;
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

public class Absensep extends Fragment {

    private Context context;

    String tag_json_obj = "json_obj_req";

    private String url = Server.URL + "api/siswa/siswa/getAbsenbybulan/";


    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";


    public static final String my_shared_preferences = "my_shared_preferences";
    public static final String session_status = "session_status";
    private static final String TAG_USER = "user";
    SharedPreferences sharedpreferences;
    Boolean session = false;
    String id_siswa, username, kelas, id_kelas, user, id_program;
    public static final String TAG_IDPROGRAM = "id_program";
    public static final String TAG_IDMAPEL = "id_mapel";
    public static final String TAG_MAPEL = "nama_mapel";
    public static final String TAG_ID = "id";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_KELAS = "kelas";
    public static final String TAG_IDKELAS = "id_kelas";

    private RecyclerView recyclerViewabsen;
    private AdapterAbsen mAdapter;
    private ProgressDialog mProgressDialog;
    private List<Modelabsen> mListData;


    private ArrayList<String> absen;

    private JSONArray result;

    public TextView takse;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_absen, container, false);

        context = this.getActivity();

        sharedpreferences = this.getActivity().getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        id_siswa = sharedpreferences.getString(TAG_ID, null);
        username = sharedpreferences.getString(TAG_USERNAME, null);
        kelas = sharedpreferences.getString(TAG_KELAS, null);
        id_kelas = sharedpreferences.getString(TAG_IDKELAS, null);
        user = sharedpreferences.getString(TAG_USER, null);
        id_program = sharedpreferences.getString(TAG_IDPROGRAM, null);

        Log.d("Absen Jan", "id :"+id_siswa+", id_prog: "+id_program+", username: "+username
                +", id_kelas: "+id_kelas+", kelas: "+kelas);


        recyclerViewabsen = (RecyclerView) view.findViewById(R.id.absen);


        mListData = new ArrayList<>();

        getabsen();

        takse = (TextView) view.findViewById(R.id.takde);


        return view;
    }

    public void getabsen(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Absen Januari", response.toString());
                iniData(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR", error.getMessage());
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_siswa", id_siswa);
                params.put("bln", Integer.toString(9));

                return params;
            }
        };
        Controller.getInstance().addToRequestQueue(stringRequest, tag_json_obj);
    }

    public void iniData(String response){
        try{
            JSONObject jsonObject = new JSONObject(response);
            int success = jsonObject.getInt("success");
            Log.d("SUCCESS", Integer.toString(success));
            if (success == 1) {
                JSONArray jsonArray = jsonObject.getJSONArray("absen");
                for (int i=0; i<jsonArray.length(); i++){
                    JSONObject absen = jsonArray.getJSONObject(i);

                    String hari  = absen.getString("hari");
                    String tanggal = absen.getString("tanggal");
                    String jam_datang = absen.getString("jam_datang");
                    String jam_pulang = absen.getString("jam_pulang");
                    String keterangan = absen.getString("keterangan");

                    Modelabsen modelabsen = new Modelabsen();
                    modelabsen.setHari(hari);
                    modelabsen.setJam_datang(jam_datang);
                    modelabsen.setJam_pulang(jam_pulang);
                    modelabsen.setTanggal(tanggal);
                    modelabsen.setKeterangan(keterangan);

                    mListData.add(modelabsen);

                    mAdapter = new AdapterAbsen(mListData, context);
                    mAdapter.notifyDataSetChanged();

                    recyclerViewabsen.setLayoutManager(new LinearLayoutManager(context));
                    recyclerViewabsen.setItemAnimator(new DefaultItemAnimator());
                    recyclerViewabsen.setAdapter(mAdapter);


                }
            } else {
                takse.setText("Belum Ada Data Absen Pada Bulan Ini");

            }



        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}

