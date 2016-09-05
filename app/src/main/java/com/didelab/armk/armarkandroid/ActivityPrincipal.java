package com.didelab.armk.armarkandroid;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.didelab.armk.armarkandroid.util.Constantes;
import com.facebook.AccessToken;
import com.facebook.FacebookRequestError;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;


    public class ActivityPrincipal extends AppCompatActivity {


        private final String TAG = getClass().getSimpleName();
        private Context context;



        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_principal);
        setSupportActionBar(toolbar);

            context = this;

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Almacenes"));
        tabLayout.addTab(tabLayout.newTab().setText("Promociones"));
        tabLayout.addTab(tabLayout.newTab().setText("Productos"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);



        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount(), Constantes.INT_ACTIVIDAD_PRINCIPAL);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setCurrentItem(1);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });




    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_principal, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case  R.id.action_settings:
                break;
            case R.id.action_cerrar:
                cerrarApp();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }


    private void cerrarApp() {
        LoginManager.getInstance().logOut();
        borrarCredenciales();
        GraphRequest delPermRequest = new GraphRequest(AccessToken.getCurrentAccessToken(), "/{user-id}/permissions/", null, HttpMethod.DELETE, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {
                if (graphResponse != null) {
                    FacebookRequestError error = graphResponse.getError();
                    if (error != null) {
                        Log.e(TAG, error.toString());
                    } else {
                        finish();
                    }
                }
            }
        });
        Log.d(TAG, "Executing revoke permissions with graph path" + delPermRequest.getGraphPath());
        delPermRequest.executeAsync();
        finish();
    }

        private void borrarCredenciales(){
            SharedPreferences prefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();

            String[] itemsUsuario = getResources().getStringArray(R.array.usuario_array);
            for (int i = 0; i < itemsUsuario.length; i++) {
                editor.putString(itemsUsuario[i], "");
            }
            editor.putString("user_id", "");
            editor.putString("auth_token", "");
            editor.commit();
        }
}
