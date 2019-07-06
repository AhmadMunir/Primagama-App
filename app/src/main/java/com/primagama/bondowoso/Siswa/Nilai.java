package com.primagama.bondowoso.Siswa;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.primagama.bondowoso.Absen;
import com.primagama.bondowoso.Controller;
import com.primagama.bondowoso.Login;
import com.primagama.bondowoso.R;
import com.primagama.bondowoso.Server;
import com.primagama.bondowoso.SiswaProfil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.primagama.bondowoso.Login.TAG_USER;

public class Nilai extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Spinner.OnItemSelectedListener {

    String tag_json_obj = "json_obj_req";

    private String url = Server.URL + "api/siswa/siswa/getNilai/";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    String id_siswa, username, kelas, id_kelas, id_program, user;
    public static final String TAG_IDPROGRAM = "id_program";
    public static final String TAG_IDMAPEL = "id_mapel";
    public static final String TAG_MAPEL = "nama_mapel";
    public static final String TAG_ID = "id";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_KELAS = "kelas";
    public static final String TAG_IDKELAS = "id_kelas";

    SharedPreferences sharedpreferences;
    Boolean session = false;

    public static final String my_shared_preferences = "my_shared_preferences";
    public static final String session_status = "session_status";

    private Spinner pilih_mapel;
    private TextView tv_to1,tv_to2,tv_to3,tv_to4,tv_to5;

    private ArrayList<String> nilai;

    private JSONArray result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nilai);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

