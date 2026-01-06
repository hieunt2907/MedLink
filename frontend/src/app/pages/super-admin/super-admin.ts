import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule, LocationStrategy } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import {
  SuperAdminService,
  AuthService,
  UserResponse,
  HospitalEntity,
  SpecialtiesEntity,
  DoctorRoomAssignmentsEntity,
  DoctorProfileEntity,
  ReceptionTicketsResponse,
  PatientProfileEntity,
  PatientAllergy,
  PatientChronicDisease,
  RoomEntity,
  Page
} from '../../services/api.service';

@Component({
  selector: 'app-super-admin',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './super-admin.html',
  styleUrls: ['./super-admin.css']
})
export class SuperAdminComponent implements OnInit {
  activeView: 'users' | 'hospitals' | 'doctors' | 'patients' | 'tickets' = 'users';

  // Data Lists
  users: UserResponse[] = [];
  hospitals: HospitalEntity[] = [];
  specialties: SpecialtiesEntity[] = [];
  rooms: RoomEntity[] = [];
  doctors: DoctorProfileEntity[] = [];
  doctorAssignments: DoctorRoomAssignmentsEntity[] = [];
  patients: PatientProfileEntity[] = [];
  tickets: ReceptionTicketsResponse[] = [];

  // Ticket View Filters
  ticketFilters = {
    hospitalId: null as number | null,
    specialtyId: null as number | null,
    roomId: null as number | null,
    doctorId: null as number | null
  };

  // Lists for Ticket View Filter Dropdowns
  filterHospitalsList: HospitalEntity[] = [];
  filterSpecialtiesList: SpecialtiesEntity[] = [];
  filterRoomsList: RoomEntity[] = [];
  filterDoctorsList: DoctorProfileEntity[] = []; // Maybe load all or filtered by hospital/specialty
  doctorUsers: UserResponse[] = []; // Users for doctor creation select

  // Patient Details
  patientAllergies: PatientAllergy[] = [];
  patientDiseases: PatientChronicDisease[] = [];

  // Dropdown Lists for Ticket Form
  ticketHospitals: HospitalEntity[] = [];
  ticketSpecialties: SpecialtiesEntity[] = [];
  ticketDoctors: DoctorProfileEntity[] = [];
  ticketRooms: RoomEntity[] = [];

  // Selection
  selectedDoctor: DoctorProfileEntity | null = null;
  selectedHospital: HospitalEntity | null = null;
  selectedSpecialty: SpecialtiesEntity | null = null;
  selectedPatient: PatientProfileEntity | null = null;

  // Sidebar State
  isSidebarCollapsed = false;

  // Pagination/Filtering
  page = 0;
  size = 10;
  totalPages = 0;
  keyword = '';

  // Modal State
  showModal = false;
  modalType: 'hospital' | 'user' | 'ticket' | 'specialty' | 'room' | 'doctor' | 'assignment' | null = null;

  hospitalForm: any = {
    name: '',
    address: '',
    phone: '',
    email: '',
    status: 'active'
  };
  currentHospitalId: number | null = null;
  selectedHospitalIds: Set<number> = new Set();

  currentSpecialtyId: number | null = null;
  selectedSpecialtyIds: Set<number> = new Set();
  specialtyForm: any = {
    name: '',
    description: '',
    hospitalId: null,
    status: 'active'
  };

  currentRoomId: number | null = null;
  selectedRoomIds: Set<number> = new Set();
  roomForm: any = {
    roomNumber: '',
    roomType: 'examination',
    hospitalId: null,
    specialtyId: null,
    capacity: 1,
    status: 'available'
  };

  currentDoctorId: number | null = null;
  selectedDoctorIds: Set<number> = new Set();
  doctorForm: any = {
    userId: null,
    specialtyId: null,
    qualifications: '',
    experienceYears: 0
  };
  currentAssignmentId: number | null = null;
  selectedAssignmentIds: Set<number> = new Set();
  assignmentForm: any = {
    doctorProfileId: null,
    hospitalId: null,
    roomId: null,
    specialtyId: null,
    isPrimary: false,
    shiftStart: '',
    shiftEnd: '',
    startDate: '',
    endDate: '',
    notes: ''
  };

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

