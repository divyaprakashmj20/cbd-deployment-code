package com.deloitte.todolist.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO,generator="native")
    @GenericGenerator(name = "native",strategy = "native")
	private Integer id;
	private String username;
	private String password;
	@JsonManagedReference
	@OneToMany(mappedBy = "userEntity",fetch = FetchType.LAZY)
	private Set<TaskEntity> taskEntities;
	@Override
	public String toString() {
		return "UserEntity [id=" + id + ", username=" + username + ", password=" + password + "]";
	}
	
	
}
