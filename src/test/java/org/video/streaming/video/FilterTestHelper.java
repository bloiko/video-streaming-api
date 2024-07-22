package org.video.streaming.video;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FilterTestHelper {

    public static List<Filter> createTitleFilter(String title) {
        Filter filter = new Filter();
        filter.setFilterName("title");
        filter.setValue(title);
        return Collections.singletonList(filter);
    }

    public static List<Filter> createDirectorFilter(String director) {
        Filter filter = new Filter();
        filter.setFilterName("director");
        filter.setValue(director);
        return Collections.singletonList(filter);
    }

    public static List<Filter> createGenreFilter(String genre) {
        Filter filter = new Filter();
        filter.setFilterName("genre");
        filter.setValue(genre);
        return Collections.singletonList(filter);
    }

    public static List<Filter> createMultipleFilters(String title, String director) {
        Filter titleFilter = new Filter();
        titleFilter.setFilterName("title");
        titleFilter.setValue(title);

        Filter directorFilter = new Filter();
        directorFilter.setFilterName("director");
        directorFilter.setValue(director);

        return Arrays.asList(titleFilter, directorFilter);
    }
}
