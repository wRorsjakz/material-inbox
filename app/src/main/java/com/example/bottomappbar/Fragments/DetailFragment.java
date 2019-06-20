package com.example.bottomappbar.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.bottomappbar.Entity.Email;
import com.example.bottomappbar.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public class DetailFragment extends Fragment implements PullDismissLayout.Listener {


    // MainActivity Views
    private BottomAppBar bottomAppBar;
    private FloatingActionButton fab;

    // Fragment Views
    private PullDismissLayout pullDismissLayout;
    private CoordinatorLayout coordinatorLayout;
    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    private Snackbar snackbar;

    // Variables
    private Email email;
    private int position;
    private static final String TAG = "DetailFragmentTAG";

    public DetailFragment() {

    }

    public DetailFragment(@NonNull Email email, int position) {
        this.email = email;
        this.position = position;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.detail_fragment, container, false);

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialiseViews(view);
        bottomAppBarItemClicked();
    }

    private void initialiseViews(View view) {
        // Activity Views
        bottomAppBar = Objects.requireNonNull(getActivity()).findViewById(R.id.main_bottomappbar);
        fab = Objects.requireNonNull(getActivity()).findViewById(R.id.main_fab);

        // Fragment Views
        coordinatorLayout = view.findViewById(R.id.detailfragment_coordinatelayout);
        pullDismissLayout = view.findViewById(R.id.detailfragment_pulldismisslayout);
        pullDismissLayout.setAnimateAlpha(true);
        appBarLayout = view.findViewById(R.id.detailfragment_appbarlayout);
        toolbar = view.findViewById(R.id.detailfragment_toolbar);
        pullDismissLayout.setListener(this);
    }

    /**
     * Handle functionality when BottomAppBar menu item is clicked - currently just show a snackbar
     */
    private void bottomAppBarItemClicked() {
        bottomAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.detailfragment_email:
                        snackbar = Snackbar.make(fab, "Email Pressed", Snackbar.LENGTH_SHORT);
                        snackbar.setAnchorView(fab);
                        snackbar.show();
                        break;
                    case R.id.detailfragment_delete:
                        snackbar = Snackbar.make(fab, "Delete Pressed", Snackbar.LENGTH_SHORT);
                        snackbar.setAnchorView(fab);
                        snackbar.setActionTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                        snackbar.setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                snackbar.dismiss();
                            }
                        });
                        snackbar.show();
                        Objects.requireNonNull(getActivity()).onBackPressed();
                        break;
                    case R.id.detailfragment_archive:
                        Toast.makeText(getContext(), "Archive Pressed", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });

    }

    /**
     * Callback method from "PullDismissLayout" java class - called when this fragment has been swiped away fully
     * The implementation which triggers the bottomappbar's animation as it returns to the main fragment is triggered in the MainActivity's
     * onBackPressed() method
     */
    @Override
    public void onDismissed() {
        Objects.requireNonNull(getActivity()).onBackPressed();
    }

    @Override
    public boolean onShouldInterceptTouchEvent() {
        return false;
    }
}
