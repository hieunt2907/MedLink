import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs'; // Fix: Observable is imported from rxjs
import { tap } from 'rxjs/operators'; // Fix: tap is imported from rxjs/operators

// Interfaces
export interface LoginRequest {
    username?: string;
    email?: string;
    password?: string;
}

export interface LoginResponse {
    token: string;
}

export interface UserResponse {
    id: number;
    username: string;
    email: string;
    fullName: string;
    phone: string;
    hospitalId: number;
    role: string;
    // Add other fields as necessary
}

export interface PatientProfileEntity {
    id: number;
    patientCode: string;
    fullName: string;
    dateOfBirth: string;
    gender: string;
    address: string;
    email: string;
    phone: string;
    emergencyContactName: string;
    emergencyContactPhone: string;
    bloodType: string;
    userId: number;
}

export interface ReceptionTicketsResponse {
    id: number;
    hospitalName: string;
    patientName: string; // Added
    specialtyName: string;
    doctorName: string;
    roomNumber: string;
    queueNumber: number;
    estimatedTime: string;
    actualStartTime?: string; // Optional
    actualEndTime?: string; // Optional
    status: string;
    payerType: string; // Added (self_pay, insurance)
    reason: string;
    priority: string; // Added (normal, urgent, emergency)
    notes: string; // Added
    createdAt: string; // Added
}

export interface PatientAllergy {
    id: number;
    allergyName: string;
    severity: 'mild' | 'moderate' | 'severe';
    notes: string;
}

export interface PatientChronicDisease {
    id: number;
    diseaseName: string;
    diagnosedDate: string;
    notes: string;
}

export interface Page<T> {
    content: T[];
    totalPages: number;
    totalElements: number;
    size: number;
    number: number;
}

export interface BaseResponse<T> {
    data: T;
    message: string;
    success: boolean; // Assuming BaseResponse structure
}

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private apiUrl = 'http://localhost:8080/api/v1'; // Adjust base URL
    private http = inject(HttpClient);
    private tokenKey = 'medlink_token';

    login(credentials: LoginRequest): Observable<BaseResponse<LoginResponse>> {
        return this.http.post<BaseResponse<LoginResponse>>(`${this.apiUrl}/public/auth/login`, credentials)
            .pipe(
                tap(response => {
                    if (response && response.data && response.data.token) {
                        localStorage.setItem(this.tokenKey, response.data.token);
                    }
                })
            );
    }

    logout() {
        localStorage.removeItem(this.tokenKey);
    }

    getToken(): string | null {
        return localStorage.getItem(this.tokenKey);
    }

    isLoggedIn(): boolean {
        return !!this.getToken();
    }

    getRoleFromToken(token: string): string | null {
        try {
            const payload = token.split('.')[1];
            if (!payload) return null;
            // Base64Url decode
            const base64 = payload.replace(/-/g, '+').replace(/_/g, '/');
            const decodedPayload = atob(base64);
            const payloadObj = JSON.parse(decodedPayload);
            return payloadObj.role || null;
        } catch (e) {
            console.error('Error decoding token', e);
            return null;
        }
    }
}

@Injectable({
    providedIn: 'root'
})
export class PatientService {
    private apiUrl = 'http://localhost:8080/api/v1/patient';
    private http = inject(HttpClient);

    getMe(): Observable<BaseResponse<UserResponse>> {
        return this.http.get<BaseResponse<UserResponse>>(`${this.apiUrl}/users/me`);
    }

    getPatientProfile(): Observable<BaseResponse<PatientProfileEntity>> {
        return this.http.get<BaseResponse<PatientProfileEntity>>(`${this.apiUrl}/patients/profile/`);
    }

    getMyTickets(params: any): Observable<BaseResponse<Page<ReceptionTicketsResponse>>> {
        // Construct query params
        let queryParams = '?';
        for (const key in params) {
            if (params[key] !== null && params[key] !== undefined) {
                queryParams += `${key}=${params[key]}&`;
            }
        }
        return this.http.get<BaseResponse<Page<ReceptionTicketsResponse>>>(`${this.apiUrl}/reception-tickets/me${queryParams}`);
    }

    getMyAllergies(params?: any): Observable<BaseResponse<Page<PatientAllergy>>> {
        let queryParams = '?';
        if (params) {
            for (const key in params) {
                if (params[key] !== null && params[key] !== undefined) {
                    queryParams += `${key}=${params[key]}&`;
                }
            }
        }
        return this.http.get<BaseResponse<Page<PatientAllergy>>>(`${this.apiUrl}/patients/allergies/${queryParams}`);
    }

