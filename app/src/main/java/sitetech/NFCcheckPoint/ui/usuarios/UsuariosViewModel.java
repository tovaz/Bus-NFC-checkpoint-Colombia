package sitetech.NFCcheckPoint.ui.usuarios;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import sitetech.NFCcheckPoint.Adapters.userAdapter;
import sitetech.NFCcheckPoint.db.Usuario;

public class UsuariosViewModel extends ViewModel {

    private MutableLiveData<String> accion;
    public UsuariosViewModel() {

    }

    public String getAction(){
        return "";
    }

    public void cargarLista(Context context, userAdapter uAdapter, RecyclerView ulista){

    }

}