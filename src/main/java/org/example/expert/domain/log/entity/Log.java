package org.example.expert.domain.log.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "logs")
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String action;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long todoId;

    private Long managerId;

    private String status;

    private String errorMessage;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public Log(String action, Long userId, Long todoId, Long managerId, String status, String errorMessage) {
        this.action = action;
        this.userId = userId;
        this.todoId = todoId;
        this.managerId = managerId;
        this.status = status;
        this.errorMessage = errorMessage;
        this.createdAt = LocalDateTime.now();
    }
}
