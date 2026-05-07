/*export interface Liga {
  id: number;
  nombre: string;
  universidad: string;
  equipos: number;
  estado: 'en curso' | 'finalizada';
}*/
export interface Liga {
  id?: number;        // El ? es porque al crearla aún no tiene ID
  nombre: string;
  temporada: string;  // Añadimos esto que es lo que viene de Java
}