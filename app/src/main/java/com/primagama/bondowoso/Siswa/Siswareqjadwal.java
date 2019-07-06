package com.primagama.bondowoso.Siswa;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.primagama.bondowoso.Absen;
import com.primagama.bondowoso.Adapter.AdapterRequestjadwal;
import com.primagama.bondowoso.Controller;
import com.primagama.bondowoso.Login;
import com.primagama.bondowoso.Model.Modelreqjadwal;
import com.primagama.bondowoso.R;
import com.primagama.bondowoso.Server;
import com.primagama.bondowoso.SiswaProfil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.primagama.bondowoso.Login.TAG_USER;

public class Siswareqjadwal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Spinner.OnItemSelectedListener {
    ConnectivityManager conMgr;

    String tag_json_obj = "json_obj_req";

    private String url = Server.URL + "api/siswa/siswa/mapelforReqjadwal/";
    private String url_reqjadwal = Server.URL + "api/siswa/siswa/Requestjadwal/";
    private String url_getreqjadwal = Server.URL + "api/siswa/siswa/getRequestjadwal/";

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




    private Spinner spinner;

    int success;

    private ArrayList<String> mapel;

    private JSONArray result;

    public TextView idmapel, tgl;

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormat;
    private Button btdatepick, req_jadwal;

    SharedPreferences sharedpreferences;
    Boolean session = false;

    public static final String my_shared_preferences = "my_shared_preferences";
    public static final String session_status = "session_status";

    //show req jadwal

    private RecyclerView recyclerViewreqjadwal;
    private AdapterRequestjadwal mAdapter;
    private ProgressDialog mProgressDialog;
    private List<Modelreqjadwal> mListData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_siswareqjadwal);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        // Cek session login
        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        id_siswa = sharedpreferences.getString(TAG_ID, null);
        username = sharedpreferences.getString(TAG_USERNAME, null);
        kelas = sharedpreferences.getString(TAG_KELAS, null);
        id_kelas = sharedpreferences.getString(TAG_IDKELAS, null);
        user = sharedpreferences.getString(TAG_USER, null);
        id_program = sharedpreferences.getString(TAG_IDPROGRAM, null);

//        drawer
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.txt_username);
        TextView navKelas = (TextView) headerView.findViewById(R.id.txt_kelas);
        navUsername.setText(username);
        navKelas.setText(kelas);

//ambil dari inten

        id_siswa = getIntent().getStringExtra(TAG_ID);
        username = getIntent().getStringExtra(TAG_USERNAME);
        kelas = getIntent().getStringExtra(TAG_KELAS);
        id_program = getIntent().getStringExtra(TAG_IDPROGRAM);
        id_kelas = getIntent().getStringExtra(TAG_IDKELAS);

        Log.d("reqjadwal", "id :"+id_siswa+", id_prog: "+id_program+", username: "+username
                +", id_kelas: "+id_kelas+", kelas: "+kelas);

        mapel = new ArrayList<String>();

        spinner =(Spinner) findViewById(R.id.listmapel);

        spinner.setOnItemSelectedListener(this);

        idmapel = (TextView) findViewById(R.id.id_mapel);

