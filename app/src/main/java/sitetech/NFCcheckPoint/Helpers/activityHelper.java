package sitetech.NFCcheckPoint.Helpers;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.internal.ResourceUtils;

import java.io.Serializable;

import sitetech.NFCcheckPoint.AppController;
import sitetech.NFCcheckPoint.miServicio;
import sitetech.routecheckapp.R;

public class activityHelper {
    public static void abrirActivity(Activity mainActivity, Activity activity, Serializable object){
        Bundle bundle = new Bundle();
        bundle.putSerializable("mainFragment", (Serializable) object);

        Intent newIntent = new Intent(mainActivity.getApplicationContext(), activity.getClass());
        newIntent.putExtra("bundle", bundle);

        mainActivity.startActivity(newIntent);

    }

    public static void abrirActivity(Activity mainActivity, Activity newActivity){
        Intent newIntent = new Intent(mainActivity.getApplicationContext(), newActivity.getClass());
        mainActivity.startActivity(newIntent);

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

    public static void cargarFragmento(Serializable back, Fragment fragmento, int animacionEntrada1, int animacionEntrada2, int animacionSalida1, int animacionSalida2){
        Bundle bundle = new Bundle();
        bundle.putSerializable("mainFragment", back);
        fragmento.setArguments(bundle);

        ((Fragment)back).getFragmentManager().beginTransaction()
                //.remove((Fragment)back)
                .setCustomAnimations(animacionEntrada1, animacionEntrada2, animacionSalida1, animacionSalida2)
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

    public static void cambiarTitulo(View v, String titulo){
        AppCompatActivity activity = (AppCompatActivity)v.getContext() ;
        Toolbar toolbar= activity.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle(titulo);
    }
}
