import { TestBed, ComponentFixture } from '@angular/core/testing';
import { of } from 'rxjs';
import { HomeComponent } from './home.component';
import { VotingSessionService } from '../../shared/services/voting-session.service';
import { SessionsByMember } from '../../model/sessions-by.member';
import { HomeModule } from './home.module';

describe('HomeComponent', () => {
  let component: HomeComponent;
  let fixture: ComponentFixture<HomeComponent>;
  let sessionServiceSpy: jasmine.SpyObj<VotingSessionService>;

  beforeEach(async () => {
    const sessionSpy = jasmine.createSpyObj('VotingSessionService', ['getSessionsCount']);

    await TestBed.configureTestingModule({
      declarations: [HomeComponent],
      imports: [HomeModule],
      providers: [
        { provide: VotingSessionService, useValue: sessionSpy }
      ]
    }).compileComponents();

    sessionServiceSpy = TestBed.inject(VotingSessionService) as jasmine.SpyObj<VotingSessionService>;

    fixture = TestBed.createComponent(HomeComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });


  it('should fetch sessions on init', () => {
    const mockSessions: SessionsByMember = {
      createdSessions: [],
      votedSessions: []
    } as unknown as SessionsByMember;

    sessionServiceSpy.getSessionsCount.and.returnValue(of(mockSessions));

    component.ngOnInit();
    fixture.detectChanges();

    expect(sessionServiceSpy.getSessionsCount).toHaveBeenCalled();
    expect(component.session).toEqual(mockSessions);
  });
});
