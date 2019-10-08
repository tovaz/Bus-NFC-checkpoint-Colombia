package sitetech.NFCcheckPoint;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.graphics.PorterDuff;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import sitetech.NFCcheckPoint.Adapters.TabsAdapter;
import sitetech.NFCcheckPoint.Helpers.ToastHelper;
import sitetech.routecheckapp.R;

public class OperadorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operador);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); //ACTIVAR ACTION BAR

        cargarTabs();
    }

    private  void cargarTabs(){
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        //tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewPager viewPager =(ViewPager)findViewById(R.id.view_pager);
        TabsAdapter tabsAdapter = new TabsAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(tabsAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                int color = ContextCompat.getColor(getBaseContext(), R.color.colorAccent);
                tab.getIcon().setColorFilter(color, PorterDuff.Mode.SRC_IN);
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int color = ContextCompat.getColor(getBaseContext(), R.color.colorAccentDark);
                tab.getIcon().setColorFilter(color, PorterDuff.Mode.SRC_IN);
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        tabLayout.getTabAt(0).select();
    }
}
