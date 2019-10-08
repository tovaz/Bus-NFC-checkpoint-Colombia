package sitetech.NFCcheckPoint.Adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import sitetech.NFCcheckPoint.ui.operador.CheckFragment;
import sitetech.NFCcheckPoint.ui.operador.HistoryFragment;

public class TabsAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    public TabsAdapter(FragmentManager fm, int NoofTabs){
        super(fm);
        this.mNumOfTabs = NoofTabs;
    }
    @Override
    public int getCount() {
        return mNumOfTabs;
    }
    @Override
    public Fragment getItem(int position){
        switch (position){
            case 0:
                CheckFragment check = new CheckFragment();
                return check;
            case 1:
                HistoryFragment history = new HistoryFragment();
                return history;
            default:
                return null;
        }
    }
}