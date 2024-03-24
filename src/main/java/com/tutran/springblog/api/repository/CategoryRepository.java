package com.tutran.springblog.api.repository;

import com.tutran.springblog.api.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}