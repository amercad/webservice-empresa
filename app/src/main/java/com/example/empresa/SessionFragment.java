package com.example.empresa;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonRequest;

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

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return vista;


    }

    @Override
    public void onErrorResponse(VolleyError error) {
    }

    @Override
    public void onResponse(JSONObject response) {
    }
}