package app.entel.com.appentelseleccion;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.evgenii.jsevaluator.JsEvaluator;

import app.entel.com.appentelseleccion.includes.AppVars;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class WebBrowser extends AppCompatActivity {

    @BindView(R.id.wv_page)
    WebView wvPage;
    private static final String TAG = "WebBrowser";
    @BindView(R.id.btn_atras_wb)
    LinearLayout btnAtrasWb;

    @BindView(R.id.txtlink)
    TextView txtLink;

    @BindView(R.id.imglink)
    ImageView imgLink;

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

        setContentView(R.layout.activity_web_browser);
        ButterKnife.bind(this);

        JsEvaluator jsEvaluator = new JsEvaluator(this);
        //Resetea Inactividad
        Intro.temp.cancel();
        Intro.temp.start();

        wvPage.getSettings().setJavaScriptEnabled(true);
        wvPage.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        //rogressBar = ProgressDialog.show(Main.this, "WebView Example", "Loading...");

        wvPage.setWebChromeClient(new WebChromeClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i(TAG, "Processing webview url click...");
                view.loadUrl(url);
                return true;
            }

            @Override
            public boolean onConsoleMessage(ConsoleMessage cm) {
                Log.d(TAG, String.format("%s @ %d: %s", cm.message(),
                        cm.lineNumber(), cm.sourceId()));
                return true;
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);

                Log.i(TAG, "Proccess: " + newProgress);

                if (newProgress == 100) {

                    String cadena = "(function(){\n" +
                            " document.getElementsByClassName('separacion-section')[0].style.display='none';\n" +
                            " document.getElementsByClassName('faq-div')[0].style.display='none';\n" +
                            " document.getElementsByClassName('section-video')[0].style.display='none';\n" +
                            "})();";

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        wvPage.evaluateJavascript(cadena, null);
                        Log.i(TAG, "JS1");
                    } else {
                        wvPage.loadUrl(cadena);
                        Log.i(TAG, "JS2");
                    }
                }

            }

            public void onPageFinished(WebView view, String url) {
                Log.i(TAG, "Finished loading URL: " + url);
                Intro.temp.cancel();
                Intro.temp.start();

            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.e(TAG, "Error: " + description);
                Toast.makeText(WebBrowser.this, "Oh no! " + description, Toast.LENGTH_SHORT).show();
                Intro.temp.cancel();
                Intro.temp.start();
            }
        });
        wvPage.loadUrl(AppVars.urlWeb);


        wvPage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Log.e(TAG, "Evento OnTouch: " + event);

                //Resetea Inactividad
                Intro.temp.cancel();
                Intro.temp.start();

                return false;
            }
        });
    }

    @OnClick({R.id.btn_atras_wb, R.id.imglink, R.id.txtlink})
    public void onViewClicked() {
        onBackPressed();
        finish();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
