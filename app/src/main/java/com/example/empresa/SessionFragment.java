package com.example.empresa;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;


public class SessionFragment extends Fragment implements Response.Listener<JSONObject>,Response.ErrorListener {

    EditText etCorreo, etClave;
    Button btnIngresar;
    TextView tvRegistrar;
    RequestQueue rq;
    JsonRequest jrq;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // return inflater.inflate(R.layout.fragment_session, container, false);
        View vista = inflater.inflate(R.layout.fragment_session,container,false);
        etCorreo=vista.findViewById(R.id.etcorreo);
        etClave=vista.findViewById(R.id.etclave);
        btnIngresar=vista.findViewById(R.id.btingresar);
        tvRegistrar=vista.findViewById(R.id.tvregistrar);
        rq = Volley.newRequestQueue(getContext());

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarSession();
            }
        });

        tvRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registarse();
            }
        });


        return vista;


    }

    private void iniciarSession() {
        String correo = etCorreo.getText().toString(),
               clave = etClave.getText().toString();

        if (correo.isEmpty() || clave.isEmpty()) {
            Toast.makeText(getContext(), "Los campos son obligatorio", Toast.LENGTH_SHORT).show();
            etCorreo.requestFocus();
        } else {
            String url = "http://192.168.1.7:80/webserver-back/Session.php?correo="+correo+"&clave="+clave;
            jrq = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
            rq.add(jrq);
        }

    }

    private void registarse() {
        Intent intentUsuario = new Intent(getContext(), UsuarioActivity.class);
        startActivity(intentUsuario);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(), "Error al iniciar la session", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONObject response) {
        Intent intentUsuario = new Intent(getContext(), LibroActivity.class);
        startActivity(intentUsuario);
    }
}