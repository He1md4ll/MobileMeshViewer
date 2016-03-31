package freifunk.bremen.de.mobilemeshviewer;

import android.app.LoaderManager;
import android.content.Loader;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.SearchView;

import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.List;

import freifunk.bremen.de.mobilemeshviewer.model.simple.Node;
import roboguice.fragment.provided.RoboListFragment;

public class NodeListFragment extends RoboListFragment implements SearchView.OnQueryTextListener, SearchView.OnCloseListener,
        LoaderManager.LoaderCallbacks<List<Node>> {

    @Inject
    private NodeListLoader nodeListLoader;
    @Inject
    private InputMethodManager imm;

    private SearchView searchView;
    private ArrayAdapter<Node> adapter;
    private String currentFilter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(android.R.layout.list_content, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(true);
        setEmptyText(getActivity().getString(R.string.list_no_nodes));
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, new ArrayList<Node>());
        setListAdapter(adapter);
        setListShown(false);

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem item = menu.add("Search");
        item.setIcon(android.R.drawable.ic_menu_search);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM
                | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
        searchView = new SearchView(getActivity());
        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);
        searchView.setIconifiedByDefault(false);
        searchView.setBackgroundColor(Color.WHITE);
        item.setActionView(searchView);
        MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                item.getActionView().requestFocus();
                item.getActionView().requestFocusFromTouch();
                if (!imm.isAcceptingText()) {
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                if (imm.isAcceptingText()) {
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
                searchView.setQuery("", false);
                return true;
            }
        });
    }

    @Override
    public Loader<List<Node>> onCreateLoader(int id, Bundle args) {
        nodeListLoader.reset();
        return nodeListLoader;
    }

    @Override
    public void onLoadFinished(Loader<List<Node>> loader, List<Node> data) {
        adapter.clear();
        adapter.addAll(data);

        if (isResumed()) {
            setListShown(true);
        } else {
            setListShownNoAnimation(true);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Node>> loader) {
        adapter.clear();
    }

    @Override
    public boolean onClose() {
        if (!TextUtils.isEmpty(searchView.getQuery())) {
            searchView.setQuery(null, true);
        }
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        currentFilter = !TextUtils.isEmpty(newText) ? newText : null;
        adapter.getFilter().filter(currentFilter);
        return true;
    }
}