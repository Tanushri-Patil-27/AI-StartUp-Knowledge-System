package com.example.OrganizationService.repository;

import com.example.OrganizationService.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepository
        extends JpaRepository<Organization, Long> {

    boolean existsByName(String name);

    boolean existsBySlug(String slug);
}