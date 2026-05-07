import { Component, inject } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service'; // <--- IMPORTA TU SERVICIO

@Component({
  selector: 'app-registro',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './registro.html',
  styleUrl: './registro.css',
})
export class RegistroComponent {
  private fb = inject(FormBuilder);
  private router = inject(Router);
  private authService = inject(AuthService); // <--- INYECTA EL SERVICIO

  registroForm = this.fb.group({
    nombre: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(6)]]
  });

  enviar() {
    if (this.registroForm.valid) {
      // 1. Guardamos los datos en el servicio
      this.authService.registrar(this.registroForm.value);
      
      // 2. Saltamos un aviso de éxito (opcional)
      alert('¡Registro completado con éxito!');
      
      // 3. Mandamos al usuario a las ligas automáticamente
      this.router.navigate(['/ligas']);
    }
  }
}