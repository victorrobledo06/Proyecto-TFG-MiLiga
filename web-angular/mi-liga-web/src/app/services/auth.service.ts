import { Injectable, signal } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private usuarioLogueado = signal<any>(null);

  registrar(datos: any) {
    localStorage.setItem('usuario', JSON.stringify(datos));
    this.usuarioLogueado.set(datos);
  }

  getUsuario() {
    return this.usuarioLogueado();
  }

  logout() {
    this.usuarioLogueado.set(null);
    localStorage.removeItem('usuario');
  }

  login(email: string, pass: string): boolean {
    const usuarioGuardado = JSON.parse(localStorage.getItem('usuario') || '{}');
    if (usuarioGuardado.email === email && usuarioGuardado.password === pass) {
      this.usuarioLogueado.set(usuarioGuardado);
      return true;
    }
    return false;
  }
}