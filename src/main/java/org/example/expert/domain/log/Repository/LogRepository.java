package org.example.expert.domain.log.Repository;

import org.example.expert.domain.log.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Log, Long> {
}
