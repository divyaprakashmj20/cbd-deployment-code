package com.deloitte.todolist.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.deloitte.todolist.DTO.TaskDTO;
import com.deloitte.todolist.DTO.TaskUpdateDTO;
import com.deloitte.todolist.entity.TaskEntity;
import com.deloitte.todolist.entity.UserEntity;
import com.deloitte.todolist.exception.PermissionDeniedException;
import com.deloitte.todolist.repository.TaskRepository;
import com.deloitte.todolist.repository.UserRepo;

@Service
public class TaskServiceImpl implements TaskService {
	
	@Autowired
	private TaskRepository taskRepo;
	
	@Autowired
	private UserRepo userRepo;

	private Logger LOGGER = LoggerFactory.getLogger(TaskServiceImpl.class);

	@Override
	public String addToTask(TaskDTO task, String userName) {
		Optional<UserEntity> userObj=userRepo.findByUsername(userName);
		if(userObj.isPresent()) {
			TaskEntity taskObj= new TaskEntity();
			taskObj.setUserEntity(userObj.get());
			taskObj.setTask(task.getTask());
			taskObj.setDescription(task.getDescription());
			taskObj.setUpdateTime(LocalDateTime.now());
			taskObj.setIsChecked(false);
			TaskEntity savedObj=taskRepo.save(taskObj);
			LOGGER.info("Task created successfully with id '{}'", savedObj.getId());
			return "Task created successfully with id "+savedObj.getId();
		}
		return "Task failed to insert";
	}

	@Override
	public Page<TaskEntity> getTask(String userName,Pageable page) {
		Optional<UserEntity> userObj=userRepo.findByUsername(userName);
		if(userObj.isPresent()) {
			return taskRepo.findByUserEntity(userObj.get(),page);
			//Set<TaskEntity> taskList= userObj.getContent().get(0).getTaskEntities();
		}
		return null;
		
	}

	@Override
	public String deleteTask(Integer taskId, String userName) {
		Optional<UserEntity> userObj=userRepo.findByUsername(userName);
		if(userObj.isPresent()) {
			Set<TaskEntity> taskSet=userObj.get().getTaskEntities();
			if(taskSet.stream().anyMatch(x->x.getId()==taskId)) {
				taskRepo.deleteById(taskId);
				LOGGER.info("Task deleted successfully with id '{}'", taskId);
				return "Task deleted succssfully";
			}else {
				throw new PermissionDeniedException("This task is created by other user. Current user is not permitted to delete this task");
			}
		}
		return "Task was unable to delete";
	}

	@Override
	public String updateTask(Integer taskId, String userName, TaskUpdateDTO taskDTO) {
		Optional<UserEntity> userObj=userRepo.findByUsername(userName);
		if(userObj.isPresent()) {
			Set<TaskEntity> taskSet=userObj.get().getTaskEntities();
			if(taskSet.stream().anyMatch(x->x.getId()==taskId)) {
				TaskEntity taskObj= taskRepo.findById(taskId).get();
				taskObj.setDescription(taskDTO.getDescription());
				taskObj.setTask(taskDTO.getTask());
				taskObj.setIsChecked(taskDTO.getIsChecked());
				taskObj.setUpdateTime(LocalDateTime.now());
				taskRepo.save(taskObj);
				return "Task updated successfully";
			}else {
				throw new PermissionDeniedException("This task is created by other user. Current user is not permitted to modify this task");
			}
		}
		return "Task was unable to update";
	}

}
