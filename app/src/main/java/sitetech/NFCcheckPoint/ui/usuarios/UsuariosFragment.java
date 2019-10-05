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
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView;

import java.io.Serializable;
import java.util.List;

import sitetech.NFCcheckPoint.Adapters.onItemClick;
import sitetech.NFCcheckPoint.Adapters.userAdapter;
import sitetech.NFCcheckPoint.AppController;
import sitetech.NFCcheckPoint.Helpers.activityHelper;
import sitetech.NFCcheckPoint.db.Usuario;
import sitetech.NFCcheckPoint.db.UsuarioDao;
import sitetech.routecheckapp.R;

public class UsuariosFragment extends Fragment implements Serializable {

    private View vista;
    private Button bagregar;
    private OmegaRecyclerView ulista;
    private TextView lnotificar;
    private userAdapter uAdapter;
    private UsuariosFragment fragmento;
    public Usuario Useleccionado;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_usuarios, container, false);
        bagregar = root.findViewById(R.id.bagregar);
        ulista = root.findViewById(R.id.ulista);
        lnotificar = root.findViewById(R.id.lnotificar);
        Useleccionado = null;

        vista = root;
        fragmento = this;
        bagregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Useleccionado = null;
                activityHelper.cargarFragmento(fragmento, new Usuario_AgregarFragment());
            }
        });

        showList();

        return root;
    }

    private void showList(){
        UsuarioDao userDao = AppController.daoSession.getUsuarioDao(); // QUERY PARA OBTENER TODOS MENOS LOS ELIMINADOS
        List<Usuario> lista = userDao.queryBuilder()
                .where(UsuarioDao.Properties.Eliminado.eq(false))
                .list();

        uAdapter = new userAdapter(lista, new onItemClick() {
            @Override
            public void onClickItem(View v, int position) {
                fragmento.Useleccionado = uAdapter.lista.get(position);
                //NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                //navController.navigate(R.id.action_nav_usuarios_to_usuario_AgregarFragment);

                activityHelper.cargarFragmento(fragmento, new Usuario_AgregarFragment());
            }
        });

        if (uAdapter.getItemCount() == 0) {
            ulista.setVisibility(View.GONE);
            lnotificar.setVisibility(View.VISIBLE);
        }


        ulista.setLayoutManager(new LinearLayoutManager(getContext()));
        ulista.setAdapter(uAdapter);
    }



    public void updateList(Usuario ux){
        uAdapter.updateData(ux);
    }

}