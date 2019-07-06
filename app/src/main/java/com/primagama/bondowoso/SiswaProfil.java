package com.primagama.bondowoso;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.primagama.bondowoso.Profile.Ortu;
import com.primagama.bondowoso.Profile.Siswasiswa;
import com.primagama.bondowoso.Siswa.Homesiswa;
import com.primagama.bondowoso.Siswa.Nilai;
import com.primagama.bondowoso.Siswa.Siswareqjadwal;
import com.primagama.bondowoso.ui.main.SectionsPagerAdapter;

public class SiswaProfil extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    String tag_json_obj = "json_obj_req";

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

    SectionsPagerAdapter mAdapter;

    private SectionsPagerAdapter mSectionPagerAdapter;
    private ViewPager mviewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absen);

        mSectionPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mviewpager = (ViewPager) findViewById(R.id.view_pager);
        setupViewPager(mviewpager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mviewpager);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_siswa);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

            //data
        sharedpreferences = getSharedPreferences(Login.my_shared_preferences, Context.MODE_PRIVATE);

        id_siswa = getIntent().getStringExtra(TAG_ID);
        username = getIntent().getStringExtra(TAG_USERNAME);
        kelas = getIntent().getStringExtra(TAG_KELAS);
        id_program = getIntent().getStringExtra(TAG_IDPROGRAM);
        id_kelas = getIntent().getStringExtra(TAG_IDKELAS);

        Log.d("Profil", "id :"+id_siswa+", id_prog: "+id_program+", username: "+username
                +", id_kelas: "+id_kelas+", kelas: "+kelas);

//        drawer
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.txt_username);
        TextView navKelas = (TextView) headerView.findViewById(R.id.txt_kelas);
        navUsername.setText(username);
        navKelas.setText(kelas);


    }

    private void setupViewPager(ViewPager viewPager){
        SectionsPagerAdapter profil = new SectionsPagerAdapter(getSupportFragmentManager());
        profil.addFragment(new Siswasiswa(), "Siswa");
        profil.addFragment(new Ortu(), "Orang Tua");

        viewPager.setAdapter(profil);
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
            Intent reqjadwal = new Intent(this, Siswareqjadwal.class);
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
            Intent absen = new Intent(this, Absen.class);
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
            Intent nilai = new Intent(this, Nilai.class);
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

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void Drawer(View view) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.openDrawer(GravityCompat.START);
    }
}