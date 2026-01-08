import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { AuthService } from '../../services/api.service';

@Component({
  selector: 'app-super-admin',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './super-admin.html',
  styleUrls: ['./super-admin.css']
})
export class SuperAdminComponent {
  isSidebarCollapsed = false;
  private authService = inject(AuthService);
  private router = inject(Router);

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  get pageTitle(): string {
    const url = this.router.url;
    if (url.includes('/users')) return 'Quản lý Tài khoản';
    if (url.includes('/hospitals')) return 'Quản lý Bệnh viện';
    if (url.includes('/doctors')) return 'Quản lý Bác sĩ';
    if (url.includes('/patients')) return 'Quản lý Bệnh nhân';
    if (url.includes('/tickets')) return 'Quản lý Phiếu tiếp đón';
    return 'Super Admin';
  }
}
