package freifunk.bremen.de.mobilemeshviewer.gateway;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.google.common.base.Optional;
import com.google.inject.Inject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.w3c.dom.Text;

import freifunk.bremen.de.mobilemeshviewer.R;
import freifunk.bremen.de.mobilemeshviewer.converter.NodeDetailConverter;
import freifunk.bremen.de.mobilemeshviewer.event.NodeDetailFoundEvent;
import freifunk.bremen.de.mobilemeshviewer.gateway.model.Gateway;
import freifunk.bremen.de.mobilemeshviewer.gateway.model.IpStatus;
import freifunk.bremen.de.mobilemeshviewer.node.NodeChecker;
import freifunk.bremen.de.mobilemeshviewer.node.NodeDetailLoader;
import freifunk.bremen.de.mobilemeshviewer.node.model.detail.NodeDetail;
import freifunk.bremen.de.mobilemeshviewer.node.model.simple.Node;
import roboguice.activity.RoboAppCompatActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_gateway)
public class GatewayActivity extends RoboAppCompatActivity {

    public static final String BUNDLE_GATEWAY = "gateway";

    @InjectExtra(value = BUNDLE_GATEWAY)
    private Gateway gateway;
    @InjectView(R.id.toolbar)
    private Toolbar toolbar;
    @InjectView(R.id.gateway_uplink_ipv4)
    private TextView gatewayUplink4;
    @InjectView(R.id.gateway_uplink_ipv6)
    private TextView gatewayUplink6;
    @InjectView(R.id.gateway_address_ipv4)
    private TextView gatewayAddress4;
    @InjectView(R.id.gateway_address_ipv6)
    private TextView gatewayAddress6;
    @InjectView(R.id.gateway_dns_ipv4)
    private TextView gatewayDns4;
    @InjectView(R.id.gateway_dns_ipv6)
    private TextView gatewayDns6;
    @InjectView(R.id.gateway_ntp_ipv4)
    private TextView gatewayNtp4;
    @InjectView(R.id.gateway_ntp_ipv6)
    private TextView gatewayNtp6;
    @InjectView(R.id.gateway_traffic)
    private TextView gatewayTraffic;
    @InjectView(R.id.gateway_firmware)
    private TextView gatewayFirmware;
    @InjectView(R.id.gateway_uptime)
    private TextView gatewayUptime;
    @InjectView(R.id.gateway_loadavg)
    private TextView gatewayLoadavg;
    @InjectView(R.id.gateway_install_date)
    private TextView gatewayInstallDate;
    @Inject
    private NodeDetailLoader nodeDetailLoader;
    @Inject
    private NodeChecker nodeChecker;
    @Inject
    private NodeDetailConverter nodeDetailConverter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(gateway.getName());

        showStatus(gateway.getUplink(), gatewayUplink4, gatewayUplink6);
        showStatus(gateway.getAddresses(), gatewayAddress4, gatewayAddress6);
        showStatus(gateway.getDns(), gatewayDns4, gatewayDns6);
        showStatus(gateway.getNtp(), gatewayNtp4, gatewayNtp6);

        Node gatewayNode = nodeChecker.getGatewayByName(gateway.getName());
        if (gatewayNode != null) {
            nodeDetailLoader.execute(gatewayNode.getId());
        }
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

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onNodeDetailFound(NodeDetailFoundEvent event) {
        final NodeDetail gatewayDetail = event.getNode();
        if (Optional.fromNullable(gatewayDetail.getNodeinfo()).isPresent()) {
            gatewayFirmware.setText(gatewayDetail.getNodeinfo().getSoftware().getFirmware().getRelease()
                    + " \\ " + gatewayDetail.getNodeinfo().getSoftware().getFirmware().getBase());
            gatewayInstallDate.setText(nodeDetailConverter.convertDate(gatewayDetail.getFirstseen()));
            if (gatewayDetail.getFlagsNode().getOnline()){
                gatewayTraffic.setText(nodeDetailConverter.convertTraffic(gatewayDetail.getStatistics().getTraffic()));
                gatewayUptime.setText(nodeDetailConverter.convertUptime(gatewayDetail.getStatistics().getUptime()));
                gatewayLoadavg.setText(gatewayDetail.getStatistics().getLoadavg().toString()
                        + " / " + Math.round(gatewayDetail.getStatistics().getMemoryUsage() * 100) + "%");
            }

        } else {
            Snackbar.make(toolbar, "Couldn't load details for " + gateway.getName(), Snackbar.LENGTH_LONG).show();
        }
    }

    private void showStatus(IpStatus status, TextView textViewv4, TextView textViewv6){
        String ok = "OK";
        String nok = "NOK";

        switch (status) {
            case BOTH:
                textViewv4.setText(ok);
                textViewv4.setTextColor(Color.GREEN);
                textViewv6.setText(ok);
                textViewv6.setTextColor(Color.GREEN);
                break;
            case IPv4:
                textViewv4.setText(ok);
                textViewv4.setTextColor(Color.GREEN);
                textViewv6.setText(nok);
                textViewv6.setTextColor(Color.RED);
                break;
            case IPv6:
                textViewv4.setText(nok);
                textViewv4.setTextColor(Color.RED);
                textViewv6.setText(ok);
                textViewv6.setTextColor(Color.GREEN);
                break;
            case NONE:
                textViewv4.setText(nok);
                textViewv4.setTextColor(Color.RED);
                textViewv6.setText(nok);
                textViewv6.setTextColor(Color.RED);
                break;
        }
    }
}