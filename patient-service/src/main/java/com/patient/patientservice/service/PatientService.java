package com.patient.patientservice.service;

import com.patient.patientservice.dto.PatientRequestDTO;
import com.patient.patientservice.dto.PatientResponseDTO;
import com.patient.patientservice.mapper.PatientMapper;
import com.patient.patientservice.model.Patient;
import com.patient.patientservice.repository.PatientRepository;
import com.patient.patientservice.exception.EmailAlreadyExistsException;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PatientService {

  private final PatientRepository patientRepository;

  public PatientService(PatientRepository patientRepository) {
    this.patientRepository = patientRepository;
  }

  public List<PatientResponseDTO> getPatients() {
    List<Patient> patients = patientRepository.findAll();

    return patients.stream().map(PatientMapper::toDTO).toList();
  }

  public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {
    if (patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
      throw new EmailAlreadyExistsException("Patient with email " + patientRequestDTO.getEmail() + " already exists.");
    }
    Patient patient = PatientMapper.toModel(patientRequestDTO);
    Patient savedPatient = patientRepository.save(patient);
    return PatientMapper.toDTO(savedPatient);
  } 
}