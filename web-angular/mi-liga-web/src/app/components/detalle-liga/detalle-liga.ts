import { Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { LigaService } from '../../services/liga'; 
import { AuthService } from '../../services/auth.service'; 
import { Goleador } from '../../models/goleador.interface'; 

@Component({
  selector: 'app-detalle-liga',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './detalle-liga.html',
  styleUrl: './detalle-liga.css'
})
export class DetalleLigaComponent implements OnInit {
  private route = inject(ActivatedRoute);
  public ligaService = inject(LigaService); 
  public authService = inject(AuthService);
  
  ligaId = signal<string | null>(null);

  // AÑADIDO 'id' A CADA EQUIPO
  equipos = [
    { id: 1, pos: 1, nombre: 'Real FC', pj: 10, pg: 8, pe: 1, pp: 1, pts: 25 },
    { id: 2, pos: 2, nombre: 'Inter Uni', pj: 10, pg: 7, pe: 2, pp: 1, pts: 23 },
    { id: 3, pos: 3, nombre: 'Rayo Vayamos', pj: 10, pg: 5, pe: 0, pp: 5, pts: 15 },
    { id: 4, pos: 4, nombre: 'Sporting B', pj: 10, pg: 2, pe: 1, pp: 7, pts: 7 },
  ];

  goleadores: Goleador[] = [
    { nombre: 'Juan Pérez', equipo: 'Real FC', goles: 12 },
    { nombre: 'Dani Gol', equipo: 'Inter Uni', goles: 10 }
  ];

  // AÑADIDO ARRAY DE ASISTENTES
  asistentes = [
    { nombre: 'Paco Pase', equipo: 'Real FC', asistencias: 8 },
    { nombre: 'Luis Centro', equipo: 'Inter Uni', asistencias: 7 }
  ];

  ngOnInit() {
    this.ligaId.set(this.route.snapshot.paramMap.get('id'));
  }

  cambiarGoles(partidoId: number, golesL: number, golesV: number) {
    this.ligaService.actualizarResultado(partidoId, golesL, golesV);
  }

  getLigaIdNumber(): number {
    const id = this.ligaId();
    return id ? parseInt(id, 10) : 0;
  }
}