package cifer.nova.ciferapp.inova.Interfaces;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;

/**
 * Created by Cifer on 04/07/2017.
 */

public interface I_AddComent {
    void comend(Context c, DatabaseReference reference, String user);
}