//        harus
        // Cek session login
        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        id_siswa = sharedpreferences.getString(TAG_ID, null);
        username = sharedpreferences.getString(TAG_USERNAME, null);
        kelas = sharedpreferences.getString(TAG_KELAS, null);
        id_kelas = sharedpreferences.getString(TAG_IDKELAS, null);
        user = sharedpreferences.getString(TAG_USER, null);
        id_program = sharedpreferences.getString(TAG_IDPROGRAM, null);

        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.txt_username);
        TextView navKelas = (TextView) headerView.findViewById(R.id.txt_kelas);
        navUsername.setText(username);
        navKelas.setText(kelas);

        nilai = new ArrayList<String>();
        pilih_mapel = (Spinner) findViewById(R.id.pilih_tmp_mapel);
        pilih_mapel.setOnItemSelectedListener(this);

        tv_to1 = (TextView) findViewById(R.id.to1);
        tv_to2 = (TextView) findViewById(R.id.to2);
        tv_to3 = (TextView) findViewById(R.id.to3);
        tv_to4 = (TextView) findViewById(R.id.to4);
        tv_to5 = (TextView) findViewById(R.id.to5);

        getNilai();
    }

    private void getNilai(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject j = null;

                try {
                    j = new JSONObject(response);
                    Log.d("Nilai : ", response.toString());

                    result = j.getJSONArray("nilai");

                    getmapel(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params =  new HashMap<String, String>();
                params.put("id_siswa", id_siswa);

                return params;
            }
        };
        Controller.getInstance().addToRequestQueue(stringRequest,tag_json_obj);
    }

    private void getmapel(JSONArray j){
        for (int i = 0; i<j.length(); i++){
            try{
                JSONObject json = j.getJSONObject(i);
                nilai.add(json.getString("nama_mapel"));
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        pilih_mapel.setAdapter(new ArrayAdapter<String>(Nilai.this, android.R.layout.simple_spinner_dropdown_item, nilai));
    }
    private String getTO1(int position){
        String to1="";
        try {
            JSONObject json = result.getJSONObject(position);
            to1 = json.getString("to1");
        }catch (JSONException e){
            e.printStackTrace();
        }
        Log.d("to1 : ", to1);
        if (to1 == "null"){
            to1 = "Belum Ada Nilai";
        }else{
            Log.d("TO 1 :", "TO 1 tidak null");
        }
        return to1;

    }

    private String getTO2(int position){
        String to2="";
        try {
            JSONObject json = result.getJSONObject(position);
            to2 = json.getString("to2");
        }catch (JSONException e){
            e.printStackTrace();
        }
        Log.d("to2 : ", to2);
        if (to2 == "null"){
            to2 = "Belum Ada Nilai";
        }else{
            Log.d("TO 2 :", "TO 2 tidak null");
        }
        return to2;
    }

    private String getTO3(int position){
        String to3="";
        try {
            JSONObject json = result.getJSONObject(position);
            to3 = json.getString("to3");
        }catch (JSONException e){
            e.printStackTrace();
        }
        Log.d("to3 : ", to3);
        if (to3 == "null"){
            to3 = "Belum Ada Nilai";
        }else{
            Log.d("TO 3 :", "TO 3 tidak null");
        }
        return to3;
    }

    private String getTO4(int position){
        String to4="";
        try {
            JSONObject json = result.getJSONObject(position);
            to4 = json.getString("to4");
        }catch (JSONException e){
            e.printStackTrace();
        }
        Log.d("to4 : ", to4);
        if (to4 == "null"){
            to4 = "Belum Ada Nilai";
        }else{
            Log.d("TO 4 :", "TO 4 tidak null");
        }
        return to4;
    }

    private String getTO5(int position){
        String to5="";
        try {
            JSONObject json = result.getJSONObject(position);
            to5 = json.getString("to5");
        }catch (JSONException e){
            e.printStackTrace();
        }
        Log.d("to5 : ", to5);
        if (to5 == "null"){
            to5 = "Belum Ada Nilai";
        }else{
            Log.d("TO 5 :", "TO 5 tidak null");
        }
        return to5;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.homesiswa, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
            Intent home = new Intent(this, Homesiswa.class);
            home.putExtra(TAG_USER, user);
            home.putExtra(TAG_ID, id_siswa);
            home.putExtra(TAG_USERNAME, username);
            home.putExtra(TAG_KELAS, kelas);
            home.putExtra(TAG_IDKELAS, id_kelas);
            home.putExtra(TAG_IDPROGRAM, id_program);
            Log.d("Nilai", "id :"+id_siswa+", id_prog: "+id_program+", username: "+username
                    +", id_kelas: "+id_kelas+", kelas: "+kelas);
            finish();
            startActivity(home);
        } else if (id == R.id.reqjadwal) {
            Intent reqjadwal = new Intent(Nilai.this, Siswareqjadwal.class);
            reqjadwal.putExtra(TAG_ID, id_siswa);
            reqjadwal.putExtra(TAG_USERNAME, username);
            reqjadwal.putExtra(TAG_KELAS, kelas);
            reqjadwal.putExtra(TAG_IDKELAS, id_kelas);
            reqjadwal.putExtra(TAG_IDPROGRAM, getIntent().getStringExtra(TAG_IDPROGRAM));
            Log.d("Nilai", "id :"+id_siswa+", id_prog: "+id_program+", username: "+username
                    +", id_kelas: "+id_kelas+", kelas: "+kelas);
            finish();
            startActivity(reqjadwal);

        } else if (id == R.id.nav_absen) {
            Intent absen = new Intent(Nilai.this, Absen.class);
            absen.putExtra(TAG_ID, id_siswa);
            absen.putExtra(TAG_USERNAME, username);
            absen.putExtra(TAG_KELAS, kelas);
            absen.putExtra(TAG_IDKELAS, id_kelas);
            absen.putExtra(TAG_IDPROGRAM, getIntent().getStringExtra(TAG_IDPROGRAM));
            Log.d("reqjadwal", "id :"+id_siswa+", id_prog: "+id_program+", username: "+username
                    +", id_kelas: "+id_kelas+", kelas: "+kelas);
            finish();
            startActivity(absen);

        } else if (id == R.id.nav_nilai) {
            Intent nilai = new Intent(Nilai.this, Nilai.class);
            nilai.putExtra(TAG_ID, id_siswa);
            nilai.putExtra(TAG_USERNAME, username);
            nilai.putExtra(TAG_KELAS, kelas);
            nilai.putExtra(TAG_IDKELAS, id_kelas);
            nilai.putExtra(TAG_IDPROGRAM, getIntent().getStringExtra(TAG_IDPROGRAM));
            Log.d("Nilai", "id :"+id_siswa+", id_prog: "+id_program+", username: "+username
                    +", id_kelas: "+id_kelas+", kelas: "+kelas);
            finish();
            startActivity(nilai);
        } else if (id == R.id.nav_profil) {
            Intent profil = new Intent(this, SiswaProfil.class);

            profil.putExtra(TAG_ID, id_siswa);
            profil.putExtra(TAG_USERNAME, username);
            profil.putExtra(TAG_KELAS, kelas);
            profil.putExtra(TAG_IDKELAS, id_kelas);
            profil.putExtra(TAG_IDPROGRAM, getIntent().getStringExtra(TAG_IDPROGRAM));
            finish();
            startActivity(profil);
        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_logout){
            new AlertDialog.Builder(this)
                    .setMessage("Apakah Kamu Yakin akan Log Out?")
                    .setCancelable(false)
                    .setPositiveButton("YA", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putBoolean(Login.session_status, false);
                            editor.putString(TAG_ID, null);
                            editor.putString(TAG_USERNAME, null);
                            editor.putString(TAG_KELAS, null);
                            editor.putString(TAG_IDPROGRAM, null);
                            editor.putString(TAG_IDKELAS, null);
                            editor.commit();

                            Intent intent = new Intent(Nilai.this, Login.class);
                            finish();
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Tidak", null)
                    .show();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        tv_to1.setText(getTO1(position));
        tv_to2.setText(getTO2(position));
        tv_to3.setText(getTO3(position));
        tv_to4.setText(getTO4(position));
        tv_to5.setText(getTO5(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        tv_to1.setText(" ");
        tv_to2.setText(" ");
        tv_to3.setText(" ");
        tv_to4.setText(" ");
        tv_to5.setText(" ");
    }
}
