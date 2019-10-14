package sitetech.NFCcheckPoint.Adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import sitetech.NFCcheckPoint.ui.operador.CheckFragment;
import sitetech.NFCcheckPoint.ui.operador.HistoryFragment;
import sitetech.NFCcheckPoint.ui.operador.TurnoFragment;

public class TabsAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    CheckFragment checkF;
    TurnoFragment turnoF;
    HistoryFragment historyF;
    public TabsAdapter(FragmentManager fm, int NoofTabs){
        super(fm);
        this.mNumOfTabs = NoofTabs;

        checkF = new CheckFragment();
        turnoF = new TurnoFragment();
        historyF = new HistoryFragment();
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    @Override
    public Fragment getItem(int position){
        switch (position){
            case 0:
                return checkF;

            case 1:
                return historyF;

            case 2:
                return turnoF;

            default:
                return null;
        }
    }
}