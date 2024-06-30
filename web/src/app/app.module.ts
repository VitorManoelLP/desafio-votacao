import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { KeycloakAngularModule } from 'keycloak-angular';
import { RouterModule } from '@angular/router';
import { routes } from './app.routes';
import { NavbarComponent } from './shared/components/navbar/navbar.component';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { KeycloakInterceptor } from './shared/security/keycloak.interceptor';
import { ToastrModule } from 'ngx-toastr';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ExceptionHandlerModule } from './shared/components/exception-handler/exception-handler.module';
import { ErrorHandlerService } from './shared/services/error-handler.service';
import { ExceptionHandlerService } from './shared/services/exception-handler.service';


@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    RouterModule.forRoot(routes, { bindToComponentInputs: true }),
    BrowserAnimationsModule,
    NavbarComponent,
    AppRoutingModule,
    ExceptionHandlerModule,
    KeycloakAngularModule,
    HttpClientModule,
    ToastrModule.forRoot()
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: KeycloakInterceptor,
      multi: true
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: ErrorHandlerService,
      multi: true,
      deps: [ExceptionHandlerService]
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
