package sitetech.NFCcheckPoint.Adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView;
import com.omega_r.libs.omegarecyclerview.swipe_menu.SwipeViewHolder;

import java.util.List;

import sitetech.NFCcheckPoint.AppController;
import sitetech.NFCcheckPoint.Helpers.Dialog;
import sitetech.NFCcheckPoint.Helpers.myDialogInterface;
import sitetech.NFCcheckPoint.db.Bus;
import sitetech.NFCcheckPoint.db.BusDao;
import sitetech.routecheckapp.R;

import static sitetech.NFCcheckPoint.Helpers.activityHelper.mostrarToast;

public class busAdapter extends OmegaRecyclerView.Adapter<busAdapter.ViewHolder> {
    public List<Bus> lista;
    private onItemClick onItemClick;


    public busAdapter(List<Bus> l, onItemClick onclick) {
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
        Bus dato = lista.get(position);
        holder.display(dato);
    }

    public void updateData(View v, Bus bx) {
        boolean nuevo = true;
        for (Bus rx : lista) {
            if (rx.getId() == bx.getId()) {
                lista.set(lista.indexOf(rx), bx);
                nuevo = false;
            }
            mostrarToast("Se a modificado el bus: " + bx.getPlaca());
        }

        if (nuevo) {
            lista.add(bx);
            mostrarToast("Se a creado el bus: " + bx.getPlaca());
        }

        notifyDataSetChanged();
    }

    public void deleteData(View v, Bus rx) {
        mostrarToast("Se a eliminado el bus: " + rx.getPlaca());
        lista.remove(rx);
        notifyDataSetChanged();
    }

    public class ViewHolder extends SwipeViewHolder implements View.OnClickListener {
        private final TextView placa;
        private final TextView interno;
        private final TextView empresa;

        BusDao busManager = AppController.daoSession.getBusDao();

        private Bus currentItem;

        public ViewHolder(ViewGroup itemView) {
            super(itemView, R.layout.bus_template, SwipeViewHolder.NO_ID, R.layout.swipe_menu);

            placa = (findViewById(R.id.tnombre));
            interno = (findViewById(R.id.tcedula));
            empresa = (findViewById(R.id.empresa));

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
                    onItemClick.onClick(v, getAdapterPosition());
                }
            });
        }

        @Override
        public void onClick(final View v) {
            switch (v.getId()) {
                case R.id.beliminar:
                    Dialog.showAsk2(v, "Eliminar Bus", "¿Desea realmente eliminar este bus?",
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
                            busManager.update(currentItem);
                            deleteData(v, currentItem);
                            smoothCloseMenu();
                        }
                    });
                    break;
            }
        }

        public void display(Bus rx) {
            currentItem = rx;
            placa.setText(rx.getPlaca());
            interno.setText("Interno: " + rx.getInterno());
            empresa.setText("Empresa: " + rx.getEmpresa().getNombre());
        }
    }
}
