import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { SuperAdminService, ReceptionTicketsResponse, HospitalEntity, SpecialtiesEntity, RoomEntity, DoctorProfileEntity } from '../../../services/api.service';
import { NotificationService } from '../../../services/notification.service';

@Component({
  selector: 'app-tickets',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './tickets.html',
  styleUrls: ['./tickets.css']
})
export class TicketsComponent implements OnInit {
  tickets: ReceptionTicketsResponse[] = [];
  page = 0;
  size = 10;
  totalPages = 0;
  keyword = '';

  // Filters
  ticketFilters = {
    hospitalId: null as number | null,
    specialtyId: null as number | null,
    roomId: null as number | null,
    doctorId: null as number | null
  };

  // Filter Lists
  filterHospitalsList: HospitalEntity[] = [];
  filterSpecialtiesList: SpecialtiesEntity[] = [];
  filterRoomsList: RoomEntity[] = [];
  filterDoctorsList: DoctorProfileEntity[] = [];

  // Modal State
  showModal = false;
  ticketForm: any = {
    hospitalId: null,
    specialtyId: null,
    doctorId: null,
    patientId: null,
    reason: '',
    priority: 'normal',
    notes: '',
    payerType: 'self_pay',
    status: 'waiting',
    estimatedTime: null,
    queueNumber: null,
    roomId: null
  };

  // Modal Dropdown Lists
  ticketHospitals: HospitalEntity[] = [];
  ticketSpecialties: SpecialtiesEntity[] = [];
  ticketDoctors: DoctorProfileEntity[] = [];
  ticketRooms: RoomEntity[] = [];

