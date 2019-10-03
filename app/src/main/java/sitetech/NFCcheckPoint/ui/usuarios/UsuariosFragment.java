package sitetech.NFCcheckPoint.ui.usuarios;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import sitetech.NFCcheckPoint.Adapters.userAdapter;
import sitetech.NFCcheckPoint.AppController;
import sitetech.NFCcheckPoint.Helpers.activityHelper;
import sitetech.NFCcheckPoint.MainActivity;
import sitetech.NFCcheckPoint.db.Usuario;
import sitetech.NFCcheckPoint.db.UsuarioDao;
import sitetech.routecheckapp.R;

public class UsuariosFragment extends Fragment {

    private UsuariosViewModel usuariosViewModel;
    private View vista;
    private Button bagregar;
    private RecyclerView ulista;
    private TextView lnotificar;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        usuariosViewModel = ViewModelProviders.of(this).get(UsuariosViewModel.class);

        View root = inflater.inflate(R.layout.fragment_usuarios, container, false);
        bagregar = root.findViewById(R.id.bagregar);
        ulista = root.findViewById(R.id.ulista);
        lnotificar = root.findViewById(R.id.lnotificar);

        vista = root;

        bagregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityHelper.cargarFragmento(vista, new Usuario_AgregarFragment());
            }
        });

        showList();

        return root;
    }

    private void showList(){
        UsuarioDao userDao = AppController.daoSession.getUsuarioDao();
        userAdapter uAdapter = new userAdapter(userDao.loadAll());

        for ( Usuario u : userDao.loadAll() ) {
            Log.d(u.getNombre(), u.getCedula());
        }

        if (uAdapter.getItemCount() == 0) {
            ulista.setVisibility(View.GONE);
            lnotificar.setVisibility(View.VISIBLE);
        }
        else {
            ulista.setVisibility(View.VISIBLE);
            lnotificar.setVisibility(View.GONE);
        }


        ulista.setLayoutManager(new LinearLayoutManager(getContext()));
        ulista.setAdapter(uAdapter);


    }

    private void changeText(TextView tx){
        tx.setText("USUARIOS");
    }
}