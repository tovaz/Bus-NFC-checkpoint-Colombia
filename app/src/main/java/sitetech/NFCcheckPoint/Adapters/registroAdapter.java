package sitetech.NFCcheckPoint.Adapters;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView;
import com.omega_r.libs.omegarecyclerview.swipe_menu.SwipeViewHolder;

import java.util.List;

import sitetech.NFCcheckPoint.AppController;
import sitetech.NFCcheckPoint.Helpers.TimeHelper;
import sitetech.NFCcheckPoint.db.Registro_Turno;
import sitetech.NFCcheckPoint.db.RutaDao;
import sitetech.NFCcheckPoint.ui.operador.RegistroEditarFragment;
import sitetech.routecheckapp.R;

public class registroAdapter extends OmegaRecyclerView.Adapter<registroAdapter.ViewHolder>  {
    public List<Registro_Turno> lista;
    private onItemClick onItemClick;
    private onEditClick onEditClick;

    public registroAdapter(List<Registro_Turno> l, onItemClick onclick, onEditClick onEditar) {
        lista = l;
        this.onItemClick = onclick;
        this.onEditClick = onEditar;
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    @Override
    public registroAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new registroAdapter.ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(registroAdapter.ViewHolder holder, int position) {
        Registro_Turno dato = lista.get(position);
        holder.display(dato);
    }

    public void updateData(Registro_Turno bx) {
        boolean nuevo = true;

        notifyDataSetChanged();
    }

    public void deleteData(Registro_Turno rx) {
        lista.remove(rx);
        notifyDataSetChanged();
    }

    public class ViewHolder extends SwipeViewHolder implements View.OnClickListener{
        private final TextView tindex;
        private final TextView truta;
        private final TextView tplaca;
        private final TextView tinterno;
        private final TextView tempresa;
        private final TextView tregistro;
        private final TextView tdespacho;
        private final TextView tdemorado;
        private final ImageView beditar;

        RutaDao empresaManager = AppController.daoSession.getRutaDao();

        private Registro_Turno currentItem;

        public ViewHolder(ViewGroup itemView) {
            super(itemView, R.layout.registro_historial_template, SwipeViewHolder.NO_ID, SwipeViewHolder.NO_ID);

            tindex = (findViewById(R.id.tindex));
            truta = (findViewById(R.id.truta));
            tplaca = (findViewById(R.id.tplaca));
            tinterno = (findViewById(R.id.tinterno));
            tempresa = (findViewById(R.id.tempresa));
            tregistro = (findViewById(R.id.tregistro));
            tdespacho = (findViewById(R.id.tdespacho));
            tdemorado = (findViewById(R.id.tdemorado));
            beditar = (findViewById(R.id.beditar));

            beditar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onEditClick.onEditItemClick(v, getAdapterPosition());
                }
            });

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

        }

        public void display(Registro_Turno rx) {
            currentItem = rx;
            tindex.setText(String.valueOf(lista.indexOf(rx)+1));
            truta.setText(rx.getRuta().getNombre());
            tplaca.setText(rx.getBus().getPlaca());
            tinterno.setText(rx.getBus().getInterno());

            try {
                tempresa.setText(rx.getBus().getEmpresa().getNombre());
            } catch (Exception e){ tempresa.setText("Error entity detached"); }

            tregistro.setText(TimeHelper.getDate(rx.getFecha(), "dd MMM yyyy - HH:mm:ss"));
            tdespacho.setText(rx.getDespacho());
            if (!rx.getMinAtrazado().equals("00:00:00"))
                tdemorado.setText(rx.getMinAtrazado());
            else
                tdemorado.setText(rx.getMinAdelantado());
            
        }
    }

}
