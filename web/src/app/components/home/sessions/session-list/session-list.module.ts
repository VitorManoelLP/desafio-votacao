import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SessionListComponent } from './session-list.component';
import { PipeModule } from '../../../../shared/pipe/pipe.module';
import { PaginatorModule } from '../../../../shared/components/paginator/paginator.module';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { HttpClientHelper } from '../../../../shared/client/http-client';
import { VotingSessionService } from '../../../../shared/services/voting-session.service';
import { SessionDetailModule } from '../session-detail/session-detail.module';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  {
    path: ':type',
    component: SessionListComponent
  }
]

@NgModule({
  declarations: [
    SessionListComponent
  ],
  imports: [
    RouterModule.forChild(routes),
    CommonModule,
    PipeModule,
    PaginatorModule,
    ReactiveFormsModule,
    HttpClientModule,
    SessionDetailModule
  ],
  providers: [HttpClientHelper, VotingSessionService],
  exports: [SessionListComponent]
})
export class SessionListModule { }
