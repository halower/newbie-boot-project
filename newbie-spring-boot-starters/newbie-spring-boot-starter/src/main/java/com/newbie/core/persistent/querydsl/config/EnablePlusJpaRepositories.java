package com.newbie.core.persistent.querydsl.config;

import com.newbie.core.persistent.querydsl.extend.DefaultExtendedQueryDslJpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@EnableJpaRepositories(repositoryBaseClass = DefaultExtendedQueryDslJpaRepository.class)
public @interface EnablePlusJpaRepositories {
}

