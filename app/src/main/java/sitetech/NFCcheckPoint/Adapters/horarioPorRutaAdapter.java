package sitetech.NFCcheckPoint.Adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView;
import com.omega_r.libs.omegarecyclerview.swipe_menu.SwipeViewHolder;

import java.io.Serializable;
import java.util.List;

import sitetech.NFCcheckPoint.AppController;
import sitetech.NFCcheckPoint.Helpers.Dialog;
import sitetech.NFCcheckPoint.Helpers.ToastHelper;
import sitetech.NFCcheckPoint.Helpers.myDialogInterface;
import sitetech.NFCcheckPoint.db.Horario;
import sitetech.NFCcheckPoint.db.HorarioDao;
import sitetech.NFCcheckPoint.db.horarioPorRuta;
import sitetech.NFCcheckPoint.db.horarioPorRutaDao;
import sitetech.routecheckapp.R;

public class horarioPorRutaAdapter extends OmegaRecyclerView.Adapter<horarioPorRutaAdapter.ViewHolder> implements Serializable {
    public List<horarioPorRuta> lista;
    private onItemClick onItemClick;


    public horarioPorRutaAdapter(List<horarioPorRuta> l, onItemClick onclick) {
        lista = l;
        this.onItemClick = onclick;
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    @Override
    public horarioPorRutaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new horarioPorRutaAdapter.ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(horarioPorRutaAdapter.ViewHolder holder, int position) {
        horarioPorRuta dato = lista.get(position);
        holder.display(dato);
    }

    public void updateData(horarioPorRuta bx) {
        boolean nuevo = true;
        for (horarioPorRuta rx : lista) {
            if (rx.getId() == bx.getId()) {
                lista.set(lista.indexOf(rx), bx);
                nuevo = false;
            }
            ToastHelper.info("Se a modificado el horario.");
        }

        if (nuevo) {
            lista.add(bx);
            ToastHelper.exito("Horario asignado a la ruta.");
        }

        notifyDataSetChanged();
    }

    public void deleteData(horarioPorRuta rx) {
        ToastHelper.normal("Horario desasignado de la ruta.");
        lista.remove(rx);
        notifyDataSetChanged();
    }

    public class ViewHolder extends SwipeViewHolder implements View.OnClickListener {
        private final TextView tnombre;
        private final TextView thora;
        private final TextView thoraFestivo;
        private final ImageView beliminar;
        private horarioPorRuta currentItem;

        horarioPorRutaDao horarioManager = AppController.daoSession.getHorarioPorRutaDao();

        public ViewHolder(ViewGroup itemView) {
            super(itemView, R.layout.horarios_porruta_template, SwipeViewHolder.NO_ID, R.layout.swipe_menu);

            tnombre = (findViewById(R.id.tnombre));
            thora = (findViewById(R.id.thora));
            thoraFestivo = (findViewById(R.id.thoraFestivo));
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
                    Dialog.showAsk2(v, "Quitar Horario", "¿Desea realmente quitar el horario de la ruta?",
                            "Quitar", "Cancelar", new myDialogInterface() {
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
                                    currentItem.setEliminado(true);
                                    horarioManager.update(currentItem);
                                    deleteData(currentItem);
                                    smoothCloseMenu();
                                }
                            });
                    break;
            }
        }

        public void display(horarioPorRuta rx) {
            currentItem = rx;
            if (rx.getHorario().getNombre() == null) tnombre.setText("");
            else tnombre.setText(rx.getHorario().getNombre().toString());

            thora.setText(rx.getHorario().getHora() + " Min Max: " + rx.getHorario().getMaxMinutos());
            thoraFestivo.setText(rx.getHorario().getHoraFestivo() + " Min Max: " + rx.getHorario().getMaxMinutosFestivo());
        }
    }
}
