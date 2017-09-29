package cifer.nova.ciferapp.inova.Clases;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import cifer.nova.ciferapp.inova.Interfaces.I_Create_Dialog_help;
import cifer.nova.ciferapp.inova.R;

/**
 * Created by Cifer on 02/07/2017.
 */

public class Create_Dialog_help implements I_Create_Dialog_help{
    @Override
    public void dialog(Context context,String ayuda) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        View mView =inflater.inflate(R.layout.dialog_help,null);
        final TextView mEmail = (TextView) mView.findViewById(R.id.textView7);
        mEmail.setText(ayuda);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();
    }
}
