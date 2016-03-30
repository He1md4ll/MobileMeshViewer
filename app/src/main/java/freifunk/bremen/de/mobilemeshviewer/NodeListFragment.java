package freifunk.bremen.de.mobilemeshviewer;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.List;

import freifunk.bremen.de.mobilemeshviewer.model.simple.Node;
import roboguice.fragment.provided.RoboFragment;
import roboguice.inject.InjectView;

public class NodeListFragment extends RoboFragment implements SearchView.OnQueryTextListener, SearchView.OnCloseListener,
        LoaderManager.LoaderCallbacks<List<Node>> {

    @Inject
    private NodeController nodeController;
    @Inject
    private NodeListLoader nodeListLoader;
    @Inject
    private InputMethodManager imm;
    @InjectView(R.id.list_view)
    private ListView listView;

    private SearchView searchView;
    private ArrayAdapter<Node> adapter;
    private String currentFilter;
    private Toolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_nodelist, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onActivityCreated(savedInstanceState);

        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, new ArrayList<Node>());
        listView.setAdapter(adapter);
        getLoaderManager().initLoader(0, null, this);

        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        MenuItem item = setupSearchMenuItem();
        setupSearchView();
        item.setActionView(searchView);
    }

    private void setupSearchView() {
        searchView = new SearchView(getActivity());
        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);
        searchView.setIconifiedByDefault(false);
        searchView.setFocusable(true);
    }

    @NonNull
    private MenuItem setupSearchMenuItem() {
        MenuItem item = toolbar.getMenu().add("Search");
        item.setIcon(android.R.drawable.ic_menu_search);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM
                | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                item.getActionView().requestFocus();
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                return true;
            }
        });
        return item;
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