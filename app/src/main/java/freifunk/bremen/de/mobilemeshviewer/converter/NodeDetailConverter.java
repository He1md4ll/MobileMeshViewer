package freifunk.bremen.de.mobilemeshviewer.converter;


import android.content.Context;

import com.google.inject.Inject;

import freifunk.bremen.de.mobilemeshviewer.R;
import freifunk.bremen.de.mobilemeshviewer.node.model.detail.Autoupdater;
import freifunk.bremen.de.mobilemeshviewer.node.model.detail.Traffic;


public class NodeDetailConverter {

    @Inject
    Context context;

    public String convertUptime(double uptime){
        String uptime_human;

        if (uptime < 60*60){ // unter 1h -> Minuten
            if (uptime > 60 && uptime < 60*2){
                uptime_human = (int)Math.floor(uptime / 60) + " " + context.getString(R.string.minute); //Singular
            }else {
                uptime_human = (int)Math.floor(uptime / 60) + " " + context.getString(R.string.minutes); //Plural
            }
        }else if (uptime < 60*60*24) { // unter 1 Tag -> Stunden
            if (uptime > 60*60 && uptime < 60*60*2){
                uptime_human = (int)Math.floor(uptime / 60 / 60) + " " + context.getString(R.string.hour); //Singular
            } else {
                uptime_human = (int)Math.floor(uptime / 60 / 60) + " " + context.getString(R.string.hours); //Plural
            }
        } else { // über 1 Tag
            if (uptime < 60*60*24*2) {
                uptime_human = (int)Math.floor(uptime / 60 / 60 / 24) + " " + context.getString(R.string.day); //Singular
            } else {
                uptime_human = (int)Math.floor(uptime / 60 / 60 / 24) + " " + context.getString(R.string.days); //Plural
            }
        }

        return uptime_human;
    }

    public String convertAutoUpdate(Autoupdater auto){
        String autoupdate;

        if (auto.isEnabled()) {
            autoupdate = context.getString(R.string.autoupdate_enabled);
        } else {
            autoupdate = context.getString(R.string.autoupdate_disabled);
        }

        autoupdate = autoupdate + " (" + auto.getBranch() + ")";

        return autoupdate;
    }

    public String convertTraffic(Traffic traffic){
        String trafficString;

        trafficString = Math.round(traffic.getTx().getBytes()/1024/1024/1024) + "GB / "
                + Math.round(traffic.getRx().getBytes()/1024/1024/1024) + "GB (out/in)";

        return trafficString;
    }

}
