import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-home',
  standalone: true,   
  imports: [RouterLink],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class HomeComponent {
  noticias = [
  { titulo: '¡Abiertas las inscripciones!', desc: 'Ya puedes inscribir a tu equipo para la Copa Primavera.', fecha: 'Hoy' },
];
}