package freifunk.bremen.de.mobilemeshviewer.node;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import freifunk.bremen.de.mobilemeshviewer.ListFragment;
import freifunk.bremen.de.mobilemeshviewer.R;
import freifunk.bremen.de.mobilemeshviewer.event.NodeStatusChangedEvent;
import freifunk.bremen.de.mobilemeshviewer.node.model.simple.Node;

public class NodeListFragment extends ListFragment<Node> implements SearchView.OnQueryTextListener, SearchView.OnCloseListener {

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
        getAdapter().getFilter().filter(currentFilter);
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNodeStatusChanged(NodeStatusChangedEvent event) {
        final Node node = event.getNode();
        Snackbar.make(getListView(), "Observed node " + node.getName() + " changed state to " + node.getStatus(), Snackbar.LENGTH_SHORT).show();
    }
}