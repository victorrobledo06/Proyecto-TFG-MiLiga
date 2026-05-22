import { Injectable, signal } from '@angular/core';
import { db } from '../firebase.config';
import { ref, onValue } from 'firebase/database';
import { Liga } from '../models/liga.interface';

@Injectable({ providedIn: 'root' })
export class LigaService {

  private _ligas = signal<Liga[]>([]);

  constructor() {
    const ligasRef = ref(db, 'ligas');
    onValue(ligasRef, (snapshot) => {
      const data = snapshot.val();
      if (data) {
        const array = Object.entries(data).map(([id, valor]: any) => ({
          id,
          ...valor
        }));
        this._ligas.set(array);
      }
    });
  }

  getLigas() {
    return this._ligas();
  }
}