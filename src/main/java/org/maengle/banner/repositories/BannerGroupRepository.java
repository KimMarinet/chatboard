package org.maengle.banner.repositories;

import org.maengle.banner.entities.BannerGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface BannerGroupRepository extends JpaRepository<BannerGroup, String>, QuerydslPredicateExecutor<BannerGroup> {
}
