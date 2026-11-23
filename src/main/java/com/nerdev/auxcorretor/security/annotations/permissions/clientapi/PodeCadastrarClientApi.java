package com.nerdev.auxcorretor.security.annotations.permissions.clientapi;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@PreAuthorize("hasAnyRole('ADMIN')")
public @interface PodeCadastrarClientApi {
}
