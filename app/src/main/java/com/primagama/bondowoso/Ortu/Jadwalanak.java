package com.primagama.bondowoso.Ortu;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.primagama.bondowoso.Absenortu;
import com.primagama.bondowoso.Adapter.AdapterListjadwal;
import com.primagama.bondowoso.Login;
import com.primagama.bondowoso.Model.ModelJadwal;
import com.primagama.bondowoso.R;
import com.primagama.bondowoso.Server;
import com.primagama.bondowoso.Siswa.Nilai;
import com.primagama.bondowoso.Siswa.Siswareqjadwal;
import com.primagama.bondowoso.SiswaProfil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.primagama.bondowoso.Ortu.Homeortu.TAG_ANAK;

public class Jadwalanak extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ConnectivityManager conMgr;

    String tag_json_obj = "json_obj_req";

    Button btn_logout;


    private String url = Server.URL + "api/siswa/Siswa/getjadwaltetap/";

    String id_siswa, username, kelas, id_kelas, id_program, user, anak;
    SharedPreferences sharedpreferences;

    public static final String TAG_ID = "id";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_KELAS = "kelas";
    public static final String TAG_IDPROGRAM = "id_program";
    public static final String TAG_IDKELAS = "id_kelas";

    private static final String TAG = Jadwalanak.class.getSimpleName();

    public final static String TAG_USER = "user";

    private RecyclerView recyclerViewjadwal;
    private AdapterListjadwal mAdapter;
    private ProgressDialog mProgressDialog;
    private List<ModelJadwal> mListData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jadwalanak);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_ortu);
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
        anak = getIntent().getStringExtra(TAG_ANAK);
        id_kelas = getIntent().getStringExtra(TAG_IDKELAS);

        Log.d("ABSEN", "id :"+id_siswa+", id_prog: "+id_program+", username: "+username
                +", id_kelas: "+id_kelas+", kelas: "+kelas);

        View headerView = navigationView.getHeaderView(0);
        TextView navAnak = (TextView) headerView.findViewById(R.id.txt_anak);
        navAnak.setText(anak);


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

                Intent intent = new Intent(Jadwalanak.this, Login.class);
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

                mAdapter = new AdapterListjadwal(mListData, Jadwalanak.this);
                mAdapter.notifyDataSetChanged();
                recyclerViewjadwal.setLayoutManager(new LinearLayoutManager(Jadwalanak.this));
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
                            Jadwalanak.this.finish();
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

        } else if (id == R.id.nav_homeortu) {
            Intent home = new Intent(this, Homeortu.class);
            home.putExtra(TAG_ID, id_siswa);
            home.putExtra(TAG_USERNAME, username);
            home.putExtra(TAG_KELAS, kelas);
            home.putExtra(TAG_ANAK, anak);
            home.putExtra(TAG_IDPROGRAM, getIntent().getStringExtra(TAG_IDPROGRAM));
            Log.d("Home", "id :"+id_siswa+", id_prog: "+id_program+", username: "+username
                    +", id_kelas: "+id_kelas+", kelas: "+kelas);
            finish();
            startActivity(home);

        } else if (id == R.id.nav_jadwalanak) {
            Intent jadwalanak = new Intent(this, Jadwalanak.class);
            jadwalanak.putExtra(TAG_ID, id_siswa);
            jadwalanak.putExtra(TAG_USERNAME, username);
            jadwalanak.putExtra(TAG_KELAS, kelas);
            jadwalanak.putExtra(TAG_ANAK, anak);
            jadwalanak.putExtra(TAG_IDPROGRAM, getIntent().getStringExtra(TAG_IDPROGRAM));
            Log.d("jadwal", "id :"+id_siswa+", id_prog: "+id_program+", username: "+username
                    +", id_kelas: "+id_kelas+", kelas: "+kelas);
            finish();
            startActivity(jadwalanak);
        } else if (id == R.id.nav_nilaianak) {
            Intent nilaianak = new Intent(this, Nilaiortu.class);
            nilaianak.putExtra(TAG_ID, id_siswa);
            nilaianak.putExtra(TAG_USERNAME, username);
            nilaianak.putExtra(TAG_KELAS, kelas);
            nilaianak.putExtra(TAG_ANAK, anak);
            nilaianak.putExtra(TAG_IDPROGRAM, id_program);
            Log.d("Nilai", "id :"+id_siswa+", id_prog: "+id_program+", username: "+username
                    +", id_kelas: "+id_kelas+", kelas: "+kelas);
            finish();
            startActivity(nilaianak);

        } else if (id == R.id.nav_absenanak) {
            Intent habsenor = new Intent(this, Absenortu.class);
            habsenor.putExtra(TAG_ID, id_siswa);
            habsenor.putExtra(TAG_USERNAME, username);
            habsenor.putExtra(TAG_KELAS, kelas);
            habsenor.putExtra(TAG_ANAK, anak);
            habsenor.putExtra(TAG_IDPROGRAM, getIntent().getStringExtra(TAG_IDPROGRAM));
            Log.d("Home", "id :"+id_siswa+", id_prog: "+id_program+", username: "+username
                    +", id_kelas: "+id_kelas+", kelas: "+kelas);
            finish();
            startActivity(habsenor);

        } else if (id == R.id.nav_logout) {
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

                            Intent intent = new Intent(Jadwalanak.this, Login.class);
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
