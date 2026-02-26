import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { PatientComponent } from './pages/patient/patient.component';
import { LandingComponent } from './pages/landing/landing.component';
import { SuperAdminComponent } from './pages/super-admin/super-admin';
import { UsersComponent } from './pages/super-admin/users/users';
import { HospitalsComponent } from './pages/super-admin/hospitals/hospitals';
import { HospitalDetailComponent } from './pages/super-admin/hospital-detail/hospital-detail';
import { SpecialtyDetailComponent } from './pages/super-admin/specialty-detail/specialty-detail';
import { DoctorsComponent } from './pages/super-admin/doctors/doctors';
import { DoctorScheduleComponent } from './pages/super-admin/doctor-schedule/doctor-schedule';
import { PatientsComponent } from './pages/super-admin/patients/patients';
import { PatientDetailComponent } from './pages/super-admin/patient-detail/patient-detail';
import { TicketsComponent } from './pages/super-admin/tickets/tickets';
import { PatientTicketsComponent } from './pages/patient/tickets/tickets.component';
import { PatientRecordsComponent } from './pages/patient/records/records.component';

export const routes: Routes = [
    { path: '', component: LandingComponent },
    { path: 'login', component: LoginComponent },
    {
        path: 'patient',
        component: PatientComponent,
        children: [
            { path: '', component: PatientTicketsComponent },
            { path: 'records', component: PatientRecordsComponent }
        ]
    },
    {
        path: 'super-admin',
        component: SuperAdminComponent,
        children: [
            { path: '', redirectTo: 'users', pathMatch: 'full' },
            { path: 'users', component: UsersComponent },
            { path: 'hospitals', component: HospitalsComponent },
            { path: 'hospitals/:hospitalId', component: HospitalDetailComponent },
            { path: 'hospitals/:hospitalId/specialties/:specialtyId', component: SpecialtyDetailComponent },
            { path: 'doctors', component: DoctorsComponent },
            { path: 'doctors/:doctorId/schedule', component: DoctorScheduleComponent },
            { path: 'patients', component: PatientsComponent },
            { path: 'patients/:patientId', component: PatientDetailComponent },
            { path: 'tickets', component: TicketsComponent }
        ]
    }
];
