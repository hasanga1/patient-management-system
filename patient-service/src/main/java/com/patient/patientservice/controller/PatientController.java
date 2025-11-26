package com.patient.patientservice.controller;

import com.patient.patientservice.dto.PatientResponseDTO;
import com.patient.patientservice.dto.PatientRequestDTO;
import com.patient.patientservice.service.PatientService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patients")
@Tag(name = "Patient", description = "APIs for managing patients")
public class PatientController {

  private final PatientService patientService;

  public PatientController(PatientService patientService) {
    this.patientService = patientService;
  }

  @GetMapping
  @Operation(summary = "Get all patients", description = "Retrieve a list of all patients")
  public ResponseEntity<List<PatientResponseDTO>> getPatients() {
    List<PatientResponseDTO> patients = patientService.getPatients();
    return ResponseEntity.ok().body(patients);
  }

  @PostMapping
  @Operation(summary = "Create a new patient", description = "Create a new patient with the provided details")
  public ResponseEntity<PatientResponseDTO> createPatient(@Valid @RequestBody PatientRequestDTO patientRequestDTO) {
    PatientResponseDTO createdPatient = patientService.createPatient(patientRequestDTO);
    return ResponseEntity.status(201).body(createdPatient);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update an existing patient", description = "Update the details of an existing patient by ID")
  public ResponseEntity<PatientResponseDTO> updatePatient(@PathVariable UUID id, @Valid @RequestBody PatientRequestDTO patientRequestDTO) {
    PatientResponseDTO updatedPatient = patientService.updatePatient(id, patientRequestDTO);
    return ResponseEntity.ok().body(updatedPatient);
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete a patient", description = "Delete an existing patient by ID")
  public ResponseEntity<Void> deletePatient(@PathVariable UUID id) {
    patientService.deletePatient(id);
    return ResponseEntity.noContent().build();
  }

}