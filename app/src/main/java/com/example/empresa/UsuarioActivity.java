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

public class UsuarioActivity extends AppCompatActivity implements Response.Listener<JSONObject>,Response.ErrorListener {

    EditText etUsuario, etNombre, etCorreo, etClave;
    CheckBox cbActivo;

    RequestQueue rq;
    JsonRequest jrq;

    String usr, nombre, clave, correo, url;

    byte sw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);

        getSupportActionBar().hide();

        etUsuario = findViewById(R.id.etUsuario);
        etNombre = findViewById(R.id.etNombre);
        etCorreo = findViewById(R.id.etCorreo1);
        etClave = findViewById(R.id.etPassword);

        cbActivo = findViewById(R.id.cbActivo);

        rq = Volley.newRequestQueue(this);

        sw = 0;

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this, "Usuario no registrado", Toast.LENGTH_SHORT).show();
        limpiarCampos();
    }

    @Override
    public void onResponse(JSONObject response) {
        sw = 1;
        Toast.makeText(this, "Usuario registrado", Toast.LENGTH_SHORT).show();

        JSONArray jsonArray = response.optJSONArray("datos");
        JSONObject jsonObject = null;

        System.out.println(jsonArray);

        try {
            jsonObject = jsonArray.getJSONObject(0);
            etNombre.setText(jsonObject.optString("nombre"));
            etCorreo.setText(jsonObject.optString("correo"));
            etClave.setText(jsonObject.optString("clave"));

            if (jsonObject.optString("activo").equals("si")) {
                cbActivo.setChecked(true);
            } else {
                cbActivo.setChecked(false);
            }

            System.out.println(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void guardar(View view) {
        usr = etUsuario.getText().toString();
        nombre = etNombre.getText().toString();
        correo = etCorreo.getText().toString();
        clave = etClave.getText().toString();

        if (usr.isEmpty() || nombre.isEmpty() || correo.isEmpty() || clave.isEmpty()) {

            Toast.makeText(this, "Todos los campos son requerido", Toast.LENGTH_SHORT).show();
            etUsuario.requestFocus();

        } else {
            if (sw == 0) {
                url = "http://172.16.59.198:8080/WebService/registrocorreo.php";
            } else {
                url = "http://172.16.59.198:8080/WebService/actualiza.php";
                Toast.makeText(getApplicationContext(), "Actualizar!!!!!!!!!!!!!", Toast.LENGTH_LONG).show();
                sw = 0;
            }
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {
                            limpiarCampos();
                            Toast.makeText(getApplicationContext(), "Registro de usuario realizado!", Toast.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Registro de usuario incorrecto!", Toast.LENGTH_LONG).show();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("usr",etUsuario.getText().toString().trim());
                    params.put("nombre", etNombre.getText().toString().trim());
                    params.put("correo",etCorreo.getText().toString().trim());
                    params.put("clave",etClave.getText().toString().trim());
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(postRequest);

            /*if (sw == 1) {

            }*/

        }

    }

    public void consultar(View view) {
        usr = etUsuario.getText().toString();

        if (usr.isEmpty()) {
            Toast.makeText(this, "El campo usuario es obligatorio", Toast.LENGTH_SHORT).show();
            etUsuario.requestFocus();
        } else {
            url = "http://172.16.59.198:8080/WebService/consulta.php?usr="+usr;
            jrq = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
            rq.add(jrq);
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
        etUsuario.setText("");
        etNombre.setText("");
        etCorreo.setText("");
        etClave.setText("");
        cbActivo.setChecked(false);
        etUsuario.requestFocus();
    }
}