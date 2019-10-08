package sitetech.NFCcheckPoint.Adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView;
import com.omega_r.libs.omegarecyclerview.swipe_menu.SwipeViewHolder;

import java.util.List;

import sitetech.NFCcheckPoint.AppController;
import sitetech.NFCcheckPoint.Helpers.Dialog;
import sitetech.NFCcheckPoint.Helpers.ToastHelper;
import sitetech.NFCcheckPoint.Helpers.myDialogInterface;
import sitetech.NFCcheckPoint.db.Horario;
import sitetech.NFCcheckPoint.db.HorarioDao;
import sitetech.NFCcheckPoint.db.Ruta;
import sitetech.routecheckapp.R;

public class rutaSelAdapter extends OmegaRecyclerView.Adapter<rutaSelAdapter.ViewHolder> {
    public List<Ruta> lista;
    private onItemClick onItemClick;


    public rutaSelAdapter(List<Ruta> l, onItemClick onclick) {
        lista = l;
        this.onItemClick = onclick;
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    @Override
    public rutaSelAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new rutaSelAdapter.ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(rutaSelAdapter.ViewHolder holder, int position) {
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
            ToastHelper.info("Se a modificado el horario.");
        }

        if (nuevo) {
            lista.add(bx);
            ToastHelper.exito("Horario asignado.");
        }

        notifyDataSetChanged();
    }

    public void deleteData(Ruta rx) {
        //ToastHelper.normal("Se a eliminado el horario " + rx.getHora());
        lista.remove(rx);
        notifyDataSetChanged();
    }

    public class ViewHolder extends SwipeViewHolder implements View.OnClickListener {
        private final TextView tnombre;
        HorarioDao horarioManager = AppController.daoSession.getHorarioDao();

        private Ruta currentItem;

        public ViewHolder(ViewGroup itemView) {
            super(itemView, R.layout.ruta_sel_template, SwipeViewHolder.NO_ID, SwipeViewHolder.NO_ID);

            tnombre = (findViewById(R.id.tnombre));

            contentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick.onClickItemList(v, getAdapterPosition());
                    //deleteData(lista.get(getAdapterPosition()));
                }
            });
        }

        @Override
        public void onClick(final View v) {

        }

        public void display(Ruta rx) {
            currentItem = rx;
            if (rx.getNombre() == null) tnombre.setText("");
            else tnombre.setText(rx.getNombre().toString());
        }
    }
}
