package com.kllhy.roadmap.common.annotation;

import java.lang.annotation.*;
import org.springframework.stereotype.Service;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Service
public @interface DomainService {}
