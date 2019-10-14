package sitetech.NFCcheckPoint.Adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import sitetech.NFCcheckPoint.AppController;
import sitetech.NFCcheckPoint.Helpers.ToastHelper;
import sitetech.NFCcheckPoint.db.HorarioDao;
import sitetech.NFCcheckPoint.db.Ruta;
import sitetech.routecheckapp.R;

public class rutaSelAdapter extends RecyclerView.Adapter<rutaSelAdapter.ViewHolder> {
    public List<Ruta> lista;
    private onItemClick onItemClick;
    private int selectedItem;

    public rutaSelAdapter(List<Ruta> l, onItemClick onclick) {
        lista = l;
        this.onItemClick = onclick;

        //if (l.size() > 0)
        //    setSelectedItem(l.get(0));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ruta_sel_template, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    @Override
    public void onBindViewHolder(rutaSelAdapter.ViewHolder holder, int position) {
        Ruta dato = lista.get(position);
        holder.display(dato);
        holder.itemView.setSelected(selectedItem == position);

        TextView tx = holder.itemView.findViewById(R.id.tnombre);
        ImageView rimg = holder.itemView.findViewById(R.id.rimg);

        if (holder.itemView.isSelected()) {
            tx.setTextColor(Color.rgb(240,240,240));
            rimg.getDrawable().setTint(Color.rgb(240,240,240));
        } else {
            tx.setTextColor(Color.rgb(32, 66, 134));
            rimg.getDrawable().setTint(Color.rgb(32, 66, 134));
        }

        //holder.itemView.setBackgroundColor(selectedItem == position ? Color.GREEN : Color.TRANSPARENT);
    }

    public void setSelectedItem(Ruta rx){
        notifyItemChanged(selectedItem);
        selectedItem = lista.indexOf(rx);
        notifyItemChanged(selectedItem);
    }

    /*public Ruta getSelectedItem(){
        return selectedItem;
    }*/

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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView tnombre;
        private final ImageView rimg;

        HorarioDao horarioManager = AppController.daoSession.getHorarioDao();

        private Ruta currentItem;


        public ViewHolder(View itemView) {
            super(itemView);
            tnombre = itemView.findViewById(R.id.tnombre);
            rimg = itemView.findViewById(R.id.rimg);

            tnombre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //setSelectedItem(currentItem);

                    onItemClick.onClickItemList(v, getAdapterPosition());
                    notifyItemChanged(selectedItem);
                    selectedItem = getLayoutPosition();
                    notifyItemChanged(selectedItem);
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //setSelectedItem(currentItem);

                    onItemClick.onClickItemList(v, getAdapterPosition());
                    notifyItemChanged(selectedItem);
                    selectedItem = getLayoutPosition();
                    notifyItemChanged(selectedItem);
                }
            });
        }

        private Ruta backItem;
        private void setSelection(Ruta rx){
            //if (backItem == null)

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
