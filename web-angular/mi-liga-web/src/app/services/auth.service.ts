import { Injectable, signal } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  // Usamos un 'signal' para saber en toda la web si hay alguien logueado
  private usuarioLogueado = signal<any>(null);

  registrar(datos: any) {
    // Simulamos que lo guardamos (podrías guardarlo en localStorage)
    localStorage.setItem('usuario', JSON.stringify(datos));
    this.usuarioLogueado.set(datos);
    console.log('Usuario guardado en el servicio:', datos);
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

  private misEquipos = signal([
  { id: 101, nombre: 'Rayo Vayamos', liga: 'Liga de Fútbol Sala', posicion: 3 },
  { id: 102, nombre: 'Inter Uni', liga: 'Trofeo Rector', posicion: 2 }
]);

getMisEquipos() {
  return this.misEquipos();
}
}
