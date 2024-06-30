import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeComponent } from './home.component';
import { Route, RouterModule } from '@angular/router';
import { VotingSessionService } from '../../shared/services/voting-session.service';
import { HttpClientHelper } from '../../shared/client/http-client';
import { SessionListModule } from './sessions/session-list/session-list.module';
import { PipeModule } from '../../shared/pipe/pipe.module';

const routes: Route = {
  path: '',
  component: HomeComponent
};

@NgModule({
  declarations: [
    HomeComponent
  ],
  imports: [
    CommonModule,
    SessionListModule,
    PipeModule,
    RouterModule.forChild([routes])
  ],
  providers: [
    VotingSessionService,
    HttpClientHelper
  ]
})
export class HomeModule { }
