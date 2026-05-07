import { Routes } from '@angular/router';
import { HomeComponent } from './components/home/home';
import { LigasComponent } from './components/ligas/ligas';
import { DetalleLigaComponent } from './components/detalle-liga/detalle-liga';
import { DetalleEquipoComponent } from './components/detalle-equipo/detalle-equipo';
import { DetallePartidoComponent } from './components/detalle-partido/detalle-partido';
import { NoticiasComponent } from './components/noticias/noticias';
import { CampeonesComponent } from './components/campeones/campeones';

export const routes: Routes = [
  { path: '', component: HomeComponent },               
  { path: 'ligas', component: LigasComponent },        
  { path: 'noticias', component: NoticiasComponent },   
  { path: 'campeones', component: CampeonesComponent },
  
  { path: 'ligas/:id', component: DetalleLigaComponent },
  { path: 'equipo/:id', component: DetalleEquipoComponent },
  { path: 'partido/:id', component: DetallePartidoComponent },

  { path: '**', redirectTo: '' } 
];