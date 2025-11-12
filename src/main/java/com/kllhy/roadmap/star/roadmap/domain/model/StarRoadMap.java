package com.kllhy.roadmap.star.roadmap.domain.model;

import com.kllhy.roadmap.common.model.IdAuditEntity;
import com.kllhy.roadmap.roadmap.domain.model.RoadMap;
import com.kllhy.roadmap.user.domain.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "star_road_map")
public class StarRoadMap extends IdAuditEntity {

    @Column(nullable = false)
    @ColumnDefault("0")
    @Getter private int value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "road_map_id", nullable = false)
    private RoadMap roadMap;

    public static StarRoadMap create() {
        return new StarRoadMap();
    }
}
