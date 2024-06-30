import { AfterViewInit, Component, ElementRef, EventEmitter, OnInit, ViewChild } from '@angular/core';
import { Modal } from 'bootstrap';
import { VotingSessionService } from '../../shared/services/voting-session.service';
import { SessionsByMember } from '../../model/sessions-by.member';
import Profile from '../../shared/model/profile';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  consultValue: 'CREATED' | 'VOTED' = 'CREATED';
  session?: SessionsByMember;
  profile: Profile = Profile.getInstance();
  showModal: boolean = false;

  constructor(private sessionService: VotingSessionService, private router: Router) { }

  ngOnInit(): void {
    this.sessionService.getSessionsCount().subscribe(session => this.session = session);
  }

  togglePanel(type: 'CREATED' | 'VOTED') {
    this.router.navigateByUrl('session-list/' + type);
  }

}
