import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SessionDetailComponent } from './session-detail.component';
import { SessionDetailModule } from './session-detail.module';

describe('SessionDetailComponent', () => {
  let component: SessionDetailComponent;
  let fixture: ComponentFixture<SessionDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SessionDetailModule],
      declarations: [SessionDetailComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SessionDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
