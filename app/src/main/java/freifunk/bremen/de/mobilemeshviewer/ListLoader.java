package freifunk.bremen.de.mobilemeshviewer;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.google.inject.Inject;

import java.util.List;

public class ListLoader<T> extends AsyncTaskLoader<List<T>> {

    @Inject
    private Checkable<T> checker;
    private List<T> list;

    @Inject
    public ListLoader(Context context) {
        super(context);
    }

    @Override
    public List<T> loadInBackground() {
        return checker.fetchList();
    }

    @Override
    public void deliverResult(List<T> list) {
        this.list = list;

        if (isStarted()) {
            super.deliverResult(list);
        }
    }

    @Override
    protected void onStartLoading() {
        if (this.list != null) {
            deliverResult(this.list);
        }

        if (takeContentChanged() || this.list == null) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    public void onCanceled(List<T> nodeList) {
        super.onCanceled(list);
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();

        if (this.list != null) {
            this.list = null;
        }
    }
}