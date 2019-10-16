package sitetech.NFCcheckPoint.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.plus.model.people.Person;

import java.util.List;

import sitetech.NFCcheckPoint.db.Empresa;
import sitetech.NFCcheckPoint.db.Ruta;
import sitetech.routecheckapp.R;

public class rutaSpinnerAdapter extends BaseAdapter {
    Context context;
    List<Ruta> rutas;
    LayoutInflater inflter;
    int itemTemplate;
    public rutaSpinnerAdapter(Context applicationContext, List<Ruta> _rutas, int _itemTemplate) {
        this.context = applicationContext;
        rutas = _rutas;
        inflter = (LayoutInflater.from(applicationContext));
        itemTemplate = _itemTemplate;
    }

    public void getSelectedItem(Ruta rx){

    }

    @Override
    public int getCount() {
        return rutas.size();
    }

    @Override
    public Object getItem(int i) {
        return rutas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(itemTemplate, null);
        ImageView icon = (ImageView) view.findViewById(R.id.imageView);
        TextView names = (TextView) view.findViewById(R.id.textView);
        names.setText(rutas.get(i).getNombre());
        return view;
    }
}