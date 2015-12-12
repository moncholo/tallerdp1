package ar.uba.fi.taller2.mensajerocliente.manejadores;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import ar.uba.fi.taller2.mensajerocliente.utilidades.ManejadorDeToken;

/**
 * Created by juan on 03/12/15.
 */
public class ManejadorViajes {
    String desde, hasta;
    String destino;
    List<String> bares;
    List<String> museos;
    List<String> restaurantes;
    List<String> atracciones;
    int edadDesde, edadHasta;

    public ManejadorViajes(String destino, String desde, String hasta, List<String> museos, List<String> bares,
                           List<String> restaurantes, List<String> atracciones, int edadDesde,
                            int edadHasta){
        this.desde = desde;
        this.hasta = hasta;
        this.edadDesde = edadDesde;
        this.edadHasta = edadHasta;
        this.destino = destino;
        this.bares = bares;
        this.museos = museos;
        this.restaurantes = restaurantes;
        this.atracciones = atracciones;

    }

    public String crearJson(){
        JSONObject object = new JSONObject();
        JSONArray museosJson = new JSONArray();
        JSONArray baresJson = new JSONArray();
        JSONArray restaurantesJson = new JSONArray();
        JSONArray atraccionesJson = new JSONArray();

        for (int i = 0; i<this.museos.size(); i++){
            museosJson.put(this.museos.get(i));
        }

        for (int i = 0; i<this.bares.size(); i++){
            baresJson.put(this.bares.get(i));
        }

        for (int i = 0; i<this.atracciones.size(); i++){
            atraccionesJson.put(this.atracciones.get(i));
        }

        for (int i = 0; i<this.restaurantes.size(); i++){
            restaurantesJson.put(this.restaurantes.get(i));
        }

        try {
            object.put("Destino", this.destino);
            object.put("Desde", this.desde);
            object.put("Hasta", this.hasta);
            object.put("Museos",museosJson);
            object.put("Bares", baresJson);
            object.put("Restaurantes", restaurantesJson);
            object.put("Atracciones", atraccionesJson);
            object.put("EdadDesde", this.edadDesde);
            object.put("EdadHasta", this.edadHasta);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object.toString();

    }

    private static String claveViajes(Context context){
        String usuario = ManejadorDeToken.obtenerUsuario(context);
        return APIConstantes.PERSISTENCIA_VIAJES+"-"+usuario;

    }
    public static String obtenerViajes(Context context) {

        SharedPreferences settings = context.getSharedPreferences(APIConstantes.PERSISTENCIA_DATOS, 0);
            return settings.getString(claveViajes(context), null);
    }

    public static void agregarYPersistir(Context context, String valor){
        String viajesGuardados = obtenerViajes(context);
        JSONArray object = null;
        JSONObject viajeNuevo = null;
        SharedPreferences settings = context.getSharedPreferences(APIConstantes.PERSISTENCIA_DATOS, 0);
        SharedPreferences.Editor editor = settings.edit();

        try {
            object = new JSONArray(viajesGuardados);
            viajeNuevo = new JSONObject(valor);
            object.put(viajeNuevo);
            editor.putString(claveViajes(context), object.toString());
            editor.commit();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void persistirViajes(Context context, String valor) {
        SharedPreferences settings = context.getSharedPreferences(APIConstantes.PERSISTENCIA_DATOS, 0);
        SharedPreferences.Editor editor = settings.edit();
        JSONArray viajes = new JSONArray();
        try {
            JSONObject object = new JSONObject(valor);
            viajes.put(object);
            editor.putString(claveViajes(context), viajes.toString());
            editor.commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static List<String> jsonArrayAList(JSONArray unArray){
        List<String> lista = new LinkedList<String>();
        for (int i = 0; i < unArray.length(); i++){
            try {
                lista.add(unArray.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return lista;

    }
    public static void imprimir(Context context){
        JSONArray viajes = null;

        try {
            viajes = new JSONArray(obtenerViajes(context));
            for (int i = 0; i < viajes.length(); i++){
               System.out.println(viajes.get(i).getClass());
                JSONObject object = new JSONObject(viajes.getString(i));
                String destino = object.get("Destino").toString();
                System.out.println(destino);
                String desde = object.getString("Desde").toString();
                System.out.println(desde);
                String hasta = object.get("Hasta").toString();
                System.out.println(hasta);
                List<String> museos = jsonArrayAList(object.getJSONArray("Museos"));
                for (int j = 0; j < museos.size(); j++){
                    System.out.println(museos.get(j));
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}
