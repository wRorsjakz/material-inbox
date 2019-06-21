package com.example.bottomappbar.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.bottomappbar.Entity.Email;
import com.example.bottomappbar.Helper.ProfilePicHelper;
import com.example.bottomappbar.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailFragment extends Fragment implements PullDismissLayout.Listener {


    // MainActivity Views
    private BottomAppBar bottomAppBar;
    private FloatingActionButton fab;

    // Fragment Views
    private PullDismissLayout pullDismissLayout;
    private CoordinatorLayout coordinatorLayout;
    private NestedScrollView nestedScrollView;
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private Snackbar snackbar;

    private TextView sender, recipents, body;
    private CircleImageView profilePic;

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

        setupToolbar();
        setPullToDismissEnabled();
        bottomAppBarItemClicked();
        setupContent();
    }

    private void initialiseViews(View view) {
        // Activity Views
        bottomAppBar = Objects.requireNonNull(getActivity()).findViewById(R.id.main_bottomappbar);
        fab = Objects.requireNonNull(getActivity()).findViewById(R.id.main_fab);

        // Fragment Views

        pullDismissLayout = view.findViewById(R.id.detailfragment_pulldismisslayout);
        pullDismissLayout.setAnimateAlpha(true);
        pullDismissLayout.setListener(this);

        collapsingToolbarLayout = view.findViewById(R.id.detailfragment_collapsingtoolbar);
        nestedScrollView = view.findViewById(R.id.detailfragment_scrollview);
        coordinatorLayout = view.findViewById(R.id.detailfragment_coordinatelayout);
        appBarLayout = view.findViewById(R.id.detailfragment_appbarlayout);
        toolbar = view.findViewById(R.id.detailfragment_toolbar);

        sender = view.findViewById(R.id.detailfragment_sender);
        recipents = view.findViewById(R.id.detailfragment_recipents);
        profilePic = view.findViewById(R.id.detailfragment_profile);
    }

    private void setupToolbar(){
        // Set navigation icon for toolbar (back arrow) and handle onClickEvent
        toolbar.setNavigationIcon(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.ic_arrow_back_darkgray_24dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Objects.requireNonNull(getActivity()).onBackPressed();
            }
        });

        // Toolbar menu
        toolbar.inflateMenu(R.menu.detailfragment_toolbar_menu);
    }

    // Display content for display
    private void setupContent(){
        sender.setText(email.getSender());
        Glide.with(getContext()).load(ProfilePicHelper.getProfilePics()[email.getProfilePic()]).into(profilePic);
    }

    /**
     * This method monitors when the collapsing toolbar is fully expanded -- enable pull-to-dismiss
     * If the collapsing toolbar is not fully expanded, disable pull-to-dismiss functionality.
     * This is to allow the user to scroll the nestedScrollView downwards without also pulling the entire fragment down
     */
    private void setPullToDismissEnabled() {
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

               if(verticalOffset == 0){
                   // Collapsing toolbar is fully expanded
                    pullDismissLayout.setScrollEnabled(true);
               } else {
                   pullDismissLayout.setScrollEnabled(false);
               }

            }
        });
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

}
