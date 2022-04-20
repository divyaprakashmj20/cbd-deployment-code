package com.deloitte.todolist.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.deloitte.todolist.DTO.TaskDTO;
import com.deloitte.todolist.DTO.TaskUpdateDTO;
import com.deloitte.todolist.entity.TaskEntity;

public interface TaskService {

	public String addToTask(TaskDTO task, String userName);
	
	public Page<TaskEntity> getTask(String userName, Pageable page);

	public String deleteTask(Integer taskId, String userName);

	public String updateTask(Integer taskId, String userName, TaskUpdateDTO taskDTO);

}
