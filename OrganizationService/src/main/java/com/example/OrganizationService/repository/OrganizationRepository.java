package com.example.OrganizationService.repository;

import com.example.OrganizationService.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    Optional<Organization> findBySlug(String slug);

    boolean existsByName(String name);

    boolean existsBySlug(String slug);
}
