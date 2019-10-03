package sitetech.NFCcheckPoint.Adapters;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import sitetech.NFCcheckPoint.db.Usuario;
import sitetech.routecheckapp.R;

public class userAdapter extends RecyclerView.Adapter<userAdapter.MyViewHolder> {
    private List<Usuario> lista;
    public userAdapter(List<Usuario> l){
        lista = l;
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.usuario_template, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Usuario user  = lista.get(position);
        holder.display(user);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView rol;
        private final TextView nombre;
        private final TextView cedula;
        private final TextView telefono;
        private final Switch activo;

        private Usuario currentItem;

        public MyViewHolder(final View itemView) {
            super(itemView);

            rol = (itemView.findViewById(R.id.rol));
            nombre = (itemView.findViewById(R.id.nombre));
            cedula = (itemView.findViewById(R.id.cedula));
            telefono = (itemView.findViewById(R.id.telefono));
            activo = (itemView.findViewById(R.id.activo));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(itemView.getContext())
                            .setTitle(currentItem.getId().toString())
                            .setMessage(currentItem.getNombre().toString())
                            .show();
                }
            });
        }

        public void display(Usuario ux) {
            currentItem = ux;
            rol.setText(ux.getRol());
            nombre.setText(ux.getNombre());
            cedula.setText(ux.getCedula());
            telefono.setText(ux.getTelefono());
            activo.setChecked(ux.getActivo());
        }
    }
}