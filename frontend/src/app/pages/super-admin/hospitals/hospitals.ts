import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { SuperAdminService, HospitalEntity } from '../../../services/api.service';
import { NotificationService } from '../../../services/notification.service';

@Component({
  selector: 'app-hospitals',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './hospitals.html',
  styleUrls: ['./hospitals.css']
})
export class HospitalsComponent implements OnInit {
  hospitals: HospitalEntity[] = [];
  page = 0;
  size = 10;
  totalPages = 0;
  keyword = '';

  selectedHospitalIds: Set<number> = new Set();

  // Modal State
  showModal = false;
  currentHospitalId: number | null = null;
  hospitalForm: any = {
    name: '',
    address: '',
    phone: '',
    email: '',
    status: 'active'
  };

  private superAdminService = inject(SuperAdminService);
  private notificationService = inject(NotificationService);
  private router = inject(Router);
  private cdr = inject(ChangeDetectorRef);

  ngOnInit() {
    this.loadHospitals();
  }

  loadHospitals() {
    this.superAdminService.filterHospitals(this.keyword, this.page, this.size).subscribe({
      next: (res) => {
        if (res.data) {
          this.hospitals = res.data.content;
          this.totalPages = res.data.totalPages;
        }
        this.cdr.detectChanges();
      },
      error: () => {
        this.notificationService.showError('Không thể tải danh sách bệnh viện.');
        this.cdr.detectChanges();
      }
    });
  }

  viewSpecialties(h: HospitalEntity) {
    this.router.navigate(['super-admin', 'hospitals', h.id]);
  }

  toggleHospitalSelection(id: number) {
    if (this.selectedHospitalIds.has(id)) {
      this.selectedHospitalIds.delete(id);
    } else {
      this.selectedHospitalIds.add(id);
    }
  }

  isHospitalSelected(id: number): boolean {
    return this.selectedHospitalIds.has(id);
  }

  toggleAllHospitals(event: any) {
    if (event.target.checked) {
      this.hospitals.forEach(h => this.selectedHospitalIds.add(h.id));
    } else {
      this.selectedHospitalIds.clear();
    }
  }

  editSelectedHospital() {
    if (this.selectedHospitalIds.size !== 1) return;
    const id = this.selectedHospitalIds.values().next().value as number;
    const hospital = this.hospitals.find(h => h.id === id);
    if (hospital) {
      this.currentHospitalId = id;
      this.hospitalForm = {
        name: hospital.name,
        address: hospital.address,
        phone: hospital.phone,
        email: hospital.email || '',
        status: hospital.status || 'active'
      };
      this.showModal = true;
    }
  }

  deleteSelectedHospitals() {
    const ids = Array.from(this.selectedHospitalIds);
    if (ids.length === 0) return;

    let completed = 0;
    let errors = 0;

    ids.forEach(id => {
      this.superAdminService.deleteHospital(id).subscribe({
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
      this.notificationService.showSuccess('Đã xóa bệnh viện thành công.');
    }
    this.loadHospitals();
    this.selectedHospitalIds.clear();
  }

  openModal() {
    this.currentHospitalId = null;
    this.hospitalForm = { name: '', address: '', phone: '', email: '', status: 'active' };
    this.showModal = true;
  }

  closeModal() {
    this.showModal = false;
  }

  saveHospital() {
    const request = this.hospitalForm;
    const obs = this.currentHospitalId
      ? this.superAdminService.updateHospital(this.currentHospitalId, request)
      : this.superAdminService.createHospital(request);

    obs.subscribe({
      next: () => {
        this.notificationService.showSuccess(this.currentHospitalId ? 'Cập nhật thành công!' : 'Tạo bệnh viện thành công!');
        this.closeModal();
        this.loadHospitals();
        this.selectedHospitalIds.clear();
      },
      error: (err) => {
        this.notificationService.showError('Lỗi: ' + (err.error?.message || err.message));
      }
    });
  }

  onPageChange(newPage: number) {
    this.page = newPage;
    this.loadHospitals();
  }
}
