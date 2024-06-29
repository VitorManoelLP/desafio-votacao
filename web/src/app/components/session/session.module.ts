import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CreateComponent } from './create/create.component';
import { EnterComponent } from './enter/enter.component';
import { RouterModule, Routes } from '@angular/router';
import { FormValidateModule } from '../../shared/directive/form-validate.module';
import { ReactiveFormsModule } from '@angular/forms';
import { VotingSessionService } from '../../shared/services/voting-session.service';
import { HttpClientHelper } from '../../shared/client/http-client';

const routes: Routes = [
  {
    path: '',
    component: CreateComponent
  },
  {
    path: 'create',
    component: CreateComponent
  },
  {
    path: 'enter',
    component: EnterComponent
  }
]

@NgModule({
  declarations: [
    CreateComponent,
    EnterComponent
  ],
  imports: [
    CommonModule,
    FormValidateModule,
    ReactiveFormsModule,
    RouterModule.forChild(routes)
  ],
  providers: [
    VotingSessionService,
    HttpClientHelper
  ]
})
export class SessionModule { }
