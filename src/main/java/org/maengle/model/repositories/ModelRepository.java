package org.maengle.model.repositories;

import org.maengle.model.entities.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ModelRepository extends JpaRepository<Model, Long>, QuerydslPredicateExecutor<Model> {
    Model findByCategoryId(String category);

}
