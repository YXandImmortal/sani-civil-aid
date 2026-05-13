package com.idtech.sanicivilaid.repository;

import com.idtech.sanicivilaid.entity.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SysUserRepository extends JpaRepository<SysUser, Long> {

    Optional<SysUser> findByUsername(String username);

    boolean existsByUsername(String username);

    Optional<SysUser> findByMobile(String mobile);

    boolean existsByMobile(String mobile);
}