    createAllergy(data: PatientAllergyRequest): Observable<BaseResponse<PatientAllergy>> {
        return this.http.post<BaseResponse<PatientAllergy>>(`${this.apiUrl}/patients/allergies/`, data);
    }

    updateAllergy(id: number, data: PatientAllergyRequest): Observable<BaseResponse<PatientAllergy>> {
        return this.http.put<BaseResponse<PatientAllergy>>(`${this.apiUrl}/patients/allergies/${id}`, data);
    }

    deleteAllergy(id: number): Observable<BaseResponse<any>> {
        return this.http.delete<BaseResponse<any>>(`${this.apiUrl}/patients/allergies/${id}`);
    }

    getMyChronicDiseases(params?: any): Observable<BaseResponse<Page<PatientChronicDisease>>> {
        let queryParams = '?';
        if (params) {
            for (const key in params) {
                if (params[key] !== null && params[key] !== undefined) {
                    queryParams += `${key}=${params[key]}&`;
                }
            }
        }
        return this.http.get<BaseResponse<Page<PatientChronicDisease>>>(`${this.apiUrl}/patients/chronic-diseases/${queryParams}`);
    }

    createChronicDisease(data: PatientChronicDiseaseRequest): Observable<BaseResponse<PatientChronicDisease>> {
        return this.http.post<BaseResponse<PatientChronicDisease>>(`${this.apiUrl}/patients/chronic-diseases/`, data);
    }

    updateChronicDisease(id: number, data: PatientChronicDiseaseRequest): Observable<BaseResponse<PatientChronicDisease>> {
        return this.http.put<BaseResponse<PatientChronicDisease>>(`${this.apiUrl}/patients/chronic-diseases/${id}`, data);
    }

    deleteChronicDisease(id: number): Observable<BaseResponse<any>> {
        return this.http.delete<BaseResponse<any>>(`${this.apiUrl}/patients/chronic-diseases/${id}`);
    }

    createPatientProfile(data: PatientProfileRequest): Observable<BaseResponse<PatientProfileEntity>> {
        return this.http.post<BaseResponse<PatientProfileEntity>>(`${this.apiUrl}/patients/profile/`, data);
    }

    updatePatientProfile(data: PatientProfileRequest): Observable<BaseResponse<PatientProfileEntity>> {
        return this.http.put<BaseResponse<PatientProfileEntity>>(`${this.apiUrl}/patients/profile/`, data);
    }
}

export interface PatientProfileRequest {
    patientCode: string;
    emergencyContactName: string;
    emergencyContactPhone: string;
    bloodType: string;
}

export interface PatientAllergyRequest {
    allergyName: string;
    severity: string; // MILD, MODERATE, SEVERE
    notes: string;
}

export interface PatientChronicDiseaseRequest {
    diseaseName: string;
    diagnosedDate: string; // YYYY-MM-DD
    notes: string;
}

// Super Admin Interfaces
export interface HospitalEntity {
    id: number;
    name: string;
    description: string;
    address: string;
    phone: string;
    email?: string;
    status?: string;
}

export interface SpecialtiesEntity {
    id: number;
    name: string;
    description: string;
    hospitalId: number;
    hospitalName?: string;
    status?: string;
}

export interface RoomEntity {
    id: number;
    hospitalId: number;
    roomNumber: string;
    roomType: string;
    specialtyId: number;
    capacity: number;
    status: string;
}

export interface DoctorRoomAssignmentsEntity {
    id: number;
    doctorProfileId: number;
    hospitalId: number;
    roomId: number;
    roomNumber: string;
    specialtyId: number;
    isPrimary: boolean;
    shiftStart: string;
    shiftEnd: string;
    startDate: string;
    endDate: string;
    notes: string;
}

export interface AdminUserUpdateRequest {
    fullName?: string;
    email?: string;
    phone?: string;
    role?: string;
    hospitalId?: number;
    isActive?: boolean;
}

export interface DoctorProfileEntity {
    id: number;
    userId: number;
    fullName?: string;
    specialtyId?: number; // Optional in response, required in request
    specialtyName?: string;
    hospitalId?: number; // Optional
    hospitalName?: string;
    experienceYears: number;
    qualifications: string;
}

@Injectable({
    providedIn: 'root'
})
export class SuperAdminService {
    private apiUrl = 'http://localhost:8080/api/v1/super-admin';
    private http = inject(HttpClient);

