import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { SuperAdminService, RoomEntity } from '../../../services/api.service';
import { NotificationService } from '../../../services/notification.service';

@Component({
  selector: 'app-specialty-detail',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './specialty-detail.html',
  styleUrls: ['./specialty-detail.css']
})
export class SpecialtyDetailComponent implements OnInit {
  hospitalId: number = 0;
  specialtyId: number = 0;
  rooms: RoomEntity[] = [];
  page = 0;
  size = 10;
  totalPages = 0;

  selectedRoomIds: Set<number> = new Set();

  // Modal
  showModal = false;
  currentRoomId: number | null = null;
  roomForm: any = {
    roomNumber: '',
    roomType: 'examination',
    hospitalId: null,
    specialtyId: null,
    capacity: 1,
    status: 'available'
  };

  private superAdminService = inject(SuperAdminService);
  private notificationService = inject(NotificationService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private cdr = inject(ChangeDetectorRef);

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      const hId = params.get('hospitalId');
      const sId = params.get('specialtyId');
      if (hId && sId) {
        this.hospitalId = +hId;
        this.specialtyId = +sId;
        this.loadRooms();
      }
    });
  }

  loadRooms() {
    this.superAdminService.filterRooms(this.specialtyId, this.page, this.size).subscribe({
      next: (res) => {
        if (res.data) {
          this.rooms = res.data.content;
          this.totalPages = res.data.totalPages;
        }
        this.cdr.detectChanges();
      },
      error: () => {
        this.notificationService.showError('Không thể tải danh sách phòng.');
        this.cdr.detectChanges();
      }
    });
  }

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
      this.showModal = true;
    }
  }

  deleteSelectedRooms() {
    const ids = Array.from(this.selectedRoomIds);
    if (ids.length === 0) return;

    let completed = 0;
    let errors = 0;
    ids.forEach(id => {
      this.superAdminService.deleteRoom(id).subscribe({
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
      this.notificationService.showSuccess('Đã xóa phòng thành công.');
    }
    this.loadRooms();
    this.selectedRoomIds.clear();
  }

  openModal() {
    this.currentRoomId = null;
    this.roomForm = {
      roomNumber: '',
      roomType: 'examination',
      hospitalId: this.hospitalId,
      specialtyId: this.specialtyId,
      capacity: 1,
      status: 'available'
    };
    this.showModal = true;
  }

  closeModal() {
    this.showModal = false;
  }

  saveRoom() {
    const request = this.roomForm;
    request.hospitalId = this.hospitalId;
    request.specialtyId = this.specialtyId;

    const obs = this.currentRoomId
      ? this.superAdminService.updateRoom(this.currentRoomId, request)
      : this.superAdminService.createRoom(request);

    obs.subscribe({
      next: () => {
        this.notificationService.showSuccess(this.currentRoomId ? 'Cập nhật phòng thành công!' : 'Tạo phòng thành công!');
        this.closeModal();
        this.loadRooms();
        this.selectedRoomIds.clear();
      },
      error: (err) => {
        this.notificationService.showError('Lỗi: ' + (err.error?.message || err.message));
      }
    });
  }

  goBack() {
    this.router.navigate(['super-admin', 'hospitals', this.hospitalId]);
  }

  onPageChange(newPage: number) {
    this.page = newPage;
    this.loadRooms();
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
