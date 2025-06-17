package com.company.repository.user;

import com.company.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    public boolean existsByEmail(String email);
    public User findByEmail(String email);
}
