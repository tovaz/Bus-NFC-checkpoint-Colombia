package sitetech.NFCcheckPoint.Adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView;
import com.omega_r.libs.omegarecyclerview.swipe_menu.SwipeViewHolder;

import java.text.SimpleDateFormat;
import java.util.List;

import sitetech.NFCcheckPoint.AppController;
import sitetech.NFCcheckPoint.Helpers.Dialog;
import sitetech.NFCcheckPoint.Helpers.ToastHelper;
import sitetech.NFCcheckPoint.Helpers.myDialogInterface;
import sitetech.NFCcheckPoint.db.DiaFestivo;
import sitetech.NFCcheckPoint.db.DiaFestivoDao;
import sitetech.routecheckapp.R;

public class diaFestivoAdapter extends OmegaRecyclerView.Adapter<diaFestivoAdapter.ViewHolder> {
    public List<DiaFestivo> lista;
    private onItemClick onItemClick;


    public diaFestivoAdapter(List<DiaFestivo> l, onItemClick onclick) {
        lista = l;
        this.onItemClick = onclick;
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        //View view = inflater.inflate(R.layout.usuario_template, parent, false);
        return new ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DiaFestivo dato = lista.get(position);
        holder.display(dato);
    }

    public void updateData(DiaFestivo bx) {
        boolean nuevo = true;
        for (DiaFestivo rx : lista) {
            if (rx.getId() == bx.getId()) {
                lista.set(lista.indexOf(rx), bx);
                nuevo = false;
            }
            ToastHelper.info("Se a modificado el dia: " + bx.getNombre());
        }

        if (nuevo) {
            lista.add(bx);
            ToastHelper.exito("Se a creado el dia festivo: " + bx.getNombre());
        }

        notifyDataSetChanged();
    }

    public void deleteData(DiaFestivo rx) {
        ToastHelper.normal("Se a eliminado el dia festivo: " + rx.getNombre());
        lista.remove(rx);
        notifyDataSetChanged();
    }

    public class ViewHolder extends SwipeViewHolder implements View.OnClickListener {
        private final TextView tnombre;
        private final TextView tfecha;
        private final ImageView beliminar;
        DiaFestivoDao diaManager = AppController.daoSession.getDiaFestivoDao();

        private DiaFestivo currentItem;

        public ViewHolder(ViewGroup itemView) {
            super(itemView, R.layout.dia_festivo_template, SwipeViewHolder.NO_ID, R.layout.swipe_menu);

            tnombre = (findViewById(R.id.tnombre));
            tfecha = (findViewById(R.id.tfecha));
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
                    Dialog.showAsk2(v, "Eliminar Dia Festivo", "¿Desea realmente eliminar este dia?",
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
                                    diaManager.update(currentItem);
                                    deleteData(currentItem);
                                    smoothCloseMenu();
                                }
                            });
                    break;
            }
        }

        public void display(DiaFestivo rx) {
            currentItem = rx;
            tnombre.setText(rx.getNombre().toString());

            SimpleDateFormat sdf = new SimpleDateFormat("dd - MMM - yyyy");
            tfecha.setText(sdf.format(rx.getDia()));
        }
    }
}

