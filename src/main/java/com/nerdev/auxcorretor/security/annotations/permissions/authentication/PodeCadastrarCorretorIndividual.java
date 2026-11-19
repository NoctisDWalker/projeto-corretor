package com.nerdev.auxcorretor.security.annotations.permissions.authentication;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@PreAuthorize("hasAnyRole('GERENTE', 'ADMIN')")
public @interface PodeCadastrarCorretorIndividual {
}
