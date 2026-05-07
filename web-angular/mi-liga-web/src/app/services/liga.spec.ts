import { TestBed } from '@angular/core/testing';

import { Liga } from './liga';

describe('Liga', () => {
  let service: Liga;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Liga);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
