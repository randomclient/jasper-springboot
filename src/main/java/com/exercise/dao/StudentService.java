package com.exercise.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.exercise.dto.StudentDto;

@Service
public class StudentService {
	@Autowired
	StudentRepository studentRepository;
	
	public List<StudentDto> getAllStudent(){
		List<StudentDto> list = (List<StudentDto>) studentRepository.findAll();
		return list;
	}
	
	public Optional<StudentDto> getStudentById(String id){
		return studentRepository.findById(id);
	}
	
	public void save(StudentDto dto) {
		studentRepository.save(dto);
	}
	
	public void delete(String id) {
		studentRepository.deleteById(id);
	}
	
	public void update(StudentDto dto, String id) {
		studentRepository.save(dto);
	}
}
