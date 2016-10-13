package com.didelab.armk.armarkandroid;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
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

import com.feedhenry.sdk.FH;
import com.feedhenry.sdk.FHActCallback;
import com.feedhenry.sdk.FHResponse;
import com.feedhenry.sdk.api.FHCloudRequest;

import org.json.fh.JSONArray;
import org.json.fh.JSONObject;

/**
 * Created by ElianaXimena on 11/08/2016.
 */
public class TabAlmacenes extends Fragment {

    private final String TAG = "TabAlmacenes";
    private SearchableAdapter mSearchableAdapter = null;
    private ArrayList<String> listaIdRtaOriginal = null;
    private Context context;
    private View viewFrg;
    private ListView lvAlmacenes;
    private ArrayList<ItemAlmacenes> datosGlobales = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //viewFrg = inflater.inflate(R.layout.tab_almacenes, container, false);

        viewFrg =  View.inflate(getActivity(), R.layout.tab_almacenes, null);

        try{
            Log.d(TAG, "init - fail");

            FH.init(getActivity(), new FHActCallback() {
                @Override
                public void success(FHResponse fhResponse) {
                    Log.d(TAG, "init - success");
                }

                @Override
                public void fail(FHResponse fhResponse) {
                    Log.d(TAG, "init - fail");
                    Log.e(TAG, fhResponse.getErrorMessage(), fhResponse.getError());
                }
            });
        }catch (Exception e){
            Log.d(TAG, "init - fail : " + e.getMessage());
        }


        context = getActivity();
        setHasOptionsMenu(true);

        lvAlmacenes =(ListView) viewFrg.findViewById(R.id.lv_almacenes);

        mSearchableAdapter = new SearchableAdapter(context);
        lvAlmacenes.setAdapter(mSearchableAdapter);
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
     * Obtiene la información
     * @return
     * @author Eliana Ximena Gonzalez Morales 11/04/2016 16:39:57
     */
    private ArrayList<ItemAlmacenes> getDatos() {

        ArrayList<ItemAlmacenes> datos =  new ArrayList<>();

        try {

            JSONObject params = new JSONObject("{fecha: '2015-09-11' }");

            FHCloudRequest request = FH.buildCloudRequest("almacenes", "POST", null, params);
            request.executeAsync(new FHActCallback() {
                @Override
                public void success(FHResponse fhResponse) {
                    Log.d(TAG, "cloudCall - success");
                    //responseTextView.setText(fhResponse.getJson().toString());

                    int Id;
                    String RazonSocial;
                    String Descripcion;
                    String Direccion;
                    String UrlImagen = "";
                    String resp = "";

                    JSONArray array = fhResponse.getArray();

                    datosGlobales.clear();

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject row = array.getJSONObject(i);
                        Id = row.getInt("Id");
                        RazonSocial = row.getString("RazonSocial");
                        Descripcion = row.getString("Descripcion");
                        Direccion = row.getString("Direccion");

                        resp += "Id : " + Id + ", Razon Social : " + RazonSocial + "\n";

                        datosGlobales.add(new ItemAlmacenes(0, RazonSocial, Descripcion,
                                Direccion,
                                "http://armark.cloudapp.net/Web/Storage/Almacenes/1.png",  false) );
                    }
                }

                @Override
                public void fail(FHResponse fhResponse) {
                    Log.d(TAG, "cloudCall - fail");
                    Log.e(TAG, fhResponse.getErrorMessage(), fhResponse.getError());

                }

            });

            Thread.sleep(1000);

        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e.getCause());
        }

        datos = datosGlobales;

