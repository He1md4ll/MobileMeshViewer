package freifunk.bremen.de.mobilemeshviewer.node;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.common.base.Optional;
import com.google.inject.Inject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import freifunk.bremen.de.mobilemeshviewer.R;
import freifunk.bremen.de.mobilemeshviewer.SwipeRefreshListRoboFragment;
import freifunk.bremen.de.mobilemeshviewer.alarm.AlarmController;
import freifunk.bremen.de.mobilemeshviewer.event.NodeListUpdatedEvent;
import freifunk.bremen.de.mobilemeshviewer.event.NodeStatusChangedEvent;
import freifunk.bremen.de.mobilemeshviewer.node.model.simple.Node;

public class NodeListFragment extends SwipeRefreshListRoboFragment implements SearchView.OnQueryTextListener, SearchView.OnCloseListener,
        LoaderManager.LoaderCallbacks<List<Node>> {

    @Inject
    private NodeListLoader nodeListLoader;
    @Inject
    private AlarmController alarmController;
    private ArrayAdapter<Node> adapter;
    private boolean visible;
    private Optional<Snackbar> snackbarOptional = Optional.absent();

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        nodeListLoader.onContentChanged();
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(true);
        setEmptyText(getActivity().getString(R.string.list_no_nodes));
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, new ArrayList<Node>());
        setListAdapter(adapter);
        setListShown(false);

        setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                alarmController.sendAlarmImmediately();
                if (snackbarOptional.isPresent()){
                    snackbarOptional.get().dismiss();
                }
            }
        });

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
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        visible = isVisibleToUser;
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
        final int index = getListView().getFirstVisiblePosition();
        final View v = getListView().getChildAt(0);
        final int top = (v == null) ? 0 : (v.getTop() - getListView().getPaddingTop());
        adapter.clear();
        adapter.addAll(data);
        getListView().setSelectionFromTop(index, top);
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
    public void onNodeListUpdatedMain(NodeListUpdatedEvent event) {
        // Hide refresh progress indicator from SwipeRefreshLayout
        if (isRefreshing()) {
            setRefreshing(false);
        }

        // Hide refresh progress indicator from ListView
        if (isResumed()) {
            setListShown(true);
        } else {
            setListShownNoAnimation(true);
        }

        // Inform user with SnackBar
        if (visible && (!snackbarOptional.isPresent() || !snackbarOptional.get().isShown())) {
            final Snackbar snackbar;
            if (event.isSuccess()) {
                snackbar = Snackbar.make(getListView(), R.string.snackbar_node_success, Snackbar.LENGTH_SHORT);
            } else {
                snackbar = Snackbar.make(getListView(), R.string.snackbar_node_fail, Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction(R.string.snackbar_button_OK, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                });
            }
            snackbar.show();
            snackbarOptional = Optional.of(snackbar);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNodeStatusChanged(NodeStatusChangedEvent event) {
        final Node node = event.getNode();
        Snackbar.make(getListView(), "Observed node " + node.getName() + " changed state to " + node.getStatus(), Snackbar.LENGTH_SHORT).show();
    }
}