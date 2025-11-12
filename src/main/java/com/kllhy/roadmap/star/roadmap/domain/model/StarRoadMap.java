package com.kllhy.roadmap.star.roadmap.domain.model;

import com.kllhy.roadmap.common.exception.DomainException;
import com.kllhy.roadmap.common.model.AggregateRoot;
import com.kllhy.roadmap.star.roadmap.domain.exception.StarRoadMapErrorCode;
import com.kllhy.roadmap.star.roadmap.domain.model.command.CreateStarRoadMapCommand;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "star_road_map",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_star_user_roadmap",
                        columnNames = {"user_id", "road_map_id"})
})
public class StarRoadMap extends AggregateRoot {

    @Column(nullable = false)
    @ColumnDefault("0")
    @Getter
    private int value;

    @Column(nullable = false)
    @Getter
    private Long userId;

    @Column(nullable = false)
    @Getter
    private Long roadMapId;

    private StarRoadMap(int value, Long userId, Long roadMapId) {
        validateStarValue(value);
        this.value = value;
        this.userId = Objects.requireNonNull(userId, "userId must not be null");
        this.roadMapId = Objects.requireNonNull(roadMapId, "roadMapId must not be null");
    }

    public static StarRoadMap create(CreateStarRoadMapCommand command) {
        return new StarRoadMap(command.value(), command.userId(), command.roadmapId());
    }

    private void validateStarValue(int value) {
        if (value < 0 || value > 5) {
            throw new DomainException(StarRoadMapErrorCode.STAR_ROAD_MAP_INVALID_VALUE);
        }
    }
}
