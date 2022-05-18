package com.bluesprint.orareacs.repository;

import com.bluesprint.orareacs.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findUserByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Long deleteByUsername(String username);

    List<User> getAllByGroup(String group);
}
