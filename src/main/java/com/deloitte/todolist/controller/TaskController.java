package com.deloitte.todolist.controller;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.deloitte.todolist.DTO.RespDTO;
import com.deloitte.todolist.DTO.TaskDTO;
import com.deloitte.todolist.DTO.TaskUpdateDTO;
import com.deloitte.todolist.entity.TaskEntity;
import com.deloitte.todolist.service.TaskService;
import com.deloitte.todolist.utils.JwtTokenUtil;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1")
public class TaskController {
	
	@Autowired
	private JwtTokenUtil jwt;
	
	@Autowired
	private TaskService taskService;

	@RequestMapping(value = "/task",method = RequestMethod.POST)
	public ResponseEntity<RespDTO> addNewTask(@RequestBody TaskDTO taskDTO, @RequestHeader("Authorization") String authToken) {
		String[] token=authToken.split(" ");
		String userName=jwt.extractusername(token[1]);
		String respMsg=taskService.addToTask(taskDTO, userName);
		RespDTO respObj= new RespDTO(respMsg);
		return new ResponseEntity<RespDTO>(respObj, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/task",method = RequestMethod.GET)
	public ResponseEntity<Page<TaskEntity>> getTaskByUser(@RequestHeader("Authorization") String authToken,Pageable page) {
		String[] token=authToken.split(" ");
		String userName=jwt.extractusername(token[1]);
		Page<TaskEntity> respSet=taskService.getTask(userName,page);
//		RespDTO respObj= new RespDTO(respMsg);
		return new ResponseEntity(respSet,HttpStatus.OK);
	}
	
	@RequestMapping(value = "/task/{taskId}",method = RequestMethod.DELETE)
	public ResponseEntity<RespDTO> deleteTaskByUser(@RequestHeader("Authorization") String authToken, @PathVariable Integer taskId) {
		String[] token=authToken.split(" ");
		String userName=jwt.extractusername(token[1]);
		String respMsg=taskService.deleteTask(taskId,userName);
		RespDTO respObj= new RespDTO(respMsg);
		return new ResponseEntity<RespDTO>(respObj,HttpStatus.OK);
	}
	

	@RequestMapping(value = "/task/{taskId}",method = RequestMethod.PUT)
	public ResponseEntity<RespDTO> updateTaskByUser(@RequestHeader("Authorization") String authToken, @PathVariable Integer taskId, @ RequestBody TaskUpdateDTO taskDTO) {
		String[] token=authToken.split(" ");
		System.out.println("taskDTO: "+taskDTO);
		String userName=jwt.extractusername(token[1]);
		String respMsg=taskService.updateTask(taskId,userName,taskDTO);
		RespDTO respObj= new RespDTO(respMsg);
		return new ResponseEntity<RespDTO>(respObj,HttpStatus.OK);
	}
}
