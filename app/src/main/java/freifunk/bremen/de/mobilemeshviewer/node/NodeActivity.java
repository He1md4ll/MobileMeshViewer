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
import org.w3c.dom.Text;

import freifunk.bremen.de.mobilemeshviewer.PreferenceController;
import freifunk.bremen.de.mobilemeshviewer.R;
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
    @InjectView(R.id.node_status)
    private TextView nodeStatus;
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
    @Inject
    private PreferenceController preferenceController;
    @Inject
    private NodeDetailLoader nodeDetailLoader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

        NodeDetail node = event.getNode();

        Snackbar.make(toolbar, node.getNodeinfo().getNodeId() + " loaded",Snackbar.LENGTH_SHORT).show();
        toolbar.setTitle(node.getNodeinfo().getHostname());
        nodeStatus.setText(node.getFlagsNode().getOnline() ? "Online" : "Offline");
        nodeHardware.setText(node.getNodeinfo().getHardware().getModel());
        nodeMac.setText(node.getNodeinfo().getNetwork().getMac());
        nodeId.setText(node.getNodeinfo().getNodeId());
        nodeFirmware.setText(node.getNodeinfo().getSoftware().getFirmware().getRelease()
                + " \\ " + node.getNodeinfo().getSoftware().getFirmware().getBase() );
        nodeUptime.setText(convertUptime(node.getStatistics().getUptime()));
        nodeAddresses1.setText(node.getNodeinfo().getNetwork().getAddresses().get(0));
        nodeAddresses2.setText(node.getNodeinfo().getNetwork().getAddresses().get(1));
        nodeAddresses3.setText(node.getNodeinfo().getNetwork().getAddresses().get(2));
        nodeAutoupdate.setText(convertAutoUpdate(node.getNodeinfo().getSoftware().getAutoupdater()));
        nodeClients.setText(node.getStatistics().getClients() + "");
        nodeLoadavg.setText(node.getStatistics().getLoadavg().toString());
        nodeTraffic.setText(convertTraffic(node.getStatistics().getTraffic()));

        if (!node.getNodeinfo().getOwner().equals(null)){
            nodeOwner.setText(node.getNodeinfo().getOwner().getContact());
        }
    }

    private String convertUptime(double uptime){
        String uptime_human;

        if (uptime < 60*60){ // unter 1h -> Minuten
            uptime_human = Math.round(uptime/60) + " Minuten";
        }else if (uptime < 60*60*24) { // unter 1 Tag -> Stunden
            uptime_human = Math.round(uptime/60/60) +  " Stunden";
        } else { // über 1 Tag
            uptime_human = Math.round(uptime/60/60/24) + " Tage";
        }

        return uptime_human;
    }

    private String convertAutoUpdate(Autoupdater auto){
        String autoupdate;

        if (auto.isEnabled()) {
            autoupdate = getString(R.string.autoupdate_enabled);
        } else {
            autoupdate = getString(R.string.autoupdate_disabled);
        }

        autoupdate = autoupdate + " (" + auto.getBranch() + ")";

        return autoupdate;
    }

    private String convertTraffic(Traffic traffic){
        String trafficString;

        trafficString = Math.round(traffic.getTx().getBytes()/1024/1024) + "GB / "
                      + Math.round(traffic.getRx().getBytes()/1024/1024) + "GB (in/out)";

        return trafficString;
    }

}