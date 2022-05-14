package com.exercise.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.exercise.dao.ClassRepository;
import com.exercise.dao.StudentRepository;
import com.exercise.dao.UserRepository;
import com.exercise.dto.ClassDto;
import com.exercise.dto.StudentDto;
import com.exercise.dto.UserDto;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class ReportService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ClassRepository classRepository;

	@Autowired
	private StudentRepository studentRepository;

	public ResponseEntity<byte[]> generatePdf() throws FileNotFoundException, JRException {

		// create collection:List
		List<UserDto> collection = (List<UserDto>) userRepository.findAll();

		// load the file
		File file = ResourceUtils.getFile("classpath:student.jrxml");

		// complie jrxml template file
		JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

		// create datasource
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(collection);

		// create map
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("reportTitle", "Student List");
		parameters.put("collectionBeanParam", dataSource);

		// fill the compiled report to be converted into JasperPrint:Object
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

		byte[] data = JasperExportManager.exportReportToPdf(jasperPrint);

		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=users.pdf");
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(data);
	}

	public ResponseEntity<byte[]> generateClassPdf() throws FileNotFoundException, JRException {
		List<ClassDto> collection = (List<ClassDto>) classRepository.findAll();

		// load the file
		File file = ResourceUtils.getFile("classpath:classreport.jrxml");

		// complie jrxml template file
		JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

		// create datasource
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(collection);

		// create map
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("classTitle", "Class List");
		parameters.put("classCollectionBeanParam", dataSource);

		// fill the compiled report to be converted into JasperPrint:Object
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

		byte[] data = JasperExportManager.exportReportToPdf(jasperPrint);

		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=class.pdf");
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(data);
	}
	
	public ResponseEntity<byte[]> generateStudentPdf() throws FileNotFoundException, JRException {

		// create collection:List
		List<StudentDto> collection = (List<StudentDto>) studentRepository.findAll();

		// load the file
		File file = ResourceUtils.getFile("classpath:studentreport.jrxml");

		// complie jrxml template file
		JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

		// create datasource
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(collection);

		// create map
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("studentTitle", "Student List");
		parameters.put("studentCollectionBeanParam", dataSource);

		// fill the compiled report to be converted into JasperPrint:Object
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

		byte[] data = JasperExportManager.exportReportToPdf(jasperPrint);

		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=students.pdf");
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(data);
	}
}
