package com.primagama.bondowoso;

import android.app.ProgressDialog;
import android.content.Context;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Homesiswa extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ConnectivityManager conMgr;

    String tag_json_obj = "json_obj_req";

    Button btn_logout;

    private String url = Server.URL + "api/siswa/jadwal/getjadwaltetap/";

    String id, username, kelas, id_kelas;
    SharedPreferences sharedpreferences;

    public static final String TAG_ID = "id";
    public static final String TAG_USERNAME = "username";
    public final static String TAG_KELAS = "kelas";
    public final static String TAG_IDKELAS = "id_kelas";

    private static final String TAG = Homesiswa.class.getSimpleName();

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


//        getJadwal(kelas);
        sharedpreferences = getSharedPreferences(Login.my_shared_preferences, Context.MODE_PRIVATE);

        id = getIntent().getStringExtra(TAG_ID);
        username = getIntent().getStringExtra(TAG_USERNAME);
        kelas = getIntent().getStringExtra(TAG_KELAS);

//        txt_username.setText(username);

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
                editor.commit();

                Intent intent = new Intent(com.primagama.bondowoso.Homesiswa.this, Login.class);
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
        recyclerViewjadwal = (RecyclerView) findViewById(R.id.jadwal);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Loading.....");
        mProgressDialog.show();

        mListData = new ArrayList<>();

        getJadwal();
    }

    public void getJadwal(){
        final StringRequest request = new StringRequest(Request.Method.GET, url+id, new Response.Listener<String>() {
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
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
