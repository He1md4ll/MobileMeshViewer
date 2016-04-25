package freifunk.bremen.de.mobilemeshviewer;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.SectionIndexer;

import com.google.common.collect.HashBiMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import com.google.common.collect.TreeRangeMap;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CustomArrayAdapter<T> extends ArrayAdapter<T> implements SectionIndexer {

    private List<String> sections = Lists.newArrayList();
    private Map<String, Range<Integer>> alphabetIndexer = HashBiMap.create();
    private RangeMap<Integer, String> rangeMap = TreeRangeMap.create();

    public CustomArrayAdapter(Context context, int resource, List<T> data) {
        super(context, resource, data);
        updateSections(data);
    }

    @Override
    public void clear() {
        super.clear();
        alphabetIndexer.clear();
        sections.clear();
    }

    @Override
    public void addAll(Collection<? extends T> data) {
        super.addAll(data);
        updateSections(data);
    }

    @Override
    public Object[] getSections() {
        return sections.toArray();
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        return alphabetIndexer.get(sections.get(sectionIndex)).lowerEndpoint();
    }

    @Override
    public int getSectionForPosition(final int position) {
        return sections.indexOf(rangeMap.get(position));
    }

    private void updateSections(Collection<? extends T> data) {
        if (!Iterables.isEmpty(data)) {
            final int size = data.size();
            int counter = 0;

            for (T item : data) {
                final String s = item.toString().substring(0, 1).toUpperCase();
                if (!sections.contains(s)) {
                    sections.add(s);
                    rangeMap.put(Range.closed(counter, size), s);
                }
                counter++;
            }
            alphabetIndexer = HashBiMap.create(rangeMap.asMapOfRanges()).inverse();
        }
    }
}