package com.example.bottomappbar;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bottomappbar.Fragments.MainFragment;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    // Views
    private RelativeLayout fragmentContainer;
    private BottomAppBar bottomAppBar;
    private FloatingActionButton fab;

    private static final String TAG = "MainActivityTAG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialiseViews();

        if (savedInstanceState == null) {
            initialiseMainFragment();
        }
    }

    private void initialiseViews() {
        fragmentContainer = findViewById(R.id.main_fragment_container);
        bottomAppBar = findViewById(R.id.main_bottomappbar);
        fab = findViewById(R.id.main_fab);
        // Set FAB animation for the BottomAppBar
        bottomAppBar.setFabAnimationMode(BottomAppBar.FAB_ANIMATION_MODE_SLIDE);
    }

    private void initialiseMainFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, new MainFragment(), "main_fragment").commit();
        // Set up BottomAppBar items for MainFragment
        bottomAppBar.inflateMenu(R.menu.main_fragment_menu);
        bottomAppBar.setNavigationIcon(R.drawable.ic_hamburger_white_24dp);
        fab.setImageResource(R.drawable.ic_add_white_24dp);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        // Determines if after back button pressed, the fragment being shown is the mainFragment.
        // If yes, then carry out the appropriate bottomappbar animations
        MainFragment mainFragment = (MainFragment)getSupportFragmentManager().findFragmentByTag("main_fragment");
        if(mainFragment != null && mainFragment.isVisible()){
            bottomAppBar.getMenu().clear();
            fab.hide();
            bottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_CENTER);
            fab.show();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottomAppBar.replaceMenu(R.menu.main_fragment_menu);
                    bottomAppBar.setNavigationIcon(R.drawable.ic_hamburger_white_24dp);
                }
            }, 250);
        }
    }

}
