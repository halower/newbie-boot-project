package com.newbie.core.persistent.config;

import com.newbie.core.persistent.querydsl.extend.DefaultExtendedQueryDslJpaRepository;
import com.newbie.core.persistent.simple.CustomizedRepositoryImpl;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@EnableJpaRepositories(repositoryBaseClass = CustomizedRepositoryImpl.class)
public @interface EnableSimpleJpaRepositories {
}

