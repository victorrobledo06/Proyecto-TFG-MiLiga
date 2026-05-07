import { Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { LigaService } from '../../services/liga';
import { Jugador } from '../../models/jugador.interface';

@Component({
  selector: 'app-detalle-equipo',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './detalle-equipo.html',
  styleUrl: './detalle-equipo.css'
})
export class DetalleEquipoComponent implements OnInit {
  private route = inject(ActivatedRoute);
  public ligaService = inject(LigaService);
  
  equipoId = signal<number>(0);
  nombreEquipo = signal<string>('Cargando...');

  // Datos de ejemplo de la plantilla
  plantilla: Jugador[] = [
    { id: 1, nombre: 'Dani Gol', posicion: 'Delantero', goles: 10, asistencias: 2, amarillas: 1, rojas: 0 },
    { id: 2, nombre: 'Paco Pase', posicion: 'Centrocampista', goles: 2, asistencias: 8, amarillas: 3, rojas: 0 },
    { id: 3, nombre: 'Muro defensivo', posicion: 'Defensa', goles: 0, asistencias: 1, amarillas: 5, rojas: 1 },
  ];

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    this.equipoId.set(Number(id));
    // Aquí podrías buscar el nombre real en el servicio, de momento simulamos:
    this.nombreEquipo.set(this.equipoId() === 1 ? 'Real FC' : 'Inter Uni');
  }

  // Filtramos los partidos del servicio donde participe este equipo
  getPartidosEquipo() {
    return this.ligaService.getPartidosPorLiga(1).filter(p => 
      p.localId === this.equipoId() || p.visitanteId === this.equipoId()
    );
  }
}