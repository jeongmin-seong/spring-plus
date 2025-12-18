package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    @Query("SELECT t FROM Todo t LEFT JOIN FETCH t.user u ORDER BY t.modifiedAt DESC")
    Page<Todo> findAllByOrderByModifiedAtDesc(Pageable pageable);

    //@Query("SELECT t FROM Todo t " +
    //        "LEFT JOIN t.user " +
    //        "WHERE t.id = :todoId")
    //Optional<Todo> findByIdWithUser(@Param("todoId") Long todoId);

    // weather 조건만
    @Query("SELECT t FROM Todo t LEFT JOIN FETCH t.user u " +
            "WHERE t.weather = :weather " +
            "ORDER BY t.modifiedAt DESC")
    Page<Todo> findByWeather(@Param("weather") String weather, Pageable pageable);

    // 날짜 범위만 (시작일)
    @Query("SELECT t FROM Todo t LEFT JOIN FETCH t.user u " +
            "WHERE t.modifiedAt >= :startDate " +
            "ORDER BY t.modifiedAt DESC")
    Page<Todo> findByModifiedAtAfter(@Param("startDate") LocalDateTime startDate, Pageable pageable);

    // 날짜 범위만 (종료일)
    @Query("SELECT t FROM Todo t LEFT JOIN FETCH t.user u " +
            "WHERE t.modifiedAt BETWEEN :startable AND :endDate " +
            "ORDER BY t.modifiedAt DESC")
    Page<Todo> findByModifiedAtBefore(@Param("endDate") LocalDateTime endDate, Pageable pageable);

    // 날짜 범위 (시작일 + 종료일)
    @Query("SELECT t FROM Todo t LEFT JOIN FETCH t.user u " +
            "WHERE t.modifiedAt BETWEEN :startDate AND :endDate " +
            "ORDER BY t.modifiedAt DESC")
    Page<Todo> findByModifiedAtBetween(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);

    // weather + 시작일
    @Query("SELECT t FROM Todo t LEFT JOIN FETCH t.user u " +
            "WHERE t.weather = :weather AND t.modifiedAt = :startDate " +
            "ORDER BY t.modifiedAt DESC")
    Page<Todo> findByWeatherAndModifiedAtAfter(
            @Param("weather") String weather,
            @Param("startDate") LocalDateTime startDate,
            Pageable pageable);

    // weather + 종료일
    @Query("SELECT t FROM Todo t left join fetch t.user u " +
            "WHERE t.weather = :weather AND t.modifiedAt = :endDate " +
            "ORDER BY t.modifiedAt DESC")
    Page<Todo> findByWeatherAndModifiedAtBefore(
            @Param("weather") String weather,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);

    // weather + 날짜 범위 (시작일 + 종료일)
    @Query("SELECT t FROM Todo t LEFT JOIN FETCH t.user u " +
            "WHERE t.weather = :weather AND t.modifiedAt BETWEEN :startDate AND :endDate " +
            "ORDER BY t.modifiedAt DESC")
    Page<Todo> findByWeatherAndModifiedAtBetween(
            @Param("weather") String weather,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);
}
