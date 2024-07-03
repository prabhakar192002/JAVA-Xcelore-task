public class PatientController {
    package com.example.demo.controller;

import com.example.demo.entity.Patient;
import com.example.demo.service.DoctorService;
import com.example.demo.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private DoctorService doctorService;

    @PostMapping
    public ResponseEntity<Patient> addPatient(@Valid @RequestBody Patient patient) {
        return ResponseEntity.ok(patientService.addPatient(patient));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removePatient(@PathVariable Long id) {
        patientService.removePatient(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/suggest-doctors")
    public ResponseEntity<?> suggestDoctors(@PathVariable Long id) {
        Optional<Patient> patientOptional = patientService.getPatientById(id);
        if (!patientOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Patient patient = patientOptional.get();
        String city = patient.getCity();
        String symptom = patient.getSymptom();
        String speciality = mapSymptomToSpeciality(symptom);

        if (speciality == null) {
            return ResponseEntity.badRequest().body("Invalid symptom");
        }

        List<Doctor> doctors = doctorService.suggestDoctors(city, speciality);

        if (doctors.isEmpty()) {
            if (!city.equalsIgnoreCase("Delhi") && !city.equalsIgnoreCase("Noida") && !city.equalsIgnoreCase("Faridabad")) {
                return ResponseEntity.ok("We are still waiting to expand to your location");
            } else {
                return ResponseEntity.ok("There isn’t any doctor present at your location for your symptom");
            }
        }

        return ResponseEntity.ok(doctors);
    }

    private String mapSymptomToSpeciality(String symptom) {
        switch (symptom.toLowerCase()) {
            case "arthritis":
            case "back pain":
            case "tissue injuries":
                return "Orthopaedic";
            case "dysmenorrhea":
                return "Gynecology";
            case "skin infection":
            case "skin burn":
                return "Dermatology";
            case "ear pain":
                return "ENT specialist";
            default:
                return null;
        }
    }
}
    
}
