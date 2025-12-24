package com.workmgmt.workmgmt.repository;

import com.workmgmt.workmgmt.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

//import java.lang.ScopedValue;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    Optional findByEmail(String email);
}
