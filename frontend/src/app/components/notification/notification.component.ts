import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NotificationService } from '../../services/notification.service';

@Component({
    selector: 'app-notification',
    standalone: true,
    imports: [CommonModule],
    template: `
    <div class="toast-container">
      <div *ngFor="let toast of notificationService.toasts()" 
           class="toast" 
           [ngClass]="toast.type"
           (click)="notificationService.remove(toast.id)">
        <div class="icon">
          <i class="material-icons" *ngIf="toast.type === 'success'">check_circle</i>
          <i class="material-icons" *ngIf="toast.type === 'error'">error</i>
          <i class="material-icons" *ngIf="toast.type === 'info'">info</i>
          <i class="material-icons" *ngIf="toast.type === 'warning'">warning</i>
        </div>
        <div class="message">{{ toast.message }}</div>
      </div>
    </div>
  `,
    styles: [`
    .toast-container {
      position: fixed;
      top: 20px;
      right: 20px;
      z-index: 9999;
      display: flex;
      flex-direction: column;
      gap: 10px;
      pointer-events: none;
    }

    .toast {
      pointer-events: auto;
      min-width: 300px;
      padding: 16px;
      border-radius: 8px;
      background: white;
      box-shadow: 0 4px 12px rgba(0,0,0,0.15);
      display: flex;
      align-items: center;
      gap: 12px;
      animation: slideIn 0.3s ease-out;
      cursor: pointer;
      border-left: 5px solid transparent;
      font-family: 'Segoe UI', sans-serif;
    }

    .toast.success { border-left-color: #28a745; }
    .toast.success .icon { color: #28a745; }
    
    .toast.error { border-left-color: #dc3545; }
    .toast.error .icon { color: #dc3545; }
    
    .toast.info { border-left-color: #007bff; }
    .toast.info .icon { color: #007bff; }
    
    .toast.warning { border-left-color: #ffc107; }
    .toast.warning .icon { color: #ffc107; }

    .message {
      font-size: 0.95rem;
      color: #333;
      font-weight: 500;
    }

    @keyframes slideIn {
      from { transform: translateX(100%); opacity: 0; }
      to { transform: translateX(0); opacity: 1; }
    }
  `]
})
export class NotificationComponent {
    notificationService = inject(NotificationService);
}
