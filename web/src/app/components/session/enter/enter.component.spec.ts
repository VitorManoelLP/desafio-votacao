import { TestBed, ComponentFixture } from '@angular/core/testing';
import { FormBuilder } from '@angular/forms';
import { EnterComponent } from './enter.component';
import { VotingSessionService } from '../../../shared/services/voting-session.service';
import { ToastrService } from 'ngx-toastr';
import { of, throwError } from 'rxjs';
import { Expiration } from '../../../shared/utils/expiration-utils';
import { VotingSessionInfo } from '../../../model/voting-session-info';
import { SessionModule } from '../session.module';
import { LastConsult } from '../../../model/last-consult';

describe('EnterComponent', () => {
  let component: EnterComponent;
  let fixture: ComponentFixture<EnterComponent>;
  let sessionServiceSpy: jasmine.SpyObj<VotingSessionService>;
  let toastrServiceSpy: jasmine.SpyObj<ToastrService>;


  beforeEach(async () => {
    const sessionSpy = jasmine.createSpyObj('VotingSessionService', ['view', 'vote', 'getLastConsultedSession']);
    const toastrSpy = jasmine.createSpyObj('ToastrService', ['error']);

    await TestBed.configureTestingModule({
      imports: [SessionModule],
      declarations: [EnterComponent],
      providers: [
        FormBuilder,
        { provide: VotingSessionService, useValue: sessionSpy },
        { provide: ToastrService, useValue: toastrSpy }
      ]
    }).compileComponents();

    sessionServiceSpy = TestBed.inject(VotingSessionService) as jasmine.SpyObj<VotingSessionService>;
    toastrServiceSpy = TestBed.inject(ToastrService) as jasmine.SpyObj<ToastrService>;

    fixture = TestBed.createComponent(EnterComponent);
    component = fixture.componentInstance;

    const mockLastConsult: LastConsult = {
      consultedAt: new Date().toISOString(),
      sessionInfo: {
        topic: 'Test Topic',
        code: '1234-5678-9012',
        closeAt: new Date().toISOString(),
        alreadyVote: false,
        isOpen: true,
        yourVote: '',
        openedAt: ''
      }
    };
    sessionServiceSpy.getLastConsultedSession.and.returnValue(of(mockLastConsult));

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize last consulted session on init', () => {
    const mockLastConsult: LastConsult = {
      consultedAt: new Date().toISOString(),
      sessionInfo: {
        topic: 'Test Topic',
        code: '1234-5678-9012',
        closeAt: new Date().toISOString(),
        alreadyVote: false,
        isOpen: true,
        yourVote: '',
        openedAt: ''
      }
    };

    sessionServiceSpy.getLastConsultedSession.and.returnValue(of(mockLastConsult));

    component.ngOnInit();
    expect(sessionServiceSpy.getLastConsultedSession).toHaveBeenCalled();
    expect(component.lastConsultedSession).toEqual(mockLastConsult);
  });

  it('should vote and handle error', () => {
    const errorMessage = 'Error occurred';
    sessionServiceSpy.vote.and.returnValue(throwError(() => ({ message: errorMessage })));

    component.voteScreenActived = {
      opened: true,
      session: {
        isOpen: true,
        openedAt: '',
        yourVote: 'Não',
        closeAt: '',
        topic: 'Test Topic',
        code: '1234-5678-9012',
        alreadyVote: false
      },
      expiration: undefined,
    };

    component.vote('Sim');

    expect(sessionServiceSpy.vote).toHaveBeenCalledWith({ session: component.code, voteOption: 'Sim' });
    expect(toastrServiceSpy.error).toHaveBeenCalledWith(errorMessage, 'Ocorreu um erro ao votar');
  });

  it('should leave session and reset form', () => {
    component.voteScreenActived = {
      opened: true,
      session: {
        isOpen: true,
        openedAt: '',
        yourVote: 'Não',
        closeAt: '',
        topic: 'Test Topic',
        code: '1234-5678-9012',
        alreadyVote: false
      },
      expiration: jasmine.createSpyObj('Expiration', ['done']),
    };

    component.leaveSession();

    expect(component.voteScreenActived?.expiration?.done).toHaveBeenCalled();
    expect(component.voteScreenActived?.opened).toBeFalse();
    expect(component.formGroup.pristine).toBeTrue();
    expect(component.lastConsultedSession?.sessionInfo.code).toEqual('1234-5678-9012');
  });

  it('should handle paste event correctly', () => {
    const clipboardEvent = {
      clipboardData: {
        getData: () => '1234-5678-9012'
      }
    } as unknown as ClipboardEvent;

    component.onPaste(clipboardEvent);

    expect(component.formGroup.get('part1')?.value).toBe('1234');
    expect(component.formGroup.get('part2')?.value).toBe('5678');
    expect(component.formGroup.get('part3')?.value).toBe('9012');
  });

  it('should handle invalid paste event correctly', () => {
    const clipboardEvent = {
      clipboardData: {
        getData: () => '123456789012'
      }
    } as unknown as ClipboardEvent;

    component.onPaste(clipboardEvent);

    expect(component.formGroup.get('part1')?.value).toBe('');
    expect(component.formGroup.get('part2')?.value).toBe('');
    expect(component.formGroup.get('part3')?.value).toBe('');
  });
});
