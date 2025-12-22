package org.example.expert.domain.log.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.domain.log.Repository.LogRepository;
import org.example.expert.domain.log.entity.Log;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogService {

    private final LogRepository logRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveManagerRegistrationLog(
                String action,
                Long userId,
                Long todoId,
                Long managerId,
                String status,
                String errorMessage
    ) {
        try {
            Log logEntity = new Log(action, userId, todoId, managerId, status, errorMessage);
            logRepository.save(logEntity);
            log.info("Manager registration log saved: action={}, userId={}, todoId={}, status={}",
                    action, userId, todoId, status);
        } catch (Exception e) {
            log.error("Failed to save manager registration log", e);
        }
    }
}
