package com.example.empresa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LibroActivity extends AppCompatActivity implements Response.Listener<JSONObject>,Response.ErrorListener {

    private EditText etCodigo, etTitulo, etAutor, etPrecio, etCategoria, etFechaPublicacion;
    private CheckBox cbActivo;

    private RequestQueue rq;
    private JsonRequest jrq;

    private String titulo, codigo, autor, precio, categoria, fechaPublicacion;

    private byte sw;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_libro);

        getSupportActionBar().hide();

        etTitulo = findViewById(R.id.etTitulo);
        etCodigo = findViewById(R.id.etCodigo);
        etAutor = findViewById(R.id.etAutor);
        etPrecio = findViewById(R.id.etPrecio);
        etCategoria = findViewById(R.id.etCategoria);
        etFechaPublicacion = findViewById(R.id.etFecha);

        cbActivo = findViewById(R.id.cbActivoProducto);

        rq = Volley.newRequestQueue(this);

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this, "Libro no registrado", Toast.LENGTH_SHORT).show();
        limpiarCampos();
    }

    @Override
    public void onResponse(JSONObject response) {
        sw = 1;
        Toast.makeText(this, "Libro registrado", Toast.LENGTH_SHORT).show();

        JSONArray jsonArray = response.optJSONArray("datos");
        JSONObject jsonObject = null;

        System.out.println(jsonArray);

        try {
            jsonObject = jsonArray.getJSONObject(0);
            etTitulo.setText(jsonObject.optString("titulo"));
            etAutor.setText(jsonObject.optString("autor"));
            etCategoria.setText(jsonObject.optString("categoria"));
            etFechaPublicacion.setText(jsonObject.optString("fecha_publicacion"));
            etPrecio.setText(jsonObject.optString("precio"));

            if (jsonObject.optString("activo").equals("si")) {
                cbActivo.setChecked(true);
                cbActivo.setClickable(true);
            } else {
                cbActivo.setChecked(false);
                cbActivo.setClickable(false);
            }

            System.out.println(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void consultar(View view) {
        codigo = etCodigo.getText().toString();

        if (codigo.isEmpty()) {
            Toast.makeText(this, "El campo codigo es obligatorio", Toast.LENGTH_SHORT).show();
            etCodigo.requestFocus();
        } else {
            url = "http://192.168.1.7:80/webserver-back/libro/consultar.php?codigo="+codigo;
            jrq = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
            rq.add(jrq);
        }
    }

    public void guardar(View view) {
         codigo = etCodigo.getText().toString();
         titulo = etTitulo.getText().toString();
         autor = etAutor.getText().toString();
         precio = etPrecio.getText().toString();
         categoria = etCategoria.getText().toString();
         fechaPublicacion = etFechaPublicacion.getText().toString();

        if (codigo.isEmpty() || titulo.isEmpty() || autor.isEmpty() || precio.isEmpty()
            || categoria.isEmpty() || fechaPublicacion.isEmpty()) {

            Toast.makeText(this, "Todos los campos son requerido", Toast.LENGTH_SHORT).show();
            etCodigo.requestFocus();

        } else {
            if (sw == 0) {
                url = "http://192.168.1.7:80/webserver-back/libro/registrar.php";
            } else {
                url = "http://192.168.1.7:80/webserver-back/libro/actualizar.php";
                sw = 0;
            }
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {
                            limpiarCampos();
                            Toast.makeText(getApplicationContext(), "Registro exitoso!", Toast.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Registro incorrecto!", Toast.LENGTH_LONG).show();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("codigo",etCodigo.getText().toString().trim());
                    params.put("titulo", etTitulo.getText().toString().trim());
                    params.put("autor",etAutor.getText().toString().trim());
                    params.put("categoria",etCategoria.getText().toString().trim());
                    params.put("fecha_publicacion",etFechaPublicacion.getText().toString().trim());
                    params.put("precio",etPrecio.getText().toString().trim());
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(postRequest);

        }

    }

    public void anular(View view) {
        codigo = etCodigo.getText().toString();

        if (codigo.isEmpty()) {

            Toast.makeText(this, "El campo codigo es requerido", Toast.LENGTH_SHORT).show();
            etCodigo.requestFocus();

        } else {
            url = "http://192.168.1.7:80/webserver-back/libro/anular.php";
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {
                            limpiarCampos();
                            Toast.makeText(getApplicationContext(), "Registro inactivado!", Toast.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "El registro es incorrecto!", Toast.LENGTH_LONG).show();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("codigo",etCodigo.getText().toString().trim());
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(postRequest);

        }

    }

    public void regresar(View view) {
        Intent intentLogin = new Intent(this, MainActivity.class);
        startActivity(intentLogin);
    }

    public void limpiar(View view) {
        limpiarCampos();
    }

    private void limpiarCampos() {
        sw = 0;
        etTitulo.setText("");
        etCodigo.setText("");
        etAutor.setText("");
        etPrecio.setText("");
        etCategoria.setText("");
        etFechaPublicacion.setText("");
        cbActivo.setChecked(false);
        etCodigo.requestFocus();
    }
}