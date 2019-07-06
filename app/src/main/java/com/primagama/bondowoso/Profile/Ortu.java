package com.primagama.bondowoso.Profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.primagama.bondowoso.Controller;
import com.primagama.bondowoso.R;
import com.primagama.bondowoso.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.primagama.bondowoso.Login.TAG_FOTO;

public class Ortu extends Fragment {
    private Context context;

    String tag_json_obj = "json_obj_req";

    private String foto;
    private String url = Server.URL + "api/siswa/siswa/getProfile";
    private String url_fp = Server.URL + "images/foto/profile/siswa/";


    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";


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


    TextView nama_ayah, no_ayah, payah, nama_ibu, no_ibu, pibu;


    private ArrayList<String> absen;

    private JSONArray result;

    public TextView takse;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ortu, container, false);

        context = this.getActivity();

        sharedpreferences = this.getActivity().getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        id_siswa = sharedpreferences.getString(TAG_ID, null);
        username = sharedpreferences.getString(TAG_USERNAME, null);
        kelas = sharedpreferences.getString(TAG_KELAS, null);
        id_kelas = sharedpreferences.getString(TAG_IDKELAS, null);
        user = sharedpreferences.getString(TAG_USER, null);
        id_program = sharedpreferences.getString(TAG_IDPROGRAM, null);
        fp = sharedpreferences.getString(TAG_FOTO, null);

        Log.d("Ortu", "id :"+id_siswa+", id_prog: "+id_program+", username: "+username
                +", id_kelas: "+id_kelas+", kelas: "+kelas+", foto:" +fp);


        nama_ayah = (TextView) view.findViewById(R.id.nama_a);
        nama_ibu = (TextView) view.findViewById(R.id.nama_i);
        payah = (TextView) view.findViewById(R.id.pekerjaan_a);
        pibu = (TextView) view.findViewById(R.id.pekerjaan_i);
        no_ayah = (TextView) view.findViewById(R.id.no_hpa);
        no_ibu = (TextView) view.findViewById(R.id.no_hpi);


        getDetail();
        return view;
    }

    private void getDetail(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Profil Siswa", response.toString());
                inidata(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.getMessage());
//                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id_siswa);

                return params;
            }
        };

        Controller.getInstance().addToRequestQueue(stringRequest, tag_json_obj);
    }

    public void inidata(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            int success = jsonObject.getInt(TAG_SUCCESS);
            Log.d("SUCCESS", Integer.toString(success));
            if (success == 1){
                JSONArray jsonArray= jsonObject.getJSONArray("detail");
                JSONObject detail = jsonArray.getJSONObject(0);

                nama_ayah.setText(detail.getString("nama_a"));
                nama_ibu.setText(detail.getString("nama_i"));
                no_ayah.setText(detail.getString("nayah"));
                no_ibu.setText(detail.getString("nibu"));
                payah.setText(detail.getString("payah"));
                pibu.setText(detail.getString("pibu"));

                Log.d("ortu : ", "AAAA");

            }else {
                Log.d("Error", "Success : " + Integer.toString(success)+
                        "message" + jsonObject.getString("detail"));
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

}
