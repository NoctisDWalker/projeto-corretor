package com.nerdev.auxcorretor.repository.specs;

import org.springframework.data.jpa.domain.Specification;

import java.util.function.Function;

public class SpecificationBuilder<T> {

    private Specification<T> spec = (root, query, cb) -> cb.conjunction();

    public <V> SpecificationBuilder<T> and(V valor, Function<V, Specification<T>> specFn) {
        if (valor != null) {
            spec = spec.and(specFn.apply(valor));
        }
        return this;
    }

    public SpecificationBuilder<T> andIfNotBlank(String valor, Function<String, Specification<T>> specFn) {
        if (valor != null && !valor.isBlank()) {
            spec = spec.and(specFn.apply(valor));
        }
        return this;
    }

    public Specification<T> build(){
        return spec;
    }

}
