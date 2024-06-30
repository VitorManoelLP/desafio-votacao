import { animate, state, style, transition, trigger } from '@angular/animations';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { VotingSessionService } from '../../../../shared/services/voting-session.service';
import { CountReport } from '../../../../model/count-report';

@Component({
  selector: 'app-session-detail',
  templateUrl: './session-detail.component.html',
  styleUrl: './session-detail.component.css',
})
export class SessionDetailComponent implements OnInit {

  @Output() goBack: EventEmitter<void> = new EventEmitter();
  @Input({ required: true }) code!: string;

  sessionDetailed!: CountReport;

  constructor(private sessionService: VotingSessionService) {}

  ngOnInit(): void {
    this.sessionService.count(this.code).subscribe(session => this.sessionDetailed = session);
  }

  onBack() {
    this.goBack.emit();
  }

}
