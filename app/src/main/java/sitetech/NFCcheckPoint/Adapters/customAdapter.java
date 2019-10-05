package sitetech.NFCcheckPoint.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import sitetech.NFCcheckPoint.db.Empresa;
import sitetech.routecheckapp.R;

public class customAdapter extends BaseAdapter {
    Context context;
    List<Empresa> empresas;
    LayoutInflater inflter;

    public customAdapter(Context applicationContext, List<Empresa> _empresas) {
        this.context = applicationContext;
        empresas = _empresas;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return empresas.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.custom_spinner, null);
        ImageView icon = (ImageView) view.findViewById(R.id.imageView);
        TextView names = (TextView) view.findViewById(R.id.textView);
        names.setText(empresas.get(i).getNombre());
        return view;
    }
}