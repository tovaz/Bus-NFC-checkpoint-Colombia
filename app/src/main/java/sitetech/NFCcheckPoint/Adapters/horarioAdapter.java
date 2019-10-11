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
import sitetech.NFCcheckPoint.db.Horario;
import sitetech.NFCcheckPoint.db.HorarioDao;
import sitetech.routecheckapp.R;

public class horarioAdapter extends OmegaRecyclerView.Adapter<horarioAdapter.ViewHolder> {
    public List<Horario> lista;
    private onItemClick onItemClick;


    public horarioAdapter(List<Horario> l, onItemClick onclick) {
        lista = l;
        this.onItemClick = onclick;
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    @Override
    public horarioAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new horarioAdapter.ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(horarioAdapter.ViewHolder holder, int position) {
        Horario dato = lista.get(position);
        holder.display(dato);
    }

    public void updateData(Horario bx) {
        boolean nuevo = true;
        for (Horario rx : lista) {
            if (rx.getId() == bx.getId()) {
                lista.set(lista.indexOf(rx), bx);
                nuevo = false;
            }
            ToastHelper.info("Se a modificado el horario.");
        }

        if (nuevo) {
            lista.add(bx);
            ToastHelper.exito("Se a creado el horario.");
        }

        notifyDataSetChanged();
    }

    public void deleteData(Horario rx) {
        ToastHelper.normal("Se a eliminado el horario " + rx.getHoraDesde());
        lista.remove(rx);
        notifyDataSetChanged();
    }

    public class ViewHolder extends SwipeViewHolder implements View.OnClickListener {
        private final TextView tnombre;
        private final TextView thora;
        private final TextView thoraFestivo;
        private final ImageView beliminar;
        HorarioDao horarioManager = AppController.daoSession.getHorarioDao();

        private Horario currentItem;

        public ViewHolder(ViewGroup itemView) {
            super(itemView, R.layout.horario_template, SwipeViewHolder.NO_ID, R.layout.swipe_menu);

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
                    DialogHelper.showAsk2(v, "Eliminar Horario", "¿Desea realmente eliminar este horario?",
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
                                    currentItem.setEliminado(true);
                                    horarioManager.update(currentItem);
                                    deleteData(currentItem);
                                    smoothCloseMenu();
                                }
                            });
                    break;
            }
        }

        public void display(Horario rx) {
            currentItem = rx;
            if (rx.getNombre() == null) tnombre.setText("");
            else tnombre.setText(rx.getNombre().toString());

            thora.setText(rx.getHoraDesde() + " a " + rx.getHoraHasta() + " Min Max: " + rx.getMaxMinutos());
            thoraFestivo.setText(rx.getHoraFestivoDesde() + " a " + rx.getHoraFestivoHasta() + " Min Max: " + rx.getMaxMinutosFestivo());
        }
    }
}
