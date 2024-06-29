import { SessionsByMember } from './../../model/sessions-by.member';
import { Component, OnInit } from '@angular/core';
import Profile from '../../shared/model/profile';
import { VotingSessionService } from '../../shared/services/voting-session.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {

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

  session?: SessionsByMember;

  profile: Profile = Profile.getInstance();

  constructor(private sessionService: VotingSessionService) {}

  ngOnInit(): void {
    this.sessionService.getSessions().subscribe(session => this.session = session);
  }

  public togglePanel(panel: string): void {
    this.expansivePanel[panel].opened = !this.expansivePanel[panel].opened;
  }

}
