package com.deloitte.todolist;


import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.deloitte.todolist.DTO.TaskUpdateDTO;
import com.deloitte.todolist.exception.PermissionDeniedException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.deloitte.todolist.DTO.TaskDTO;
import com.deloitte.todolist.entity.TaskEntity;
import com.deloitte.todolist.entity.UserEntity;
import com.deloitte.todolist.repository.TaskRepository;
import com.deloitte.todolist.repository.UserRepo;
import com.deloitte.todolist.service.TaskServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class TodolistApplicationTests {

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Mock
	private TaskRepository taskRepo;
	
	@Mock
	private UserRepo userRepo;
	
	@InjectMocks
	private TaskServiceImpl taskService;
	
	@Test
	public void whenUserNameNotPresentThenTaskAdditionFailed() {
		TaskDTO task= new TaskDTO();
		when(userRepo.findByUsername(ArgumentMatchers.any())).thenReturn(Optional.ofNullable(null));
		assertEquals("Task failed to insert", taskService.addToTask(task, "Antony"));
	}
	
	@Test
	public void whenUserNamePresentThenMessageThenTaskAddedSuccessfully() {
		UserEntity userObj= new UserEntity(1,"Antony","p",new HashSet<TaskEntity>());
		TaskDTO task= new TaskDTO();
		TaskEntity taskEntity = new TaskEntity();
		taskEntity.setId(1);
		when(userRepo.findByUsername(ArgumentMatchers.any())).thenReturn(Optional.ofNullable(userObj));
		when(taskRepo.save(any(TaskEntity.class))).thenReturn(taskEntity);
		assertEquals("Task created successfully with id "+taskEntity.getId(), taskService.addToTask(task, "Antony"));
	}

	@Test
	public void whenUserNameNotPresentThenTaskNotDeleted(){
		Integer taskId = new Integer(1);
		when(userRepo.findByUsername(ArgumentMatchers.any())).thenReturn(Optional.ofNullable(null));
		assertEquals("Task was unable to delete", taskService.deleteTask(taskId, "Antony"));
	}

	@Test
	public void whenUserNamePresentAndTaskNotRelatedWithTheUserThenExceptionThrownWhenDeleting(){
		TaskEntity taskEntity = new TaskEntity();
		taskEntity.setId(1);
		Set<TaskEntity> taskSet = new HashSet<>();
		taskSet.add(taskEntity);

		UserEntity userObj= new UserEntity(1,"Antony","p",taskSet);

		when(userRepo.findByUsername(anyString())).thenReturn(Optional.ofNullable(userObj));
		exception.expect(PermissionDeniedException.class);
		exception.expectMessage("This task is created by other user. Current user is not permitted to delete this task");

		taskService.deleteTask(2,"Antony");
	}

	@Test
	public void whenUserNamePresentAndTaskRelatedWithTheUserThenTaskDeleted(){
		Integer taskId = new Integer(1);
		TaskEntity taskEntity = new TaskEntity();
		taskEntity.setId(taskId);
		Set<TaskEntity> taskSet = new HashSet<>();
		taskSet.add(taskEntity);
		UserEntity userObj= new UserEntity(1,"Antony","p",taskSet);

		when(userRepo.findByUsername(anyString())).thenReturn(Optional.ofNullable(userObj));

		assertEquals("Task deleted succssfully",taskService.deleteTask(taskId,"Antony"));

		verify(taskRepo,times(1)).deleteById(taskId);

	}

	@Test
	public void whenUserNameNotPresentThenTheTaskIsNotUpdated(){
		Integer taskId = new Integer(1);
		when(userRepo.findByUsername(ArgumentMatchers.any())).thenReturn(Optional.ofNullable(null));
		assertEquals("Task was unable to update", taskService.updateTask(taskId, "Antony",null));
	}

	@Test
	public void whenUserNamePresentAndTaskNotRelatedWithTheUserThenExceptionThrownWhenUpdating(){
		TaskEntity taskEntity = new TaskEntity();
		taskEntity.setId(1);
		Set<TaskEntity> taskSet = new HashSet<>();
		taskSet.add(taskEntity);

		UserEntity userObj= new UserEntity(1,"Antony","p",taskSet);

		when(userRepo.findByUsername(anyString())).thenReturn(Optional.ofNullable(userObj));
		exception.expect(PermissionDeniedException.class);
		exception.expectMessage("This task is created by other user. Current user is not permitted to modify this task");

		taskService.updateTask(2,"Antony",null);
	}

	@Test
	public void whenUserNamePresentAndTaskRelatedThenTaskUpdated(){
		Integer taskId = new Integer(1);
		TaskEntity taskEntity = new TaskEntity();
		taskEntity.setId(taskId);
		Set<TaskEntity> taskSet = new HashSet<>();
		taskSet.add(taskEntity);
		UserEntity userObj= new UserEntity(1,"Antony","p",taskSet);
		TaskUpdateDTO taskUpdateDTO = new TaskUpdateDTO();

		when(userRepo.findByUsername(anyString())).thenReturn(Optional.ofNullable(userObj));
		when(taskRepo.findById(anyInt())).thenReturn(Optional.of(taskEntity));

		assertEquals("Task updated successfully",taskService.updateTask(taskId,"Antony",taskUpdateDTO));

		verify(taskRepo,times(1)).save(any(TaskEntity.class));
	}
}
