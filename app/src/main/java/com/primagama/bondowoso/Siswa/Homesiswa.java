package com.primagama.bondowoso.Siswa;

import android.app.AlertDialog;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.primagama.bondowoso.Absen;
import com.primagama.bondowoso.Adapter.AdapterListjadwal;
import com.primagama.bondowoso.Login;
import com.primagama.bondowoso.Model.ModelJadwal;
import com.primagama.bondowoso.R;
import com.primagama.bondowoso.Server;
import com.primagama.bondowoso.SiswaProfil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Homesiswa extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ConnectivityManager conMgr;

    String tag_json_obj = "json_obj_req";

    Button btn_logout;


    private String url = Server.URL + "api/siswa/Siswa/getjadwaltetap/";

    String id_siswa, username, kelas, id_kelas, id_program, user;
    SharedPreferences sharedpreferences;

    public static final String TAG_ID = "id";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_KELAS = "kelas";
    public static final String TAG_IDPROGRAM = "id_program";
    public static final String TAG_IDKELAS = "id_kelas";

    private static final String TAG = Homesiswa.class.getSimpleName();

    public final static String TAG_USER = "user";

    private RecyclerView recyclerViewjadwal;
    private AdapterListjadwal mAdapter;
    private ProgressDialog mProgressDialog;
    private List<ModelJadwal> mListData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homesiswa);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_siswa);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

//        txt_username = (TextView) findViewById(R.id.txt_username);
        btn_logout = (Button) findViewById(R.id.btn_logout);


     sharedpreferences = getSharedPreferences(Login.my_shared_preferences, Context.MODE_PRIVATE);

        id_siswa = getIntent().getStringExtra(TAG_ID);
        username = getIntent().getStringExtra(TAG_USERNAME);
        kelas = getIntent().getStringExtra(TAG_KELAS);
        id_program = getIntent().getStringExtra(TAG_IDPROGRAM);
        id_kelas = getIntent().getStringExtra(TAG_IDKELAS);

        Log.d("ABSEN", "id :"+id_siswa+", id_prog: "+id_program+", username: "+username
                +", id_kelas: "+id_kelas+", kelas: "+kelas);

        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.txt_username);
        TextView navKelas = (TextView) headerView.findViewById(R.id.txt_kelas);
        navUsername.setText(username);
        navKelas.setText(kelas);

        btn_logout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // session destroy
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean(Login.session_status, false);
                editor.putString(TAG_ID, null);
                editor.putString(TAG_USERNAME, null);
                editor.putString(TAG_KELAS, null);
                editor.putString(TAG_IDPROGRAM, null);
                editor.putString(TAG_IDKELAS, null);
                editor.commit();

                Intent intent = new Intent(Homesiswa.this, Login.class);
                finish();
                startActivity(intent);
            }
        });

        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if (conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected()) {
//                getJadwal(kelas);
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection",
                        Toast.LENGTH_LONG).show();
            }
        }
//
        recyclerViewjadwal = (RecyclerView) findViewById(R.id.jadwal) ;

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Loading.....");
        mProgressDialog.show();

        mListData = new ArrayList<>();

        getJadwal();
//
////        drawer
//        View view = (View) findViewById(R.id.reqjadwal);
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent reqjadwal = new Intent(Homesiswa.this, Siswareqjadwal.class);
//                startActivity(reqjadwal);
//                finish();
//            }String id, username, kelas, id_kelas, id_program, user;
//        });
    }

    public void getJadwal(){
        final StringRequest request = new StringRequest(Request.Method.GET, url+kelas, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mProgressDialog.dismiss();
                iniData(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void iniData(String response){
        try{
            JSONObject jsonObject = new JSONObject(response);

            JSONArray jsonArray = jsonObject.getJSONArray("response");

            for(int i=0; i<jsonArray.length(); i++){
                JSONObject jadwal = jsonArray.getJSONObject(i);

                String hari = jadwal.getString("hari");
                String mapel = jadwal.getString("mapel");
                String tentor = jadwal.getString("tentor");
                String ruang = jadwal.getString("ruang");
                String jam = jadwal.getString("jam");

                ModelJadwal jadwalModel = new ModelJadwal();
                jadwalModel.setHari(hari);
                jadwalModel.setMapel(mapel);
                jadwalModel.setTentor(tentor);
                jadwalModel.setRuang(ruang);
                jadwalModel.setJam(jam);

                mListData.add(jadwalModel);

                mAdapter = new AdapterListjadwal(mListData, Homesiswa.this);
                mAdapter.notifyDataSetChanged();
                recyclerViewjadwal.setLayoutManager(new LinearLayoutManager(Homesiswa.this));
                recyclerViewjadwal.setItemAnimator(new DefaultItemAnimator());
                recyclerViewjadwal.setAdapter(mAdapter);
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            new AlertDialog.Builder(this)
                    .setMessage("Apakah Kamu Yakin akan Keluar?")
                    .setCancelable(false)
                    .setPositiveButton("YA", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Homesiswa.this.finish();
                        }
                    })
                    .setNegativeButton("Tidak", null)
                    .show();
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
            home.putExtra(TAG_ID, id_siswa);
            home.putExtra(TAG_USERNAME, username);
            home.putExtra(TAG_KELAS, kelas);
            home.putExtra(TAG_IDKELAS, id_kelas);
            home.putExtra(TAG_IDPROGRAM, getIntent().getStringExtra(TAG_IDPROGRAM));
            Log.d("reqjadwal", "id :"+id_siswa+", id_prog: "+id_program+", username: "+username
                    +", id_kelas: "+id_kelas+", kelas: "+kelas);
            finish();
            startActivity(home);

        } else if (id == R.id.reqjadwal) {
            Intent reqjadwal = new Intent(Homesiswa.this, Siswareqjadwal.class);
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
            Intent absen = new Intent(Homesiswa.this, Absen.class);
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
            Intent nilai = new Intent(Homesiswa.this, Nilai.class);
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
            // session destroy
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

                            Intent intent = new Intent(Homesiswa.this, Login.class);
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
}
