package freifunk.bremen.de.mobilemeshviewer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.inject.Inject;

import java.util.ArrayList;

import freifunk.bremen.de.mobilemeshviewer.model.simple.Node;
import freifunk.bremen.de.mobilemeshviewer.model.simple.NodeList;
import roboguice.fragment.provided.RoboFragment;


public class SimpleNodeListFragment extends RoboFragment {

    @Inject
    private NodeController nodeController;
    private ArrayAdapter<Node> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        adapter = new ArrayAdapter<>(rootView.getContext(), android.R.layout.simple_list_item_1, new ArrayList<Node>());
        final ListView lv = (ListView) rootView.findViewById(R.id.list_view);
        lv.setAdapter(adapter);

        obtainNodeList();

        return rootView;
    }

    private void obtainNodeList() {
        new AsyncTask<Void, Void, NodeList>() {
            @Override
            protected NodeList doInBackground(Void... voids) {
                final NodeList simpleNodeList = nodeController.getSimpleNodeList();
                return simpleNodeList;
            }

            @Override
            protected void onPostExecute(NodeList nodeList) {
                super.onPostExecute(nodeList);
                adapter.addAll(nodeList.nodes);
            }
        }.execute((Void) null);
    }
}
