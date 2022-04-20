package com.deloitte.todolist.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.deloitte.todolist.entity.UserEntity;
import com.deloitte.todolist.repository.UserRepo;

@Component
public class JwtUserDetailsService implements UserDetailsService { //To load user deatils from Database
	
	@Autowired
	private UserRepo userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		Optional<UserEntity> userOptional = userRepository.findByUsername(username);
		System.out.println(username);
		System.out.println(userOptional.get().getUsername());
		if(userOptional.isPresent()) {
			UserEntity user = userOptional.get();
			Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            //authorities.add(new SimpleGrantedAuthority(user.getRole()));
            return new User(user.getUsername().toLowerCase(), user.getPassword(), authorities);
		}
		return null;
	}
}