import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { SuperAdminService, PatientAllergy, PatientChronicDisease, PatientProfileEntity } from '../../../services/api.service';

@Component({
  selector: 'app-patient-detail',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './patient-detail.html',
  styleUrls: ['./patient-detail.css']
})
export class PatientDetailComponent implements OnInit {
  patientId: number = 0;
  patientProfile: PatientProfileEntity | null = null;
  patientAllergies: PatientAllergy[] = [];
  patientDiseases: PatientChronicDisease[] = [];

  private superAdminService = inject(SuperAdminService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private cdr = inject(ChangeDetectorRef);

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      const id = params.get('patientId');
      if (id) {
        this.patientId = +id;
        this.loadPatientDetails();
      }
    });
  }

  loadPatientDetails() {
    // We might need to fetch profile explicitly if we want to show name in header
    this.superAdminService.getPatientProfile(this.patientId).subscribe(res => {
      if (res.data) this.patientProfile = res.data;
    });

    this.superAdminService.filterPatientAllergies(this.patientId).subscribe({
      next: (res) => {
        if (res.data) this.patientAllergies = res.data.content;
        this.cdr.detectChanges();
      }
    });
    this.superAdminService.filterPatientChronicDiseases(this.patientId).subscribe({
      next: (res) => {
        if (res.data) this.patientDiseases = res.data.content;
        this.cdr.detectChanges();
      }
    });
  }

  goBack() {
    this.router.navigate(['super-admin', 'patients']);
  }
}
