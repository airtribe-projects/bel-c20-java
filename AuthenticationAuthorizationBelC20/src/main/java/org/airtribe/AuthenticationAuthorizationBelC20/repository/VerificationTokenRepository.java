package org.airtribe.AuthenticationAuthorizationBelC20.repository;

import java.util.Optional;
import org.airtribe.AuthenticationAuthorizationBelC20.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
  Optional<VerificationToken> findByToken(String token);
}
