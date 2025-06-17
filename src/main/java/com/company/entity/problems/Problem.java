package com.company.entity.problems;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="problems")
@EntityListeners(AuditingEntityListener.class)
@org.hibernate.annotations.DynamicUpdate
public class Problem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;
    private String language;
    private String difficulty;

    @ElementCollection
    @CollectionTable(
            name = "problem_topics",
            joinColumns = @JoinColumn(name = "problem_id")
    )
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    private List<String> topics;

    @ElementCollection
    @CollectionTable(
            name = "problem_links",
            joinColumns = @JoinColumn(name = "problem_id")
    )
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    private List<String> links;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