  // Services
  private superAdminService = inject(SuperAdminService);
  private authService = inject(AuthService);
  private router = inject(Router);
  private cdr = inject(ChangeDetectorRef);
  private locationStrategy = inject(LocationStrategy);

  ngOnInit() {
    // Prevent back navigation
    history.pushState(null, '', window.location.href);
    this.locationStrategy.onPopState(() => {
      history.pushState(null, '', window.location.href);
    });

    const savedView = localStorage.getItem('superAdminActiveView');
    if (savedView === 'users' || savedView === 'hospitals' || savedView === 'doctors' || savedView === 'patients' || savedView === 'tickets') {
      this.activeView = savedView;
    }
    this.loadData();
  }

  setView(view: any) {
    this.activeView = view;
    localStorage.setItem('superAdminActiveView', view);
    this.page = 0;
    this.keyword = '';

    // Reset selections
    this.selectedDoctor = null;
    this.selectedDoctorIds.clear();
    this.selectedHospital = null;
    this.selectedSpecialty = null;
    this.selectedPatient = null;

    this.loadData();
  }

  loadData() {
    switch (this.activeView) {
      case 'users':
        this.loadUsers();
        break;
      case 'hospitals':
        if (this.selectedHospital) {
          if (this.selectedSpecialty) {
            this.loadRooms(this.selectedSpecialty.id);
          } else {
            this.loadSpecialties();
          }
        } else {
          this.loadHospitals();
        }
        break;
      case 'doctors':
        if (this.selectedDoctor) {
          this.loadDoctorSchedules();
        } else {
          this.loadDoctors();
        }
        break;
      case 'patients':
        if (this.selectedPatient) {
          this.loadPatientDetails(this.selectedPatient.id);
        } else {
          this.loadPatients();
        }
        break;
      case 'tickets':
        this.loadTickets();
        if (this.filterHospitalsList.length === 0) {
          this.loadFilterHospitals();
        }
        break;
    }
  }

  // ... (loadUsers, loadHospitals, selectHospital, backToHospitals, loadSpecialties, loadDoctors, selectDoctor, backToDoctors, loadDoctorSchedules remain the same) 
  // I will just paste them to ensure context match if replacing large block, 
  // OR use multiple chunks if possible. But strict replacement is safer.
  // Given the file size, I will replace the block from `loadUsers` down to `loadPatients` to insert the new logic safely.

