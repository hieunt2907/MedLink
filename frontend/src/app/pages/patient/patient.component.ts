import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule, LocationStrategy } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import {
    PatientService,
    AuthService,
    UserResponse,
    PatientProfileEntity,
    ReceptionTicketsResponse,
    PatientAllergy,
    PatientChronicDisease,
    Page
} from '../../services/api.service';

@Component({
    selector: 'app-patient',
    standalone: true,
    imports: [CommonModule, FormsModule],
    templateUrl: './patient.component.html',
    styleUrls: ['./patient.component.css']
})
export class PatientComponent implements OnInit {
    user: UserResponse | null = null;
    patientProfile: PatientProfileEntity | null = null;
    tickets: ReceptionTicketsResponse[] = [];
    allergies: PatientAllergy[] = [];
    chronicDiseases: PatientChronicDisease[] = [];

    // Filter for Tickets (Required by API)
    filters = {
        keyword: '',
        patientId: undefined as number | undefined
    };

    activeTab: 'overview' | 'medical-records' = 'overview';
    isSidebarCollapsed = false;

    isLoading = false;
    isLoadingTickets = false;
    error = '';

    // Pagination
    ticketsPage = 0;
    ticketsSize = 6;
    ticketsTotalPages = 0;

    allergiesPage = 0;
    allergiesSize = 5;
    allergiesTotalPages = 0;

    diseasesPage = 0;
    diseasesSize = 5;
    diseasesTotalPages = 0;

    // State for Profile Editing
    isEditingProfile = false;
    isCreatingProfile = false; // To distinguish between create and edit mode if needed, though they reuse the form
    profileForm: any = {};

    // State for Allergies
    isAddingAllergy = false;
    editingAllergyId: number | null = null;
    allergyForm: any = { allergyName: '', severity: 'mild', notes: '' };

    // State for Diseases
    isAddingDisease = false;
    editingDiseaseId: number | null = null;
    diseaseForm: any = { diseaseName: '', diagnosedDate: '', notes: '' };

    private patientService = inject(PatientService);
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

