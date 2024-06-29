import { TestBed, ComponentFixture } from '@angular/core/testing';
import { FormBuilder } from '@angular/forms';
import { EnterComponent } from './enter.component';
import { VotingSessionService } from '../../../shared/services/voting-session.service';
import { ToastrService } from 'ngx-toastr';
import { of, throwError } from 'rxjs';
import { Expiration } from '../../../shared/utils/expiration-utils';
import { VotingSessionInfo } from '../../../model/voting-session-info';
import { SessionModule } from '../session.module';

describe('EnterComponent', () => {
  let component: EnterComponent;
  let fixture: ComponentFixture<EnterComponent>;
  let sessionServiceSpy: jasmine.SpyObj<VotingSessionService>;
  let toastrServiceSpy: jasmine.SpyObj<ToastrService>;

  beforeEach(async () => {
    const sessionSpy = jasmine.createSpyObj('VotingSessionService', ['view', 'vote']);
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
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should enter a session and initialize expiration', () => {
    const mockSession: VotingSessionInfo = {
      topic: 'Test Topic',
      code: '1234-5678-9012',
      closeAt: new Date(Date.now() + 3 * 24 * 60 * 60 * 1000).toISOString(),
      alreadyVote: false,
      isOpen: true
    } as VotingSessionInfo;

    sessionServiceSpy.view.and.returnValue(of(mockSession));

    component.enterSession();

    expect(sessionServiceSpy.view).toHaveBeenCalledWith(component.code);
    expect(component.voteScreenActived?.description).toEqual(mockSession.topic);
    expect(component.voteScreenActived?.code).toEqual(component.code);
    expect(component.voteScreenActived?.opened).toBeTrue();
    expect(component.voteScreenActived?.expired).toBeFalse();
    expect(component.voteScreenActived?.alreadyVoted).toBeFalse();

    spyOn(Expiration.prototype, 'init');
    component.voteScreenActived?.expiration?.init();
    expect(Expiration.prototype.init).toHaveBeenCalled();
  });

  it('should vote and handle success', () => {
    sessionServiceSpy.vote.and.callFake(() => of());

    component.voteScreenActived = {
      expired: false,
      opened: true,
      description: 'Test Topic',
      code: '1234-5678-9012',
      expiration: undefined,
      alreadyVoted: false
    };

    component.vote('Sim');

    expect(sessionServiceSpy.vote).toHaveBeenCalledWith({ session: component.code, voteOption: 'Sim' });
  });

  it('should vote and handle error', () => {
    const errorMessage = 'Error occurred';
    sessionServiceSpy.vote.and.returnValue(throwError({ message: errorMessage }));

    component.voteScreenActived = {
      expired: false,
      opened: true,
      description: 'Test Topic',
      code: '1234-5678-9012',
      expiration: undefined,
      alreadyVoted: false
    };

    component.vote('Sim');

    expect(sessionServiceSpy.vote).toHaveBeenCalledWith({ session: component.code, voteOption: 'Sim' });
    expect(toastrServiceSpy.error).toHaveBeenCalledWith(errorMessage, 'Ocorreu um erro ao votar');
  });

  it('should leave session and reset form', () => {
    component.voteScreenActived = {
      expired: false,
      opened: true,
      description: 'Test Topic',
      code: '1234-5678-9012',
      expiration: jasmine.createSpyObj('Expiration', ['done']),
      alreadyVoted: false
    };

    component.leaveSession();

    expect(component.voteScreenActived?.expiration?.done).toHaveBeenCalled();
    expect(component.voteScreenActived?.opened).toBeFalse();
    expect(component.formGroup.pristine).toBeTrue();
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
