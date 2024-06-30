import { WritableSignal, signal } from '@angular/core';
import { Component, Input, OnInit } from '@angular/core';
import { VotingSessionService } from '../../../../shared/services/voting-session.service';
import { VotingSessionInfo } from '../../../../model/voting-session-info';
import { Page } from '../../../../shared/model/page';
import { PageParameter } from '../../../../shared/model/page.parameter';
import { FormControl } from '@angular/forms';
import { animate, style, transition, trigger } from '@angular/animations';

@Component({
  selector: 'app-session-list',
  templateUrl: './session-list.component.html',
  styleUrl: './session-list.component.css',
  animations: [
    trigger('slideInOut', [
      transition(':enter', [
        style({ transform: 'translateX(100%)', position: 'absolute', top: 0, left: 0, width: '100%', height: '100%' }),
        animate('300ms ease-in')
      ]),
      transition(':leave', [
        animate('300ms ease-in', style({ transform: 'translateX(-100%)', position: 'absolute', top: 0, left: 0, width: '100%', height: '100%' }))
      ])
    ])
  ]
})
export class SessionListComponent implements OnInit {

  @Input() type!: 'CREATED' | 'VOTED';

  page?: Page<VotingSessionInfo>;
  typeSelected: 'CREATED' | 'VOTED' = 'CREATED';
  detailSession?: string | null;

  get signalPage() {
    return this.onPageChange.asReadonly();
  }

  formFilter: FormControl = new FormControl('');
  pageParameter: PageParameter = PageParameter.withSize(5);

  private onPageChange: WritableSignal<Page<any> | null> = signal<Page<any> | null>(null);

  constructor(private sessionService: VotingSessionService) {
  }

  ngOnInit(): void {
    this.search(this.type, '');
  }

  onFilter() {
    this.pageParameter = PageParameter.withSize(5);
    this.search(this.typeSelected, this.formFilter?.value);
  }

  pageChange(page: PageParameter) {
    this.pageParameter = page;
    this.search(this.typeSelected);
  }

  search(type: 'CREATED' | 'VOTED' = 'CREATED', search: string = '') {
    this.typeSelected = type;
    this.sessionService.getSessions(search, type, this.pageParameter)?.subscribe(page => {
      this.page = page;
      this.onPageChange.set(page);
    });
  }

  hideDetail() {
    this.detailSession = null;
  }

  detail(code: string) {
    this.detailSession = code;
  }

}
