package com.deloitte.todolist.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.deloitte.todolist.DTO.RespDTO;

@ControllerAdvice
public class TaskException {

	@ExceptionHandler(value = PermissionDeniedException.class)
	public ResponseEntity<Object> exception(PermissionDeniedException exception) {
		RespDTO respObj= new RespDTO(exception.getMessage());
		return new ResponseEntity<Object>(respObj, HttpStatus.BAD_REQUEST);
		
	}
}
