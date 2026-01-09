import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { SuperAdminService, PatientProfileEntity } from '../../../services/api.service';
import { NotificationService } from '../../../services/notification.service';

@Component({
  selector: 'app-patients',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './patients.html',
  styleUrls: ['./patients.css']
})
export class PatientsComponent implements OnInit {
  patients: PatientProfileEntity[] = [];
  page = 0;
  size = 10;
  totalPages = 0;
  keyword = '';

  private superAdminService = inject(SuperAdminService);
  private notificationService = inject(NotificationService);
  private router = inject(Router);
  private cdr = inject(ChangeDetectorRef);

  ngOnInit() {
    this.loadPatients();
  }

  loadPatients() {
    this.superAdminService.filterPatientProfiles(this.keyword, this.page, this.size).subscribe({
      next: (res) => {
        if (res.data) {
          this.patients = res.data.content;
          this.totalPages = res.data.totalPages;
        }
        this.cdr.detectChanges();
      },
      error: () => {
        this.notificationService.showError('Không thể tải danh sách bệnh nhân.');
        this.cdr.detectChanges();
      }
    });
  }

  viewPatient(p: PatientProfileEntity) {
    this.router.navigate(['super-admin', 'patients', p.id]);
  }

  createTicket(p: PatientProfileEntity) {
    this.router.navigate(['super-admin', 'tickets'], { queryParams: { createForPatientId: p.userId } });
  }

  getBloodTypeDisplay(type: string): string {
    const map: { [key: string]: string } = {
      'A_POSITIVE': 'A+',
      'A_NEGATIVE': 'A-',
      'B_POSITIVE': 'B+',
      'B_NEGATIVE': 'B-',
      'AB_POSITIVE': 'AB+',
      'AB_NEGATIVE': 'AB-',
      'O_POSITIVE': 'O+',
      'O_NEGATIVE': 'O-'
    };
    return map[type] || type;
  }

  onPageChange(newPage: number) {
    this.page = newPage;
    this.loadPatients();
  }
}
