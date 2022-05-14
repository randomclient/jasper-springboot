package com.exercise.dao;

import org.springframework.data.repository.CrudRepository;
import com.exercise.dto.UserDto;


public interface UserRepository extends CrudRepository<UserDto, Integer>{

}
