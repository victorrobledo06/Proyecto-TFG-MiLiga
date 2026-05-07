import { Injectable, signal, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Liga } from '../models/liga.interface';
import { Partido } from '../models/partido.interface';

@Injectable({
  providedIn: 'root'
})
export class LigaService {
  private http = inject(HttpClient);
 private url = 'http://18.205.244.57:8080/ligas';

  // --- SECCIÓN LIGAS (AWS) ---
  private ligas = signal<Liga[]>([]);

  constructor() {
    this.cargarLigasDesdeAWS();
  }

  getLigas() {
    return this.ligas();
  }

  cargarLigasDesdeAWS() {
    this.http.get<Liga[]>(this.url).subscribe({
      next: (datos) => this.ligas.set(datos),
      error: (err) => console.error('Error cargando ligas de AWS:', err)
    });
  }

  agregarLiga(nuevaLiga: any) {
  console.log("Enviando a la API:", nuevaLiga);
  this.http.post<Liga>(this.url, nuevaLiga).subscribe({
    next: (ligaGuardada) => {
      console.log("AWS respondió con éxito:", ligaGuardada);
      this.ligas.update(lista => [...lista, ligaGuardada]);
      alert("¡Liga guardada en AWS correctamente!");
    },
    error: (err) => {
      console.error("ERROR AL GUARDAR EN AWS:", err);
      alert("Error: No se pudo conectar con la API de Eclipse");
    }
  });
}

  // --- SECCIÓN PARTIDOS (Local hasta que hagamos el Java de Partidos) ---
  private partidos = signal<Partido[]>([
    { id: 1, ligaId: 1, local: 'Real FC', localId: 1, visitante: 'Inter Uni', visitanteId: 2, golesLocal: 2, golesVisitante: 1, jugado: true },
    { id: 2, ligaId: 1, local: 'Rayo Vayamos', localId: 3, visitante: 'Sporting B', visitanteId: 4, golesLocal: 0, golesVisitante: 0, jugado: false }
  ]);

  getPartidosPorLiga(id: number) {
    return this.partidos().filter(p => p.ligaId === id);
  }

  actualizarResultado(id: number, gL: number, gV: number) {
    this.partidos.update(lista => 
      lista.map(p => p.id === id ? { ...p, golesLocal: gL, golesVisitante: gV, jugado: true } : p)
    );
  }
}