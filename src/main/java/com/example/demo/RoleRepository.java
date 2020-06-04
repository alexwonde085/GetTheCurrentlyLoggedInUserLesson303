package com.example.demo;

import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Set<Role> findAllByUsername(String username);
}
