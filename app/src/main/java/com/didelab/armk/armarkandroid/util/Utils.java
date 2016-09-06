package com.didelab.armk.armarkandroid.util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ElianaXimena on 04/09/2016.
 */
public class Utils {

    public static String cadenaJson(ArrayList<ItemJson> items) {
        JSONObject cadena = new JSONObject();
        try {
            for (ItemJson item:items) {
                cadena.put(item.getNombre(),item.getValor());
            }
            return cadena.toString(); // Para obtener la cadena de texto de tipo
        }
        catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }
}
