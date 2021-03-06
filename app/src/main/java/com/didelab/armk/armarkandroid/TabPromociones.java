package com.didelab.armk.armarkandroid;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ElianaXimena on 11/08/2016.
 */
public class TabPromociones extends Fragment {
    private final String TAG = "TabPromociones";
    private SearchableAdapter mSearchableAdapter = null;
    private ArrayList<String> listaIdRtaOriginal = null;
    private Context context;
    private View viewFrg;
    private ListView lvPromociones;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewFrg = inflater.inflate(R.layout.tab_promociones, container, false);
        context = getActivity();
        setHasOptionsMenu(true);

        lvPromociones =(ListView) viewFrg.findViewById(R.id.lv_promociones);

        mSearchableAdapter = new SearchableAdapter(context);
        lvPromociones.setAdapter(mSearchableAdapter);
        return viewFrg;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_buscar));
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(getActivity().SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mSearchableAdapter.getFilter().filter(newText);
                return false;
            }
        });

    }

    /**
     * Obtiene la información.
     * @return
     * @author Eliana Ximena Gonzalez Morales 11/04/2016 16:39:57
     */
    private ArrayList<ItemPromociones> getDatos() {
        ArrayList<ItemPromociones> datos = new ArrayList<>();

/*        datos.add(new ItemAlmacenes(id, nombrePromocion, descripcion1, descripcion2,
                rutaImagenProducto,  promocion) );*/
        datos.add(new ItemPromociones(0, "BOLSO ROJO", "Payless","-25%",
                "http://bmujer.com/wp-content/uploads/2006/05/hermes.jpg") );
        datos.add(new ItemPromociones(2, "ANILLOS", "Lynx","-5%",
                "http://www.brides.com/images/editorial/2008_bridescom/engagement_colors/00_main/009_primary.jpg") );
        datos.add(new ItemPromociones(3, "SANDWICH ESPECIAL", "Lynx","2x1",
                "http://middletownpizzapalace.com/theme/images/header/sandwich.png") );
        datos.add(new ItemPromociones(4, "ZAPATILLA CHAROL", "Payless","-10%",
                "http://zapachic.com/wp-content/uploads/2013/08/4-Payless-Colombia-zapatos-de-tac%C3%B3n-charol-azul-.jpg") );


        return datos;
    }


/*
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);
    }*/




    /**
     * Adapter que gestiona los datos en la lista.
     *
     * @author Eliana Ximena Gonzalez Morales
     *
     */
    class SearchableAdapter extends BaseAdapter implements Filterable {

        private ArrayList<ItemPromociones> originalData = null;
        private ArrayList<ItemPromociones> filteredData = null;
        private LayoutInflater mInflater;
        private ItemFilter mFilter = new ItemFilter();

        public SearchableAdapter(Context context) {
            this.originalData = getDatos();
            this.filteredData = originalData;
            mInflater = LayoutInflater.from(context);
        }

        public int getCount() {
            return filteredData.size();
        }

        public Object getItem(int position) {
            return filteredData.get(position);
        }

        public long getItemId(int position) {
            return position;
        }
//
//        public void remove(int posicionOriginal) {
//            originalData.remove(posicionOriginal);
//            listaIdRtaOriginal.remove(posicionOriginal);
//            getFilter().filter(strDatoBusqueda);
//        }

        public View getView(final int position, View convertView,
                            ViewGroup parent) {

            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(
                        R.layout.lista_promociones_row, null);
                holder = new ViewHolder();
           /*     holder.id = (TextView) convertView
                        .findViewById(R.id.txt_lista_envios_id);*/
                holder.txtNombreProducto = (TextView) convertView
                        .findViewById(R.id.txt_lista_promociones_nombre_producto);
                holder.txtDescripcion1 = (TextView) convertView
                        .findViewById(R.id.txt_lista_promociones_descripcion1);
                holder.txtDescripcion2 = (TextView) convertView
                        .findViewById(R.id.txt_lista_promociones_descripcion2);
                holder.llyInformacionPromocion = (LinearLayout) convertView
                        .findViewById(R.id.lly_lista_promociones_informacion);
                holder.imvLogo = (ImageView) convertView
                        .findViewById(R.id.imv_lista_promociones_producto);
                convertView.setTag(holder);
            } else {

                holder = (ViewHolder) convertView.getTag();
            }

            final ItemPromociones item = filteredData.get(position);
            //holder.id.setText((position + 1) + ". ");
            holder.txtNombreProducto.setText(item.getNombrePromocion());
            holder.txtDescripcion1.setText(item.descripcion1);
            holder.txtDescripcion2.setText(item.descripcion2);


            Picasso.with(context).load(item.getRutaImagenProducto()).placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_menu_close_clear_cancel)
                   // .memoryPolicy(MemoryPolicy.NO_CACHE )
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .resize(200, 200).into(holder.imvLogo);

            holder.llyInformacionPromocion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Click en posicion: " + position);
                }
            });


            return convertView;
        }


        public Filter getFilter() {
            return mFilter;
        }

        class ViewHolder {
            //  TextView id;
            TextView txtNombreProducto;
            TextView txtDescripcion1;
            TextView txtDescripcion2;
            ImageView imvLogo;
            LinearLayout llyInformacionPromocion;
        }

        class ItemFilter extends Filter {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                String filterString = constraint.toString().toLowerCase();
                FilterResults results = new FilterResults();
                final ArrayList<ItemPromociones> listaOriginal = originalData;
                int count = listaOriginal.size();
                final ArrayList<ItemPromociones> listaFiltrada = new ArrayList<ItemPromociones>(count);

                String criterioBusqueda1;
                String criterioBusqueda2;
                String criterioBusqueda3;


                for (int i = 0; i < count; i++) {
                    criterioBusqueda1 = listaOriginal.get(i).getNombrePromocion();
                    criterioBusqueda2 = listaOriginal.get(i).getDescripcion1();
                    criterioBusqueda3 = listaOriginal.get(i).getDescripcion2();


                    if (criterioBusqueda1.toLowerCase().contains(filterString)
                            || criterioBusqueda2.toLowerCase().contains(
                            filterString)
                            || criterioBusqueda3.toLowerCase().contains(
                            filterString)) {
                        listaFiltrada.add(listaOriginal.get(i));
                    }
                }

                results.values = listaFiltrada;
                results.count = listaFiltrada.size();

                return results;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                try{
                    filteredData = (ArrayList<ItemPromociones>) results.values;
                    notifyDataSetChanged();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }

        }

    }
}
