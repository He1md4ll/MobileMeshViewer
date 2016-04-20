package freifunk.bremen.de.mobilemeshviewer.node;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.common.base.Optional;
import com.google.inject.Inject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import freifunk.bremen.de.mobilemeshviewer.PreferenceController;
import freifunk.bremen.de.mobilemeshviewer.R;
import freifunk.bremen.de.mobilemeshviewer.converter.NodeDetailConverter;
import freifunk.bremen.de.mobilemeshviewer.event.NodeDetailFoundEvent;
import freifunk.bremen.de.mobilemeshviewer.node.model.detail.Autoupdater;
import freifunk.bremen.de.mobilemeshviewer.node.model.detail.NodeDetail;
import freifunk.bremen.de.mobilemeshviewer.node.model.detail.Traffic;
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
    @InjectView(R.id.node_hardware)
    private TextView nodeHardware;
    @InjectView(R.id.toolbar)
    private Toolbar toolbar;
    @InjectView(R.id.fab)
    private FloatingActionButton fab;
    @InjectView(R.id.node_mac)
    private TextView nodeMac;
    @InjectView(R.id.node_id)
    private TextView nodeId;
    @InjectView(R.id.node_firmware)
    private TextView nodeFirmware;
    @InjectView(R.id.node_uptime)
    private TextView nodeUptime;
    @InjectView(R.id.node_addresses1)
    private TextView nodeAddresses1;
    @InjectView(R.id.node_addresses2)
    private TextView nodeAddresses2;
    @InjectView(R.id.node_addresses3)
    private TextView nodeAddresses3;
    @InjectView(R.id.node_autoupdate)
    private TextView nodeAutoupdate;
    @InjectView(R.id.node_clients)
    private TextView nodeClients;
    @InjectView(R.id.node_loadavg)
    private TextView nodeLoadavg;
    @InjectView(R.id.node_owner)
    private TextView nodeOwner;
    @InjectView(R.id.node_traffic)
    private  TextView nodeTraffic;
    @InjectView(R.id.node_install_date)
    private TextView nodeInstallDate;
    @Inject
    private PreferenceController preferenceController;
    @Inject
    private NodeDetailLoader nodeDetailLoader;
    @Inject
    private NodeDetailConverter nodeDetailConverter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(node.getName());
        if (node.getStatus().getOnline()) {
            toolbar.setTitleTextColor(Color.GREEN);
        } else {
            toolbar.setTitleTextColor(Color.RED);
        }

        nodeDetailLoader.execute(node.getId());

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
        final NodeDetail nodeDetail = event.getNode();
        if (Optional.fromNullable(nodeDetail.getNodeinfo()).isPresent()) {
            nodeHardware.setText(nodeDetail.getNodeinfo().getHardware().getModel());
            nodeMac.setText(nodeDetail.getNodeinfo().getNetwork().getMac());
            nodeId.setText(nodeDetail.getNodeinfo().getNodeId());
            nodeFirmware.setText(nodeDetail.getNodeinfo().getSoftware().getFirmware().getRelease()
                    + " \\ " + nodeDetail.getNodeinfo().getSoftware().getFirmware().getBase());
            nodeAddresses1.setText(nodeDetail.getNodeinfo().getNetwork().getAddresses().get(0));
            nodeAddresses2.setText(nodeDetail.getNodeinfo().getNetwork().getAddresses().get(1));
            nodeAddresses3.setText(nodeDetail.getNodeinfo().getNetwork().getAddresses().get(2));
            nodeAutoupdate.setText(nodeDetailConverter.convertAutoUpdate(nodeDetail.getNodeinfo().getSoftware().getAutoupdater()));
            nodeInstallDate.setText(nodeDetailConverter.convertDate(nodeDetail.getFirstseen()));

            if (nodeDetail.getNodeinfo().getOwner() != null) {
                nodeOwner.setText(nodeDetail.getNodeinfo().getOwner().getContact());
            }

            if (nodeDetail.getFlagsNode().getOnline()) {
                nodeUptime.setText(nodeDetailConverter.convertUptime(nodeDetail.getStatistics().getUptime()));
                nodeLoadavg.setText(nodeDetail.getStatistics().getLoadavg().toString()
                        + " / " + Math.round(nodeDetail.getStatistics().getMemoryUsage() * 100) + "%");
                nodeClients.setText(nodeDetail.getStatistics().getClients() + "");
                nodeTraffic.setText(nodeDetailConverter.convertTraffic(nodeDetail.getStatistics().getTraffic()));
            }
        } else {
            Snackbar.make(fab, "Couldn't load details for " + node.getName(), Snackbar.LENGTH_LONG).show();
        }
    }

}