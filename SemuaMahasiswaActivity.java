package prak11_00000055770.com;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SemuaMahasiswaActivity extends AppCompatActivity {

    ProgressBar progressBar2;
    ListView lv;
    ArrayList<HashMap<String,String>> list_anggota;
    String url_get_mahasiswa="http://192.168.88.10/Week10/getMahasiswa.php";//ganti dengan url kalian sendiri

    static String json = null;

    private static final String TAG_MAHASISWA="data";
    private static final String TAG_ID="id";
    private static final String TAG_NAMA="nama";
    private static final String TAG_ALAMAT = "alamat";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semua_mahasiswa);

        list_anggota=new ArrayList<>();
        lv=findViewById(R.id.listView);
        progressBar2=findViewById(R.id.progressBar2);
        progressBar2.setVisibility(View.VISIBLE);

        RequestQueue queue = Volley.newRequestQueue(SemuaMahasiswaActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_get_mahasiswa, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                    JSONArray member = jObj.getJSONArray(TAG_MAHASISWA);

                    for (int i = 0; i < member.length(); i++) {
                        JSONObject a = member.getJSONObject(i);
                        String id = a.getString(TAG_ID);
                        String nama = a.getString(TAG_NAMA);
                        String alamat = a.getString(TAG_ALAMAT);

                        HashMap<String, String> map = new HashMap<>();
                        map.put("id", id);
                        map.put("nama", nama);
                        map.put("alamat",alamat);

                        list_anggota.add(map);
                        String[] from = {"id", "nama", "alamat"};
                        int[] to = {R.id.edtID, R.id.edtNama, R.id.edtAlamat};

                        ListAdapter adapter = new SimpleAdapter(
                                SemuaMahasiswaActivity.this, list_anggota, R.layout.list_item,
                                from, to);
                        lv.setAdapter(adapter);
                        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                            @Override
                            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                                String nomor = list_anggota.get(position).get(TAG_ID);
                                Intent i = new Intent(getApplicationContext(),EditMahasiwaActivity.class);
                                i.putExtra("id", nomor);
                                startActivity(i);

                                return true;
                            }
                        });
                    }
                }
                catch (Exception ex) {
                        Log.e("Error", ex.toString());
                        progressBar2.setVisibility(View.GONE);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Error", error.getMessage());
                    Toast.makeText(SemuaMahasiswaActivity.this, "silahkan cek koneksi internet anda", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
            queue.add(stringRequest);
            progressBar2.setVisibility(View.GONE);
    }
}