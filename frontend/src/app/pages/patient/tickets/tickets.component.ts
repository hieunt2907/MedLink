import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PatientService, ReceptionTicketsResponse, UserResponse } from '../../../services/api.service';
import { NotificationService } from '../../../services/notification.service';

@Component({
    selector: 'app-patient-tickets',
    standalone: true,
    imports: [CommonModule, FormsModule],
    templateUrl: './tickets.component.html',
    styleUrls: ['./tickets.component.css']
})
export class PatientTicketsComponent implements OnInit {
    tickets: ReceptionTicketsResponse[] = [];
    user: UserResponse | null = null;

    // Filter
    filters = {
        keyword: '',
        patientId: undefined as number | undefined
    };

    isLoadingTickets = false;

    // Pagination
    ticketsPage = 0;
    ticketsSize = 5;
    ticketsTotalPages = 0;

    private patientService = inject(PatientService);
    private notificationService = inject(NotificationService);
    private cdr = inject(ChangeDetectorRef);

    ngOnInit() {
        this.patientService.getMe().subscribe({
            next: (res) => {
                this.user = res.data;
                if (this.user?.id) {
                    this.filters.patientId = this.user.id;
                    this.loadTickets();
                }
            },
            error: (err) => {
                this.notificationService.showError('Không thể tải thông tin người dùng.');
            }
        });
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
                this.notificationService.showError('Không thể tải danh sách phiếu tiếp đón.');
                this.isLoadingTickets = false;
            }
        });
    }

    onTicketPageChange(page: number) {
        this.ticketsPage = page;
        this.loadTickets();
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
}
