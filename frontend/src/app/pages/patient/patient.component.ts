import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule, LocationStrategy } from '@angular/common';
import { RouterModule, Router } from '@angular/router'; // Import RouterModule
import {
    PatientService,
    AuthService,
    UserResponse,
    PatientProfileEntity
} from '../../services/api.service';

@Component({
    selector: 'app-patient',
    standalone: true,
    imports: [CommonModule, RouterModule], // Add RouterModule here
    templateUrl: './patient.component.html',
    styleUrls: ['./patient.component.css']
})
export class PatientComponent implements OnInit {
    user: UserResponse | null = null;
    patientProfile: PatientProfileEntity | null = null;
    isSidebarCollapsed = false;
    isLoading = false;

    private patientService = inject(PatientService);
    private authService = inject(AuthService);
    private router = inject(Router);
    private cdr = inject(ChangeDetectorRef);
    private locationStrategy = inject(LocationStrategy);

    // Helper getter for title based on route could go here, or simple logic in HTML

    ngOnInit() {
        // Prevent back navigation
        history.pushState(null, '', window.location.href);
        this.locationStrategy.onPopState(() => {
            history.pushState(null, '', window.location.href);
        });

        this.loadProfile();
    }

    loadProfile() {
        this.isLoading = true;
        this.patientService.getMe().subscribe({
            next: (res) => {
                this.user = res.data;
                this.patientService.getPatientProfile().subscribe({
                    next: (pRes) => {
                        this.patientProfile = pRes.data;
                        this.isLoading = false;
                        this.cdr.detectChanges();
                    },
                    error: () => {
                        this.patientProfile = null;
                        this.isLoading = false;
                        this.cdr.detectChanges();
                    }
                });
            },
            error: (err) => {
                console.error('Failed to load user', err);
                this.isLoading = false;
                if (err.status === 401 || err.status === 403) {
                    this.authService.logout();
                    this.router.navigate(['/login']);
                }
            }
        });
    }

    toggleSidebar() {
        this.isSidebarCollapsed = !this.isSidebarCollapsed;
    }

    logout() {
        this.authService.logout();
        this.router.navigate(['/login']);
    }
}
