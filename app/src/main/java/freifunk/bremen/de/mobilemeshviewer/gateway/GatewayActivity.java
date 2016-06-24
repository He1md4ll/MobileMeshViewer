package freifunk.bremen.de.mobilemeshviewer.gateway;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.inject.Inject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import freifunk.bremen.de.mobilemeshviewer.R;
import freifunk.bremen.de.mobilemeshviewer.converter.NodeDetailConverter;
import freifunk.bremen.de.mobilemeshviewer.event.NodeDetailFoundEvent;
import freifunk.bremen.de.mobilemeshviewer.event.NodeDetailNotFoundEvent;
import freifunk.bremen.de.mobilemeshviewer.gateway.model.Gateway;
import freifunk.bremen.de.mobilemeshviewer.gateway.model.IpStatus;
import freifunk.bremen.de.mobilemeshviewer.node.model.detail.NodeDetail;
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
    @InjectView(R.id.gateway_status_table)
    private TableLayout tableLayout;
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
    private GatewayNodeDetailLoader gatewayNodeDetailLoader;
    @Inject
    private NodeDetailConverter nodeDetailConverter;
    @Inject
    private LayoutInflater layoutInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(gateway.getName());

        createTable();
        gatewayNodeDetailLoader.execute(gateway.getName());
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

    private void createTable() {
        tableLayout.addView(createRow("UpLink", gateway.getUplink()));
        tableLayout.addView(createRow("Address", gateway.getAddresses()));
        tableLayout.addView(createRow("DNS", gateway.getDns()));
        tableLayout.addView(createRow("NTP", gateway.getNtp()));
    }

    private TableRow createRow(String label, IpStatus status) {
        final TableRow tableRow = (TableRow) layoutInflater.inflate(R.layout.template_gateway_table_row, null);
        final TextView tableRowLabel = (TextView) tableRow.findViewById(R.id.table_row_label);
        tableRowLabel.setText(label);

        final ImageView tableRowStatus1 = (ImageView) tableRow.findViewById(R.id.table_row_status_1);
        final ImageView tableRowStatus2 = (ImageView) tableRow.findViewById(R.id.table_row_status_2);
        setStatus(status, tableRowStatus1, tableRowStatus2);

        return tableRow;
    }

    private void setStatus(IpStatus status, ImageView imageIpv4, ImageView imageIpv6) {
        switch (status) {
            case BOTH:
                imageIpv4.setImageResource(R.drawable.ic_online);
                imageIpv6.setImageResource(R.drawable.ic_online);
                break;
            case IPv4:
                imageIpv4.setImageResource(R.drawable.ic_online);
                imageIpv6.setImageResource(R.drawable.ic_offline);
                break;
            case IPv6:
                imageIpv4.setImageResource(R.drawable.ic_offline);
                imageIpv6.setImageResource(R.drawable.ic_online);
                break;
            default:
                imageIpv4.setImageResource(R.drawable.ic_offline);
                imageIpv6.setImageResource(R.drawable.ic_offline);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onGatewayNodeDetailFound(NodeDetailFoundEvent event) {
        final NodeDetail gatewayDetail = event.getNode();
        gatewayFirmware.setText(gatewayDetail.getNodeinfo().getSoftware().getFirmware().getRelease()
                + " \\ " + gatewayDetail.getNodeinfo().getSoftware().getFirmware().getBase());
        gatewayInstallDate.setText(nodeDetailConverter.convertDate(gatewayDetail.getFirstseen()));
        if (gatewayDetail.getFlagsNode().getOnline()) {
            gatewayTraffic.setText(nodeDetailConverter.convertTraffic(gatewayDetail.getStatistics().getTraffic()));
            gatewayUptime.setText(nodeDetailConverter.convertUptime(gatewayDetail.getStatistics().getUptime()));
            gatewayLoadavg.setText(String.valueOf(gatewayDetail.getStatistics().getLoadavg().toString())
                    + " / " + Math.round(gatewayDetail.getStatistics().getMemoryUsage() * 100) + "%");
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onGatewayNodeDetailFound(NodeDetailNotFoundEvent ignored) {
        Snackbar.make(tableLayout, "Couldn't load details for " + gateway.getName(), Snackbar.LENGTH_LONG).show();
    }
}