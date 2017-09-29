package cifer.nova.ciferapp.inova.Clases;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;

import cifer.nova.ciferapp.inova.Interfaces.I_AddComent;
import cifer.nova.ciferapp.inova.R;

/**
 * Created by Cifer on 04/07/2017.
 */

public class AddComent implements I_AddComent {

    @Override
    public void comend(Context c, final DatabaseReference reference, final String user) {
        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE );
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(c);
        View mView = inflater.inflate(R.layout.add_comend, null);
        //final EditText title = (EditText) mView.findViewById(R.id.add_comd_titulo);
        final EditText descript = (EditText) mView.findViewById(R.id.add_comd_desc);
        Button guardar = (Button) mView.findViewById(R.id.button3);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // reference.child("titulo").setValue(title.getText().toString());
                reference.child("comentario").setValue(descript.getText().toString());
                reference.child("usuario").setValue("por: "+user);
                dialog.dismiss();
            }
        });

    }
}
