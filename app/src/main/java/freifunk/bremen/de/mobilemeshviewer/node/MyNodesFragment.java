package freifunk.bremen.de.mobilemeshviewer.node;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import javax.inject.Inject;

import freifunk.bremen.de.mobilemeshviewer.PreferenceController;
import freifunk.bremen.de.mobilemeshviewer.R;
import freifunk.bremen.de.mobilemeshviewer.binding.MeshViewerApp;
import freifunk.bremen.de.mobilemeshviewer.node.model.simple.Node;

public class MyNodesFragment extends ListFragment {

    @Inject
    PreferenceController preferenceController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((MeshViewerApp) getActivity().getApplication()).getMeshViewerComponent().inject(this);
        return inflater.inflate(android.R.layout.list_content, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(false);
        setEmptyText(getActivity().getString(R.string.list_empty));
        final List<Node> myNodeList = preferenceController.getObservedNodeList();
        ArrayAdapter<Node> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, myNodeList);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Node node = (Node) l.getItemAtPosition(position);
        final Intent intent = new Intent(this.getActivity(), NodeActivity.class);
        intent.putExtra(NodeActivity.BUNDLE_NODE, node);
        startActivity(intent);
    }
}
