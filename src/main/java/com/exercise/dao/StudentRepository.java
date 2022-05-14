package com.exercise.dao;

import org.springframework.data.repository.CrudRepository;

import com.exercise.dto.StudentDto;

public interface StudentRepository extends CrudRepository<StudentDto, String> {

}
