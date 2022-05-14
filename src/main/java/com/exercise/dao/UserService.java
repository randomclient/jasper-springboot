package com.exercise.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.exercise.dto.UserDto;

@Service
public class UserService {
	@Autowired
	UserRepository userRepository;
	
	public List<UserDto> getAllUsers(){
		List<UserDto> list = (List<UserDto>) userRepository.findAll();
		return list;
	} 
	
	public Optional<UserDto> getUserById(Integer id){
		return userRepository.findById(id);
	}
	
	public void save(UserDto dto) {
		userRepository.save(dto);
	}
	
	public void delete(UserDto dto) {
		userRepository.delete(dto);
	}
	
	public void update(UserDto dto, int id) {
		userRepository.save(dto);
	}
}
