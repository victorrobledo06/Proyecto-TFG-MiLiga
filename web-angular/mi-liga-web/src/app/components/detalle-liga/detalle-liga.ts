import { Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { db } from '../../firebase.config';
import { ref, onValue } from 'firebase/database';

@Component({
  selector: 'app-detalle-liga',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './detalle-liga.html',
  styleUrl: './detalle-liga.css'
})
export class DetalleLigaComponent implements OnInit {
  private route = inject(ActivatedRoute);
  public authService = inject(AuthService);

  ligaId = signal<string | null>(null);
  nombreLiga = signal<string>('');
  equipos = signal<any[]>([]);
  clasificacion = signal<any[]>([]);
  partidos = signal<any[]>([]);
  goleadores = signal<any[]>([]);
  asistentes = signal<any[]>([]);
  tarjetas = signal<any[]>([]);
  partidosPorJornada = signal<{ jornada: string, partidos: any[] }[]>([]);

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    this.ligaId.set(id);
    if (!id) return;

    onValue(ref(db, `ligas/${id}`), (snapshot) => {
      const data = snapshot.val();
      if (!data) return;

      this.nombreLiga.set(data.nombre || '');


      const equiposArr = data.equipos
        ? Object.entries(data.equipos).map(([eqId, eqVal]: any) => ({
          id: eqId,
          nombre: eqVal.nombre,
          jugadores: eqVal.jugadores
            ? Object.values(eqVal.jugadores)
            : []
        }))
        : [];
      this.equipos.set(equiposArr);


      const partidosArr = data.partidos
        ? Object.entries(data.partidos).map(([pId, pVal]: any) => ({
          id: pId, ...pVal,
          goles: pVal.goles || [],
          asistencias: pVal.asistencias || [],
          amarillas: pVal.amarillas || (pVal.amarilla ? [pVal.amarilla] : []),
          rojas: pVal.rojas || (pVal.roja ? [pVal.roja] : [])
        }))
        : [];
      this.partidos.set(partidosArr);

      const jornadaMap: Record<string, any[]> = {};
      partidosArr.forEach(p => {
        const j = p.jornada || 'Sin jornada';
        if (!jornadaMap[j]) jornadaMap[j] = [];
        jornadaMap[j].push(p);
      });
      this.partidosPorJornada.set(
        Object.entries(jornadaMap)
          .sort(([a], [b]) => {
            if (a === 'Sin jornada') return 1;
            if (b === 'Sin jornada') return -1;
            return parseInt(a) - parseInt(b);
          })
          .map(([jornada, partidos]) => ({ jornada, partidos }))
      );


      const tabla: Record<string, any> = {};
      equiposArr.forEach(e => {
        tabla[e.nombre] = { nombre: e.nombre, pj: 0, pg: 0, pe: 0, pp: 0, gf: 0, gc: 0, pts: 0 };
      });

      partidosArr.forEach(p => {
        const gl = parseInt(p.golesLocal) || 0;
        const gv = parseInt(p.golesVisitante) || 0;
        if (!tabla[p.equipoLocal]) tabla[p.equipoLocal] = { nombre: p.equipoLocal, pj: 0, pg: 0, pe: 0, pp: 0, gf: 0, gc: 0, pts: 0 };
        if (!tabla[p.equipoVisitante]) tabla[p.equipoVisitante] = { nombre: p.equipoVisitante, pj: 0, pg: 0, pe: 0, pp: 0, gf: 0, gc: 0, pts: 0 };

        tabla[p.equipoLocal].pj++; tabla[p.equipoVisitante].pj++;
        tabla[p.equipoLocal].gf += gl; tabla[p.equipoLocal].gc += gv;
        tabla[p.equipoVisitante].gf += gv; tabla[p.equipoVisitante].gc += gl;

        if (gl > gv) {
          tabla[p.equipoLocal].pg++; tabla[p.equipoLocal].pts += 3;
          tabla[p.equipoVisitante].pp++;
        } else if (gl < gv) {
          tabla[p.equipoVisitante].pg++; tabla[p.equipoVisitante].pts += 3;
          tabla[p.equipoLocal].pp++;
        } else {
          tabla[p.equipoLocal].pe++; tabla[p.equipoLocal].pts++;
          tabla[p.equipoVisitante].pe++; tabla[p.equipoVisitante].pts++;
        }
      });

      this.clasificacion.set(
        Object.values(tabla).sort((a: any, b: any) => b.pts - a.pts || (b.gf - b.gc) - (a.gf - a.gc))
      );


      const golesMap: Record<string, number> = {};
      const asistMap: Record<string, number> = {};
      const amarillasMap: Record<string, number> = {};
      const rojasMap: Record<string, number> = {};


      const limpiarNombre = (s: string) => s.replace(/\s*\(\d+'?\)/, '').replace(/\s*⚽🥅/, '').trim();

      partidosArr.forEach(p => {
        p.goles.forEach((j: string) => { const n = limpiarNombre(j); if (n) golesMap[n] = (golesMap[n] || 0) + 1; });
        p.asistencias.forEach((j: string) => { const n = limpiarNombre(j); if (n) asistMap[n] = (asistMap[n] || 0) + 1; });
        p.amarillas.forEach((j: string) => { const n = limpiarNombre(j); if (n) amarillasMap[n] = (amarillasMap[n] || 0) + 1; });
        p.rojas.forEach((j: string) => { const n = limpiarNombre(j); if (n) rojasMap[n] = (rojasMap[n] || 0) + 1; });
      });

      this.goleadores.set(Object.entries(golesMap).map(([n, g]) => ({ nombre: n, goles: g })).sort((a, b) => b.goles - a.goles));
      this.asistentes.set(Object.entries(asistMap).map(([n, a]) => ({ nombre: n, asistencias: a })).sort((a, b) => b.asistencias - a.asistencias));

      const todosConTarjetas = new Set([...Object.keys(amarillasMap), ...Object.keys(rojasMap)]);
      this.tarjetas.set(
        Array.from(todosConTarjetas).map(n => ({
          nombre: n,
          amarillas: amarillasMap[n] || 0,
          rojas: rojasMap[n] || 0
        })).sort((a, b) => b.rojas - a.rojas || b.amarillas - a.amarillas)
      );
    });
  }
 golesEquipo(partido: any, esLocal: boolean): string[] {
  const equipoNombre = esLocal ? partido.equipoLocal : partido.equipoVisitante;
  const equipo = this.equipos().find(e => e.nombre === equipoNombre);
  if (!equipo) return [];
  const nombresJugadores = equipo.jugadores.map((j: any) => j.nombre);
  return partido.goles.filter((g: string) => {
    const nombre = g.replace(/\s*\(\d+'?\)/, '').replace(/\s*⚽🥅/, '').trim();
    return nombresJugadores.includes(nombre);
  });
}
asistenciasEquipo(partido: any, esLocal: boolean): string[] {
  const equipoNombre = esLocal ? partido.equipoLocal : partido.equipoVisitante;
  const equipo = this.equipos().find(e => e.nombre === equipoNombre);
  if (!equipo) return [];
  const nombresJugadores = equipo.jugadores.map((j: any) => j.nombre);
  return partido.asistencias.filter((a: string) => {
    const nombre = a.replace(/\s*\(\d+'?\)/, '').replace(/\s*⚽🥅/, '').trim();
    return nombresJugadores.includes(nombre);
  });
}


  tarjetasEquipo(lista: string[], equipoNombre: string): string[] {
  const eq = this.equipos().find(e => e.nombre === equipoNombre);
  if (!eq) return [];
  const nombres = eq.jugadores.map((j: any) => j.nombre);
  return lista.filter((t: string) => {
    const nombre = t.replace(/\s*\(\d+'?\)/, '').trim();
    return nombres.includes(nombre);
  });
}
}