/*        datos.add(new ItemAlmacenes(id, nombrePromocion, descripcion1, descripcion2,
                rutaImagenProducto,  promocion) );*/
        datos.add(new ItemAlmacenes(0, "FRAPPÉ REPUBLIC", "Frappé Mango biche 100% extracto natural.",
                "Nivel 2 local K-203b",
                "http://frapperepublic.com/wp-content/uploads/2015/11/cropped-favicon.png",  false) );
        datos.add(new ItemAlmacenes(1, "LYNX", "Tienda de accesorios.",
                "Nivel 2 K 202 A",
                "http://atlantisplaza.com/wp-content/uploads/2015/07/ATLANTIS-logo-20.jpg",  true) );
        datos.add(new ItemAlmacenes(2, "LA TOTUGA", "Distribución de contenidos personalizados para niños.",
                "Nivel 3 local K 306",
                "http://atlantisplaza.com/wp-content/uploads/2015/07/ATLANTIS-logos-62.png",  false) );
        datos.add(new ItemAlmacenes(3, "PAYLESS", "Zapatos y accesorios para hombres, mujeres y niños.",
                "Nivel 1 locales 112-113-114",
                "https://pbs.twimg.com/profile_images/698234972973432832/sO-0XQ1q.jpg",  true) );
        datos.add(new ItemAlmacenes(4, "SANDWICH QBANO", "Gran variedad de sándwiches a tu gusto.",
                "Nivel 4 local 405",
                "https://pbs.twimg.com/profile_images/606194576047509504/ljoHyJGe.png",  true) );

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

        private ArrayList<ItemAlmacenes> originalData = null;
        private ArrayList<ItemAlmacenes> filteredData = null;
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
                        R.layout.lista_almacenes_row, null);
                holder = new ViewHolder();
           /*     holder.id = (TextView) convertView
                        .findViewById(R.id.txt_lista_envios_id);*/
                holder.txtNombreAlmacen = (TextView) convertView
                        .findViewById(R.id.txt_lista_almacenes_nombre);
                holder.txtDescripcion1 = (TextView) convertView
                        .findViewById(R.id.txt_lista_almacenes_descripcion1);
                holder.txtDescripcion2 = (TextView) convertView
                        .findViewById(R.id.txt_lista_almacenes_descripcion2);
                holder.llyInformacionAlmacen = (LinearLayout) convertView
                        .findViewById(R.id.lly_lista_almacenes_informacion);
                holder.imvLogo = (ImageView) convertView
                        .findViewById(R.id.imv_lista_almacenes_logo);
                holder.imvPromocion = (ImageView) convertView
                        .findViewById(R.id.imv_lista_almacenes_promocion);
                convertView.setTag(holder);
            } else {

                holder = (ViewHolder) convertView.getTag();
            }

            final ItemAlmacenes item = filteredData.get(position);
            //holder.id.setText((position + 1) + ". ");
            holder.txtNombreAlmacen.setText(item.getNombreAlmacen());
            holder.txtDescripcion1.setText(item.descripcion1);
            holder.txtDescripcion2.setText(item.descripcion2);


            Picasso.with(context).load(item.getRutaLogo()).placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_menu_close_clear_cancel)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .resize(200, 200).into(holder.imvLogo);

            Picasso.with(context).load(android.R.drawable.btn_star_big_on)
                    .resize(150, 150).into(holder.imvPromocion);

            if(item.isPromocion()){
                holder.imvPromocion.setVisibility(View.VISIBLE);
            }else{
                holder.imvPromocion.setVisibility(View.INVISIBLE);
            }


            holder.llyInformacionAlmacen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Click en posicion: " + position);
                    Intent i = new Intent(context, ActivityAlmacenes.class);
                    i.putExtra("posicion", position);
                    startActivity(i);
                }
            });


            return convertView;
        }


        public Filter getFilter() {
            return mFilter;
        }

        class ViewHolder {
          //  TextView id;
            TextView txtNombreAlmacen;
            TextView txtDescripcion1;
            TextView txtDescripcion2;
            ImageView imvLogo;
            ImageView imvPromocion;
            LinearLayout llyInformacionAlmacen;
        }

        class ItemFilter extends Filter {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                String filterString = constraint.toString().toLowerCase();
                FilterResults results = new FilterResults();
                final ArrayList<ItemAlmacenes> listaOriginal = originalData;
                int count = listaOriginal.size();
                final ArrayList<ItemAlmacenes> listaFiltrada = new ArrayList<ItemAlmacenes>(count);

                String criterioBusqueda1;
                String criterioBusqueda2;
                String criterioBusqueda3;


                for (int i = 0; i < count; i++) {
                    criterioBusqueda1 = listaOriginal.get(i).getNombreAlmacen();
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
                    filteredData = (ArrayList<ItemAlmacenes>) results.values;
                    notifyDataSetChanged();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }

        }

    }
}
