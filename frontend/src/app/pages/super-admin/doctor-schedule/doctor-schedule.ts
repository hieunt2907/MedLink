import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { SuperAdminService, DoctorRoomAssignmentsEntity, RoomEntity, DoctorProfileEntity } from '../../../services/api.service';
import { NotificationService } from '../../../services/notification.service';

@Component({
  selector: 'app-doctor-schedule',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './doctor-schedule.html',
  styleUrls: ['./doctor-schedule.css']
})
export class DoctorScheduleComponent implements OnInit {
  doctorId: number = 0;
  doctorAssignments: DoctorRoomAssignmentsEntity[] = [];
  doctorProfile: DoctorProfileEntity | null = null;

  page = 0;
  size = 10;
  totalPages = 0;
  keyword = '';

  selectedAssignmentIds: Set<number> = new Set();

  // Modal State
  showModal = false;
  currentAssignmentId: number | null = null;
  assignmentForm: any = {
    doctorProfileId: null,
    hospitalId: null,
    roomId: null,
    specialtyId: null,
    isPrimary: false,
    shiftStart: '08:00:00',
    shiftEnd: '17:00:00',
    startDate: '',
    endDate: null,
    notes: ''
  };

  // Dropdown lists
  ticketRooms: RoomEntity[] = [];

  private superAdminService = inject(SuperAdminService);
  private notificationService = inject(NotificationService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private cdr = inject(ChangeDetectorRef);

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      const id = params.get('doctorId');
      if (id) {
        this.doctorId = +id;
        this.loadDoctorProfile();
        this.loadDoctorSchedules();
      }
    });
  }

  loadDoctorProfile() {
    this.superAdminService.filterDoctorProfiles('', 0, 1000).subscribe(res => {
      if (res.data) {
        this.doctorProfile = res.data.content.find(d => d.id === this.doctorId) || null;
      }
    });
  }

  loadDoctorSchedules() {
    this.superAdminService.filterDoctorRoomAssignments(this.doctorId, this.keyword, this.page, this.size).subscribe({
      next: (res) => {
        if (res.data) {
          this.doctorAssignments = res.data.content;
          this.totalPages = res.data.totalPages;
        }
        this.cdr.detectChanges();
      },
      error: () => this.cdr.detectChanges()
    });
  }

  toggleAssignmentSelection(id: number) {
    if (this.selectedAssignmentIds.has(id)) {
      this.selectedAssignmentIds.delete(id);
    } else {
      this.selectedAssignmentIds.add(id);
    }
  }

  isAssignmentSelected(id: number): boolean {
    return this.selectedAssignmentIds.has(id);
  }

  toggleAllAssignments(event: any) {
    if (event.target.checked) {
      this.doctorAssignments.forEach(a => this.selectedAssignmentIds.add(a.id));
    } else {
      this.selectedAssignmentIds.clear();
    }
  }

  editSelectedAssignment() {
    if (this.selectedAssignmentIds.size !== 1) return;
    const id = this.selectedAssignmentIds.values().next().value as number;
    const assignment = this.doctorAssignments.find(a => a.id === id);
    if (assignment) {
      this.currentAssignmentId = id;
      this.assignmentForm = {
        doctorProfileId: assignment.doctorProfileId,
        hospitalId: assignment.hospitalId,
        roomId: assignment.roomId,
        specialtyId: assignment.specialtyId,
        isPrimary: assignment.isPrimary,
        shiftStart: assignment.shiftStart,
        shiftEnd: assignment.shiftEnd,
        startDate: assignment.startDate,
        endDate: assignment.endDate,
        notes: assignment.notes
      };

      if (assignment.specialtyId) {
        this.loadRooms(assignment.specialtyId);
      }

      this.showModal = true;
    }
  }

  deleteSelectedAssignments() {
    const ids = Array.from(this.selectedAssignmentIds);
    if (ids.length === 0) return;

    let completed = 0;
    let errors = 0;
    ids.forEach(id => {
      this.superAdminService.deleteDoctorRoomAssignment(id).subscribe({
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
      this.notificationService.showSuccess('Đã xóa lịch phân công thành công.');
    }
    this.loadDoctorSchedules();
    this.selectedAssignmentIds.clear();
  }

  openModal() {
    this.currentAssignmentId = null;
    this.assignmentForm = {
      doctorProfileId: this.doctorId,
      hospitalId: this.doctorProfile?.hospitalId || null,
      specialtyId: this.doctorProfile?.specialtyId || null,
      roomId: null,
      isPrimary: false,
      shiftStart: '08:00:00',
      shiftEnd: '17:00:00',
      startDate: new Date().toISOString().split('T')[0],
      endDate: null,
      notes: ''
    };

    if (this.assignmentForm.specialtyId) {
      this.loadRooms(this.assignmentForm.specialtyId);
    }

    this.showModal = true;
  }

  closeModal() {
    this.showModal = false;
  }

  loadRooms(specialtyId: number) {
    this.superAdminService.filterRooms(specialtyId, 0, 100).subscribe(r => {
      if (r.data) {
        this.ticketRooms = r.data.content;
        this.cdr.detectChanges();
      }
    });
  }

  saveAssignment() {
    const request = { ...this.assignmentForm };

    const obs = this.currentAssignmentId
      ? this.superAdminService.updateDoctorRoomAssignment(this.currentAssignmentId, request)
      : this.superAdminService.createDoctorRoomAssignment(request);

    obs.subscribe({
      next: () => {
        this.notificationService.showSuccess(this.currentAssignmentId ? 'Cập nhật lịch thành công!' : 'Tạo lịch làm việc thành công!');
        this.closeModal();
        this.loadDoctorSchedules();
        this.selectedAssignmentIds.clear();
      },
      error: (err) => {
        this.notificationService.showError('Lỗi: ' + (err.error?.message || err.message));
      }
    });
  }

  goBack() {
    this.router.navigate(['super-admin', 'doctors']);
  }
}
