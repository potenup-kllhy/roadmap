package com.kllhy.roadmap.common.annotation;

import java.lang.annotation.*;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface DomainService {}
