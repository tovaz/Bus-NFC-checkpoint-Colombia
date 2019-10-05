package sitetech.NFCcheckPoint.Adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView;
import com.omega_r.libs.omegarecyclerview.swipe_menu.SwipeViewHolder;

import java.util.List;

import sitetech.NFCcheckPoint.AppController;
import sitetech.NFCcheckPoint.Helpers.Dialog;
import sitetech.NFCcheckPoint.Helpers.ToastHelper;
import sitetech.NFCcheckPoint.Helpers.myDialogInterface;
import sitetech.NFCcheckPoint.db.Usuario;
import sitetech.NFCcheckPoint.db.UsuarioDao;
import sitetech.routecheckapp.R;

public class userAdapter extends OmegaRecyclerView.Adapter<userAdapter.MyViewHolder>   {
    public List<Usuario> lista;
    private onItemClick onItemClick;


    public userAdapter(List<Usuario> l, onItemClick onclick){
        lista = l;
        this.onItemClick = onclick;
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        //View view = inflater.inflate(R.layout.usuario_template, parent, false);
        return new MyViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Usuario user  = lista.get(position);
        holder.display(user);
    }

    public void updateData(Usuario ux){
        boolean nuevo = true;
        for (Usuario u : lista){
            if (u.getId() == ux.getId()) { lista.set(lista.indexOf(u), ux); nuevo = false; }
            ToastHelper.info("Se a modificado el usuario: " + ux.getNombre());
        }

        if (nuevo) {
            lista.add(ux);
            ToastHelper.exito("Se a creado el usuario: " + ux.getNombre());
        }

        notifyDataSetChanged();
    }

    public void deleteData(Usuario ux){
        ToastHelper.normal("Se a eliminado el usuario: " + ux.getNombre());
        lista.remove(ux);
        notifyDataSetChanged();
    }

    public class MyViewHolder extends SwipeViewHolder implements View.OnClickListener {
        private final TextView rol;
        private final TextView nombre;
        private final TextView cedula;
        private final TextView telefono;
        private final ToggleButton activo;
        private final ImageView beliminar;

        UsuarioDao userManager = AppController.daoSession.getUsuarioDao();

        private Usuario currentItem;

        public MyViewHolder(ViewGroup itemView) {
            super(itemView, R.layout.usuario_template, SwipeViewHolder.NO_ID, R.layout.swipe_menu);

            rol = (findViewById(R.id.empresa));
            nombre = (findViewById(R.id.tnombre));
            cedula = (findViewById(R.id.tcedula));
            telefono = (findViewById(R.id.telefono));
            activo = (findViewById(R.id.activo));
            beliminar = (findViewById(R.id.beliminar));
            beliminar.setOnClickListener(this);
            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClick.onClick(view, getAdapterPosition());
                }
            });*/

            contentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //smoothOpenLeftMenu();
                    onItemClick.onClickItemList(v, getAdapterPosition());
                }
            });

            activo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentItem.setActivo(!currentItem.getActivo());
                    userManager.update(currentItem);
                    updateData(currentItem);
                }
            });
        }

        @Override
        public void onClick(final View v) {
            switch (v.getId()) {
                /*case R.id.bestado:
                    currentItem.setActivo(!currentItem.getActivo());
                    userManager.update(currentItem);
                    updateData(currentItem);
                    smoothCloseMenu();
                    break;
*/
                case R.id.beliminar:
                    Dialog.showAsk2(v, "Eliminar usuario", "¿Desea realmente eliminar este usuario?",
                            "Eliminar", "Cancelar", new myDialogInterface() {
                                @Override
                                public View onBuildDialog() {
                                    return null;
                                }
                                @Override
                                public void onCancel()  { smoothCloseMenu(); }
                                @Override
                                public void onResult(View vista) {
                                    currentItem.setEliminado(true);
                                    userManager.update(currentItem);
                                    deleteData(currentItem);
                                    smoothCloseMenu();
                                }
                            });
                    break;
            }
        }

        public void display(Usuario ux) {
            currentItem = ux;
            rol.setText(ux.getRol());
            nombre.setText(ux.getNombre());
            cedula.setText("Cedula: " + ux.getCedula());
            telefono.setText("Tel: " + ux.getTelefono());
            activo.setChecked(ux.getActivo());
        }
    }
}