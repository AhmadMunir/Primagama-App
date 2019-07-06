package com.primagama.bondowoso.Profile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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

import static com.primagama.bondowoso.Login.TAG_FOTO;
import static com.primagama.bondowoso.Login.TAG_IDSISWA;

public class Siswasiswa extends Fragment {


    private String foto;
    private String url = Server.URL + "api/siswa/siswa/getProfile";
    private String url_fp = Server.URL + "images/foto/profile/siswa/";


    private Context context;

    String tag_json_obj = "json_obj_req";
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


    TextView nama, kls, sklh, ttl, alamat, email;


    private ArrayList<String> absen;

    private JSONArray result;

    public TextView takse;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_siswasiswa, container, false);

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


        nama = (TextView) view.findViewById(R.id.nama);
        kls = (TextView) view.findViewById(R.id.klz);
        sklh = (TextView) view.findViewById(R.id.sekolah);
        ttl = (TextView) view.findViewById(R.id.ttl);
        alamat = (TextView) view.findViewById(R.id.alamat);
        email = (TextView) view.findViewById(R.id.email);

        kls.setText(kelas);

        ImageView fotoprofil = (ImageView) view.findViewById(R.id.fotoprofil);

        getDetail();

        if (fp != null){
            ImageView imv;
            Glide.with(context).load(url_fp+fp)
                    .fitCenter().crossFade().diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(fotoprofil);
            Log.d("FOTO", fp);
        }else{
            ImageView imv;
            String fto = url_fp+"default.png";
            Glide.with(context).load(url_fp+"default.jpg")
                    .fitCenter().crossFade().diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(fotoprofil);
            Log.d("FOTO", "=null"+ fto);
        }

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
               Log.d("Error", error.getMessage());
               Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
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

                String nama_lengkap = detail.getString("nama_lengkap");
                String sekolah = detail.getString("sekolah");
                String ft = detail.getString("foto");


                nama.setText(nama_lengkap);
                sklh.setText(sekolah);
                ttl.setText(detail.getString("ttl"));
                alamat.setText(detail.getString("alamat"));
                email.setText(detail.getString("email"));
            }else {
                Log.d("Error", "Success : " + Integer.toString(success)+
                        "message" + jsonObject.getString("detail"));
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
   }


}
