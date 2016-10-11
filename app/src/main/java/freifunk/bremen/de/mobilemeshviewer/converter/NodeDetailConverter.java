package freifunk.bremen.de.mobilemeshviewer.converter;


import android.content.Context;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import freifunk.bremen.de.mobilemeshviewer.R;
import freifunk.bremen.de.mobilemeshviewer.node.model.detail.Autoupdater;
import freifunk.bremen.de.mobilemeshviewer.node.model.detail.Traffic;


public class NodeDetailConverter {

    private Context context;

    @Inject
    public NodeDetailConverter(Context context) {
        this.context = context;
    }

    public String convertUptime(double uptime){
        return secondsToString(uptime, TimeUnit.SECONDS);
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
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss", Locale.GERMAN);
        try {
            installed = df.parse(dateString);
            Log.i(this.getClass().getSimpleName(), "Converted date successful");
        } catch (ParseException e){
            Log.e(this.getClass().getSimpleName(),"could not convert date of node detail");
        }

        return secondsToString(now.getTime() - installed.getTime(), TimeUnit.MILLISECONDS);
    }

    private String secondsToString(double uptime, final TimeUnit timeUnit) {
        String secondsHuman;
        double uptimeConverted = uptime;

        if (timeUnit.equals(TimeUnit.MILLISECONDS)) {
            uptimeConverted = uptime / 1000;
        }

        if (uptimeConverted < 60 * 60) { // unter 1h -> Minuten
            if (uptimeConverted >= 60 && uptimeConverted < 60 * 2) {
                secondsHuman = (int) Math.floor(uptimeConverted / 60) + " " + context.getString(R.string.minute); //Singular
            }else {
                secondsHuman = (int) Math.floor(uptimeConverted / 60) + " " + context.getString(R.string.minutes); //Plural
            }
        } else if (uptimeConverted < 60 * 60 * 24) { // unter 1 Tag -> Stunden
            if (uptimeConverted >= 60 * 60 && uptimeConverted < 60 * 60 * 2) {
                secondsHuman = (int) Math.floor(uptimeConverted / 60 / 60) + " " + context.getString(R.string.hour); //Singular
            } else {
                secondsHuman = (int) Math.floor(uptimeConverted / 60 / 60) + " " + context.getString(R.string.hours); //Plural
            }
        } else { // über 1 Tag
            if (uptimeConverted < 60 * 60 * 24 * 2) {
                secondsHuman = (int) Math.floor(uptimeConverted / 60 / 60 / 24) + " " + context.getString(R.string.day); //Singular
            } else {
                secondsHuman = (int) Math.floor(uptimeConverted / 60 / 60 / 24) + " " + context.getString(R.string.days); //Plural
            }
        }

        return secondsHuman;
    }
}
