import { TestBed, ComponentFixture } from '@angular/core/testing';
import { FormBuilder } from '@angular/forms';
import { CreateComponent } from './create.component';
import { VotingSessionService } from '../../../shared/services/voting-session.service';
import { ToastrService } from 'ngx-toastr';
import { of } from 'rxjs';
import { SessionModule } from '../session.module';

describe('CreateComponent', () => {
  let component: CreateComponent;
  let fixture: ComponentFixture<CreateComponent>;
  let sessionServiceSpy: jasmine.SpyObj<VotingSessionService>;
  let toastrServiceSpy: jasmine.SpyObj<ToastrService>;

  beforeEach(async () => {
    const sessionSpy = jasmine.createSpyObj('VotingSessionService', ['init']);
    const toastrSpy = jasmine.createSpyObj('ToastrService', ['success']);

    await TestBed.configureTestingModule({
      imports: [SessionModule],
      declarations: [CreateComponent],
      providers: [
        FormBuilder,
        { provide: VotingSessionService, useValue: sessionSpy },
        { provide: ToastrService, useValue: toastrSpy }
      ]
    }).compileComponents();

    sessionServiceSpy = TestBed.inject(VotingSessionService) as jasmine.SpyObj<VotingSessionService>;
    toastrServiceSpy = TestBed.inject(ToastrService) as jasmine.SpyObj<ToastrService>;

    fixture = TestBed.createComponent(CreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should create a session and show success message', () => {
    const mockSession = { sessionCode: '1234-5678-9012' };
    sessionServiceSpy.init.and.returnValue(of(mockSession));

    component.createSession();

    expect(sessionServiceSpy.init).toHaveBeenCalledWith({ description: '' });
    expect(component.generateCode.code).toEqual(mockSession.sessionCode);
    expect(component.generateCode.has).toBeTrue();
    expect(toastrServiceSpy.success).toHaveBeenCalledWith('Sessão criada com sucesso!');
  });

  it('should reset form and generateCode when createNew is called', () => {
    component.createNew();

    expect(component.generateCode.has).toBeFalse();
    expect(component.generateCode.code).toBe('');
    expect(component.generateCode.copied).toBeFalse();
    expect(component.formGroup.pristine).toBeTrue();
  });

  it('should copy code to clipboard and set copied to true', async () => {
    component.generateCode.code = '1234-5678-9012';
    spyOn(navigator.clipboard, 'writeText').and.returnValue(Promise.resolve());

    await component.copyCode();

    expect(navigator.clipboard.writeText).toHaveBeenCalledWith('1234-5678-9012');
    expect(component.generateCode.copied).toBeTrue();
  });
});
