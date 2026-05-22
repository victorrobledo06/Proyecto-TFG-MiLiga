import { TestBed } from '@angular/core/testing';

import { LigaService } from './liga';

describe('Liga', () => {
  let service: LigaService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LigaService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
