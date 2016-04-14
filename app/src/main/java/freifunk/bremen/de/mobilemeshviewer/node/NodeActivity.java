package freifunk.bremen.de.mobilemeshviewer.node;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.inject.Inject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import freifunk.bremen.de.mobilemeshviewer.PreferenceController;
import freifunk.bremen.de.mobilemeshviewer.R;
import freifunk.bremen.de.mobilemeshviewer.event.NodeDetailFoundEvent;
import freifunk.bremen.de.mobilemeshviewer.node.model.simple.Node;
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
    @Inject
    private PreferenceController preferenceController;
    @Inject
    private NodeDetailLoader nodeDetailLoader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nodeName.setText(node.getName());
        nodeId.setText(node.getId());

        nodeDetailLoader.execute("30b5c26e99d5");

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preferenceController.addNodeToObservedNodeList(node);
                Snackbar.make(view, "Added Node to observed Nodes", Snackbar.LENGTH_LONG)
                        .show();
            }
        });
    }

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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    TaskStackBuilder.create(this)
                            .addNextIntentWithParentStack(upIntent)
                            .startActivities();
                } else {
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onNodeDetailFound(NodeDetailFoundEvent event){
        Snackbar.make(nodeName, event.getNode().getNodeinfo().getNodeId(),Snackbar.LENGTH_SHORT).show();
    }
}