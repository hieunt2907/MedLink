package com.hieunt.medlink.app.responses.patient;

import java.time.LocalDate;

public interface PatientChronicDiseasesResponse {
    Long getId();
    String getDiseaseName();
    LocalDate getDiagnosedDate();
    String getNotes();
}
