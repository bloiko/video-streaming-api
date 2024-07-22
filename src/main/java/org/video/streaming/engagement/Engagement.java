package org.video.streaming.engagement;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.video.streaming.video.Video;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
class Engagement {

    @Id
    @Column(name = "video_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn(name = "video_id")
    private Video video;

    @Column(nullable = false)
    private long impressions;

    @Column(nullable = false)
    private long views;

    Engagement(Long videoId) {
        this.id = videoId;
    }
}