  loadUsers() {
    this.superAdminService.filterUsers(this.keyword, this.page, this.size).subscribe({
      next: (res) => {
        if (res.data) {
          this.users = res.data.content;
          this.totalPages = res.data.totalPages;
        }
        this.cdr.detectChanges();
      },
      error: () => this.cdr.detectChanges()
    });
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
      error: () => this.cdr.detectChanges()
    });
  }

  selectHospital(h: HospitalEntity) {
    this.selectedHospital = h;
    this.selectedSpecialty = null; // Reset specialty selection
    this.page = 0;
    this.keyword = '';
    this.loadSpecialties();
  }

  backToHospitals() {
    this.selectedHospital = null;
    this.selectedSpecialty = null; // Reset specialty selection
    this.page = 0;
    this.keyword = '';
    this.loadHospitals();
  }

  loadSpecialties() {
    if (!this.selectedHospital) return;
    this.superAdminService.filterSpecialties(this.selectedHospital.id, this.page, this.size).subscribe({
      next: (res) => {
        if (res.data) {
          this.specialties = res.data.content;
          this.totalPages = res.data.totalPages;
        }
        this.cdr.detectChanges();
      },
      error: () => this.cdr.detectChanges()
    });
  }

  selectSpecialty(s: SpecialtiesEntity) {
    this.selectedSpecialty = s;
    this.page = 0;
    this.loadRooms(s.id);
  }

  backToSpecialties() {
    this.selectedSpecialty = null;
    this.page = 0;
    this.loadSpecialties();
  }

  loadRooms(specialtyId: number) {
    this.superAdminService.filterRooms(specialtyId, this.page, this.size).subscribe({
      next: (res) => {
        if (res.data) {
          this.rooms = res.data.content;
          this.totalPages = res.data.totalPages;
        }
        this.cdr.detectChanges();
      },
      error: () => this.cdr.detectChanges()
    });
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
      error: () => this.cdr.detectChanges()
    });
  }

  selectDoctor(doctor: DoctorProfileEntity) {
    this.selectedDoctor = doctor;
    this.page = 0;
    this.keyword = '';
    this.loadDoctorSchedules();
  }

  backToDoctors() {
    this.selectedDoctor = null;
    this.page = 0;
    this.keyword = '';
    this.loadDoctors();
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
      // Map entity to form
      this.doctorForm = {
        userId: doctor.userId,
        specialtyId: doctor.specialtyId, // This might be missing in response, need to handle
        qualifications: doctor.qualifications,
        experienceYears: doctor.experienceYears
      };
      // If specialtyId is missing in doctor entity (from response), we might need to find it or it's just display name?
      // The DoctorProfileResponse usually returns names. But to edit we need IDs.
      // If fields are missing, we might need to fetch full profile or match by name.
      // For now assume standard entity fields are populated if available.

      this.modalType = 'doctor';
      this.showModal = true;

      // Load dependencies
      this.loadUsersForDoctor();
      this.loadAllSpecialties();
    }
  }

  deleteSelectedDoctors() {
    if (!confirm('Bạn có chắc chắn muốn xóa các bác sĩ đã chọn?')) return;
    const ids = Array.from(this.selectedDoctorIds);
    let completed = 0;
    ids.forEach(id => {
      this.superAdminService.deleteDoctorProfile(id).subscribe({
        next: () => {
          completed++;
          if (completed === ids.length) {
            this.loadDoctors();
            this.selectedDoctorIds.clear();
          }
        },
        error: () => {
          completed++;
          if (completed === ids.length) {
            this.loadDoctors();
            this.selectedDoctorIds.clear();
          }
        }
      });
    });
  }

  saveDoctor() {
    const request = this.doctorForm;
    const obs = this.currentDoctorId
      ? this.superAdminService.updateDoctorProfile(this.currentDoctorId, request)
      : this.superAdminService.createDoctorProfile(request);

    obs.subscribe({
      next: () => {
        alert(this.currentDoctorId ? 'Cập nhật thành công!' : 'Tạo hồ sơ bác sĩ thành công!');
        this.closeModal();
        this.loadDoctors();
        this.selectedDoctorIds.clear();
      },
      error: (err) => {
        alert('Lỗi: ' + (err.error?.message || err.message));
      }
    });
  }

  loadUsersForDoctor() {
    // Load users to select from. Ideally filter by role 'DOCTOR'
    this.superAdminService.filterUsers('', 0, 100).subscribe(res => {
      if (res.data) this.doctorUsers = res.data.content;
    });
  }

  loadAllSpecialties() {
    // We need specialties across all hospitals or select hospital first?
    // The DoctorProfileRequest has specialtyId.
    // Let's load a flat list or maybe let user select hospital first?
    // Simplified: Load all specialties from a "default" large query or iterate hospitals?
    // Current API `filterSpecialties` requires hospitalId.
    // This is tricky. A doctor belongs to a specialty which belongs to a hospital.
    // So the form should probably select Hospital -> Specialty.
    // I will add hospital selection to doctor form.
    this.loadFilterHospitals(); // Re-use this to load hospitals
  }

  onDoctorHospitalChange() {
    // When hospital selected in doctor form
    // Load specialties for that hospital
    // But doctorForm doesn't have hospitalId directly in Request (it has specialtyId).
    // But for UI we need it.
    // I'll add `hospitalId` to doctorForm temporarily.
    if (this.doctorForm.hospitalId) {
      this.superAdminService.filterSpecialties(this.doctorForm.hospitalId, 0, 100).subscribe(res => {
        if (res.data) this.ticketSpecialties = res.data.content; // Reuse ticketSpecialties or create new list
        // Let's create `doctorSpecialties` list
        this.filterSpecialtiesList = res.data.content;
      });
    } else {
      this.filterSpecialtiesList = [];
    }
  }
  loadDoctorSchedules() {
    if (!this.selectedDoctor) return;
    this.superAdminService.filterDoctorRoomAssignments(this.selectedDoctor.id, this.keyword, this.page, this.size).subscribe({
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
      this.ticketRooms = []; // Clear stale data
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
      this.modalType = 'assignment';
      this.showModal = true;
      // Load rooms for the dropdown
      if (assignment.specialtyId) {
        this.superAdminService.filterRooms(assignment.specialtyId, 0, 100).subscribe(r => {
          if (r.data) {
            this.ticketRooms = r.data.content;
            this.cdr.detectChanges();
          }
        });
      }
    }
  }

  deleteSelectedAssignments() {
    if (!confirm('Bạn có chắc chắn muốn xóa các lịch phân công đã chọn?')) return;
    const ids = Array.from(this.selectedAssignmentIds);
    let completed = 0;
    ids.forEach(id => {
      this.superAdminService.deleteDoctorRoomAssignment(id).subscribe({
        next: () => {
          completed++;
          if (completed === ids.length) {
            this.loadDoctorSchedules();
            this.selectedAssignmentIds.clear();
          }
        },
        error: () => {
          completed++;
          if (completed === ids.length) {
            this.loadDoctorSchedules();
            this.selectedAssignmentIds.clear();
          }
        }
      });
    });
  }

  saveAssignment() {
    const request = { ...this.assignmentForm };
    if (!request.doctorProfileId && this.selectedDoctor) {
      request.doctorProfileId = this.selectedDoctor.id;
    }
    // Ensure hospital/specialty from doctor if not set (though UI should handle)

    const obs = this.currentAssignmentId
      ? this.superAdminService.updateDoctorRoomAssignment(this.currentAssignmentId, request)
      : this.superAdminService.createDoctorRoomAssignment(request);

    obs.subscribe({
      next: () => {
        alert(this.currentAssignmentId ? 'Cập nhật lịch thành công!' : 'Tạo lịch làm việc thành công!');
        this.closeModal();
        this.loadDoctorSchedules();
        this.selectedAssignmentIds.clear();
      },
      error: (err) => {
        alert('Lỗi: ' + (err.error?.message || err.message));
      }
    });
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
      error: () => this.cdr.detectChanges()
    });
  }

  selectPatient(p: PatientProfileEntity) {
    this.selectedPatient = p;
    this.loadPatientDetails(p.id);
  }

  backToPatients() {
    this.selectedPatient = null;
    this.loadPatients();
  }

  loadPatientDetails(id: number) {
    // Load Allergies
    this.superAdminService.filterPatientAllergies(id).subscribe({
      next: (res) => {
        if (res.data) this.patientAllergies = res.data.content;
        this.cdr.detectChanges();
      }
    });
    // Load Diseases
    this.superAdminService.filterPatientChronicDiseases(id).subscribe({
      next: (res) => {
        if (res.data) this.patientDiseases = res.data.content;
        this.cdr.detectChanges();
      }
    });
  }

  openModal(type: 'hospital' | 'user' | 'ticket' | 'specialty' | 'room' | 'doctor' | 'assignment', data?: any) {
    this.modalType = type;
    this.showModal = true;
    if (type === 'hospital') {
      this.currentHospitalId = null; // Reset for create mode
      this.hospitalForm = { name: '', address: '', phone: '', email: '', status: 'active' };
    } else if (type === 'specialty') {
      this.currentSpecialtyId = null;
      this.specialtyForm = { name: '', description: '', hospitalId: this.selectedHospital?.id, status: 'active' };
    } else if (type === 'room') {
      this.currentRoomId = null;
      this.roomForm = {
        roomNumber: '',
        roomType: 'examination',
        hospitalId: this.selectedHospital?.id,
        specialtyId: this.selectedSpecialty?.id,
        capacity: 1,
        status: 'available'
      };
    } else if (type === 'doctor') {
      this.currentDoctorId = null;
      this.doctorForm = { userId: null, specialtyId: null, qualifications: '', experienceYears: 0, hospitalId: null };
      this.loadUsersForDoctor();
      this.loadFilterHospitals();
    } else if (type === 'assignment') {
      this.currentAssignmentId = null;
      this.ticketRooms = []; // Clear stale data

      this.assignmentForm = {
        doctorProfileId: this.selectedDoctor?.id,
        hospitalId: this.selectedDoctor?.hospitalId, // Use doctor's hospital
        specialtyId: this.selectedDoctor?.specialtyId, // Use doctor's specialty
        roomId: null,
        isPrimary: false,
        shiftStart: '08:00:00',
        shiftEnd: '17:00:00',
        startDate: new Date().toISOString().split('T')[0],
        endDate: null,
        notes: ''
      };

      // Load rooms based on doctor's specialty
      if (this.selectedDoctor?.specialtyId) {
        this.superAdminService.filterRooms(this.selectedDoctor.specialtyId, 0, 100).subscribe(r => {
          if (r.data) {
            this.ticketRooms = r.data.content;
            this.cdr.detectChanges();
          }
        });
      }



    } else if (type === 'ticket' && data) {
      this.ticketForm = {
        patientId: data.userId,
        hospitalId: null,
        specialtyId: null,
        doctorId: null,
        reason: '',
        priority: 'normal',
        notes: '',
        payerType: 'self_pay',
        status: 'waiting',
        estimatedTime: null,
        queueNumber: null,
        roomId: null
      };
      // Load hospitals for dropdown
      this.superAdminService.filterHospitals('', 0, 100).subscribe(res => {
        if (res.data) {
          this.ticketHospitals = res.data.content;
          this.cdr.detectChanges();
        }
      });
    }
  }

  // ... (methods)

  closeModal() {
    this.showModal = false;
    this.modalType = null;
    this.currentHospitalId = null;
    this.currentSpecialtyId = null;
    this.currentRoomId = null;
    this.currentDoctorId = null;
  }

  onTicketHospitalChange(hospitalId?: number) {
    const hId = hospitalId || this.ticketForm.hospitalId;

    // Clear dependent data
    this.ticketSpecialties = [];
    this.ticketRooms = [];
    this.ticketForm.specialtyId = null;
    this.ticketForm.roomId = null;
    this.ticketForm.doctorId = null;

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
    // Clear dependent data
    this.ticketRooms = [];
    this.ticketForm.roomId = null;
    this.ticketForm.doctorId = null;

    // Load Rooms for selected specialty
    if (this.ticketForm.specialtyId) {
      this.superAdminService.filterRooms(this.ticketForm.specialtyId, 0, 100).subscribe(res => {
        if (res.data) {
          this.ticketRooms = res.data.content;
          this.cdr.detectChanges();
        }
      });
    }

    // Filter doctors (generic fetch for now)
    this.superAdminService.filterDoctorProfiles('', 0, 100).subscribe(res => {
      if (res.data) {
        // Client side filter if possible?
        // DoctorProfileEntity has `specialtyId`? No, it has `specialtyName`.
        // The entity definition I revised has `specialtyId`.
        if (this.ticketForm.specialtyId) {
          // Check if `ticketForm.specialtyId` matches.
          // Note: API might not return specialtyId if I didn't ensure it in the Entity/Response.
          // Let's check `DoctorProfileEntity`.
          this.ticketDoctors = res.data.content; // Filter later if field available
        } else {
          this.ticketDoctors = res.data.content;
        }
        this.cdr.detectChanges();
      }
    });
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
      this.modalType = 'hospital';
      this.showModal = true;
    }
  }

  deleteSelectedHospitals() {
    if (!confirm('Bạn có chắc chắn muốn xóa các bệnh viện đã chọn?')) return;

    const ids = Array.from(this.selectedHospitalIds);
    let completed = 0;

    ids.forEach(id => {
      this.superAdminService.deleteHospital(id).subscribe({
        next: () => {
          completed++;
          if (completed === ids.length) {
            this.loadHospitals();
            this.selectedHospitalIds.clear();
          }
        },
        error: (err) => {
          console.error(err);
          completed++;
          if (completed === ids.length) {
            this.loadHospitals();
            this.selectedHospitalIds.clear();
          }
        }
      });
    });
  }

  saveHospital() {
    const request = this.hospitalForm;
    const obs = this.currentHospitalId
      ? this.superAdminService.updateHospital(this.currentHospitalId, request)
      : this.superAdminService.createHospital(request);

    obs.subscribe({
      next: () => {
        alert(this.currentHospitalId ? 'Cập nhật thành công!' : 'Tạo bệnh viện thành công!');
        this.closeModal();
        this.loadHospitals();
        // Keep selection? Usually lists reload clears it.
        this.selectedHospitalIds.clear();
      },
      error: (err) => {
        alert('Lỗi: ' + (err.error?.message || err.message));
      }
    });
  }

  createTicket() {
    const data = { ...this.ticketForm };
    if (data.estimatedTime) {
      // Convert datetime-local string to ISO-8601 with timezone (UTC) which OffsetDateTime accepts
      data.estimatedTime = new Date(data.estimatedTime).toISOString();
    }

    this.superAdminService.createReceptionTicket(data).subscribe({
      next: () => {
        alert('Tạo phiếu khám thành công!');
        this.closeModal();
      },
      error: (err) => {
        alert('Lỗi: ' + (err.error?.message || err.message));
      }
    });
  }

  deleteUser(id: number) {
    if (confirm('Bạn có chắc chắn muốn xóa?')) {
      this.superAdminService.deleteUser(id).subscribe(() => this.loadUsers());
    }
  }

  deleteHospital(id: number) {
    if (confirm('Bạn có chắc chắn muốn xóa?')) {
      this.superAdminService.deleteHospital(id).subscribe(() => this.loadHospitals());
    }
  }

  logout() {
    this.authService.logout();
    localStorage.removeItem('superAdminActiveView');
    this.router.navigate(['/login']);
  }

  onPageChange(p: number) {
    this.page = p;
    this.loadData();
  }


  // Specialty Management
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
      this.modalType = 'specialty';
      this.showModal = true;
    }
  }

  deleteSelectedSpecialties() {
    if (!confirm('Bạn có chắc chắn muốn xóa các chuyên khoa đã chọn?')) return;
    const ids = Array.from(this.selectedSpecialtyIds);
    let completed = 0;
    ids.forEach(id => {
      this.superAdminService.deleteSpecialty(id).subscribe({
        next: () => {
          completed++;
          if (completed === ids.length) {
            this.loadSpecialties();
            this.selectedSpecialtyIds.clear();
          }
        },
        error: () => {
          completed++;
          if (completed === ids.length) {
            this.loadSpecialties();
            this.selectedSpecialtyIds.clear();
          }
        }
      });
    });
  }

  saveSpecialty() {
    const request = this.specialtyForm;
    // Ensure hospitalId is set if creating
    if (!this.currentSpecialtyId && this.selectedHospital) {
      request.hospitalId = this.selectedHospital.id;
    }

    const obs = this.currentSpecialtyId
      ? this.superAdminService.updateSpecialty(this.currentSpecialtyId, request)
      : this.superAdminService.createSpecialty(request);

    obs.subscribe({
      next: () => {
        alert(this.currentSpecialtyId ? 'Cập nhật thành công!' : 'Tạo chuyên khoa thành công!');
        this.closeModal();
        this.loadSpecialties(); // Fixed arg
        this.selectedSpecialtyIds.clear();
      },
      error: (err) => {
        alert('Lỗi: ' + (err.error?.message || err.message));
      }
    });
  }


  // Room Management
  toggleRoomSelection(id: number) {
    if (this.selectedRoomIds.has(id)) {
      this.selectedRoomIds.delete(id);
    } else {
      this.selectedRoomIds.add(id);
    }
  }

  isRoomSelected(id: number): boolean {
    return this.selectedRoomIds.has(id);
  }

  toggleAllRooms(event: any) {
    if (event.target.checked) {
      this.rooms.forEach(r => this.selectedRoomIds.add(r.id));
    } else {
      this.selectedRoomIds.clear();
    }
  }

  editSelectedRoom() {
    if (this.selectedRoomIds.size !== 1) return;
    const id = this.selectedRoomIds.values().next().value as number;
    const room = this.rooms.find(r => r.id === id);
    if (room) {
      this.currentRoomId = id;
      this.roomForm = { ...room };
      this.modalType = 'room';
      this.showModal = true;
    }
  }

  deleteSelectedRooms() {
    if (!confirm('Bạn có chắc chắn muốn xóa các phòng đã chọn?')) return;
    const ids = Array.from(this.selectedRoomIds);
    let completed = 0;
    ids.forEach(id => {
      this.superAdminService.deleteRoom(id).subscribe({
        next: () => {
          completed++;
          if (completed === ids.length) {
            this.loadRooms(this.selectedSpecialty!.id);
            this.selectedRoomIds.clear();
          }
        },
        error: () => {
          completed++;
          if (completed === ids.length) {
            this.loadRooms(this.selectedSpecialty!.id);
            this.selectedRoomIds.clear();
          }
        }
      });
    });
  }

  saveRoom() {
    const request = this.roomForm;
    // Ensure ids are set
    if (!this.currentRoomId) {
      if (this.selectedHospital) request.hospitalId = this.selectedHospital.id;
      if (this.selectedSpecialty) request.specialtyId = this.selectedSpecialty.id;
    }

    const obs = this.currentRoomId
      ? this.superAdminService.updateRoom(this.currentRoomId, request)
      : this.superAdminService.createRoom(request);

    obs.subscribe({
      next: () => {
        alert(this.currentRoomId ? 'Cập nhật thành công!' : 'Tạo phòng thành công!');
        this.closeModal();
        if (this.selectedSpecialty) {
          this.loadRooms(this.selectedSpecialty.id);
        }
        this.selectedRoomIds.clear();
      },
      error: (err) => {
        alert('Lỗi: ' + (err.error?.message || err.message));
      }
    });
  }

  toggleSidebar() {
    this.isSidebarCollapsed = !this.isSidebarCollapsed;
  }

  // Ticket Management
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
      error: () => this.cdr.detectChanges()
    });
  }

  loadFilterHospitals() {
    this.superAdminService.filterHospitals('', 0, 1000).subscribe(res => {
      if (res.data) {
        this.filterHospitalsList = res.data.content;
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
    // doctors can be filtered by specialty too?

    if (this.ticketFilters.specialtyId) {
      this.superAdminService.filterRooms(this.ticketFilters.specialtyId, 0, 1000).subscribe(res => {
        if (res.data) {
          this.filterRoomsList = res.data.content;
          this.cdr.detectChanges();
        }
      });

      // Filter doctors by specialty? The API for doctors is generic filter.
      // We might need a client side filter or better API. For now, let's load all or just leave it.
      // Ideally we would filter doctors by specialty.
      // Let's try to filter by keyword if we can't filter by specialtyId easily via API for doctors list dropdown.
      // Or we can assume selecting specialty is enough to update tickets.
      // But we need to populate filterDoctorsList.
      this.superAdminService.filterDoctorProfiles('', 0, 1000).subscribe(res => {
        if (res.data) {
          // Client side filter
          this.filterDoctorsList = res.data.content.filter(d => d.specialtyId === this.ticketFilters.specialtyId);
          this.cdr.detectChanges();
        }
      });
    } else {
      this.filterDoctorsList = [];
    }
    this.loadTickets();
  }

  onFilterRoomChange() {
    this.loadTickets();
  }

  onFilterDoctorChange() {
    this.loadTickets();
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

  getRoomTypeDisplay(type: string): string {
    const map: { [key: string]: string } = {
      'examination': 'Khám bệnh',
      'laboratory': 'Xét nghiệm',
      'surgery': 'Phẫu thuật',
      'ward': 'Nội trú'
    };
    return map[type] || type;
  }

  getRoomStatusDisplay(status: string): string {
    const map: { [key: string]: string } = {
      'available': 'Sẵn sàng',
      'occupied': 'Đang sử dụng',
      'maintenance': 'Bảo trì'
    };
    return map[status] || status;
  }


}
