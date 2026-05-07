import { Component } from '@angular/core';

@Component({
  selector: 'app-noticias',
  standalone: true,
  template: `
    <div class="container" style="margin-top: 50px; padding-bottom: 50px;">
      <h1 style="color: white; margin-bottom: 40px;">Todas las Noticias</h1>
      
      <div class="news-list">
        @for (n of noticias; track n.titulo) {
          <div class="main-news-card" style="margin-bottom: 30px; display: flex; background: #1e293b; border-radius: 15px; overflow: hidden; border: 1px solid #334155;">
            <div style="flex: 1; background: #334155;">
               <img [src]="n.img" style="width: 100%; height: 100%; object-fit: cover;" alt="Noticia">
            </div>

            <div style="flex: 1.5; padding: 30px; display: flex; flex-direction: column; justify-content: center;">
              <span style="color: #22c55e; font-weight: bold; font-size: 0.8rem;">{{ n.fecha }}</span>
              <h2 style="color: white; margin: 10px 0;">{{ n.titulo }}</h2>
              <p style="color: #94a3b8;">{{ n.desc }}</p>
              <button style="width: fit-content; margin-top: 20px; background: white; border: none; padding: 8px 15px; border-radius: 5px; font-weight: bold; cursor: pointer;">
                Leer noticia completa
              </button>
            </div>
          </div>
        }
      </div>
    </div>
  `
})
export class NoticiasComponent {
  noticias = [
    { 
      titulo: '¡Abiertas las inscripciones para la Copa Primavera!', 
      desc: 'Ya está disponible el formulario para registrar a tu equipo. No te quedes fuera del torneo más importante del año.', 
      fecha: '20 Abril 2026',
      img: 'https://images.unsplash.com/photo-1574629810360-7efbbe195018?q=80&w=1000'
    },
    { 
      titulo: 'Sanciones disciplinarias: Jornada 12', 
      desc: 'El comité ha publicado la lista de jugadores suspendidos para los próximos partidos de liga universitaria.', 
      fecha: '19 Abril 2026',
      img: 'https://images.unsplash.com/photo-1519052537078-e6302a4968d4?q=80&w=1000'
    },
    { 
      titulo: 'Nuevas equipaciones disponibles', 
      desc: 'Ya puedes solicitar las nuevas camisetas oficiales para tu facultad en el centro de atención al alumno.', 
      fecha: '18 Abril 2026',
      img: 'https://images.unsplash.com/photo-1522778119026-d647f0596c20?q=80&w=1000'
    }
  ];
}