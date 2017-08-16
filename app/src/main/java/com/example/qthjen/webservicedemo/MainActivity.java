package com.example.qthjen.webservicedemo;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    String url = "http://192.168.1.3/androidwebservice/getdata.php";
    String urlInsert = "http://192.168.1.3/androidwebservice/insert.php";
    String urlUpdate = "http://192.168.1.3/androidwebservice/update.php";
    String urlDelete = "http://192.168.1.3/androidwebservice/delete.php";

    ArrayList<SinhVien> listSinhVien;
    CustomAdapter customAdapter;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = (ListView) findViewById(R.id.lv);

        listSinhVien = new ArrayList<SinhVien>();
        customAdapter = new CustomAdapter(MainActivity.this, R.layout.custom_listview, listSinhVien);

        ReadData(url);

        lv.setAdapter(customAdapter);

    }

    private void ReadData(String uri) {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, uri, null,
                new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for ( int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        listSinhVien.add(new SinhVien(
                                jsonObject.getInt("ID"),
                                jsonObject.getString("HoTen"),
                                jsonObject.getString("NamSinh"),
                                jsonObject.getString("DiaChi")));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                customAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if ( item.getItemId() == R.id.mnAdd ){
            AddDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    private void AddDialog() {

        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_add);

        final EditText etEnterName = (EditText) dialog.findViewById(R.id.etEnterName);
        final EditText etEnterBerthday = (EditText) dialog.findViewById(R.id.etEnterBerthday);
        final EditText etEnterAdress = (EditText) dialog.findViewById(R.id.etEnterAdress);
        Button btCancel = (Button) dialog.findViewById(R.id.btCancel);
        Button btAdd = (Button) dialog.findViewById(R.id.btAdd);

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ( etEnterName.getText().toString().trim().isEmpty() || etEnterBerthday.getText().toString().trim().isEmpty()
                        || etEnterAdress.getText().toString().trim().isEmpty()) {

                    Toast.makeText(MainActivity.this, "Please enter information", Toast.LENGTH_SHORT).show();

                } else {

                StringRequest stringRequest = new StringRequest(Request.Method.POST, urlInsert, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if ( response.toString().trim().equals("success")) {
                            Toast.makeText(MainActivity.this, "Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
                ){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        Map<String, String> params = new HashMap<>();
                        params.put("hotenSV", etEnterName.getText().toString().trim());
                        params.put("namsinhSV", etEnterBerthday.getText().toString().trim());
                        params.put("diachiSV", etEnterAdress.getText().toString().trim());

                        return params;
                    }
                };

                    RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                    requestQueue.add(stringRequest);

                    dialog.dismiss();

                    /** reload mainActivity khi tắt cửa sổ dialog **/
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);

                }

            }

        });

        dialog.show();

    }

    public void EditDialog(final int idSV, final String nameSV, final String berthdaySV, final String AdressSV) {

        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.edit_activity);

        final EditText etEnterNameUpdate = (EditText) dialog.findViewById(R.id.etEnterNameUpdate);
        final EditText etEnterBerthdayUpdate = (EditText) dialog.findViewById(R.id.etEnterBerthdayUpdate);
        final EditText etEnterAdressUpdate = (EditText) dialog.findViewById(R.id.etEnterAdressUpdate);
        Button btConfirm = (Button) dialog.findViewById(R.id.btConfirm);
        Button btCancel = (Button) dialog.findViewById(R.id.btCancelUpdate);

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        etEnterNameUpdate.setText(nameSV);
        etEnterBerthdayUpdate.setText(berthdaySV);
        etEnterAdressUpdate.setText(AdressSV);

        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (etEnterNameUpdate.getText().toString().trim().isEmpty() || etEnterBerthdayUpdate.getText().toString().trim().isEmpty()
                        || etEnterAdressUpdate.getText().toString().trim().isEmpty()) {

                    Toast.makeText(MainActivity.this, "Please enter information", Toast.LENGTH_SHORT).show();

                } else {

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, urlUpdate, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (response.toString().trim().equals("success")) {
                                Toast.makeText(MainActivity.this, "Done", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(MainActivity.this, "Error:" + error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    ){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {

                            Map<String, String> param = new HashMap<>();
                            param.put("IDSV", String.valueOf(idSV));
                            param.put("nameSV", etEnterNameUpdate.getText().toString());
                            param.put("namsinhSV", etEnterBerthdayUpdate.getText().toString());
                            param.put("diachiSV", etEnterAdressUpdate.getText().toString() );

                            return param;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                    requestQueue.add(stringRequest);
                    dialog.dismiss();

                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);

                }

            }
        });

        dialog.show();

    }

    public void DeleteDialog(final int id) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setMessage("Would you like to delete this information");

        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                StringRequest stringRequest = new StringRequest(Request.Method.POST, urlDelete,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                if (response.toString().trim().equals("success")) {
                                    Toast.makeText(MainActivity.this, "Done", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                }

                            }
                        },
                        new Response.ErrorListener() {
                             @Override
                             public void onErrorResponse(VolleyError error) {
                                 Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                             }
                         }
                        ){
                              @Override
                             protected Map<String, String> getParams() throws AuthFailureError {

                                  Map<String, String> params = new HashMap<>();
                                  params.put("idS", String.valueOf(id));

                                  return params;

                              }
                        };

                RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                requestQueue.add(stringRequest);

                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            }
        });

        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {}
        });

        dialog.show();

    }


//    private void JsonPostData(String url) {
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                if ( response.equals("success")) {
//                    Toast.makeText(MainActivity.this, "Done", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
//            }
//        }
//        ){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//
//                Map<String, String> params = new HashMap<>();
//                params.put("hotenSV", name);
//                params.put("namsinhSV", berthday);
//                params.put("diachiSV", adress);
//
//                return params;
//            }
//        };
//
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(stringRequest);
//
//    }


}
