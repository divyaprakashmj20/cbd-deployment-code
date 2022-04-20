package com.deloitte.todolist.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deloitte.todolist.entity.TaskEntity;
import com.deloitte.todolist.entity.UserEntity;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Integer> {

	Page<TaskEntity> findByUserEntity(UserEntity userEntity,Pageable page);
}
