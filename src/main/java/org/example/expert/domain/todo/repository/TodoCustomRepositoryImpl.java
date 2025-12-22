package org.example.expert.domain.todo.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.todo.dto.request.TodoSearchCondition;
import org.example.expert.domain.todo.dto.response.QTodoSearchResponse;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.example.expert.domain.comment.entity.QComment.comment;
import static org.example.expert.domain.manager.entity.QManager.manager;
import static org.example.expert.domain.todo.entity.QTodo.todo;
import static org.example.expert.domain.user.entity.QUser.user;

@RequiredArgsConstructor
public class TodoCustomRepositoryImpl implements TodoCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Todo> findByIdWithUser(Long todoId) {
        Todo result = queryFactory
                .selectFrom(todo)
                .leftJoin(todo.user, user).fetchJoin()
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Page<TodoSearchResponse> searchTodos(TodoSearchCondition condition, Pageable pageable) {
        List<TodoSearchResponse> content = queryFactory
                .select(new QTodoSearchResponse(
                        todo.id,
                        todo.title,
                        manager.countDistinct(),
                        comment.countDistinct(),
                        todo.createdAt
                ))
                .from(todo)
                .leftJoin(todo.managers, manager)
                .leftJoin(todo.comments, comment)
                .leftJoin(manager.user, user)
                .where(
                        titleContains(condition.getKeyword()),
                        createdAtBetween(condition.getStartDate(), condition.getEndDate()),
                        managerNicknameContains(condition.getManagerNickname())
                )
                .groupBy(todo.id)
                .orderBy(todo.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(Wildcard.count)
                .from(todo)
                .leftJoin(todo.managers, manager)
                .leftJoin(manager.user, user)
                .where(
                        titleContains(condition.getKeyword()),
                        createdAtBetween(condition.getStartDate(), condition.getEndDate()),
                        managerNicknameContains(condition.getManagerNickname())
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0L);
    }

    private BooleanExpression titleContains(String keyword) {
        return StringUtils.hasText(keyword) ? todo.title.containsIgnoreCase(keyword) : null;
    }

    private BooleanExpression createdAtBetween(String startDate, String endDate) {
        if (!StringUtils.hasText(startDate) && !StringUtils.hasText(endDate)) {
            return null;
        }

        LocalDateTime start = StringUtils.hasText(startDate)
                ? LocalDate.parse(startDate).atStartOfDay()
                : LocalDateTime.of(1970, 1, 1, 0, 0);

        LocalDateTime end = StringUtils.hasText(endDate)
                ? LocalDate.parse(endDate).atTime(LocalTime.MAX)
                : LocalDateTime.now();

        return todo.createdAt.between(start, end);
    }

    private BooleanExpression managerNicknameContains(String nickname) {
        return StringUtils.hasText(nickname) ? user.nickname.containsIgnoreCase(nickname) : null;
    }
}
