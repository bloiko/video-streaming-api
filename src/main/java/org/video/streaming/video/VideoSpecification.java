package org.video.streaming.video;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

class VideoSpecification {

    static Specification<Video> getVideosByFilters(List<Filter> filters) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            for (Filter filter : filters) {
                switch (filter.getFilterName().toLowerCase()) {
                    case "title":
                        predicates.add(cb.equal(root.get("title"), filter.getValue()));
                        break;
                    case "director":
                        predicates.add(cb.equal(root.get("director").get("name"), filter.getValue()));
                        break;
                    case "genre":
                        predicates.add(cb.equal(root.get("genre").get("name"), filter.getValue()));
                        break;
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
