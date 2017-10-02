package app.entel.com.appentelseleccion;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import app.entel.com.appentelseleccion.includes.Funciones;
import app.entel.com.appentelseleccion.modelo.Mensaje;
import app.entel.com.appentelseleccion.modelo.MensajeService;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.et_nombre)
    EditText etNombre;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_mensaje)
    EditText etMensaje;
    @BindView(R.id.btn_enviar)
    FrameLayout btnEnviar;


    FragmentManager fgManager;
    FragmentTransaction fragmentTransaction;
    @BindView(R.id.contentfragment)
    ConstraintLayout contentfragment;
    @BindView(R.id.imgAsterisco)
    ImageView imgAsterisco;
    int contador = 0;
    @BindView(R.id.contenedorPadre)
    ConstraintLayout contenedorPadre;
    @BindView(R.id.loguito)
    ImageView loguito;

    Temporizador2 temp2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/DINNextLTPro-BoldItalic.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        int currentApiVersion = Build.VERSION.SDK_INT;
        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {

            getWindow().getDecorView().setSystemUiVisibility(flags);
            final View decorView = getWindow().getDecorView();
            decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                @Override
                public void onSystemUiVisibilityChange(int visibility) {
                    if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                        decorView.setSystemUiVisibility(flags);
                    }
                }
            });
        }

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //Resetea Inactividad
        Intro.temp.cancel();
        Intro.temp.start();
    }


    @OnTextChanged({R.id.et_mensaje, R.id.et_email, R.id.et_nombre})
    void onTextChangedOk(CharSequence cs, int start, int count, int after) {
        //Toast.makeText(MainActivity.this, "Ingreso text onchanged.", Toast.LENGTH_SHORT).show();
        Intro.temp.cancel();
        Intro.temp.start();
    }


    // Wrap the Activity Context:
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @OnClick({R.id.btn_enviar, R.id.imgAsterisco, R.id.contenedorPadre, R.id.loguito, R.id.btn_atras})
    public void onViewClicked(View v) {


        switch (v.getId()) {

            case R.id.btn_enviar:
                if (validaText(etNombre.getText().toString()) &&
                        validaText(etEmail.getText().toString()) &&
                        validaText(etMensaje.getText().toString())) {

                    if (Funciones.checkEmail(etEmail.getText().toString())) {

                        /*Code para guardar datos*/
                        contentfragment.setVisibility(View.VISIBLE);
                        contenedorPadre.setVisibility(View.GONE);

                        temp2 = new Temporizador2(5000, 1000);
                        temp2.start();

                        Realm.init(this);
                        RealmConfiguration realconfig = new RealmConfiguration.Builder().name("Mensajes").schemaVersion(1).build();
                        Realm.setDefaultConfiguration(realconfig);
                        Realm realm = Realm.getDefaultInstance();


                        realm.executeTransaction(new Realm.Transaction() { // must be in transaction for this to work
                            @Override
                            public void execute(Realm realm) {
                                // increment index
                                Number currentIdNum = realm.where(Mensaje.class).max("id");
                                int nextId;
                                if (currentIdNum == null) {
                                    nextId = 1;
                                } else {
                                    nextId = currentIdNum.intValue() + 1;
                                }
                                MensajeService ms = new MensajeService(realm);

                                Mensaje msj = new Mensaje(); // unmanaged
                                msj.setId(nextId);
                                msj.setNombre(etNombre.getText().toString());
                                msj.setEmail(etEmail.getText().toString());
                                msj.setMensaje(etMensaje.getText().toString());
                                msj.setFechaRegistro(Funciones.datenow());
                                msj.setDispositivo("Totem");
                                msj.setIp("192.168.1.1");
                                //msj.setEsEnviado(false);
                                // ms.add(msj,realm);
                                //...
                                realm.insertOrUpdate(msj); // using insert API
                            }
                        });

                        MensajeService ms = new MensajeService(realm);
                        Mensaje[] mensajes = ms.get();
                        Log.e("Data:", mensajes.length + "");

                        for (int i = 0; i < mensajes.length; i++) {
                            Log.e("Result:", mensajes[i].getId() + " - " + mensajes[i].getNombre() + " - " + mensajes[i].getEmail() + " - " + mensajes[i].getMensaje());
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "El Email es incorrecto.", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(MainActivity.this, "Debe ingresar todos los campos correctamente", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.imgAsterisco:

                if (contador == 20) {
                    Intent i = new Intent(MainActivity.this, Configuration.class);
                    startActivity(i);
                    finish();
                    contador = 0;
                } else {
                    if (contador > 10) {
                        int quedan = 20 - contador;
                        Toast.makeText(MainActivity.this, "Quedan " + quedan + " para entrar a la configuración", Toast.LENGTH_SHORT).show();
                    }
                    contador++;
                }

                break;

            case R.id.contenedorPadre:

                //Cerrar teclado
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                break;

            case R.id.loguito:

                Intent i = new Intent(MainActivity.this, MainActivity.class);
                startActivity(i);
                finish();

                break;

            case R.id.btn_atras:

                /*Intent ix = new Intent(MainActivity.this, Intro.class);
                startActivity(ix);*/
                onBackPressed();
                finish();

                break;
        }
    }

    public boolean validaText(String text) {
        return (text.trim().equals("")) ? false : true;
    }


    @Override
    public void onBackPressed() {
        //Resetea Inactividad
        Intro.temp.cancel();
        Intro.temp.start();
        finish();
    }


    @Override
    public void onUserInteraction() {
        Intro.temp.cancel();
        Intro.temp.start();
    }


    /**
     * Class Temorizador
     */
    public class Temporizador2 extends CountDownTimer {

        public Temporizador2(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            Intent intent = new Intent(getApplicationContext(), Intro.class);
            View view = MainActivity.this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            //finish();
            overridePendingTransition(0, 0);
            //temp2.cancel();
        }

    }


}


