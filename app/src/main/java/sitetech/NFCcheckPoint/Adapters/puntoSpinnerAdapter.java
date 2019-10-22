package sitetech.NFCcheckPoint.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import sitetech.NFCcheckPoint.db.Punto;
import sitetech.NFCcheckPoint.db.Ruta;
import sitetech.routecheckapp.R;

public class puntoSpinnerAdapter extends BaseAdapter {
    Context context;
    List<Punto> puntos;
    LayoutInflater inflter;
    int itemTemplate;
    public puntoSpinnerAdapter(Context applicationContext, List<Punto> _puntos, int _itemTemplate) {
        this.context = applicationContext;
        puntos = _puntos;
        inflter = (LayoutInflater.from(applicationContext));
        itemTemplate = _itemTemplate;
    }

    public void getSelectedItem(Ruta rx){

    }

    @Override
    public int getCount() {
        return puntos.size();
    }

    @Override
    public Object getItem(int i) {
        return puntos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(itemTemplate, null);
        ImageView icon = (ImageView) view.findViewById(R.id.imageView);
        TextView nombre = (TextView) view.findViewById(R.id.tnombre);
        nombre.setText(puntos.get(i).getNombre());
        return view;
    }
}