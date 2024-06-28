import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

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

  constructor() {
    this.formGroup = new FormBuilder().group({
      'description': ['', Validators.required]
    });
  }

  public createSession() {
    this.generateCode.has = true;
    this.generateCode.code = '2222-3333-4444';
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
