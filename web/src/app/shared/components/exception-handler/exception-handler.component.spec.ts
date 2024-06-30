import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExceptionHandlerComponent } from './exception-handler.component';

describe('ExceptionHandlerComponent', () => {
  let component: ExceptionHandlerComponent;
  let fixture: ComponentFixture<ExceptionHandlerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ExceptionHandlerComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ExceptionHandlerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
