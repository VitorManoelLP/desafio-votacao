import { Directive, ElementRef, Input, OnInit, Optional, Renderer2 } from '@angular/core';
import { FormControl, FormControlName } from '@angular/forms';

@Directive({ selector: '[form-validate]' })
export class FormValidateDirective implements OnInit {

  private validatorElement?: Element;
  private createdElements: any[] = [];

  constructor(@Optional() private formControlName: FormControlName, protected renderer: Renderer2, protected elementRef: ElementRef) {}

  ngOnInit(): void {
    this.createMessageValidatorElement();
    if (this.formControlName) {
      this.formControlName.statusChanges?.subscribe(status => {
        if (status === 'INVALID') {
          Object.keys(this.formControlName.errors ?? {}).forEach(error => {
            if (error === 'required') {
              const child = document.createTextNode('Campo obrigatório');
              this.createdElements.push(child);
              this.renderer.appendChild(this.validatorElement, child);
              this.renderer.addClass(this.elementRef.nativeElement, 'is-invalid')
            }
          });
        } else {
          this.renderer.removeClass(this.elementRef.nativeElement, 'is-invalid');
          this.createdElements.forEach(element => this.renderer.removeChild(this.validatorElement, element));
          this.createdElements = [];
        }
      });
    }
  }


  private createMessageValidatorElement() {
    this.validatorElement = this.renderer.createElement('div');
    this.renderer.addClass(this.validatorElement, 'invalid-feedback');
    this.renderer.appendChild(this.elementRef.nativeElement.parentNode, this.validatorElement);
  }

}
