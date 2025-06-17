package com.company.repository.problem;

import com.company.entity.problems.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Integer> {

    public List<Problem> findProblemsByDifficulty(String difficulty);
}
