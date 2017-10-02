package app.entel.com.appentelseleccion;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

import app.entel.com.appentelseleccion.includes.Funciones;
import app.entel.com.appentelseleccion.modelo.Mensaje;
import app.entel.com.appentelseleccion.modelo.MensajeService;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class Configuration extends AppCompatActivity {


    @BindView(R.id.button)
    Button button;
    @BindView(R.id.btnver)
    Button btnver;
    @BindView(R.id.btnsalir)
    Button btnsalir;
    @BindView(R.id.btnvolver)
    Button btnvolver;
    @BindView(R.id.btnsubir)
    Button btnsubir;
    @BindView(R.id.tv_llave)
    TextView tvLlave;
    final String llave = "1044";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.button, R.id.btnver, R.id.btnsalir, R.id.btnvolver, R.id.btnsubir})
    public void onViewClicked(View view) {

        Realm.init(this);
        RealmConfiguration realconfig = new RealmConfiguration.Builder().name("Mensajes").schemaVersion(1).build();
        Realm.setDefaultConfiguration(realconfig);
        Realm realm = Realm.getDefaultInstance();
        MensajeService ms = new MensajeService(realm);

        switch (view.getId()) {
            //Opción para eliminar los registros locales
            case R.id.button:
                Intent home = new Intent(this, Configuration.class);
                home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(home);

                //finish();
                /*if(tvLlave.getText().toString().trim().equals(llave)) {
                    RealmResults<Mensaje> results = realm.where(Mensaje.class).findAll();
                    realm.beginTransaction();
                    results.deleteAllFromRealm();
                    realm.commitTransaction();
                    Toast.makeText(Configuration.this, "Mensajes eliminados.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(Configuration.this, "Ingresa la llave correcta. oOo", Toast.LENGTH_SHORT).show();
                }*/


                break;
            // Ver opciones
            case R.id.btnver:

                Mensaje[] mensajes = ms.get();
                int y = 0;
                for (int x = 0; x < mensajes.length; x++) {

                    if (!mensajes[x].isEsEnviado()) {
                        y++;
                    }
                    //ms.updateStatus(mensajes[x],false, realm);
                }
                Toast.makeText(Configuration.this, "Cantidad de mensajes :" + mensajes.length + " y " + y + " elementos sin enviar", Toast.LENGTH_SHORT).show();

                break;
            // Salir de la pantalla
            case R.id.btnsalir:
                /*Intent home = new Intent(this, Configuration.class);
                home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(home);*/

                Intro.ac.finish();
                System.exit(0);
                finish(); // La cerramos
                //getApplicationContext().


                break;

            case R.id.btnvolver:
                Intent i = new Intent(Configuration.this, Intro.class);
                startActivity(i);
                finish();
                break;

            case R.id.btnsubir:

                Mensaje[] msg = ms.get();
                int pendientes = 0;
                for (int x = 0; x < msg.length; x++) {

                    if (!msg[x].isEsEnviado()) {
                        Mensaje m = new Mensaje();
                        m.setEmail(Funciones.notNull(msg[x].getEmail()));
                        m.setIp(Funciones.notNull(msg[x].getIp()));
                        m.setMensaje(Funciones.notNull(msg[x].getMensaje()));
                        m.setDispositivo(Funciones.notNull(msg[x].getDispositivo()) + " ID:" + Funciones.getIdAndroid(getApplication().getBaseContext()));
                        m.setNombre(Funciones.notNull(msg[x].getNombre()));
                        m.setFechaRegistro(Funciones.notNull(msg[x].getFechaRegistro()));
                        m.setEsEnviado(msg[x].isEsEnviado());
                        Log.d("EsEnviado", ".-" + msg[x].isEsEnviado());
                        m.setId(msg[x].getId());
                        subeData(m, msg[x]);
                        pendientes++;
                    }
                    //mm.add(m);
                }

                String cadena = (pendientes==0) ? " elementos pendientes para subir." : " elementos cargados a la base de datos.";

                Toast.makeText(Configuration.this, pendientes + cadena , Toast.LENGTH_SHORT).show();

                break;
        }
    }

    public void subeData(final Mensaje mensaje, final Mensaje msgrealm) {

        Realm.init(this);
        RealmConfiguration realconfig = new RealmConfiguration.Builder().name("Mensajes").schemaVersion(1).build();
        Realm.setDefaultConfiguration(realconfig);
        final Realm realm = Realm.getDefaultInstance();
        final MensajeService ms = new MensajeService(realm);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        final String json = gson.toJson(mensaje);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://apientel.rinnolab.cl/insertmensaje";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("Respuesta:", response);
                        if (response.trim().equals("1")) {
                            Log.d("Status:", "Cambia Estado");
                            ms.updateStatus(msgrealm, true, realm);
                        }
                        // Display the first 500 characters of the response string.
                        // mTextView.setText("Response is: "+ response.substring(0,500));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mTextView.setText("That didn't work!");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                Log.e("TAG", "Json Enviado: " + json);
                params.put("json", json);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

}
