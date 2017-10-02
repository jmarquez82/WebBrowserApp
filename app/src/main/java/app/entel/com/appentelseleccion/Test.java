package app.entel.com.appentelseleccion;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Test extends AppCompatActivity {

    TextView tvLlamar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        tvLlamar = (TextView)findViewById(R.id.btnLlamar);

        tvLlamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* Code */
                Intent i1 = new Intent(Test.this, MainActivity.class);
                startActivity(i1);


            }
        });

    }
}
