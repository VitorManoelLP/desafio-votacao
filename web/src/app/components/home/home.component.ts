import { Component } from '@angular/core';
import Profile from '../../shared/model/profile';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {

  profile: Profile = Profile.getInstance();

}
