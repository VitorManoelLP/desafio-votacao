import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HomeComponent } from './home.component';
import { TestUtils } from '../../shared/testing/test.utils';
import { HomeModule } from './home.module';

describe('HomeComponent', () => {
  let component: HomeComponent;
  let fixture: ComponentFixture<HomeComponent>;

  beforeEach(async () => {
    await TestUtils.configure(HomeModule, HomeComponent)
    .then(fixtureCreated => {
      fixture = fixtureCreated;
      component = fixtureCreated.componentInstance;
    });
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

});
