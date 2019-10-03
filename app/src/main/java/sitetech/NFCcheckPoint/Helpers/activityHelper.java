package sitetech.NFCcheckPoint.Helpers;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import sitetech.routecheckapp.R;

public class activityHelper {
    public static void abrirActivity(Activity main, Object activity){
        Intent listar = new Intent(main.getApplicationContext(), activity.getClass());
        main.startActivity(listar);
    }

    public static void abrirActivity(View vista, Object activity){
        AppCompatActivity main = (AppCompatActivity) vista.getContext();
        Intent listar = new Intent(main.getApplicationContext(), activity.getClass());
        main.startActivity(listar);
    }

    public static void cargarFragmento(Fragment back, Fragment fragmento){
        AppCompatActivity activity = (AppCompatActivity)back.getActivity();
        activity.getSupportFragmentManager().popBackStack();
        activity.getSupportFragmentManager().beginTransaction()
                .remove(back)
                .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_left, R.anim.slide_out_right)
                .replace(R.id.nav_host_fragment, fragmento, "agregar_usuario")
                //.addToBackStack(fragmento.getTag())
                .commit();
    }

    public static void goBackStack(View vista){
        AppCompatActivity activity = (AppCompatActivity) vista.getContext();
        activity.getSupportFragmentManager().popBackStack();
    }

}
