package freifunk.bremen.de.mobilemeshviewer.node;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.inject.Inject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import freifunk.bremen.de.mobilemeshviewer.R;
import freifunk.bremen.de.mobilemeshviewer.event.NodeListUpdatedEvent;
import freifunk.bremen.de.mobilemeshviewer.event.NodeStatusChangedEvent;
import freifunk.bremen.de.mobilemeshviewer.node.model.simple.Node;
import roboguice.fragment.provided.RoboListFragment;

public class NodeListFragment extends RoboListFragment implements SearchView.OnQueryTextListener, SearchView.OnCloseListener,
        LoaderManager.LoaderCallbacks<List<Node>> {

    @Inject
    private NodeListLoader nodeListLoader;
    private ArrayAdapter<Node> adapter;

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
        MenuItem searchViewItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchViewItem.getActionView();
        searchView.setIconifiedByDefault(true);
        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Node node = (Node) l.getItemAtPosition(position);
        final Intent intent = new Intent(this.getActivity(), NodeActivity.class);
        intent.putExtra(NodeActivity.BUNDLE_NODE, node);
        startActivity(intent);
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
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String currentFilter = !TextUtils.isEmpty(newText) ? newText : null;
        adapter.getFilter().filter(currentFilter);
        return true;
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onNodeListUpdated(NodeListUpdatedEvent ignored) {
        nodeListLoader.onContentChanged();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNodeListUpdatedMain(NodeListUpdatedEvent ignored) {
        Snackbar.make(getListView(), "List reloaded", Snackbar.LENGTH_SHORT).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNodeStatusChanged(NodeStatusChangedEvent event) {
        final Node node = event.getNode();
        Snackbar.make(getListView(), "Observed node " + node.getName() + " changed state to " + node.getStatus(), Snackbar.LENGTH_SHORT).show();
    }
}