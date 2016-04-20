package freifunk.bremen.de.mobilemeshviewer.converter;


import android.content.Context;
import android.util.Log;

import com.google.inject.Inject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

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

    public String convertDate(String dateString){
        Date installed = new Date();
        Date now = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-mm-dd'T'kk:mm:ss", Locale.GERMAN);
        try {
            installed = df.parse(dateString);
            Log.i(this.getClass().getSimpleName(), "Converted date successful");
        } catch (ParseException e){
            Log.e(this.getClass().getSimpleName(),"could not convert date of node detail");
        }

        long difference = TimeUnit.DAYS.convert(now.getTime() - installed.getTime(), TimeUnit.MILLISECONDS);

        if (difference < 2) {
            return difference + " " + context.getString(R.string.day);
        } else {
            return difference + " " + context.getString(R.string.days);
        }

    }

}
