package app.entel.com.appentelseleccion.includes;

import android.content.Context;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by Dev21 on 15-06-17.
 */

public class Funciones {


    public Funciones(){

    }

    public static boolean createFileAndDir(String dirHome, String fileDefault) {
        boolean out = false;
        try {
            //Functions.jsonData = Functions.setObject();
            //Metodo que realiza una accion que requiere ser llamada en segundo plano u otro Hilo
            String pathFile = Environment.getExternalStorageDirectory().getAbsolutePath()+ "/" + dirHome + "/" + fileDefault;
            String pathDir = dirHome + "/" + fileDefault;
            File f = new File(pathFile);
            if (f.exists()) {
                /*Genera json*/
                /*FileWriter file = new FileWriter(pathFile);
                file.write(Functions.jsonData);
                file.flush();
                file.close();*/

                /*Lee Archivo .Json*/
                File file = new File(pathFile);
                int length = (int) file.length();
                byte[] bytes = new byte[length];
                FileInputStream in = new FileInputStream(file);
                try {
                    in.read(bytes);
                } finally {
                    in.close();
                }

                String data = new String(bytes);
                Log.i("Json Read:", data);
                out = true;

            } else {
                makeDirectories(pathDir);
                boolean cf = createFileAndDir(dirHome,fileDefault);
                Log.d("File Crate","Result create file:" + cf);
                //Functions.desSerialize(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            out = false;
        }
        return out;
    }


    public static boolean makeDirectories(String dirPath)
            throws IOException {
        String[] pathElements = dirPath.split("/");
        if (pathElements != null && pathElements.length > 0) {
            String cadena = "";
            for (String singleDir : pathElements) {

                if (!singleDir.equals("")) {

                    cadena += "/" + singleDir;
                    String path = Environment.getExternalStorageDirectory().getAbsolutePath() + cadena;
                    File dir = new File(path);

                    boolean existed = dir.exists();
                    boolean created = false;
                    if (!existed) {

                        try {
                            int resultado = singleDir.indexOf(".");

                            if(resultado != -1) {
                                FileOutputStream f = new FileOutputStream(dir);
                            }else{
                                created = dir.mkdir();
                            }

                        } catch (Exception ex) {

                        }
                        if (created) {
                            Log.i("CREATED directory: ", singleDir);
                        } else {
                            Log.i("COULD NOT directory: ", singleDir);
                            //return false;
                        }
                    }
                }
            }
        }
        return true;
    }


    public static int keygen(){
        SimpleDateFormat sm1 = new SimpleDateFormat("yyyyMMddHHmmss");
        Date d1 = new Date();
        BigInteger bi =  new BigInteger(sm1.format(d1)) ;
        BigInteger bir =  new BigInteger("100000") ;
        BigInteger result = bi.divide( bir);
        int key =  Integer.parseInt(result.toString()) ;
        Log.e("Mega Key:",key + "");
        return (key);
    }


    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    public static boolean checkEmail(String email) {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

    public static String getIdAndroid(Context c){
        String android_id = Settings.Secure.getString(c.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        return android_id;
    }


    public static String datenow(){

        SimpleDateFormat sm2 = new SimpleDateFormat("yyyy/dd/MM HH:mm:ss");
        Date d2 = new Date();
        String f = sm2.format(d2);
        return f;
    }

    public static String notNull(String value){
        return (value == null ) ? "" : value;
    }



}
