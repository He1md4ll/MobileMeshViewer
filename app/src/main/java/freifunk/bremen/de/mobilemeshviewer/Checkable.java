package freifunk.bremen.de.mobilemeshviewer;

import java.util.List;

public interface Checkable<T> {
    List<T> fetchList();

    void reloadList();
}