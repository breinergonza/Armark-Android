package com.didelab.armk.armarkandroid;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.didelab.armk.armarkandroid.util.Constantes;

/**
 * Created by ElianaXimena on 11/08/2016.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    int requestCode = Constantes.INT_ACTIVIDAD_PRINCIPAL;

    public PagerAdapter(FragmentManager fm, int NumOfTabs,int requestCode) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.requestCode = requestCode;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                if(requestCode == Constantes.INT_ACTIVIDAD_INFO_ALMACEN){
                    return new TabInformacionAlmacen();
                }else {
                    return new TabAlmacenes();
                }
            case 1:
                return new TabPromociones();
            case 2:
                return new TabProductos();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
