package com.exercise.dao;

import org.springframework.data.repository.CrudRepository;

import com.exercise.dto.ClassDto;

public interface ClassRepository extends CrudRepository<ClassDto, String> {

}
