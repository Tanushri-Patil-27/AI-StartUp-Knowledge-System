package com.ai.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ai.user.model.Invitation;

public interface InvitationRepository
        extends JpaRepository<Invitation, Long> {

    Optional<Invitation> findByToken(String token);

    boolean existsByToken(String token);
}