        const savedTab = localStorage.getItem('patientActiveTab');
        if (savedTab === 'overview' || savedTab === 'medical-records') {
            this.activeTab = savedTab;
        }
        this.loadProfile();
    }

    loadProfile() {
        this.isLoading = true;

        // 1. Get User Info
        this.patientService.getMe().subscribe({
            next: (res) => {
                this.user = res.data;

                // 2. Get Patient Profile
                this.patientService.getPatientProfile().subscribe({
                    next: (pRes) => {
                        this.patientProfile = pRes.data;

                        // Auto-fill filters
                        if (this.user?.id) {
                            this.filters.patientId = this.user.id;
                        }

                        this.isLoading = false;
                        this.cdr.detectChanges(); // Force update

                        // Load data for the initial active tab
                        this.loadTabContent();
                    },
                    error: (err) => {
                        console.error('Failed to load patient profile', err);
                        this.patientProfile = null; // Ensure null on error (e.g. 404)
                        this.isLoading = false;
                        this.cdr.detectChanges();
                    }
                });
            },
            error: (err) => {
                console.error('Failed to load user', err);
                this.error = 'Không thể tải thông tin người dùng. Vui lòng đăng nhập lại.';
                this.isLoading = false;
                this.cdr.detectChanges();

                if (err.status === 401 || err.status === 403) {
                    this.authService.logout();
                    this.router.navigate(['/login']);
                }
            }
        });
    }

    loadTabContent() {
        if (!this.user?.id) return;

        if (this.activeTab === 'overview') {
            this.loadTickets();
        } else if (this.activeTab === 'medical-records') {
            this.loadAllergies();
            this.loadChronicDiseases();
        }
    }

    loadTickets() {
        if (!this.user?.id) return;

        const params = {
            patientId: this.user.id,
            keyword: this.filters.keyword,
            page: this.ticketsPage,
            size: this.ticketsSize
        };

        this.isLoadingTickets = true;
        this.cdr.detectChanges(); // Immediate UI update
        this.patientService.getMyTickets(params).subscribe({
            next: (res) => {
                if (res.data) {
                    this.tickets = res.data.content || [];
                    this.ticketsTotalPages = res.data.totalPages || 0;
                } else {
                    this.tickets = [];
                    this.ticketsTotalPages = 0;
                }
                this.isLoadingTickets = false;
                this.cdr.detectChanges();
            },
            error: (err) => {
                console.error('Failed to load tickets', err);
                this.isLoadingTickets = false;
            }
        });
    }

    loadAllergies() {
        const params = { page: this.allergiesPage, size: this.allergiesSize };
        this.patientService.getMyAllergies(params).subscribe(res => {
            this.allergies = res.data?.content || [];
            this.allergiesTotalPages = res.data?.totalPages || 0;
            this.cdr.detectChanges();
        });
    }

    loadChronicDiseases() {
        const params = { page: this.diseasesPage, size: this.diseasesSize };
        this.patientService.getMyChronicDiseases(params).subscribe(res => {
            this.chronicDiseases = res.data?.content || [];
            this.diseasesTotalPages = res.data?.totalPages || 0;
            this.cdr.detectChanges();
        });
    }

    onTicketPageChange(page: number) {
        this.ticketsPage = page;
        this.loadTickets();
    }

    onAllergyPageChange(page: number) {
        this.allergiesPage = page;
        this.loadAllergies();
    }

    onDiseasePageChange(page: number) {
        this.diseasesPage = page;
        this.loadChronicDiseases();
    }

    toggleSidebar() {
        this.isSidebarCollapsed = !this.isSidebarCollapsed;
    }

    setTab(tab: 'overview' | 'medical-records') {
        this.activeTab = tab;
        localStorage.setItem('patientActiveTab', tab);
        this.loadTabContent(); // Load data when switching tabs
    }

    logout() {
        this.authService.logout();
        localStorage.removeItem('patientActiveTab');
        this.router.navigate(['/login']);
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

    getGenderDisplay(gender: string): string {
        const map: { [key: string]: string } = {
            'male': 'Nam',
            'female': 'Nữ',
            'other': 'Khác'
        };
        return map[gender] || gender;
    }

    getPriorityDisplay(priority: string): string {
        const map: { [key: string]: string } = {
            'normal': 'Thường',
            'urgent': 'Khẩn cấp',
            'emergency': 'Cấp cứu'
        };
        return map[priority?.toLowerCase()] || priority;
    }

    getStatusDisplay(status: string): string {
        const map: { [key: string]: string } = {
            'waiting': 'Đang chờ',
            'called': 'Đã gọi',
            'in_progress': 'Đang khám',
            'completed': 'Hoàn thành',
            'skipped': 'Đã bỏ qua',
            'cancelled': 'Đã hủy',
            'pending': 'Chờ xử lý'
        };
        return map[status?.toLowerCase()] || status;
    }

    getPayerTypeDisplay(type: string): string {
        const map: { [key: string]: string } = {
            'self_pay': 'Dịch vụ',
            'insurance': 'Bảo hiểm'
        };
        return map[type?.toLowerCase()] || type;
    }

    getSeverityDisplay(severity: string): string {
        const map: { [key: string]: string } = {
            'mild': 'Nhẹ',
            'moderate': 'Trung bình',
            'severe': 'Nặng'
        };
        return map[severity?.toLowerCase()] || severity;
    }

    // Actions
    createProfile() {
        this.isCreatingProfile = true;
        this.profileForm = {
            patientCode: '',
            emergencyContactName: '',
            emergencyContactPhone: '',
            bloodType: ''
        };
        this.cdr.detectChanges();
        // Scroll to form if needed?
    }

    editProfile() {
        if (!this.patientProfile) return;
        this.isEditingProfile = true;
        this.isCreatingProfile = false;
        this.profileForm = { ...this.patientProfile }; // Copy fields
        this.cdr.detectChanges();
    }

    cancelEditProfile() {
        this.isEditingProfile = false;
        this.isCreatingProfile = false;
        this.profileForm = {};
    }

    saveProfile() {
        if (!this.isEditingProfile && !this.isCreatingProfile) return;

        const data: any = {
            patientCode: this.profileForm.patientCode,
            emergencyContactName: this.profileForm.emergencyContactName,
            emergencyContactPhone: this.profileForm.emergencyContactPhone,
            bloodType: this.profileForm.bloodType
        };

        if (this.isCreatingProfile) {
            this.patientService.createPatientProfile(data).subscribe({
                next: (res) => {
                    alert('Tạo hồ sơ thành công!');
                    this.loadProfile();
                    this.cancelEditProfile();
                },
                error: (err) => {
                    console.error(err);
                    alert('Có lỗi xảy ra: ' + (err.error?.message || err.message));
                }
            });
        } else {
            this.patientService.updatePatientProfile(data).subscribe({
                next: (res) => {
                    alert('Cập nhật hồ sơ thành công!');
                    this.loadProfile();
                    this.cancelEditProfile();
                },
                error: (err) => {
                    console.error(err);
                    alert('Có lỗi xảy ra: ' + err.message);
                }
            });
        }
    }

    deleteProfile() {
        alert('Chức năng xóa hồ sơ hiện không được hỗ trợ bởi hệ thống.');
    }

    // Allergies
    addAllergy() {
        this.isAddingAllergy = true;
        this.allergyForm = { allergyName: '', severity: 'mild', notes: '' };
        this.cdr.detectChanges();
    }

    cancelAddAllergy() {
        this.isAddingAllergy = false;
        this.allergyForm = {};
    }

    saveNewAllergy() {
        const data: any = {
            allergyName: this.allergyForm.allergyName,
            severity: this.allergyForm.severity,
            notes: this.allergyForm.notes
        };
        this.patientService.createAllergy(data).subscribe({
            next: () => {
                this.loadAllergies();
                this.cancelAddAllergy();
            },
            error: (err) => alert('Lỗi: ' + err.message)
        });
    }

    editAllergy(allergy: any) {
        this.editingAllergyId = allergy.id;
        this.allergyForm = { ...allergy }; // Copy data
        this.cdr.detectChanges();
    }

    cancelEditAllergy() {
        this.editingAllergyId = null;
        this.allergyForm = {};
    }

    saveEditAllergy() {
        if (this.editingAllergyId === null) return;
        const data: any = {
            allergyName: this.allergyForm.allergyName,
            severity: this.allergyForm.severity,
            notes: this.allergyForm.notes
        };
        this.patientService.updateAllergy(this.editingAllergyId, data).subscribe({
            next: () => {
                this.loadAllergies();
                this.cancelEditAllergy();
            },
            error: (err) => alert('Lỗi: ' + err.message)
        });
    }

    deleteAllergy(allergy: any) {
        if (!confirm('Bạn có chắc chắn muốn xóa dị ứng này không?')) return;
        this.patientService.deleteAllergy(allergy.id).subscribe({
            next: () => {
                this.loadAllergies();
            },
            error: (err) => alert('Lỗi: ' + err.message)
        });
    }

    // Diseases
    addDisease() {
        this.isAddingDisease = true;
        this.diseaseForm = { diseaseName: '', diagnosedDate: '', notes: '' };
        this.cdr.detectChanges();
    }

    cancelAddDisease() {
        this.isAddingDisease = false;
        this.diseaseForm = {};
    }

    saveNewDisease() {
        const data: any = {
            diseaseName: this.diseaseForm.diseaseName,
            diagnosedDate: this.diseaseForm.diagnosedDate,
            notes: this.diseaseForm.notes
        };
        this.patientService.createChronicDisease(data).subscribe({
            next: () => {
                this.loadChronicDiseases();
                this.cancelAddDisease();
            },
            error: (err) => alert('Lỗi: ' + err.message)
        });
    }

    editDisease(disease: any) {
        this.editingDiseaseId = disease.id;
        this.diseaseForm = { ...disease };
        this.cdr.detectChanges();
    }

    cancelEditDisease() {
        this.editingDiseaseId = null;
        this.diseaseForm = {};
    }

    saveEditDisease() {
        if (this.editingDiseaseId === null) return;
        const data: any = {
            diseaseName: this.diseaseForm.diseaseName,
            diagnosedDate: this.diseaseForm.diagnosedDate,
            notes: this.diseaseForm.notes
        };
        this.patientService.updateChronicDisease(this.editingDiseaseId, data).subscribe({
            next: () => {
                this.loadChronicDiseases();
                this.cancelEditDisease();
            },
            error: (err) => alert('Lỗi: ' + err.message)
        });
    }

    deleteDisease(disease: any) {
        if (!confirm('Bạn có chắc chắn muốn xóa bệnh mãn tính này không?')) return;
        this.patientService.deleteChronicDisease(disease.id).subscribe({
            next: () => {
                this.loadChronicDiseases();
            },
            error: (err) => alert('Lỗi: ' + err.message)
        });
    }
}
