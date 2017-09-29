package cifer.nova.ciferapp.inova;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Cifer on 23/10/2016.
 */

public class RegisterRequest extends StringRequest{

    private static  final  String REGISTER_REQUEST_URL = "http://709573.esy.es/RegistarUsusario.php";
    private Map<String , String> params;


    public RegisterRequest(String NombreUsuario, String Email, String password , int  IdUbicacion,  int IdLugares, Response.Listener<String> listener) {
        super(Method.POST,REGISTER_REQUEST_URL,listener,null);
        params = new HashMap<>();
        params.put("NombreUsuario",NombreUsuario);
        params.put("Email",Email);
        params.put("password",password);
        params.put("IdUbicacion",IdUbicacion+"");
        params.put("IdLugares",IdLugares+"");
    }

    public Map<String, String> getParams()
    {
        return params;
    }
}
