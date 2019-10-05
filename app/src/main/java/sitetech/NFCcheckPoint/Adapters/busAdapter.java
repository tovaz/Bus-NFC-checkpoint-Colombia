package sitetech.NFCcheckPoint.Adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView;
import com.omega_r.libs.omegarecyclerview.swipe_menu.SwipeViewHolder;

import java.util.List;

import sitetech.NFCcheckPoint.AppController;
import sitetech.NFCcheckPoint.Helpers.Dialog;
import sitetech.NFCcheckPoint.Helpers.ToastHelper;
import sitetech.NFCcheckPoint.Helpers.myDialogInterface;
import sitetech.NFCcheckPoint.db.Bus;
import sitetech.NFCcheckPoint.db.BusDao;
import sitetech.routecheckapp.R;

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

    public void updateData(Bus bx) {
        boolean nuevo = true;
        for (Bus rx : lista) {
            if (rx.getId() == bx.getId()) { lista.set(lista.indexOf(rx), bx); nuevo = false; }
            ToastHelper.info("Se a modificado el bus: " + bx.getPlaca());
        }

        if (nuevo) {
            lista.add(bx);
            ToastHelper.exito("Se a creado el bus: " + bx.getPlaca());
        }

        notifyDataSetChanged();
    }

    public void deleteData(Bus rx) {
        ToastHelper.normal("Se a eliminado el bus: " + rx.getPlaca());
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
            super(itemView, R.layout.bus_template, SwipeViewHolder.NO_ID, R.layout.swipe_menu_bus);

            placa = (findViewById(R.id.tnombre));
            interno = (findViewById(R.id.tcedula));
            empresa = (findViewById(R.id.empresa));

            contentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //smoothOpenRightMenu();
                    onItemClick.onClickItem(v, getAdapterPosition());
                }
            });
        }

        @Override
        public void onClick(final View v) {
            ToastHelper.info("HOLA dio click aqui...");
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
                            deleteData(currentItem);
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
