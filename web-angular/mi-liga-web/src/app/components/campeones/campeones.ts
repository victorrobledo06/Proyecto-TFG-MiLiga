import { Component } from '@angular/core';

@Component({
  selector: 'app-campeones',
  standalone: true,
  template: `
    <div class="container" style="margin-top: 100px; padding-bottom: 50px; text-align: center;">
      <h1 style="color: white; font-weight: 800; text-transform: uppercase;">🏆 Galería de Campeones</h1>
      <p style="color: #94a3b8; margin-bottom: 40px;">Los que mandan en la pista.</p>
      
      <div class="champions-grid" style="display: grid; grid-template-columns: repeat(auto-fill, minmax(300px, 1fr)); gap: 20px;">
        @for (c of campeones; track c.equipo) {
          <div class="card" style="background: #111; border: 1px solid #333; border-radius: 8px; overflow: hidden;">
            <img [src]="c.foto" style="width: 100%; height: 250px; object-fit: cover;" alt="equipo">
            <div style="padding: 15px; text-align: left;">
              <h3 style="color: #22c55e; margin: 0;">{{ c.equipo }}</h3>
              <p style="color: #eee; margin: 5px 0; font-size: 0.9rem;">{{ c.temporada }}</p>
            </div>
          </div>
        }
      </div>
    </div>
  `
})
export class CampeonesComponent {
 campeones = [
  { 
    equipo: 'Rayo Vayamos FC', 
    temporada: '2025/26', 
    foto: 'assets/ganador1.jpg'
  },
  { 
    equipo: 'Drink Team', 
    temporada: 'Copa Universitaria', 
    foto: 'assets/ganador2.jpg'
  },
  { 
    equipo: 'Nottingham Miedo', 
    temporada: 'Temporada Verano', 
    foto: 'assets/ganador3.jpg'
  }
];
}