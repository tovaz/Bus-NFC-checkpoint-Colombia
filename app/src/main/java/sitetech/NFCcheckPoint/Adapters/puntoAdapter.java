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
import sitetech.NFCcheckPoint.db.Punto;
import sitetech.NFCcheckPoint.db.PuntoDao;
import sitetech.routecheckapp.R;

public class puntoAdapter extends OmegaRecyclerView.Adapter<puntoAdapter.ViewHolder>  {
    public List<Punto> lista;
    private onItemClick onItemClick;


    public puntoAdapter(List<Punto> l, onItemClick onclick) {
        lista = l;
        this.onItemClick = onclick;
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    @Override
    public puntoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new puntoAdapter.ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(puntoAdapter.ViewHolder holder, int position) {
        Punto dato = lista.get(position);
        holder.display(dato);
    }

    public void updateData(Punto bx) {
        boolean nuevo = true;
        for (Punto rx : lista) {
            if (rx.getId() == bx.getId()) {
                lista.set(lista.indexOf(rx), bx);
                nuevo = false;
            }
            ToastHelper.info("Se a modificado el punto: " + bx.getNombre());
        }

        if (nuevo) {
            lista.add(bx);
            ToastHelper.exito("Se a creado el punto: " + bx.getNombre());
        }

        notifyDataSetChanged();
    }

    public void deleteData(Punto rx) {
        ToastHelper.normal("Se a eliminado el punto: " + rx.getNombre());
        lista.remove(rx);
        notifyDataSetChanged();
    }

    public class ViewHolder extends SwipeViewHolder implements View.OnClickListener {
        private final TextView tnombre;
        private final ImageView beliminar;
        PuntoDao puntosManager = AppController.daoSession.getPuntoDao();

        private Punto currentItem;

        public ViewHolder(ViewGroup itemView) {
            super(itemView, R.layout.ruta_template, SwipeViewHolder.NO_ID, R.layout.swipe_menu);

            tnombre = (findViewById(R.id.tnombre));
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
                    DialogHelper.showAsk2(v, "Eliminar Punto", "¿Desea realmente eliminar este punto?",
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
                                    puntosManager.update(currentItem);
                                    deleteData(currentItem);
                                    smoothCloseMenu();
                                }
                            });
                    break;
            }
        }

        public void display(Punto rx) {
            currentItem = rx;
            tnombre.setText(rx.getNombre().toString());
        }
    }

}
