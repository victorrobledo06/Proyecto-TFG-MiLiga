import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-noticias',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './noticias.html',
  styleUrl: './noticias.css'
})
export class NoticiasComponent {
  noticiaAbierta = signal<any>(null);

  abrirNoticia(n: any) { this.noticiaAbierta.set(n); }
  cerrarNoticia() { this.noticiaAbierta.set(null); }

  noticias = [
    { 
      titulo: '¡Abiertas las inscripciones para la Copa Primavera!', 
      desc: 'Ya está disponible el formulario para registrar a tu equipo. No te quedes fuera del torneo más importante del año.', 
      contenido: 'El plazo de inscripción estará abierto hasta el 30 de abril. Cada equipo debe presentar un mínimo de 10 jugadores y un máximo de 18. Las inscripciones se realizan a través de la app oficial MiLiga. Los equipos inscritos en ediciones anteriores tienen prioridad hasta el 25 de abril. Para más información contacta con la organización a través del correo torneos@miliga.es.',
      fecha: '20 Abril 2026',
      img: 'https://images.unsplash.com/photo-1574629810360-7efbbe195018?q=80&w=1000'
    },
    { 
      titulo: 'Sanciones disciplinarias: Jornada 12', 
      desc: 'El comité ha publicado la lista de jugadores suspendidos para los próximos partidos de liga universitaria.', 
      contenido: 'Tras revisar las actas de la jornada 12, el comité disciplinario ha decidido sancionar con un partido de suspensión a los jugadores que acumularon cinco tarjetas amarillas. Adicionalmente, dos jugadores recibieron sanción de dos partidos por conducta antideportiva. Las apelaciones pueden presentarse antes del próximo miércoles a las 23:59h a través del formulario oficial.',
      fecha: '19 Abril 2026',
      img: 'https://images.unsplash.com/photo-1606925797300-0b35e9d1794e?q=80&w=1000'
    },
    { 
      titulo: 'Nuevas equipaciones disponibles', 
      desc: 'Ya puedes solicitar las nuevas camisetas oficiales para tu facultad en el centro de atención al alumno.', 
      contenido: 'Las nuevas equipaciones están disponibles en tallas XS, S, M, L, XL y XXL. El precio por camiseta es de 15€ y el plazo de solicitud es hasta fin de existencias. Puedes recogerlas en el centro de atención al alumno de lunes a viernes de 9:00 a 14:00h. Se requiere presentar el carné universitario en vigor.',
      fecha: '18 Abril 2026',
      img: 'https://images.unsplash.com/photo-1522778119026-d647f0596c20?q=80&w=1000'
    }
  ];
}