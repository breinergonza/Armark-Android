package com.didelab.armk.armarkandroid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.didelab.armk.armarkandroid.util.Constantes;

public class ActivityAlmacenes extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    private Context context;

    public static int posicion = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_almacenes);
        Intent i = getIntent();
        posicion = i.getIntExtra("posicion",0);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_almacenes);
        setSupportActionBar(toolbar);

        context = this;

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout_almacenes);
        tabLayout.addTab(tabLayout.newTab().setText("Información"));
        tabLayout.addTab(tabLayout.newTab().setText("Promociones"));
        tabLayout.addTab(tabLayout.newTab().setText("Productos"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);



        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager_almacenes);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount(), Constantes.INT_ACTIVIDAD_INFO_ALMACEN);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setCurrentItem(0);
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
}
