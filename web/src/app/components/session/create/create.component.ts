import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { VotingSessionService } from '../../../shared/services/voting-session.service';
import { RegisterSession } from '../../../model/register-session';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-create',
  templateUrl: './create.component.html',
  styleUrl: './create.component.css'
})
export class CreateComponent {

  readonly formGroup: FormGroup;
  generateCode = {
    has: false,
    code: '',
    copied: false
  };

  get registerSession(): RegisterSession {

    const payload: RegisterSession = {
      description: this.formGroup.get('description')?.value
    };

    if (this.formGroup.get('expirationValue')?.value) payload.expiration = {
      value: this.formGroup.get('expirationValue')?.value,
      expirationType: this.formGroup.get('expirationType')?.value != '' ? this.formGroup.get('expirationType')?.value : 'MINUTES'
    };

    return payload;
  }

  constructor(private sessionService: VotingSessionService, private toastrService: ToastrService) {
    this.formGroup = new FormBuilder().group({
      'description': ['', Validators.required],
      'expirationType': [''],
      'expirationValue': ['']
    });
  }

  public createSession() {
    this.sessionService.init(this.registerSession)
      .subscribe(session => {
        this.generateCode.code = session.sessionCode;
        this.generateCode.has = true;
        this.formGroup.reset();
        this.toastrService.success('Sessão criada com sucesso!');
      });
  }

  public createNew() {
    this.generateCode = {
      has: false,
      code: '',
      copied: false
    };
    this.formGroup.reset();
  }

  public copyCode() {
    if (!this.generateCode.copied) {
      navigator.clipboard.writeText(this.generateCode.code).then(() => this.generateCode.copied = true);
    }
  }

}
