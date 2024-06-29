import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeComponent } from './home.component';
import { Route, RouterModule } from '@angular/router';
import { VotingSessionService } from '../../shared/services/voting-session.service';
import { HttpClientHelper } from '../../shared/client/http-client';

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
    RouterModule.forChild([routes])
  ],
  providers: [
    VotingSessionService,
    HttpClientHelper
  ]
})
export class HomeModule { }
