import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetalleLigaComponent } from './detalle-liga';

describe('DetalleLiga', () => {
  let component: DetalleLigaComponent;
  let fixture: ComponentFixture<DetalleLigaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DetalleLigaComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(DetalleLigaComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
