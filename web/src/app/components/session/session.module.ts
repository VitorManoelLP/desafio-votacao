import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CreateComponent } from './create/create.component';
import { EnterComponent } from './enter/enter.component';
import { RouterModule, Routes } from '@angular/router';

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
    RouterModule.forChild(routes)
  ]
})
export class SessionModule { }
