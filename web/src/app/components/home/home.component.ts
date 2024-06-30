import { SessionsByMember } from './../../model/sessions-by.member';
import { Component, EventEmitter, OnInit } from '@angular/core';
import Profile from '../../shared/model/profile';
import { VotingSessionService } from '../../shared/services/voting-session.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {

  consultValue: 'CREATED' | 'VOTED' = 'CREATED';
  onRefresh: EventEmitter<'CREATED' | 'VOTED'> = new EventEmitter();

  session?: SessionsByMember;

  profile: Profile = Profile.getInstance();

  constructor(private sessionService: VotingSessionService) {}

  ngOnInit(): void {
    this.sessionService.getSessionsCount().subscribe(session => this.session = session);
  }

  public togglePanel(panel: 'CREATED' | 'VOTED'): void {
    this.onRefresh.emit(panel);
  }

}
