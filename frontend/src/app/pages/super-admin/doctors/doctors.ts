import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { SuperAdminService, DoctorProfileEntity, HospitalEntity, SpecialtiesEntity, UserResponse } from '../../../services/api.service';
import { NotificationService } from '../../../services/notification.service';

@Component({
  selector: 'app-doctors',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './doctors.html',
  styleUrls: ['./doctors.css']
})
export class DoctorsComponent implements OnInit {
  doctors: DoctorProfileEntity[] = [];
  page = 0;
  size = 10;
  totalPages = 0;
  keyword = '';

  selectedDoctorIds: Set<number> = new Set();

  // Modal State
  showModal = false;
  currentDoctorId: number | null = null;
  doctorForm: any = {
    userId: null,
    specialtyId: null,
    hospitalId: null, // UI helper
    qualifications: '',
    experienceYears: 0
  };

  // Lists for Modal
  doctorUsers: UserResponse[] = [];
  filterHospitalsList: HospitalEntity[] = [];
  filterSpecialtiesList: SpecialtiesEntity[] = [];

  private superAdminService = inject(SuperAdminService);
  private notificationService = inject(NotificationService);
  private router = inject(Router);
  private cdr = inject(ChangeDetectorRef);

  ngOnInit() {
    this.loadDoctors();
  }

  loadDoctors() {
    this.superAdminService.filterDoctorProfiles(this.keyword, this.page, this.size).subscribe({
      next: (res) => {
        if (res.data) {
          this.doctors = res.data.content;
          this.totalPages = res.data.totalPages;
        }
        this.cdr.detectChanges();
      },
      error: () => {
        this.notificationService.showError('Không thể tải danh sách bác sĩ.');
        this.cdr.detectChanges();
      }
    });
  }

  viewSchedule(d: DoctorProfileEntity) {
    this.router.navigate(['super-admin', 'doctors', d.id, 'schedule']);
  }

  toggleDoctorSelection(id: number) {
    if (this.selectedDoctorIds.has(id)) {
      this.selectedDoctorIds.delete(id);
    } else {
      this.selectedDoctorIds.add(id);
    }
  }

  isDoctorSelected(id: number): boolean {
    return this.selectedDoctorIds.has(id);
  }

  toggleAllDoctors(event: any) {
    if (event.target.checked) {
      this.doctors.forEach(d => this.selectedDoctorIds.add(d.id));
    } else {
      this.selectedDoctorIds.clear();
    }
  }

  editSelectedDoctor() {
    if (this.selectedDoctorIds.size !== 1) return;
    const id = this.selectedDoctorIds.values().next().value as number;
    const doctor = this.doctors.find(d => d.id === id);
    if (doctor) {
      this.currentDoctorId = id;
      this.doctorForm = {
        userId: doctor.userId,
        specialtyId: doctor.specialtyId,
        hospitalId: doctor.hospitalId,
        qualifications: doctor.qualifications,
        experienceYears: doctor.experienceYears
      };

      this.loadUsersForDoctor();
      this.loadFilterHospitals();

      // If hospitalId is present, define specialties
      if (doctor.hospitalId) {
        this.superAdminService.filterSpecialties(doctor.hospitalId, 0, 100).subscribe(res => {
          if (res.data) {
            this.filterSpecialtiesList = res.data.content;
            this.cdr.detectChanges();
          }
        });
      }

      this.showModal = true;
    }
  }

  deleteSelectedDoctors() {
    const ids = Array.from(this.selectedDoctorIds);
    if (ids.length === 0) return;

    let completed = 0;
    let errors = 0;
    ids.forEach(id => {
      this.superAdminService.deleteDoctorProfile(id).subscribe({
        next: () => {
          completed++;
          if (completed === ids.length) {
            this.handleDeleteComplete(errors);
          }
        },
        error: () => {
          completed++;
          errors++;
          if (completed === ids.length) {
            this.handleDeleteComplete(errors);
          }
        }
      });
    });
  }

  private handleDeleteComplete(errors: number) {
    if (errors > 0) {
      this.notificationService.showWarning(`Đã xóa với ${errors} lỗi.`);
    } else {
      this.notificationService.showSuccess('Đã xóa hồ sơ bác sĩ thành công.');
    }
    this.loadDoctors();
    this.selectedDoctorIds.clear();
  }

  openModal() {
    this.currentDoctorId = null;
    this.doctorForm = { userId: null, specialtyId: null, hospitalId: null, qualifications: '', experienceYears: 0 };
    this.loadUsersForDoctor();
    this.loadFilterHospitals();
    this.showModal = true;
  }

  closeModal() {
    this.showModal = false;
  }

  saveDoctor() {
    const request = { ...this.doctorForm };
    delete request.hospitalId;

    const obs = this.currentDoctorId
      ? this.superAdminService.updateDoctorProfile(this.currentDoctorId, request)
      : this.superAdminService.createDoctorProfile(request);

    obs.subscribe({
      next: () => {
        this.notificationService.showSuccess(this.currentDoctorId ? 'Cập nhật thành công!' : 'Tạo hồ sơ bác sĩ thành công!');
        this.closeModal();
        this.loadDoctors();
        this.selectedDoctorIds.clear();
      },
      error: (err) => {
        this.notificationService.showError('Lỗi: ' + (err.error?.message || err.message));
      }
    });
  }

  loadUsersForDoctor() {
    this.superAdminService.filterUsers('', 0, 100).subscribe(res => {
      if (res.data) {
        this.doctorUsers = res.data.content;
        this.cdr.detectChanges();
      }
    });
  }

  loadFilterHospitals() {
    this.superAdminService.filterHospitals('', 0, 100).subscribe(res => {
      if (res.data) {
        this.filterHospitalsList = res.data.content;
        this.cdr.detectChanges();
      }
    });
  }

  onDoctorHospitalChange() {
    if (this.doctorForm.hospitalId) {
      this.superAdminService.filterSpecialties(this.doctorForm.hospitalId, 0, 100).subscribe(res => {
        if (res.data) {
          this.filterSpecialtiesList = res.data.content;
          this.cdr.detectChanges();
        }
      });
    } else {
      this.filterSpecialtiesList = [];
      this.cdr.detectChanges();
    }
  }

  onPageChange(newPage: number) {
    this.page = newPage;
    this.loadDoctors();
  }
}
