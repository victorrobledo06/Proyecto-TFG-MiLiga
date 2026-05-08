import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';

@Component({
  selector: 'app-detalle-partido',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './detalle-partido.html',
  styleUrl: './detalle-partido.css'
})
export class DetallePartidoComponent implements OnInit {
  private route = inject(ActivatedRoute);
  partidoId: string | null = null;

  // Datos simulados (En el futuro vendrán de tu LigaService)
  partido = {
    liga: 'Liga de Fútbol 7 - Badajoz',
    local: 'Real FC',
    visitante: 'Inter Uni',
    golesL: 3,
    golesV: 2,
    jugado: true,
    fecha: '11 Abr 2026',
    hora: '10:30',
    campo: 'Pista Central UEx',
    
    eventos: [
      { min: 10, tipo: 'gol', jugador: 'Dani Gol', equipo: 'local' },
      { min: 22, tipo: 'tarjeta', color: 'amarilla', jugador: 'Muro defensivo', equipo: 'local' },
      { min: 30, tipo: 'gol', jugador: 'Luis Centro', equipo: 'visitante' },
      { min: 42, tipo: 'gol', jugador: 'Dani Gol', equipo: 'local' },
      { min: 48, tipo: 'gol', jugador: 'Paco Pase', equipo: 'visitante' },
      { min: 50, tipo: 'gol', jugador: 'Álex Zurdo', equipo: 'local' }
    ],
    
    alineacionLocal: ['Iker (P)', 'Carvajal', 'Ramos', 'Modric', 'Kroos', 'Vinicius', 'Dani Gol'],
    alineacionVisitante: ['Oblak (P)', 'Savic', 'Koke', 'De Paul', 'Griezmann', 'Correa', 'Luis Centro']
  };

  ngOnInit() {
    this.partidoId = this.route.snapshot.paramMap.get('id');
  }
}