package lk.ijse.pos.servlet.util;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 * @author : Jayani_Arunika  8/25/2023 - 1:19 PM
 * @since : v0.01.0
 **/

public class ResponseUtil {
    public static JsonObject genJson(String state, String message, JsonArray...data){
        JsonObjectBuilder object = Json.createObjectBuilder();
        object.add("state",state);
        object.add("message",message);
        if (data.length>0){
            object.add("data",data[0]);
        }else{
            object.add("data","");
        }

        return object.build();
    }
}
