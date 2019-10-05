package sitetech.NFCcheckPoint.Helpers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.io.Serializable;

import sitetech.NFCcheckPoint.AppController;
import sitetech.NFCcheckPoint.miServicio;
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

    public static void cargarFragmento(Serializable back, Fragment fragmento){
        Bundle bundle = new Bundle();
        bundle.putSerializable("mainFragment", back);
        fragmento.setArguments(bundle);

        ((Fragment)back).getFragmentManager().beginTransaction()
                //.remove((Fragment)back)
                .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left, R.anim.slide_in_right, R.anim.slide_out_right)
                .replace(R.id.nav_host_fragment, fragmento)
                .addToBackStack(null)
                .commit();
    }

    public static void goBackStack(View vista){
        AppCompatActivity activity = (AppCompatActivity) vista.getContext();
        activity.getSupportFragmentManager().popBackStack();
    }

    public static void mostrarToast(String mensaje) {
        Toast.makeText(AppController.getAppContext(), mensaje, Toast.LENGTH_SHORT).show();

    }
}
