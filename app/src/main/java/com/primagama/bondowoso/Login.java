package com.primagama.bondowoso;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.primagama.bondowoso.Ortu.Homeortu;
import com.primagama.bondowoso.Siswa.Homesiswa;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    ProgressDialog pDialog;
    Button  btn_login;
    EditText txt_username, txt_password;
    Intent intent;

    int success;
    ConnectivityManager conMgr;

    private String url = Server.URL + "/Login_api/proses";

    private static final String TAG = Login.class.getSimpleName();

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    public final static String TAG_USERNAME = "username";
    public final static String TAG_ID = "id";
    public final static String TAG_FOTO = "foto";

    public final static String TAG_KELAS = "kelas";
    public final static String TAG_IDPROGRAM = "id_program";
    public final static String TAG_IDKELAS = "id_kelas";
    public final static String TAG_ANAK = "anak";

    public final static String TAG_IDSISWA = "id_siswa";
    public final static String TAG_PROGRAM = "program";
    public final static String TAG_SEKOLAH = "sekolah";


    public final static String TAG_USER = "user";

    String tag_json_obj = "json_obj_req";

    SharedPreferences sharedpreferences;
    Boolean session = false;
    String id, username, kelas, id_kelas, user, id_program, fp, id_siswa, program, sekolah,anak;

    public static final String my_shared_preferences = "my_shared_preferences";
    public static final String session_status = "session_status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if (conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected()) {
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection",
                        Toast.LENGTH_LONG).show();
            }
        }

        btn_login = (Button) findViewById(R.id.btn_login);
        txt_username = (EditText) findViewById(R.id.txt_username);
        txt_password = (EditText) findViewById(R.id.txt_password);

        // Cek session login
        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        id = sharedpreferences.getString(TAG_ID, null);
        username = sharedpreferences.getString(TAG_USERNAME, null);
        kelas = sharedpreferences.getString(TAG_KELAS, null);
        id_kelas = sharedpreferences.getString(TAG_IDKELAS, null);
        user = sharedpreferences.getString(TAG_USER, null);
        id_program = sharedpreferences.getString(TAG_IDPROGRAM, null);
        fp = sharedpreferences.getString(TAG_FOTO, null);
        id_siswa = sharedpreferences.getString(TAG_IDSISWA, null);
        program = sharedpreferences.getString(TAG_PROGRAM, null);
        sekolah = sharedpreferences.getString(TAG_SEKOLAH, null);
        anak = sharedpreferences.getString(TAG_ANAK, null);

        // jika true maka start mainactivity
        if (session) {
            switch (user){
                case "siswa":
                    Intent intent = new Intent(Login.this, Homesiswa.class);
                    intent.putExtra(TAG_ID, id);
                    intent.putExtra(TAG_USERNAME, username);
                    intent.putExtra(TAG_KELAS, kelas);
                    intent.putExtra(TAG_IDKELAS, id_kelas);
                    intent.putExtra(TAG_IDPROGRAM, id_program);
                    intent.putExtra(TAG_FOTO, fp);
                    Log.d("reqjadwal", "id :"+id+", id_prog: "+id_program+", username: "+username
                            +", id_kelas: "+id_kelas+", kelas: "+kelas);
                    finish();
                    startActivity(intent);
                    break;
                case "ortu":
                    Intent ortu = new Intent(Login.this, Homeortu.class);
                    ortu.putExtra(TAG_ID, id);
                    ortu.putExtra(TAG_USERNAME, username);
                    ortu.putExtra(TAG_KELAS, kelas);
                    ortu.putExtra(TAG_IDKELAS, id_kelas);
                    ortu.putExtra(TAG_IDSISWA, id_siswa);
                    ortu.putExtra(TAG_ANAK, anak);
                    ortu.putExtra(TAG_IDPROGRAM, id_program);
                    ortu.putExtra(TAG_KELAS, kelas);
                    finish();
                    startActivity(ortu);
                    break;
                    default:
                        break;

            }
        }


        btn_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String username = txt_username.getText().toString();
                String password = txt_password.getText().toString();

                //cek empty form
                if (username.trim().length() > 0 && password.trim().length() > 0) {
                    if (conMgr.getActiveNetworkInfo() != null
                            && conMgr.getActiveNetworkInfo().isAvailable()
                            && conMgr.getActiveNetworkInfo().isConnected()) {
                        checkLogin(username, password);
                    } else {
                        Toast.makeText(getApplicationContext() ,"No Connection", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext() ,"Field not be empty", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    private void checkLogin(final String username, final String password) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Login Process ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Login Status: " + response.toString());
                hideDialog();
                SharedPreferences.Editor editor = sharedpreferences.edit();

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Check error json
                    switch (success){
                        case 1 :
                            String username = jObj.getString(TAG_USERNAME);
                            String id = jObj.getString(TAG_ID);
                            String kelas = jObj.getString(TAG_KELAS);
                            String id_kelas = jObj.getString(TAG_IDKELAS);
                            String user = jObj.getString(TAG_USER);
                            String id_program = jObj.getString(TAG_IDPROGRAM);
                            String fto = jObj.getString(TAG_FOTO);



                            Log.e("Login Success!", jObj.toString());

                            Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                            // menyimpan data login ke session
//                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putBoolean(session_status, true);

                            editor.putString(TAG_USER, user);
                            editor.putString(TAG_ID, id);
                            editor.putString(TAG_USERNAME, username);
                            editor.putString(TAG_KELAS, kelas);
                            editor.putString(TAG_IDKELAS, id_kelas);
                            editor.putString(TAG_IDPROGRAM, id_program);
                            editor.putString(TAG_FOTO, fto);
                            editor.putString(TAG_IDSISWA, id);

                            editor.commit();


                            Intent intent = new Intent(Login.this, Homesiswa.class);
                            intent.putExtra(TAG_USER, user);
                            intent.putExtra(TAG_ID, id);
                            intent.putExtra(TAG_USERNAME, username);
                            intent.putExtra(TAG_KELAS, kelas);
                            intent.putExtra(TAG_IDKELAS, id_kelas);
                            intent.putExtra(TAG_IDPROGRAM, id_program);
                            intent.putExtra(TAG_FOTO, fto);


                            Log.d("reqjadwal", "id :"+id+", id_prog: "+id_program+", username: "+username
                                    +", id_kelas: "+id_kelas+", kelas: "+kelas);
                            finish();
                            startActivity(intent);

                            break;

                        case 2 :
                            String usernam = jObj.getString(TAG_USERNAME);
                            String ank = jObj.getString("anak");
                            String ortu = jObj.getString(TAG_USER);
                            String id_siswa = jObj.getString(TAG_IDSISWA);
                            String klas = jObj.getString(TAG_KELAS);
                            String program = jObj.getString(TAG_PROGRAM);
                            String sekolah = jObj.getString(TAG_SEKOLAH);



                            Log.e("Login Success!", jObj.toString());

                            Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();


                            editor.putBoolean(session_status, true);
                            editor.putString(TAG_ID, id_siswa);
                            editor.putString(TAG_USER, ortu);
                            editor.putString(TAG_IDSISWA, id_siswa);
                            editor.putString(TAG_USERNAME, usernam);
                            editor.putString(TAG_ANAK, ank);
                            editor.putString(TAG_KELAS, klas);
                            editor.putString(TAG_PROGRAM, program);
                            editor.putString(TAG_SEKOLAH, sekolah);

                            editor.commit();


                            Intent inten = new Intent(Login.this, Homeortu.class);
                            inten.putExtra(TAG_ID, id_siswa);
                            inten.putExtra(TAG_USERNAME, usernam);
                            inten.putExtra(TAG_ANAK, ank);
                            inten.putExtra(TAG_USER, ortu);
                            inten.putExtra(TAG_IDSISWA, id_siswa);
                            inten.putExtra(TAG_KELAS, klas);
                            inten.putExtra(TAG_PROGRAM, program);
                            inten.putExtra(TAG_SEKOLAH, sekolah);

                                Log.i("Login Ortu ", ", username :" + usernam+
                                        ", anak : "+ank+
                                        ", id_siswa : "+ id_siswa+
                                        ", kelas : "+klas+
                                        ", program : "+ program+
                                        ", sekolah : "+ sekolah);
                            finish();
                            startActivity(inten);
                            break;

                        default:
                            Toast.makeText(getApplicationContext(),
                                jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                            break;
                    }
//
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

                hideDialog();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }

        };

        Controller.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}
