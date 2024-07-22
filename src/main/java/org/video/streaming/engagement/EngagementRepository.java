package org.video.streaming.engagement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface EngagementRepository extends JpaRepository<Engagement, Long> {
}
