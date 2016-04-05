package freifunk.bremen.de.mobilemeshviewer;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import freifunk.bremen.de.mobilemeshviewer.model.simple.Node;
import roboguice.activity.RoboAppCompatActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_node)
public class NodeActivity extends RoboAppCompatActivity {

    public static final String BUNDLE_NODE = "node";

    @InjectExtra(value = BUNDLE_NODE)
    private Node node;

    @InjectView(R.id.node_name)
    private TextView nodeName;

    @InjectView(R.id.node_id)
    private TextView nodeId;

    @InjectView(R.id.toolbar)
    private Toolbar toolbar;

    @InjectView(R.id.fab)
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nodeName.setText(node.getName());
        nodeId.setText(node.getId());

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Not implemented yet", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}