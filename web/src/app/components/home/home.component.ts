import { Component } from '@angular/core';
import Profile from '../../shared/model/profile';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {

  readonly expansivePanel: { [key: string]: { opened: boolean, content: any[] } } = {
    'CREATED_SESSION': {
      opened: false,
      content: []
    },
    'VOTED_SESSION': {
      opened: false,
      content: []
    }
  }

  profile: Profile = Profile.getInstance();

  public togglePanel(panel: string): void {
    this.expansivePanel[panel].opened = !this.expansivePanel[panel].opened;
  }

}
