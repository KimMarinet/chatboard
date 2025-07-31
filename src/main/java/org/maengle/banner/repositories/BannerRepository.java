package org.maengle.banner.repositories;

import org.maengle.banner.entities.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface BannerRepository extends JpaRepository<Banner, Long>, QuerydslPredicateExecutor<Banner> {
}
