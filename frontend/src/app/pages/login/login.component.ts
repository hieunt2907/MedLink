import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService, LoginRequest } from '../../services/api.service';

@Component({
    selector: 'app-login',
    standalone: true,
    imports: [CommonModule, FormsModule],
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.css']
})
export class LoginComponent {
    loginData: LoginRequest = { username: '', password: '' };
    isLoading = false;
    errorMessage = '';

    private authService = inject(AuthService);
    private router = inject(Router);

    onLogin() {
        this.isLoading = true;
        this.errorMessage = '';

        // Simple validation
        if (!this.loginData.username || !this.loginData.password) {
            this.errorMessage = 'Vui lòng nhập tên đăng nhập và mật khẩu';
            this.isLoading = false;
            return;
        }

        this.authService.login(this.loginData).subscribe({
            next: (res) => {
                if (res && res.data && res.data.token) {
                    // Token is already stored by AuthService via tap()

                    const role = this.authService.getRoleFromToken(res.data.token);
                    console.log('User role:', role);

                    switch (role) {
                        case 'patient':
                            this.router.navigate(['/patient'], { replaceUrl: true });
                            break;
                        case 'doctor':
                            this.router.navigate(['/doctor'], { replaceUrl: true });
                            break;
                        case 'admin':
                            this.router.navigate(['/admin'], { replaceUrl: true });
                            break;
                        case 'super_admin':
                            this.router.navigate(['/super-admin'], { replaceUrl: true });
                            break;
                        case 'technician':
                            this.router.navigate(['/technician'], { replaceUrl: true });
                            break;
                        case 'nurse':
                            this.router.navigate(['/nurse'], { replaceUrl: true });
                            break;
                        default:
                            this.router.navigate(['/patient'], { replaceUrl: true }); // Default fallback to patient or home
                            console.warn('Unknown role, redirecting to patient dashboard');
                    }
                } else {
                    this.errorMessage = 'Đăng nhập thất bại: Không nhận được token';
                }
                this.isLoading = false;
            },
            error: (err) => {
                this.errorMessage = err.error?.message || 'Đăng nhập thất bại. Vui lòng thử lại.';
                this.isLoading = false;
            }
        });
    }
}
