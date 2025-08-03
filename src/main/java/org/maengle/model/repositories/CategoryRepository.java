package org.maengle.model.repositories;

import org.maengle.model.entities.Categorys;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface CategoryRepository extends JpaRepository<Categorys, Long> , QuerydslPredicateExecutor<Categorys> {

}
