import { Component, inject, signal, computed } from '@angular/core';
import { RouterLink } from '@angular/router';
import { LigaService } from '../../services/liga'; 
import { AuthService } from '../../services/auth.service';

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

  busqueda = signal('');

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
}