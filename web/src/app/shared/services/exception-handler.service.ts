import { Injectable, WritableSignal, signal } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ExceptionHandlerService {

  errorHandle: WritableSignal<string> = signal('');

  set handle(message: string) {
    this.errorHandle.set(message);
  }

  constructor() { }


}
