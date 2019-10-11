package sitetech.NFCcheckPoint.Adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView;
import com.omega_r.libs.omegarecyclerview.swipe_menu.SwipeViewHolder;

import java.util.List;

import sitetech.NFCcheckPoint.AppController;
import sitetech.NFCcheckPoint.Helpers.ToastHelper;
import sitetech.NFCcheckPoint.db.Horario;
import sitetech.NFCcheckPoint.db.HorarioDao;
import sitetech.routecheckapp.R;

public class horarioSelAdapter extends OmegaRecyclerView.Adapter<horarioSelAdapter.ViewHolder> {
    public List<Horario> lista;
    private onItemClick onItemClick;


    public horarioSelAdapter(List<Horario> l, onItemClick onclick) {
        lista = l;
        this.onItemClick = onclick;
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    @Override
    public horarioSelAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new horarioSelAdapter.ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(horarioSelAdapter.ViewHolder holder, int position) {
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
            ToastHelper.exito("Horario asignado.");
        }

        notifyDataSetChanged();
    }

    public void deleteData(Horario rx) {
        //ToastHelper.normal("Se a eliminado el horario " + rx.getHora());
        lista.remove(rx);
        notifyDataSetChanged();
    }

    public class ViewHolder extends SwipeViewHolder implements View.OnClickListener {
        private final TextView tnombre;
        private final TextView thora;
        private final TextView thoraFestivo;
        HorarioDao horarioManager = AppController.daoSession.getHorarioDao();

        private Horario currentItem;

        public ViewHolder(ViewGroup itemView) {
            super(itemView, R.layout.horario_sel_template, SwipeViewHolder.NO_ID, SwipeViewHolder.NO_ID);

            tnombre = (findViewById(R.id.tnombre));
            thora = (findViewById(R.id.thora));
            thoraFestivo = (findViewById(R.id.thoraFestivo));

            contentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick.onClickItemList(v, getAdapterPosition());
                    deleteData(lista.get(getAdapterPosition()));
                }
            });
        }

        @Override
        public void onClick(final View v) {

        }

        public void display(Horario rx) {
            currentItem = rx;
            if (rx.getNombre() == null) tnombre.setText("");
            else tnombre.setText(rx.getNombre().toString());

            thora.setText(rx.getHoraDesde() + " a " + rx.getHoraHasta() + " Min Max: " + rx.getMaxMinutos());
            thoraFestivo.setText("Dia Fest y FD: " + rx.getHoraFestivoDesde() + " a " + rx.getHoraFestivoHasta() + " Min Max: " + rx.getMaxMinutosFestivo());
        }
    }
}
