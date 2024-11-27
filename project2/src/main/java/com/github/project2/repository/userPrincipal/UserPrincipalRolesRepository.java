package com.github.project2.repository.userPrincipal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPrincipalRolesRepository extends JpaRepository<UserPrincipalRoles, Long> {
}
