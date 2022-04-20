package com.deloitte.todolist.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskUpdateDTO {

	private String task;
	private String description;
	private Boolean isChecked;
}
