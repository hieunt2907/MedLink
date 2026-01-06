import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PatientService, PatientProfileEntity, PatientAllergy, PatientChronicDisease } from '../../../services/api.service';
import { NotificationService } from '../../../services/notification.service';

@Component({
    selector: 'app-patient-records',
    standalone: true,
    imports: [CommonModule, FormsModule],
    templateUrl: './records.component.html',
    styleUrls: ['./records.component.css']
})
export class PatientRecordsComponent implements OnInit {
    patientProfile: PatientProfileEntity | null = null;
    allergies: PatientAllergy[] = [];
    chronicDiseases: PatientChronicDisease[] = [];

    // Profile State
    isEditingProfile = false;
    isCreatingProfile = false;
    profileForm: any = {};

    // Allergy State
    isAddingAllergy = false;
    editingAllergyId: number | null = null;
    allergyForm: any = { allergyName: '', severity: 'mild', notes: '' };
    allergiesPage = 0;
    allergiesSize = 5;
    allergiesTotalPages = 0;

    // Disease State
    isAddingDisease = false;
    editingDiseaseId: number | null = null;
    diseaseForm: any = { diseaseName: '', diagnosedDate: '', notes: '' };
    diseasesPage = 0;
    diseasesSize = 5;
    diseasesTotalPages = 0;

    private patientService = inject(PatientService);
    private notificationService = inject(NotificationService);
    private cdr = inject(ChangeDetectorRef);

    ngOnInit() {
        this.loadProfile();
        this.loadAllergies();
        this.loadChronicDiseases();
    }

    loadProfile() {
        this.patientService.getPatientProfile().subscribe({
            next: (res) => {
                this.patientProfile = res.data;
                this.cdr.detectChanges();
            },
            error: (err) => {
                this.patientProfile = null;
                this.cdr.detectChanges();
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

    // Display Helpers
    getGenderDisplay(gender: string): string {
        const map: { [key: string]: string } = {
            'male': 'Nam',
            'female': 'Nữ',
            'other': 'Khác'
        };
        return map[gender] || gender;
    }

    getBloodTypeDisplay(type: string): string {
        const map: { [key: string]: string } = {
            'A_POSITIVE': 'A+', 'A_NEGATIVE': 'A-',
            'B_POSITIVE': 'B+', 'B_NEGATIVE': 'B-',
            'AB_POSITIVE': 'AB+', 'AB_NEGATIVE': 'AB-',
            'O_POSITIVE': 'O+', 'O_NEGATIVE': 'O-'
        };
        return map[type] || type;
    }

    getSeverityDisplay(severity: string): string {
        const map: { [key: string]: string } = {
            'mild': 'Nhẹ', 'moderate': 'Trung bình', 'severe': 'Nặng'
        };
        return map[severity?.toLowerCase()] || severity;
    }

    // Profile Actions
    createProfile() {
        this.isCreatingProfile = true;
        this.profileForm = {
            patientCode: '',
            emergencyContactName: '',
            emergencyContactPhone: '',
            bloodType: ''
        };
        this.cdr.detectChanges();
    }

    editProfile() {
        if (!this.patientProfile) return;
        this.isEditingProfile = true;
        this.isCreatingProfile = false;
        this.profileForm = { ...this.patientProfile };
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
                next: () => {
                    this.notificationService.showSuccess('Tạo hồ sơ thành công!');
                    this.loadProfile();
                    this.cancelEditProfile();
                },
                error: (err) => this.notificationService.showError('Lỗi: ' + (err.error?.message || err.message))
            });
        } else {
            this.patientService.updatePatientProfile(data).subscribe({
                next: () => {
                    this.notificationService.showSuccess('Cập nhật hồ sơ thành công!');
                    this.loadProfile();
                    this.cancelEditProfile();
                },
                error: (err) => this.notificationService.showError('Lỗi: ' + err.message)
            });
        }
    }

    // Allergy Actions
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
                this.notificationService.showSuccess('Thêm dị ứng thành công');
                this.loadAllergies();
                this.cancelAddAllergy();
            },
            error: (err) => this.notificationService.showError('Lỗi: ' + err.message)
        });
    }

    editAllergy(allergy: any) {
        this.editingAllergyId = allergy.id;
        this.allergyForm = { ...allergy };
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
                this.notificationService.showSuccess('Cập nhật dị ứng thành công');
                this.loadAllergies();
                this.cancelEditAllergy();
            },
            error: (err) => this.notificationService.showError('Lỗi: ' + err.message)
        });
    }

    deleteAllergy(allergy: any) {
        // Direct delete, no confirmation dialog per user request
        this.patientService.deleteAllergy(allergy.id).subscribe({
            next: () => {
                this.notificationService.showSuccess('Đã xóa dị ứng');
                this.loadAllergies();
            },
            error: (err) => this.notificationService.showError('Lỗi xóa dị ứng: ' + err.message)
        });
    }

    onAllergyPageChange(page: number) {
        this.allergiesPage = page;
        this.loadAllergies();
    }

    // Disease Actions
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
                this.notificationService.showSuccess('Thêm bệnh mãn tính thành công');
                this.loadChronicDiseases();
                this.cancelAddDisease();
            },
            error: (err) => this.notificationService.showError('Lỗi: ' + err.message)
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
                this.notificationService.showSuccess('Cập nhật bệnh mãn tính thành công');
                this.loadChronicDiseases();
                this.cancelEditDisease();
            },
            error: (err) => this.notificationService.showError('Lỗi: ' + err.message)
        });
    }

    deleteDisease(disease: any) {
        // Direct delete, no confirmation dialog
        this.patientService.deleteChronicDisease(disease.id).subscribe({
            next: () => {
                this.notificationService.showSuccess('Đã xóa bệnh mãn tính');
                this.loadChronicDiseases();
            },
            error: (err) => this.notificationService.showError('Lỗi xóa bệnh: ' + err.message)
        });
    }

    onDiseasePageChange(page: number) {
        this.diseasesPage = page;
        this.loadChronicDiseases();
    }
}
