import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SuperAdminService, UserResponse } from '../../../services/api.service';
import { NotificationService } from '../../../services/notification.service';

@Component({
  selector: 'app-users',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './users.html',
  styleUrls: ['./users.css']
})
export class UsersComponent implements OnInit {
  users: UserResponse[] = [];

  // Pagination/Filtering
  page = 0;
  size = 10;
  totalPages = 0;
  keyword = '';

  // Selection
  selectedUserIds: Set<number> = new Set();

  // Modal
  showModal = false;

  private superAdminService = inject(SuperAdminService);
  private notificationService = inject(NotificationService);
  private cdr = inject(ChangeDetectorRef);

  ngOnInit() {
    this.loadUsers();
  }

  loadUsers() {
    this.superAdminService.filterUsers(this.keyword, this.page, this.size).subscribe({
      next: (res) => {
        if (res.data) {
          this.users = res.data.content;
          this.totalPages = res.data.totalPages;
        }
        this.cdr.detectChanges();
      },
      error: () => {
        this.notificationService.showError('Không thể tải danh sách tài khoản.');
        this.cdr.detectChanges();
      }
    });
  }

  toggleUserSelection(id: number) {
    if (this.selectedUserIds.has(id)) {
      this.selectedUserIds.delete(id);
    } else {
      this.selectedUserIds.add(id);
    }
  }

  isUserSelected(id: number): boolean {
    return this.selectedUserIds.has(id);
  }

  toggleAllUsers(event: any) {
    if (event.target.checked) {
      this.users.forEach(u => this.selectedUserIds.add(u.id));
    } else {
      this.selectedUserIds.clear();
    }
  }

  deleteSelectedUsers() {
    // Removed confirm dialog as per request
    const ids = Array.from(this.selectedUserIds);
    if (ids.length === 0) return;

    let completed = 0;
    let errors = 0;

    ids.forEach(id => {
      this.superAdminService.deleteUser(id).subscribe({
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
      this.notificationService.showSuccess('Đã xóa thành công.');
    }
    this.loadUsers();
    this.selectedUserIds.clear();
  }

  openModal() {
    this.showModal = true;
  }

  closeModal() {
    this.showModal = false;
  }

  onPageChange(newPage: number) {
    this.page = newPage;
    this.loadUsers();
  }
}
