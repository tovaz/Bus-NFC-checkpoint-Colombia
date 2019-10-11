package sitetech.NFCcheckPoint.Adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView;
import com.omega_r.libs.omegarecyclerview.swipe_menu.SwipeViewHolder;

import java.util.List;

import sitetech.NFCcheckPoint.AppController;
import sitetech.NFCcheckPoint.Helpers.DialogHelper;
import sitetech.NFCcheckPoint.Helpers.ToastHelper;
import sitetech.NFCcheckPoint.Helpers.myDialogInterface;
import sitetech.NFCcheckPoint.db.Ruta;
import sitetech.NFCcheckPoint.db.RutaDao;
import sitetech.NFCcheckPoint.db.horarioPorRutaDao;
import sitetech.routecheckapp.R;

public class rutaAdapter extends OmegaRecyclerView.Adapter<rutaAdapter.ViewHolder>  {
    public List<Ruta> lista;
    private onItemClick onItemClick;


    public rutaAdapter(List<Ruta> l, onItemClick onclick) {
        lista = l;
        this.onItemClick = onclick;
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    @Override
    public rutaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new rutaAdapter.ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(rutaAdapter.ViewHolder holder, int position) {
        Ruta dato = lista.get(position);
        holder.display(dato);
    }

    public void updateData(Ruta bx) {
        boolean nuevo = true;
        for (Ruta rx : lista) {
            if (rx.getId() == bx.getId()) {
                lista.set(lista.indexOf(rx), bx);
                nuevo = false;
            }
            ToastHelper.info("Se a modificado la ruta: " + bx.getNombre());
        }

        if (nuevo) {
            lista.add(bx);
            ToastHelper.exito("Se a creado la ruta: " + bx.getNombre());
        }

        notifyDataSetChanged();
    }

    public void deleteData(Ruta rx) {
        ToastHelper.normal("Se a eliminado la ruta: " + rx.getNombre());
        lista.remove(rx);
        notifyDataSetChanged();
    }

    public class ViewHolder extends SwipeViewHolder implements View.OnClickListener {
        private final TextView tnombre;
        private final TextView thorarios;
        private final ImageView beliminar;
        RutaDao empresaManager = AppController.daoSession.getRutaDao();

        private Ruta currentItem;

        public ViewHolder(ViewGroup itemView) {
            super(itemView, R.layout.ruta_template, SwipeViewHolder.NO_ID, R.layout.swipe_menu);

            tnombre = (findViewById(R.id.tnombre));
            thorarios = (findViewById(R.id.thorarios));
            beliminar = (findViewById(R.id.beliminar));
            beliminar.setOnClickListener(this);

            contentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //smoothOpenLeftMenu();
                    onItemClick.onClickItemList(v, getAdapterPosition());
                }
            });
        }

        @Override
        public void onClick(final View v) {
            switch (v.getId()) {
                case R.id.beliminar:
                    DialogHelper.showAsk2(v, "Eliminar Ruta", "¿Desea realmente eliminar esta ruta?",
                            "Eliminar", "Cancelar", new myDialogInterface() {
                                @Override
                                public View onBuildDialog() {
                                    return null;
                                }

                                @Override
                                public void onCancel() {
                                    smoothCloseMenu();
                                }

                                @Override
                                public void onResult(View vista) {
                                    currentItem.setEliminada(true);
                                    empresaManager.update(currentItem);
                                    deleteData(currentItem);
                                    smoothCloseMenu();
                                }
                            });
                    break;
            }
        }

        public void display(Ruta rx) {
            currentItem = rx;
            tnombre.setText(rx.getNombre().toString());

            horarioPorRutaDao hxruta= AppController.daoSession.getHorarioPorRutaDao();
            int horarios = hxruta.queryBuilder()
                    .where(horarioPorRutaDao.Properties.RutaId.eq(rx.getId()), horarioPorRutaDao.Properties.Eliminado.eq(false))
                    .list().size();

            thorarios.setText("Horarios asignados: " + String.valueOf(horarios));
        }
    }

}
