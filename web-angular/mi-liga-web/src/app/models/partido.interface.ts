export interface Partido {
  id: number;
  ligaId: number;
  local: string;
  localId: number;
  visitante: string;
  visitanteId: number;
  golesLocal: number;
  golesVisitante: number;
  jugado: boolean;
  
}