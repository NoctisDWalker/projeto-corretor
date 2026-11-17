package com.nerdev.auxcorretor.security.annotations.permissions.imoveis;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@PreAuthorize("hasAnyRole('CORRETOR_INDIVIDUAL', 'IMOBILIARIA', 'CORRETOR_VINCULADO', 'GERENTE', 'ADMIN')")
public @interface PodeReativarImoveis {
}