    // Users
    filterUsers(keyword: string = '', page: number = 0, size: number = 20): Observable<BaseResponse<Page<UserResponse>>> {
        return this.http.get<BaseResponse<Page<UserResponse>>>(`${this.apiUrl}/users/?keyword=${keyword}&page=${page}&size=${size}`);
    }

    updateUser(id: number, data: AdminUserUpdateRequest): Observable<BaseResponse<UserResponse>> {
        return this.http.put<BaseResponse<UserResponse>>(`${this.apiUrl}/users/${id}`, data);
    }

    deleteUser(id: number): Observable<BaseResponse<boolean>> {
        return this.http.delete<BaseResponse<boolean>>(`${this.apiUrl}/users/${id}`);
    }

    // Hospitals
    createHospital(data: any): Observable<BaseResponse<HospitalEntity>> {
        return this.http.post<BaseResponse<HospitalEntity>>(`${this.apiUrl}/hospitals/`, data);
    }

    updateHospital(id: number, data: any): Observable<BaseResponse<HospitalEntity>> {
        return this.http.put<BaseResponse<HospitalEntity>>(`${this.apiUrl}/hospitals/${id}`, data);
    }

    deleteHospital(id: number): Observable<BaseResponse<HospitalEntity>> {
        return this.http.delete<BaseResponse<HospitalEntity>>(`${this.apiUrl}/hospitals/${id}`);
    }

    // Patient Details (Super Admin)
    filterPatientAllergies(profileId: number, page: number = 0, size: number = 20): Observable<BaseResponse<Page<PatientAllergy>>> {
        return this.http.get<BaseResponse<Page<PatientAllergy>>>(`${this.apiUrl}/patients/allergies/${profileId}?page=${page}&size=${size}`);
    }

    filterPatientChronicDiseases(profileId: number, page: number = 0, size: number = 20): Observable<BaseResponse<Page<PatientChronicDisease>>> {
        return this.http.get<BaseResponse<Page<PatientChronicDisease>>>(`${this.apiUrl}/patients/chronic-diseases/${profileId}?page=${page}&size=${size}`);
    }

    // Reception Tickets (Super Admin)
    createReceptionTicket(data: any): Observable<BaseResponse<ReceptionTicketsResponse>> {
        return this.http.post<BaseResponse<ReceptionTicketsResponse>>(`${this.apiUrl}/reception-tickets/`, data);
    }

    filterHospitals(keyword: string = '', page: number = 0, size: number = 20): Observable<BaseResponse<Page<HospitalEntity>>> {
        return this.http.get<BaseResponse<Page<HospitalEntity>>>(`${this.apiUrl}/hospitals/?keyword=${keyword}&page=${page}&size=${size}`);
    }

    // Specialties
    createSpecialty(data: any): Observable<BaseResponse<SpecialtiesEntity>> {
        return this.http.post<BaseResponse<SpecialtiesEntity>>(`${this.apiUrl}/hospital/specialties/`, data);
    }

    updateSpecialty(id: number, data: any): Observable<BaseResponse<SpecialtiesEntity>> {
        return this.http.put<BaseResponse<SpecialtiesEntity>>(`${this.apiUrl}/hospital/specialties/${id}`, data);
    }

    deleteSpecialty(id: number): Observable<BaseResponse<SpecialtiesEntity>> {
        return this.http.delete<BaseResponse<SpecialtiesEntity>>(`${this.apiUrl}/hospital/specialties/${id}`);
    }

    filterSpecialties(hospitalId: number, page: number = 0, size: number = 20): Observable<BaseResponse<Page<SpecialtiesEntity>>> {
        return this.http.get<BaseResponse<Page<SpecialtiesEntity>>>(`${this.apiUrl}/hospital/specialties/${hospitalId}?page=${page}&size=${size}`);
    }

    // Rooms
    filterRooms(specialtyId: number, page: number = 0, size: number = 20): Observable<BaseResponse<Page<RoomEntity>>> {
        return this.http.get<BaseResponse<Page<RoomEntity>>>(`${this.apiUrl}/hospital/rooms/${specialtyId}?page=${page}&size=${size}`);
    }

    createRoom(data: any): Observable<BaseResponse<RoomEntity>> {
        return this.http.post<BaseResponse<RoomEntity>>(`${this.apiUrl}/hospital/rooms/`, data);
    }

    updateRoom(id: number, data: any): Observable<BaseResponse<RoomEntity>> {
        return this.http.put<BaseResponse<RoomEntity>>(`${this.apiUrl}/hospital/rooms/${id}`, data);
    }

    deleteRoom(id: number): Observable<BaseResponse<RoomEntity>> {
        return this.http.delete<BaseResponse<RoomEntity>>(`${this.apiUrl}/hospital/rooms/${id}`);
    }

