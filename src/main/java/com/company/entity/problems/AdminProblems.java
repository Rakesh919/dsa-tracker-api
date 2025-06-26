package com.company.entity.problems;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="admin_problems")
public class AdminProblems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull(message = "Name can't be null")
    private String name;

    @ElementCollection
    @CollectionTable(
            name = "admin_problem_links",
            joinColumns = @JoinColumn(name = "admin_problem_id")
    )
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    private List<String> links;

}
