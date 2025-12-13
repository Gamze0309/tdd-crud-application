package com.gamze.tdd_crud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gamze.tdd_crud.entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {}
