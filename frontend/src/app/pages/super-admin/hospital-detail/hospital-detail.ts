import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { SuperAdminService, SpecialtiesEntity } from '../../../services/api.service';
import { NotificationService } from '../../../services/notification.service';

@Component({
  selector: 'app-hospital-detail',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './hospital-detail.html',
  styleUrls: ['./hospital-detail.css']
})
export class HospitalDetailComponent implements OnInit {
  hospitalId: number = 0;
  specialties: SpecialtiesEntity[] = [];
  page = 0;
  size = 10;
  totalPages = 0;

  selectedSpecialtyIds: Set<number> = new Set();

  // Modal State
  showModal = false;
  currentSpecialtyId: number | null = null;
  specialtyForm: any = {
    name: '',
    description: '',
    hospitalId: null,
    status: 'active'
  };

  private superAdminService = inject(SuperAdminService);
  private notificationService = inject(NotificationService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private cdr = inject(ChangeDetectorRef);

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      const id = params.get('hospitalId');
      if (id) {
        this.hospitalId = +id;
        this.loadSpecialties();
      }
    });
  }

  loadSpecialties() {
    this.superAdminService.filterSpecialties(this.hospitalId, this.page, this.size).subscribe({
      next: (res) => {
        if (res.data) {
          this.specialties = res.data.content;
          this.totalPages = res.data.totalPages;
        }
        this.cdr.detectChanges();
      },
      error: () => {
        this.notificationService.showError('Không thể tải danh sách chuyên khoa.');
        this.cdr.detectChanges();
      }
    });
  }

  viewRooms(s: SpecialtiesEntity) {
    this.router.navigate(['super-admin', 'hospitals', this.hospitalId, 'specialties', s.id]);
  }

  toggleSpecialtySelection(id: number) {
    if (this.selectedSpecialtyIds.has(id)) {
      this.selectedSpecialtyIds.delete(id);
    } else {
      this.selectedSpecialtyIds.add(id);
    }
  }

  isSpecialtySelected(id: number): boolean {
    return this.selectedSpecialtyIds.has(id);
  }

  toggleAllSpecialties(event: any) {
    if (event.target.checked) {
      this.specialties.forEach(s => this.selectedSpecialtyIds.add(s.id));
    } else {
      this.selectedSpecialtyIds.clear();
    }
  }

  editSelectedSpecialty() {
    if (this.selectedSpecialtyIds.size !== 1) return;
    const id = this.selectedSpecialtyIds.values().next().value as number;
    const specialty = this.specialties.find(s => s.id === id);
    if (specialty) {
      this.currentSpecialtyId = id;
      this.specialtyForm = { ...specialty };
      this.showModal = true;
    }
  }

  deleteSelectedSpecialties() {
    const ids = Array.from(this.selectedSpecialtyIds);
    if (ids.length === 0) return;

    let completed = 0;
    let errors = 0;
    ids.forEach(id => {
      this.superAdminService.deleteSpecialty(id).subscribe({
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
      this.notificationService.showSuccess('Đã xóa chuyên khoa thành công.');
    }
    this.loadSpecialties();
    this.selectedSpecialtyIds.clear();
  }

  openModal() {
    this.currentSpecialtyId = null;
    this.specialtyForm = { name: '', description: '', hospitalId: this.hospitalId, status: 'active' };
    this.showModal = true;
  }

  closeModal() {
    this.showModal = false;
  }

  saveSpecialty() {
    const request = this.specialtyForm;
    request.hospitalId = this.hospitalId;

    const obs = this.currentSpecialtyId
      ? this.superAdminService.updateSpecialty(this.currentSpecialtyId, request)
      : this.superAdminService.createSpecialty(request);

    obs.subscribe({
      next: () => {
        this.notificationService.showSuccess(this.currentSpecialtyId ? 'Cập nhật thành công!' : 'Tạo chuyên khoa thành công!');
        this.closeModal();
        this.loadSpecialties();
        this.selectedSpecialtyIds.clear();
      },
      error: (err) => {
        this.notificationService.showError('Lỗi: ' + (err.error?.message || err.message));
      }
    });
  }

  goBack() {
    this.router.navigate(['super-admin', 'hospitals']);
  }

  onPageChange(newPage: number) {
    this.page = newPage;
    this.loadSpecialties();
  }
}
