import { Component, inject } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class LoginComponent {
  private fb = inject(FormBuilder);
  private router = inject(Router);
  private authService = inject(AuthService); 

  loginForm = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(6)]]
  });

  errorLogin = false; 

  acceder() {
    if (this.loginForm.valid) {
      
      const email = this.loginForm.value.email as string;
      const password = this.loginForm.value.password as string;
      
      const exito = this.authService.login(email, password);

      if (exito) {
        this.router.navigate(['/ligas']);
      } else {
        this.errorLogin = true; 
      }
    }
  }
}