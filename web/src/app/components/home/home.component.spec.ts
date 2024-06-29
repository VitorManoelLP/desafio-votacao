import { TestBed, ComponentFixture } from '@angular/core/testing';
import { of } from 'rxjs';
import { HomeComponent } from './home.component';
import { VotingSessionService } from '../../shared/services/voting-session.service';
import { SessionsByMember } from '../../model/sessions-by.member';
import Profile from '../../shared/model/profile';

describe('HomeComponent', () => {
  let component: HomeComponent;
  let fixture: ComponentFixture<HomeComponent>;
  let sessionServiceSpy: jasmine.SpyObj<VotingSessionService>;

  beforeEach(async () => {
    const sessionSpy = jasmine.createSpyObj('VotingSessionService', ['getSessions']);

    await TestBed.configureTestingModule({
      declarations: [HomeComponent],
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

  it('should initialize profile and expansivePanel correctly', () => {
    expect(component.profile).toBe(Profile.getInstance());
    expect(component.expansivePanel).toEqual({
      'CREATED_SESSION': {
        opened: false,
        content: []
      },
      'VOTED_SESSION': {
        opened: false,
        content: []
      }
    });
  });

  it('should toggle panel state correctly', () => {
    component.togglePanel('CREATED_SESSION');
    expect(component.expansivePanel['CREATED_SESSION'].opened).toBeTrue();

    component.togglePanel('CREATED_SESSION');
    expect(component.expansivePanel['CREATED_SESSION'].opened).toBeFalse();
  });

  it('should fetch sessions on init', () => {
    const mockSessions: SessionsByMember = {
      createdSessions: [],
      votedSessions: []
    } as unknown as SessionsByMember;

    sessionServiceSpy.getSessions.and.returnValue(of(mockSessions));

    component.ngOnInit();
    fixture.detectChanges();

    expect(sessionServiceSpy.getSessions).toHaveBeenCalled();
    expect(component.session).toEqual(mockSessions);
  });
});
