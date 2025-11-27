package com.patient.patientservice.service;

import com.patient.patientservice.dto.PatientRequestDTO;
import com.patient.patientservice.dto.PatientResponseDTO;
import com.patient.patientservice.mapper.PatientMapper;
import com.patient.patientservice.model.Patient;
import com.patient.patientservice.repository.PatientRepository;
import com.patient.patientservice.exception.EmailAlreadyExistsException;
import com.patient.patientservice.exception.PatientNotFoundException;
import com.patient.patientservice.grpc.BillingServiceGrpcClient;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class PatientService {

  private final PatientRepository patientRepository;
  private final BillingServiceGrpcClient billingServiceGrpcClient;

  public PatientService(PatientRepository patientRepository, BillingServiceGrpcClient billingServiceGrpcClient) {
    this.patientRepository = patientRepository;
    this.billingServiceGrpcClient = billingServiceGrpcClient;
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

    // Create billing account via GRPC
    billingServiceGrpcClient.createBillingAccount(
        savedPatient.getId().toString(),
        savedPatient.getName(),
        savedPatient.getEmail()
    );
    return PatientMapper.toDTO(savedPatient);
  } 

  public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO patientRequestDTO) {
    Patient existingPatient = patientRepository.findById(id).orElseThrow(() -> new PatientNotFoundException("Patient not found with id: " + id));
    if (patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(), id)) {
      throw new EmailAlreadyExistsException("Patient with email " + patientRequestDTO.getEmail() + " already exists.");
    }
    existingPatient.setName(patientRequestDTO.getName());
    existingPatient.setEmail(patientRequestDTO.getEmail());
    existingPatient.setAddress(patientRequestDTO.getAddress());
    existingPatient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));
    Patient updatedPatient = patientRepository.save(existingPatient);
    return PatientMapper.toDTO(updatedPatient);
  }

  public void deletePatient(UUID id) {
    Patient existingPatient = patientRepository.findById(id).orElseThrow(() -> new PatientNotFoundException("Patient not found with id: " + id));
    patientRepository.delete(existingPatient);
  }
}