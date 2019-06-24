package com.example.bottomappbar.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.bottomappbar.BottomSheets.MainFragmentBottomSheet;
import com.example.bottomappbar.Entity.Email;
import com.example.bottomappbar.Helper.ProfilePicHelper;
import com.example.bottomappbar.ItemDecoration.MyRVItemDecoration;
import com.example.bottomappbar.R;
import com.example.bottomappbar.RecyclerViewAdapter.MainFragmentRVAdapter;
import com.example.bottomappbar.ViewModel.EmailViewModel;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

public class MainFragment extends Fragment implements MainFragmentRVAdapter.mainFragmentItemClickListener,
        SwipeRefreshLayout.OnRefreshListener, MainFragmentBottomSheet.NavDrawerItemClickedListener {

    // Fragment Views
    private CoordinatorLayout coordinatorLayout;
    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;

    // Activity Views
    private BottomAppBar bottomAppBar;
    private FloatingActionButton fab;

    // Variables
    private MainFragmentRVAdapter adapter;
    private EmailViewModel emailViewModel;
    private MainFragmentBottomSheet mainFragmentBottomSheet;
    private static final String TAG = "MainFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_fragment, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: Run");
        super.onViewCreated(view, savedInstanceState);

        initialiseViews(view);
        bottomAppBarItemClicked();
        setupRecyclerView();
        setupViewModel();
    }

    private void initialiseViews(View view) {
        // Fragment Views
        coordinatorLayout = view.findViewById(R.id.mainfragment_coordinatorlayout);
        appBarLayout = view.findViewById(R.id.main_appbarlayout);
        toolbar = view.findViewById(R.id.main_toolbar);
        swipeRefreshLayout = view.findViewById(R.id.main_swiperefreshlayout);
        recyclerView = view.findViewById(R.id.main_recyclerview);
        swipeRefreshLayout.setOnRefreshListener(this);
        // Activity Views
        bottomAppBar = Objects.requireNonNull(getActivity()).findViewById(R.id.main_bottomappbar);
        fab = Objects.requireNonNull(getActivity()).findViewById(R.id.main_fab);

        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.colorAccent),
                ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.colorPrimary));

    }

    /**
     * Handle bottomAppBar item click
     */
    private void bottomAppBarItemClicked() {
        mainFragmentBottomSheet = new MainFragmentBottomSheet();
        // BottomSheet/Navigation Drawer menu item click listener
        mainFragmentBottomSheet.setNavDrawerItemClickedListener(this);
        mainFragmentBottomSheet.setExitTransition(AnimationUtils.loadAnimation(getContext(),
                R.anim.bottom_dialog_slide_down));

        // Hamburger navigation icon pressed - it opens the bottom sheet which acts as the navigation drawer
        bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mainFragmentBottomSheet.isVisible()) {
                    mainFragmentBottomSheet.show(getChildFragmentManager(), "main_Fragment_bottom_sheet");
                }
            }
        });

        // Menu item clicked
        bottomAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.search_icon:
                        Toast.makeText(getContext(), "Search pressed", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.sort_by_icon:
                        Toast.makeText(getContext(), "Sort by pressed", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.view_by_icon:
                        Toast.makeText(getContext(), "View by pressed", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.archive_icon:
                        Toast.makeText(getContext(), "Archive pressed", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.settings_icon:
                        Toast.makeText(getContext(), "Settings pressed", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });

    }

    private void setupRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ////////////////////////////////////////////////////////////////////////////////////////////
        /*
            Set up recyclerview divider using custom "MyRVItemDecoration" which extends RecyclerView.ItemDecoration
         */
        MyRVItemDecoration dividerItemDecoration = new MyRVItemDecoration(
                ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.recyclerview_divider));
        recyclerView.addItemDecoration(dividerItemDecoration);
        ////////////////////////////////////////////////////////////////////////////////////////////
        adapter = new MainFragmentRVAdapter(getContext(), ProfilePicHelper.getProfilePics(), "Today");
        recyclerView.setAdapter(adapter);
        adapter.setmListener(this);

        recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                /*
                 * - Handle the elevation animation of the appbar layout
                 * - The animation is defined in "android:stateListAnimator="@animator/toolbar_elevation_animator" in the fragment's
                 *   appbar xml layout
                 */
                appBarLayout.setSelected(recyclerView.canScrollVertically(-1));
            }
        });
    }

    private void setupViewModel() {
        emailViewModel = ViewModelProviders.of(this).get(EmailViewModel.class);
        emailViewModel.getAllEmails().observe(this, new Observer<List<Email>>() {
            @Override
            public void onChanged(List<Email> emails) {
                adapter.setEmailList(emails);
            }
        });
    }

    /**
     * Callback for recyclerview item clicked
     *
     * @param email
     * @param position
     */
    @Override
    public void itemClick(Email email, int position) {

        HideBottomViewOnScrollBehavior<BottomAppBar> hideBottomViewOnScrollBehavior = new HideBottomViewOnScrollBehavior<>();
        hideBottomViewOnScrollBehavior.slideUp(bottomAppBar);

        bottomAppBar.getMenu().clear();
        fab.hide();
        bottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_END);
        fab.setImageDrawable(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.heart_white_24dp));
        fab.show();
        bottomAppBar.setNavigationIcon(null);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                bottomAppBar.replaceMenu(R.menu.detail_fragment_menu);
            }
        }, 250);

        FragmentTransaction fragmentTransaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fragment_slide_up, R.anim.fragment_slide_down,
                R.anim.bottom_dialog_slide_up, R.anim.bottom_dialog_slide_down);
        fragmentTransaction.add(R.id.main_fragment_container, new DetailFragment(email, position), "detail_fragment").addToBackStack(null)
                .commit();
    }

    /**
     * Handle pull-to-refresh functionality ( Stuck here! )
     */
    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Refresh data here
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 1000);
    }

    /**
     * Callback method defined in "MainFragmentBottomSheet.java"
     * Handles Navigation Drawer/Bottom Sheet item clicked
     *
     * @param menuItem
     */
    @Override
    public void navDrawerItemClicked(MenuItem menuItem) {
        Toast.makeText(getContext(), menuItem.getTitle() + " Clicked", Toast.LENGTH_SHORT).show();
        if (mainFragmentBottomSheet.isVisible()) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    mainFragmentBottomSheet.dismiss();

                }
            }, 300);

        }
    }

}
