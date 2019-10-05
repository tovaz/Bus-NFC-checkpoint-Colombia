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
import sitetech.NFCcheckPoint.db.Empresa;
import sitetech.NFCcheckPoint.db.EmpresaDao;
import sitetech.routecheckapp.R;

import static sitetech.NFCcheckPoint.Helpers.activityHelper.mostrarToast;

public class empresaAdapter extends OmegaRecyclerView.Adapter<empresaAdapter.ViewHolder> {
    public List<Empresa> lista;
    private onItemClick onItemClick;


    public empresaAdapter(List<Empresa> l, onItemClick onclick) {
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
        Empresa dato = lista.get(position);
        holder.display(dato);
    }

    public void updateData(Empresa bx) {
        boolean nuevo = true;
        for (Empresa rx : lista) {
            if (rx.getId() == bx.getId()) {
                lista.set(lista.indexOf(rx), bx);
                nuevo = false;
            }
            mostrarToast("Se a modificado la empresa: " + bx.getNombre());
        }

        if (nuevo) {
            lista.add(bx);
            mostrarToast("Se a creado la empresa: " + bx.getNombre());
        }

        notifyDataSetChanged();
    }

    public void deleteData(Empresa rx) {
        mostrarToast("Se a eliminado la empresa: " + rx.getNombre());
        lista.remove(rx);
        notifyDataSetChanged();
    }

    public class ViewHolder extends SwipeViewHolder implements View.OnClickListener {
        private final TextView tnombre;
        private final TextView ttelefono;

        EmpresaDao empresaManager = AppController.daoSession.getEmpresaDao();

        private Empresa currentItem;

        public ViewHolder(ViewGroup itemView) {
            super(itemView, R.layout.empresa_template, SwipeViewHolder.NO_ID, R.layout.swipe_menu);

            tnombre = (findViewById(R.id.tnombre));
            ttelefono = (findViewById(R.id.tcedula));

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
                    Dialog.showAsk2(v, "Eliminar Empresa", "¿Desea realmente eliminar esta empresa?",
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
                                    empresaManager.update(currentItem);
                                    deleteData(currentItem);
                                    smoothCloseMenu();
                                }
                            });
                    break;
            }
        }

        public void display(Empresa rx) {
            currentItem = rx;
            tnombre.setText(rx.getNombre().toString());
            ttelefono.setText("Telefono: " + rx.getTelefono().toString());
        }
    }
}
