import { Component, inject, signal, computed } from '@angular/core';
import { RouterLink } from '@angular/router';
import { LigaService } from '../../services/liga'; 
import { AuthService } from '../../services/auth.service';
import { Liga } from '../../models/liga.interface'; 

@Component({
  selector: 'app-ligas',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './ligas.html',
  styleUrl: './ligas.css'
})
export class LigasComponent {
  public ligaService = inject(LigaService);
  public authService = inject(AuthService);

  // 1. Señal para la búsqueda
  busqueda = signal('');

  // 2. Lista filtrada (Solo por nombre, ya que universidad no existe en tu Java)
  ligasFiltradas = computed(() => {
    const texto = this.busqueda().toLowerCase();
    return this.ligaService.getLigas().filter(liga => 
      liga.nombre.toLowerCase().includes(texto)
    );
  });

  actualizarBusqueda(evento: Event) {
    const elemento = evento.target as HTMLInputElement;
    this.busqueda.set(elemento.value);
  }

crearLigaRapida() {
  console.log("Botón pulsado, intentando guardar..."); // Esto saldrá en el F12
  const nueva = {
    nombre: 'Nueva Liga desde la Web',
    temporada: '2025/2026'
  };
  this.ligaService.agregarLiga(nueva);
}
}