//        Dateformat
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        tgl = (TextView) findViewById(R.id.tgl_terpilih);

        req_jadwal = (Button) findViewById(R.id.btn_reqjdwl);
        req_jadwal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tgal = tgl.getText().toString();
                if (tgal == "Pilih Tanggal"){
                    Toast.makeText(Siswareqjadwal.this, "Silahkan tentukan jadwal dulu", Toast.LENGTH_LONG).show();
                }else {
                    request_jadwal();
                }

            }
        });

        btdatepick = (Button) findViewById(R.id.btntgl);
        btdatepick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdDatepick();
            }
        });

        //reqjadwal
        recyclerViewreqjadwal = (RecyclerView) findViewById(R.id.list_reqjadwal);

        mListData = new ArrayList<>();

        getlistmapel();
        getreqjadwal();
    }

    public void getreqjadwal(){
        StringRequest strRequest = new StringRequest(Request.Method.POST, url_getreqjadwal, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("SHOW REQ JADWAL", response.toString());
                iniData(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR", error.getMessage());
                Toast.makeText(Siswareqjadwal.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_siswa", id_siswa);
                params.put("id_program", id_program);

                return params;
            }
        };
        Controller.getInstance().addToRequestQueue(strRequest, tag_json_obj);
    }

    public void iniData(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);

            JSONArray jsonArray = jsonObject.getJSONArray("reqjadwal");

            for (int i = 0; i<jsonArray.length(); i++){
                JSONObject reqjadwal = jsonArray.getJSONObject(i);

                String id_reqmapel = reqjadwal.getString("id_reqmapel");
                String nama_reqmapel = reqjadwal.getString("nama_reqmapel");
                String ada = reqjadwal.getString("ada");
                String total_request = reqjadwal.getString("total_request");

                Modelreqjadwal modelreqjadwal = new Modelreqjadwal();
                modelreqjadwal.setId_reqmapel(id_reqmapel);
                modelreqjadwal.setNama_reqmapel(nama_reqmapel);
                modelreqjadwal.setTotal(total_request);

                mListData.add(modelreqjadwal);

                mAdapter = new AdapterRequestjadwal(mListData, Siswareqjadwal.this);
                mAdapter.notifyDataSetChanged();
                recyclerViewreqjadwal.setLayoutManager(new LinearLayoutManager(Siswareqjadwal.this));
                recyclerViewreqjadwal.setItemAnimator(new DefaultItemAnimator());
                recyclerViewreqjadwal.setAdapter(mAdapter);

            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void request_jadwal(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_reqjadwal, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("REQ jADWAL", response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    if (success == 1) {
                        Log.d("Reqjadwal", jObj.toString());

                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR", error.getMessage());
                Toast.makeText(Siswareqjadwal.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params =  new HashMap<String, String>();
                params.put("id_siswa", id_siswa);
                params.put("tanggal", tgl.getText().toString());
                params.put("mapel", idmapel.getText().toString());
                params.put("id_grade", Integer.toString(12));

                return params;
            }
        };

        Controller.getInstance().addToRequestQueue(stringRequest, tag_json_obj);
    }

    private void showdDatepick(){
        Calendar calendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                tgl.setText(dateFormat.format(newDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH) );
        datePickerDialog.show();
    }

    private void getlistmapel(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url+id_program, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject j = null;
                try {
                    j = new JSONObject(response);

                    result = j.getJSONArray("listmapel");
                    Log.d("result", result.toString());
                    getMapel(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getMapel(JSONArray j){
        for (int i=0; i<j.length(); i++){
            try {
                JSONObject json = j.getJSONObject(i);

                mapel.add(json.getString(TAG_MAPEL));
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        spinner.setAdapter(new ArrayAdapter<String>(Siswareqjadwal.this, android.R.layout.simple_spinner_dropdown_item, mapel));
    }

    private String getIdMapel(int position){
        String idmpl ="";
        try {
            JSONObject json = result.getJSONObject(position);
            idmpl = json.getString(TAG_IDMAPEL);
            Log.d("id_mapel", idmpl);
        }catch (JSONException e){
            e.printStackTrace();
        }

        return idmpl;
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
            Intent home = new Intent(this, Homesiswa.class);
            home.putExtra(TAG_USER, user);
            home.putExtra(TAG_ID, id_siswa);
            home.putExtra(TAG_USERNAME, username);
            home.putExtra(TAG_KELAS, kelas);
            home.putExtra(TAG_IDKELAS, id_kelas);
            home.putExtra(TAG_IDPROGRAM, id_program);
            Log.d("reqjadwal", "id :"+id_siswa+", id_prog: "+id_program+", username: "+username
                    +", id_kelas: "+id_kelas+", kelas: "+kelas);
            finish();
            startActivity(home);

        } else if (id == R.id.reqjadwal) {
            Intent reqjadwal = new Intent(Siswareqjadwal.this, Siswareqjadwal.class);
            reqjadwal.putExtra(TAG_ID, id_siswa);
            reqjadwal.putExtra(TAG_USERNAME, username);
            reqjadwal.putExtra(TAG_KELAS, kelas);
            reqjadwal.putExtra(TAG_IDKELAS, id_kelas);
            reqjadwal.putExtra(TAG_IDPROGRAM, getIntent().getStringExtra(TAG_IDPROGRAM));
            Log.d("reqjadwal", "id :"+id_siswa+", id_prog: "+id_program+", username: "+username
                    +", id_kelas: "+id_kelas+", kelas: "+kelas);
            finish();
            startActivity(reqjadwal);


        } else if (id == R.id.nav_absen) {
            Intent absen = new Intent(Siswareqjadwal.this, Absen.class);
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
            Intent nilai = new Intent(Siswareqjadwal.this, Nilai.class);
            nilai.putExtra(TAG_ID, id_siswa);
            nilai.putExtra(TAG_USERNAME, username);
            nilai.putExtra(TAG_KELAS, kelas);
            nilai.putExtra(TAG_IDKELAS, id_kelas);
            nilai.putExtra(TAG_IDPROGRAM, getIntent().getStringExtra(TAG_IDPROGRAM));
            Log.d("reqjadwal", "id :"+id_siswa+", id_prog: "+id_program+", username: "+username
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

                            Intent intent = new Intent(Siswareqjadwal.this, Login.class);
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

//        Log.d("positition", Integer.toString(position));
        idmapel.setText(getIdMapel(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        idmapel.setText("");
    }
}
