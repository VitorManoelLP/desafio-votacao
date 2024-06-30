import { WritableSignal, signal } from '@angular/core';
import { Component, EventEmitter, Input, OnDestroy, OnInit } from '@angular/core';
import { VotingSessionService } from '../../../../shared/services/voting-session.service';
import { VotingSessionInfo } from '../../../../model/voting-session-info';
import { Page } from '../../../../shared/model/page';
import { Subscription } from 'rxjs';
import { PageParameter } from '../../../../shared/model/page.parameter';
import { FormControl } from '@angular/forms';

@Component({
  selector: 'app-session-list',
  templateUrl: './session-list.component.html',
  styleUrl: './session-list.component.css'
})
export class SessionListComponent implements OnInit, OnDestroy {

  @Input({ required: true })
  refresh!: EventEmitter<'CREATED' | 'VOTED'>;

  page?: Page<VotingSessionInfo>;
  typeSelected: 'CREATED' | 'VOTED' = 'CREATED';

  get signalPage() {
    return this.onPageChange.asReadonly();
  }

  formFilter: FormControl = new FormControl('');
  pageParameter: PageParameter = PageParameter.withSize(5);

  private onPageChange: WritableSignal<Page<any> | null> = signal<Page<any> | null>(null);
  private subscription?: Subscription;

  constructor(private sessionService: VotingSessionService) {
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  ngOnInit(): void {
    this.subscription = this.refresh.subscribe(type => this.search(type))
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
}
