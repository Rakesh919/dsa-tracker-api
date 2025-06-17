package com.company.repository.problem;

import com.company.entity.problems.UserProblem;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserProblemRepository extends JpaRepository<UserProblem,Integer> {
 List<UserProblem> findProblemByUserId(int id);
 List<UserProblem> findByUserIdAndSolved(int id,boolean isSolved);

 @Modifying
 @Transactional
 @Query("DELETE FROM UserProblem u where u.problem.id =:id")
 void findByProblemIdAndDelete(int id);

 @Modifying
 @Transactional
 @Query("DELETE FROM UserProblem u where u.user.id =:id")
 void findByUserIdAndDelete(int id);
}
