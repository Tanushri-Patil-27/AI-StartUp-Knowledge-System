package com.example.OrganizationService.repository;


import com.example.OrganizationService.model.OrganizationMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrganizationMemberRepository
        extends JpaRepository<OrganizationMember, Long> {

    Optional<OrganizationMember> findByOrganizationIdAndUserId(
            Long organizationId,
            Long userId
    );

    List<OrganizationMember> findByOrganizationId(Long organizationId);

    List<OrganizationMember> findByUserId(Long userId);

    boolean existsByOrganizationIdAndUserId(
            Long organizationId,
            Long userId
    );

    void deleteByOrganizationIdAndUserId(
            Long organizationId,
            Long userId
    );
}
