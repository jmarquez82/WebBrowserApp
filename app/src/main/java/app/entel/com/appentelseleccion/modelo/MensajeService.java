package app.entel.com.appentelseleccion.modelo;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Dev21 on 15-06-17.
 */

public class MensajeService {

    private Realm realm;

    public MensajeService(Realm realm){
        this.realm = realm;

    }

    public Mensaje[] get(){
        RealmResults<Mensaje> results = realm.where(Mensaje.class).findAll();
        return results.toArray(new Mensaje[results.size()]);
    }

    public void update(){

    }

    public void updateStatus(Mensaje mensaje,boolean estado,Realm r){

        r.beginTransaction();
        mensaje.setEsEnviado(estado);
        r.commitTransaction();

    }

    public void add(Mensaje mensaje, Realm realm2){

        //realm2.beginTransaction();
        Mensaje msj = realm.createObject(Mensaje.class, mensaje.getId());
        msj.setNombre(mensaje.getNombre());
        msj.setEmail(mensaje.getEmail());
        msj.setMensaje(mensaje.getMensaje());

        realm2.commitTransaction();

    }

    public void delete(int id){

    }



}
