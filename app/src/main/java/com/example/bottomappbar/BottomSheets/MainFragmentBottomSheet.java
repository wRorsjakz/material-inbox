package com.example.bottomappbar.BottomSheets;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bottomappbar.Helper.BottomNavColor;
import com.example.bottomappbar.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class MainFragmentBottomSheet extends BottomSheetDialogFragment {

    private NavigationView navigationView;

    private static final String TAG = "MainFragmentBottomSheet";

    ////////////////////////////////////////////////////////////////////////////////////////////////

    /*
       Interface callback for navigation drawer / bottom sheet item clicked
     */

    private NavDrawerItemClickedListener mListener;

    public interface NavDrawerItemClickedListener {
        void navDrawerItemClicked(MenuItem menuItem);
    }

    public void setNavDrawerItemClickedListener(NavDrawerItemClickedListener mListener) {
        this.mListener = mListener;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_bottomsheet, container, false);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // Set Bottom Nav Color to white instead of faded out
        Activity activity = getActivity();
        if (activity != null) {
            BottomNavColor.setWhiteNavigationBar(dialog, getActivity());
        }

        // Sets the sliding animation as defined in the styles.xml
        Objects.requireNonNull(dialog.getWindow()).getAttributes().windowAnimations = R.style.DialogAnimation;

        return dialog;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navigationView = view.findViewById(R.id.bottomsheet_navigation_view);

        // Set default checked navigation drawer item
        if (savedInstanceState == null) {
            navigationView.getMenu().getItem(2).setChecked(true);
        }
        /*
           - Handle NavigationView/BottomSheet menu item click
         */
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (mListener != null) {
                    mListener.navDrawerItemClicked(item);
                }

                return true;
            }
        });
    }


}