  private superAdminService = inject(SuperAdminService);
  private notificationService = inject(NotificationService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private cdr = inject(ChangeDetectorRef);

  ngOnInit() {
    this.loadTickets();
    this.loadFilterHospitals();

    // Check for create request
    this.route.queryParams.subscribe(params => {
      const patientId = params['createForPatientId'];
      if (patientId) {
        this.openModal(patientId);
      }
    });
  }

  loadTickets() {
    const params = {
      keyword: this.keyword,
      page: this.page,
      size: this.size,
      hospitalId: this.ticketFilters.hospitalId,
      specialtyId: this.ticketFilters.specialtyId,
      roomId: this.ticketFilters.roomId,
      doctorId: this.ticketFilters.doctorId
    };
    this.superAdminService.filterTickets(params).subscribe({
      next: (res) => {
        if (res.data) {
          this.tickets = res.data.content;
          this.totalPages = res.data.totalPages;
        }
        this.cdr.detectChanges();
      },
      error: () => {
        this.notificationService.showError('Không thể tải danh sách phiếu.');
        this.cdr.detectChanges();
      }
    });
  }

  loadFilterHospitals() {
    this.superAdminService.filterHospitals('', 0, 1000).subscribe(res => {
      if (res.data) {
        this.filterHospitalsList = res.data.content;
        this.ticketHospitals = res.data.content; // Also use for modal
        this.cdr.detectChanges();
      }
    });
  }

  onFilterHospitalChange() {
    this.ticketFilters.specialtyId = null;
    this.ticketFilters.roomId = null;
    this.ticketFilters.doctorId = null;
    this.filterSpecialtiesList = [];
    this.filterRoomsList = [];
    this.filterDoctorsList = [];
    this.cdr.detectChanges();

    if (this.ticketFilters.hospitalId) {
      this.superAdminService.filterSpecialties(this.ticketFilters.hospitalId, 0, 1000).subscribe(res => {
        if (res.data) {
          this.filterSpecialtiesList = res.data.content;
          this.cdr.detectChanges();
        }
      });
    }
    this.loadTickets();
  }

  onFilterSpecialtyChange() {
    this.ticketFilters.roomId = null;
    this.ticketFilters.doctorId = null;
    this.filterRoomsList = [];
    this.filterDoctorsList = [];
    this.cdr.detectChanges();

    if (this.ticketFilters.specialtyId) {
      this.superAdminService.filterRooms(this.ticketFilters.specialtyId, 0, 1000).subscribe(res => {
        if (res.data) {
          this.filterRoomsList = res.data.content;
          this.cdr.detectChanges();
        }
      });
      // Filter doctors
      this.superAdminService.filterDoctorProfiles('', 0, 1000).subscribe(res => {
        if (res.data) {
          this.filterDoctorsList = res.data.content.filter(d => d.specialtyId === this.ticketFilters.specialtyId);
          this.cdr.detectChanges();
        }
      });
    }
    this.loadTickets();
  }

  onFilterRoomChange() { this.loadTickets(); }
  onFilterDoctorChange() { this.loadTickets(); }

  hasActiveFilters(): boolean {
    return !!(this.ticketFilters.hospitalId || this.ticketFilters.specialtyId ||
      this.ticketFilters.roomId || this.ticketFilters.doctorId);
  }

  clearFilters() {
    this.ticketFilters.hospitalId = null;
    this.ticketFilters.specialtyId = null;
    this.ticketFilters.roomId = null;
    this.ticketFilters.doctorId = null;
    this.filterSpecialtiesList = [];
    this.filterRoomsList = [];
    this.filterDoctorsList = [];
    this.cdr.detectChanges();
    this.loadTickets();
  }

  openModal(patientId?: number) {
    this.ticketForm = {
      hospitalId: null,
      specialtyId: null,
      doctorId: null,
      patientId: patientId ? +patientId : null,
      reason: '',
      priority: 'normal',
      notes: '',
      payerType: 'self_pay',
      status: 'waiting',
      estimatedTime: null,
      queueNumber: null,
      roomId: null
    };
    this.showModal = true;
  }

  closeModal() {
    this.showModal = false;
    // Clear query params to prevent modal from reopening
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {},
      replaceUrl: true
    });
  }

  onTicketHospitalChange(hospitalId?: number) {
    const hId = hospitalId || this.ticketForm.hospitalId;
    this.ticketSpecialties = [];
    this.ticketRooms = [];
    this.ticketDoctors = [];
    this.ticketForm.specialtyId = null;
    this.ticketForm.roomId = null;
    this.ticketForm.doctorId = null;
    this.cdr.detectChanges();

    if (hId) {
      this.superAdminService.filterSpecialties(hId, 0, 100).subscribe(res => {
        if (res.data) {
          this.ticketSpecialties = res.data.content;
          this.cdr.detectChanges();
        }
      });
    }
  }

  onTicketSpecialtyChange() {
    this.ticketRooms = [];
    this.ticketDoctors = [];
    this.ticketForm.roomId = null;
    this.ticketForm.doctorId = null;
    this.cdr.detectChanges();

    if (this.ticketForm.specialtyId) {
      this.superAdminService.filterRooms(this.ticketForm.specialtyId, 0, 100).subscribe(res => {
        if (res.data) {
          this.ticketRooms = res.data.content;
          this.cdr.detectChanges();
        }
      });
      this.superAdminService.filterDoctorProfiles('', 0, 100).subscribe(res => {
        if (res.data) {
          this.ticketDoctors = res.data.content.filter(d => d.specialtyId === this.ticketForm.specialtyId);
          this.cdr.detectChanges();
        }
      });
    }
  }

  createTicket() {
    const data = { ...this.ticketForm };
    if (data.estimatedTime) {
      data.estimatedTime = new Date(data.estimatedTime).toISOString();
    }

    this.superAdminService.createReceptionTicket(data).subscribe({
      next: () => {
        this.notificationService.showSuccess('Tạo phiếu khám thành công!');
        this.closeModal();
        this.loadTickets();
        // Clear query params to prevent modal from reopening
        this.router.navigate([], {
          relativeTo: this.route,
          queryParams: {},
          replaceUrl: true
        });
      },
      error: (err) => {
        this.notificationService.showError('Lỗi: ' + (err.error?.message || err.message));
      }
    });
  }

  getRoomTypeDisplay(type: string): string {
    const map: { [key: string]: string } = {
      'examination': 'Khám bệnh',
      'laboratory': 'Xét nghiệm',
      'surgery': 'Phẫu thuật',
      'ward': 'Nội trú'
    };
    return map[type] || type;
  }

  onPageChange(newPage: number) {
    this.page = newPage;
    this.loadTickets();
  }
}
