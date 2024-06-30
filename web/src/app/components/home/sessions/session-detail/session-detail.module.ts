import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SessionDetailComponent } from './session-detail.component';
import { VotingSessionService } from '../../../../shared/services/voting-session.service';
import { HttpClientHelper } from '../../../../shared/client/http-client';
import { HttpClientModule } from '@angular/common/http';

@NgModule({
  declarations: [
    SessionDetailComponent
  ],
  exports: [SessionDetailComponent],
  imports: [
    CommonModule,
    HttpClientModule
  ],
  providers: [
    VotingSessionService, HttpClientHelper
  ]
})
export class SessionDetailModule { }
