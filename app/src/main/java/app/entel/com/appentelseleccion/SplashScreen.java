package app.entel.com.appentelseleccion;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import app.entel.com.appentelseleccion.includes.AppVars;
import app.entel.com.appentelseleccion.includes.Funciones;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


            new AsyncTask<Void, Void, Void>() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    isStoragePermission();
                    isStoragePermissionRead();
                }

                @Override
                protected Void doInBackground(Void... params) {


                    Funciones.createFileAndDir(AppVars.pathHome, AppVars.fileDefault);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    Intent i = new Intent(SplashScreen.this, Intro.class);
                    startActivity(i);
                    finish();
                }
            }.execute();


    }

    public boolean isStoragePermission(){
        if(Build.VERSION.SDK_INT >=23){
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                //Toast.makeText(SplashScreen.this,"Permiso activado.",Toast.LENGTH_SHORT).show();
                return true;
            }else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                //Toast.makeText(SplashScreen.this,"Permiso resuelto",Toast.LENGTH_SHORT).show();
                return false;
            }

        }else{
            //Toast.makeText(SplashScreen.this,"Permiso activado..",Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    public boolean isStoragePermissionRead(){
        if(Build.VERSION.SDK_INT >=23){
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                //Toast.makeText(SplashScreen.this,"Permiso activado.",Toast.LENGTH_SHORT).show();
                return true;
            }else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                //Toast.makeText(SplashScreen.this,"Permiso resuelto",Toast.LENGTH_SHORT).show();
                return false;
            }

        }else{
            //Toast.makeText(SplashScreen.this,"Permiso activado..",Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            //Toast.makeText(SplashScreen.this,"Permiso " + permissions[0] + " was " + grantResults[0],Toast.LENGTH_SHORT).show();
            //desSerialize();
        }
    }

    @Override
    public void onBackPressed() {
        //moveTaskToBack(false);
    }
}
