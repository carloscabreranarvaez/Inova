package cifer.nova.ciferapp.inova;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;

import cifer.nova.ciferapp.inova.Clases.Buid_dialog_all;
import cifer.nova.ciferapp.inova.Clases.Create_Dialog_help;

public class CambioMonedaActivity extends AppCompatActivity {
    private Button convertir,cambiar;
    private TextView soles, dolares,resultado;
    private EditText monto;
    private Boolean SOLES_A_DOLARES = true;
    private Boolean DOLARES_A_SOLES = false;
    private float monto_real;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambio_moneda);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        convertir = (Button) findViewById(R.id.cbb_button);
        cambiar = (Button) findViewById(R.id.direccion_button);
        //-------------------------------------------------------------------
        dolares  = (TextView) findViewById(R.id.dolarid);
        soles = (TextView) findViewById(R.id.solesid);
        resultado = (TextView) findViewById(R.id.resultaoid);
        //-------------------------------------------------------------------
        monto = (EditText) findViewById(R.id.montoid);
        //-------------------------------------------------------------------
        if (SOLES_A_DOLARES){
            cambiar.setText("S/ > $");
            dolares.setText("Soles:");
            soles.setText("Dólares:");
        }
        cambiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SOLES_A_DOLARES){
                    cambiar.setText("$ > S/");
                    dolares.setText("Dólares:");
                    soles.setText("Soles:");
                    DOLARES_A_SOLES = true;
                    SOLES_A_DOLARES = false;
                }else {
                    cambiar.setText("S/ > $");
                    dolares.setText("Soles:");
                    soles.setText("Dólares:");
                    DOLARES_A_SOLES = false;
                    SOLES_A_DOLARES = true;
                }
            }
        });
        convertir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(String.valueOf(monto.getText().toString()).isEmpty())
                {
                    Toast.makeText(CambioMonedaActivity.this,"vacio",Toast.LENGTH_SHORT).show();
                }else{
                    if (DOLARES_A_SOLES){

                        monto_real = Float.parseFloat(monto.getText().toString());
                        resultado.setText((monto_real*3.27)+"");

                    }else {
                        if (SOLES_A_DOLARES){

                            monto_real = Float.parseFloat(monto.getText().toString());
                            resultado.setText((monto_real*0.31)+"");

                        }
                    }
                }
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        /*switch (item.getItemId()) {
            case R.id.action_settings:
                break;
            case R.id.action_salir:
                finish();
                break;
        }*/

        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_salir) {
            //return true;
            //startActivity(new Intent(MainActivity.this,MejorRutaActivity.class));
            //Toast.makeText(VerMisRutasODMActivitye.this,mMap.getMapType()+"",Toast.LENGTH_LONG).show();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
