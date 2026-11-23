package com.nerdev.auxcorretor.security.annotations.permissions.clientes;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@PreAuthorize("hasAnyRole('CORRETOR_INDIVIDUAL', 'CORRETOR_VINCULADO', 'GERENTE', 'ADMIN')")
public @interface PodeCadastrarCliente {
}
