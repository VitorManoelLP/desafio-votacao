import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, catchError, throwError } from 'rxjs';
import { ExceptionHandlerService } from './exception-handler.service';

@Injectable()
export class ErrorHandlerService implements HttpInterceptor {

  constructor(private exceptionHandler: ExceptionHandlerService) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(catchError((error: HttpErrorResponse) => {

      if (!error.error) {
        this.exceptionHandler.handle = error.message;
      } else {
        this.exceptionHandler.handle = error.error['message'];
      }

      return throwError(() => error);
    }));
  }

}
