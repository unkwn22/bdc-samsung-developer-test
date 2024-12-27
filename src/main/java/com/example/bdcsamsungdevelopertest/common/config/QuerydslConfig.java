package com.example.bdcsamsungdevelopertest.common.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuerydslConfig {

    private final EntityManager entityManager;
    private final JPAQueryFactory jpaQueryFactory;

    public QuerydslConfig(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public JPAQueryFactory getJpaQueryFactory() {
        return jpaQueryFactory;
    }
}
