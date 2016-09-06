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

import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ElianaXimena on 11/08/2016.
 */
public class TabProductos extends Fragment {

    private final String TAG = "TabProductos";
    private SearchableAdapter mSearchableAdapter = null;
    private ArrayList<String> listaIdRtaOriginal = null;
    private Context context;
    private View viewFrg;
    private ListView lvProductos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewFrg = inflater.inflate(R.layout.tab_productos, container, false);
        context = getActivity();
        setHasOptionsMenu(true);

        lvProductos =(ListView) viewFrg.findViewById(R.id.lv_productos);

        mSearchableAdapter = new SearchableAdapter(context);
        lvProductos.setAdapter(mSearchableAdapter);
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
    private ArrayList<ItemProductos> getDatos() {
        ArrayList<ItemProductos> datos = new ArrayList<>();

/*        datos.add(new ItemAlmacenes(id, nombrePromocion, descripcion1, descripcion2,
                rutaImagenProducto,  promocion) );*/
        datos.add(new ItemProductos(2, "ANILLOS",
                "http://static.imujer.com/sites/default/files/imujer/anillos-de-casamiento-grabados.jpg") );
        datos.add(new ItemProductos(0, "BOLSOS",
                "http://novedadesenlaweb.com/wp-content/uploads/2016/07/cuidar-los-bolsos.jpg") );
        datos.add(new ItemProductos(0, "COLLARES",
                "http://www.modaguia.com/wp-content/uploads/2012/05/collar-cuentas-color-turquesa-hm.jpg") );

        datos.add(new ItemProductos(0, "JUGUETES",
                "http://dirz8dubrwck5.cloudfront.net/wp-content/uploads/sites/3/2008/04/29210751/shutterstock_133636886-1024x992.jpg") );
       /* datos.add(new ItemProductos(3, "SANDWICHES",
                "http://middletownpizzapalace.com/theme/images/header/sandwich.png") );*/

//        datos.add(new ItemProductos(4, "ZAPATOS CABALLERO",
//                "http://zapatosdamaycaballero.com/wp-content/uploads/2015/11/Zapatos-de-cuero-para-caballero-3.gif") );
        datos.add(new ItemProductos(4, "ZAPATOS DAMA",
                "http://modadezapatos.com/wp-content/uploads/2012/07/Zapatos-formales-de-dama-4-.jpg") );

        datos.add(new ItemProductos(4, "ZAPATOS NIÑOS",
                "http://media.parabebes.com/productos/4/8/9/h.zapatillas-ninos-tipo-victoria-cordones-cuquito_1416833984.jpg") );
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

        private ArrayList<ItemProductos> originalData = null;
        private ArrayList<ItemProductos> filteredData = null;
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
                        R.layout.lista_productos_row, null);
                holder = new ViewHolder();
           /*     holder.id = (TextView) convertView
                        .findViewById(R.id.txt_lista_envios_id);*/
                holder.txtNombreProducto = (TextView) convertView
                        .findViewById(R.id.txt_lista_productos_nombre_producto);
                holder.llyInformacionPromocion = (LinearLayout) convertView
                        .findViewById(R.id.lly_lista_productos_informacion);
                holder.imvLogo = (ImageView) convertView
                        .findViewById(R.id.imv_lista_productos_producto);
                convertView.setTag(holder);
            } else {

                holder = (ViewHolder) convertView.getTag();
            }

            final ItemProductos item = filteredData.get(position);
            //holder.id.setText((position + 1) + ". ");
            holder.txtNombreProducto.setText(item.getNombrePromocion());

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
            ImageView imvLogo;
            LinearLayout llyInformacionPromocion;
        }

        class ItemFilter extends Filter {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                String filterString = constraint.toString().toLowerCase();
                FilterResults results = new FilterResults();
                final ArrayList<ItemProductos> listaOriginal = originalData;
                int count = listaOriginal.size();
                final ArrayList<ItemProductos> listaFiltrada = new ArrayList<ItemProductos>(count);

                String criterioBusqueda1;

                for (int i = 0; i < count; i++) {
                    criterioBusqueda1 = listaOriginal.get(i).getNombrePromocion();


                    if (criterioBusqueda1.toLowerCase().contains(filterString)) {
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
                    filteredData = (ArrayList<ItemProductos>) results.values;
                    notifyDataSetChanged();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }

        }

    }
}
