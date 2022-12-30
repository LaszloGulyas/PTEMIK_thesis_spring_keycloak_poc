package com.tm7xco.springkeycloakpoc.repository;

import com.tm7xco.springkeycloakpoc.domain.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {

    AppUser findByUsername(String username);

    Integer deleteAppUserByUsername(String username);

}