    // Doctor Room Assignments
    filterDoctorRoomAssignments(doctorProfileId?: number, keyword: string = '', page: number = 0, size: number = 20): Observable<BaseResponse<Page<DoctorRoomAssignmentsEntity>>> {
        let url = `${this.apiUrl}/doctors/room-assignments/filter?keyword=${keyword}&page=${page}&size=${size}`;
        if (doctorProfileId) url += `&doctorProfileId=${doctorProfileId}`;
        return this.http.get<BaseResponse<Page<DoctorRoomAssignmentsEntity>>>(url);
    }

    createDoctorRoomAssignment(data: any): Observable<BaseResponse<DoctorRoomAssignmentsEntity>> {
        return this.http.post<BaseResponse<DoctorRoomAssignmentsEntity>>(`${this.apiUrl}/doctors/room-assignments/`, data);
    }

    updateDoctorRoomAssignment(id: number, data: any): Observable<BaseResponse<DoctorRoomAssignmentsEntity>> {
        return this.http.put<BaseResponse<DoctorRoomAssignmentsEntity>>(`${this.apiUrl}/doctors/room-assignments/${id}`, data);
    }

    deleteDoctorRoomAssignment(id: number): Observable<BaseResponse<DoctorRoomAssignmentsEntity>> {
        return this.http.delete<BaseResponse<DoctorRoomAssignmentsEntity>>(`${this.apiUrl}/doctors/room-assignments/${id}`);
    }

    getDoctorRoomAssignment(id: number): Observable<BaseResponse<DoctorRoomAssignmentsEntity>> {
        return this.http.get<BaseResponse<DoctorRoomAssignmentsEntity>>(`${this.apiUrl}/doctors/room-assignments/${id}`);
    }

    // Doctors (Profiles)
    createDoctorProfile(data: any): Observable<BaseResponse<DoctorProfileEntity>> {
        return this.http.post<BaseResponse<DoctorProfileEntity>>(`${this.apiUrl}/doctors/profile/`, data);
    }

    updateDoctorProfile(id: number, data: any): Observable<BaseResponse<DoctorProfileEntity>> {
        return this.http.put<BaseResponse<DoctorProfileEntity>>(`${this.apiUrl}/doctors/profile/${id}`, data);
    }

    deleteDoctorProfile(id: number): Observable<BaseResponse<DoctorProfileEntity>> {
        return this.http.delete<BaseResponse<DoctorProfileEntity>>(`${this.apiUrl}/doctors/profile/${id}`);
    }

    filterDoctorProfiles(keyword: string = '', page: number = 0, size: number = 20): Observable<BaseResponse<Page<DoctorProfileEntity>>> {
        return this.http.get<BaseResponse<Page<DoctorProfileEntity>>>(`${this.apiUrl}/doctors/profile/filter?keyword=${keyword}&page=${page}&size=${size}`);
    }

    // Reception Tickets
    createTicket(data: any): Observable<BaseResponse<ReceptionTicketsResponse>> {
        return this.http.post<BaseResponse<ReceptionTicketsResponse>>(`${this.apiUrl}/reception-tickets/`, data);
    }

    filterTickets(params: any): Observable<BaseResponse<Page<ReceptionTicketsResponse>>> {
        let queryParams = '?';
        for (const key in params) {
            if (params[key] !== null && params[key] !== undefined) {
                queryParams += `${key}=${params[key]}&`;
            }
        }
        return this.http.get<BaseResponse<Page<ReceptionTicketsResponse>>>(`${this.apiUrl}/reception-tickets/filter${queryParams}`);
    }

    // Patients
    filterPatientProfiles(keyword: string = '', page: number = 0, size: number = 20): Observable<BaseResponse<Page<PatientProfileEntity>>> {
        return this.http.get<BaseResponse<Page<PatientProfileEntity>>>(`${this.apiUrl}/patients/profile/?keyword=${keyword}&page=${page}&size=${size}`);
    }

    getPatientProfile(id: number): Observable<BaseResponse<PatientProfileEntity>> {
        return this.http.get<BaseResponse<PatientProfileEntity>>(`${this.apiUrl}/patients/profile/${id}`);
    }

    getPatientAllergies(profileId: number, page: number = 0, size: number = 20): Observable<BaseResponse<Page<PatientAllergy>>> {
        return this.http.get<BaseResponse<Page<PatientAllergy>>>(`${this.apiUrl}/patients/profile/${profileId}/allergies?page=${page}&size=${size}`);
    }
}
