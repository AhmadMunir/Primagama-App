package com.primagama.bondowoso;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import com.primagama.bondowoso.Absen_bulan.Absenags;
import com.primagama.bondowoso.Absen_bulan.Absenap;
import com.primagama.bondowoso.Absen_bulan.Absendes;
import com.primagama.bondowoso.Absen_bulan.Absenfeb;
import com.primagama.bondowoso.Absen_bulan.Absenjan;
import com.primagama.bondowoso.Absen_bulan.Absenjul;
import com.primagama.bondowoso.Absen_bulan.Absenjun;
import com.primagama.bondowoso.Absen_bulan.Absenmar;
import com.primagama.bondowoso.Absen_bulan.Absenmei;
import com.primagama.bondowoso.Absen_bulan.Absennov;
import com.primagama.bondowoso.Absen_bulan.Absenokt;
import com.primagama.bondowoso.Absen_bulan.Absensep;
import com.primagama.bondowoso.Ortu.Homeortu;
import com.primagama.bondowoso.Ortu.Jadwalanak;
import com.primagama.bondowoso.Ortu.Nilaiortu;
import com.primagama.bondowoso.Siswa.Homesiswa;
import com.primagama.bondowoso.Siswa.Nilai;
import com.primagama.bondowoso.Siswa.Siswareqjadwal;
import com.primagama.bondowoso.ui.main.SectionsPagerAdapter;

import static com.primagama.bondowoso.Ortu.Homeortu.TAG_ANAK;

public class Absenortu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    String tag_json_obj = "json_obj_req";

    private String url = Server.URL + "api/siswa/Siswa/getjadwaltetap/";

    String id_siswa, username, kelas, id_kelas, id_program, user, anak;
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
        setContentView(R.layout.activity_absenortu);

        mSectionPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mviewpager = (ViewPager) findViewById(R.id.view_pager);
        setupViewPager(mviewpager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mviewpager);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_ortu);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

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

    }

    private void setupViewPager(ViewPager viewPager){
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Absenjan(), "Jan");
        adapter.addFragment(new Absenfeb(), "Feb");
        adapter.addFragment(new Absenmar(), "Mar");
        adapter.addFragment(new Absenap(), "Apr");
        adapter.addFragment(new Absenmei(), "Mei");
        adapter.addFragment(new Absenjun(), "Jun");
        adapter.addFragment(new Absenjul(), "Jul");
        adapter.addFragment(new Absenags(), "Ags");
        adapter.addFragment(new Absensep(), "Sep");
        adapter.addFragment(new Absenokt(), "Okt");
        adapter.addFragment(new Absennov(), "Nov");
        adapter.addFragment(new Absendes(), "Des");

        viewPager.setAdapter(adapter);
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

                            Intent intent = new Intent(Absenortu.this, Login.class);
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

    public void Drawer(View view) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.openDrawer(GravityCompat.START);
    }
}