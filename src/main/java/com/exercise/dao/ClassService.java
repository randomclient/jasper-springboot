package com.exercise.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.exercise.dto.ClassDto;

@Service
public class ClassService {

	@Autowired
	ClassRepository classRepository;

	public List<ClassDto> getAllClass() {
		List<ClassDto> list = (List<ClassDto>) classRepository.findAll();
		return list;
	}

	public Optional<ClassDto> getClassById(String id) {
		return classRepository.findById(id);
	}

	public void save(ClassDto dto) {
		classRepository.save(dto);
	}


}
