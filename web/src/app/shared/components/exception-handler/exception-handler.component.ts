import { Component, ElementRef, OnInit, ViewChild, effect } from '@angular/core';
import { ExceptionHandlerService } from '../../services/exception-handler.service';
import { Modal } from 'bootstrap';

@Component({
  selector: 'app-exception-handler',
  templateUrl: './exception-handler.component.html'
})
export class ExceptionHandlerComponent {

  @ViewChild('modal') modal!: ElementRef;

  modalProperties = {
    message: ''
  };

  constructor(private globalExceptionHandlerService: ExceptionHandlerService) {
    effect(() => {
      const exceptionError = this.globalExceptionHandlerService.errorHandle();
      if (exceptionError != '') {
        this.modalProperties = {
          message: exceptionError
        }
        const modal = new Modal(this.modal.nativeElement);
        modal.show();
      }
    });
  }

}
