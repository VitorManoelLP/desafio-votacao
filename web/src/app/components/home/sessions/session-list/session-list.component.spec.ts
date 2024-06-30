import { ComponentFixture, TestBed } from '@angular/core/testing';
import { SessionListComponent } from './session-list.component';
import { VotingSessionService } from '../../../../shared/services/voting-session.service';
import { VotingSessionInfo } from '../../../../model/voting-session-info';
import { Page } from '../../../../shared/model/page';
import { PageParameter } from '../../../../shared/model/page.parameter';
import { EventEmitter } from '@angular/core';
import { of } from 'rxjs';
import { SessionListModule } from './session-list.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

describe('SessionListComponent', () => {
  let component: SessionListComponent;
  let fixture: ComponentFixture<SessionListComponent>;
  let votingSessionServiceSpy: jasmine.SpyObj<VotingSessionService>;

  beforeEach(async () => {
    const spy = jasmine.createSpyObj('VotingSessionService', ['getSessions']);

    await TestBed.configureTestingModule({
      declarations: [SessionListComponent],
      imports: [SessionListModule, BrowserAnimationsModule],
      providers: [
        { provide: VotingSessionService, useValue: spy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(SessionListComponent);
    component = fixture.componentInstance;
    votingSessionServiceSpy = TestBed.inject(VotingSessionService) as jasmine.SpyObj<VotingSessionService>;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize with default values', () => {
    expect(component.typeSelected).toBe('CREATED');
    expect(component.formFilter.value).toBe('');
  });

  it('should call search with filter value on filter', () => {
    const mockPage: Page<VotingSessionInfo> = {
      content: [],
      empty: false,
      first: true,
      last: false,
      number: 0,
      size: 5,
      totalElements: 0,
      totalPages: 1
    };

    votingSessionServiceSpy.getSessions.and.returnValue(of(mockPage));
    component.formFilter.setValue('test topic');
    component.onFilter();
    expect(votingSessionServiceSpy.getSessions).toHaveBeenCalledWith('test topic', 'CREATED', component.pageParameter);
    expect(component.page).toBe(mockPage);
    expect(component.signalPage()).toBe(mockPage);
  });

  it('should call search with correct parameters on page change', () => {
    const mockPage: Page<VotingSessionInfo> = {
      content: [],
      empty: false,
      first: true,
      last: false,
      number: 0,
      size: 5,
      totalElements: 0,
      totalPages: 1
    };

    votingSessionServiceSpy.getSessions.and.returnValue(of(mockPage));
    const pageParameter = PageParameter.withSize(10);
    component.pageChange(pageParameter);
    expect(votingSessionServiceSpy.getSessions).toHaveBeenCalledWith('', 'CREATED', pageParameter);
    expect(component.pageParameter).toBe(pageParameter);
    expect(component.page).toBe(mockPage);
    expect(component.signalPage()).toBe(mockPage);
  });

  it('should update page and signalPage on search', () => {
    const mockPage: Page<VotingSessionInfo> = {
      content: [],
      empty: false,
      first: true,
      last: false,
      number: 0,
      size: 5,
      totalElements: 0,
      totalPages: 1
    };

    votingSessionServiceSpy.getSessions.and.returnValue(of(mockPage));
    component.search('CREATED', 'test topic');
    expect(votingSessionServiceSpy.getSessions).toHaveBeenCalledWith('test topic', 'CREATED', component.pageParameter);
    expect(component.page).toBe(mockPage);
    expect(component.signalPage()).toBe(mockPage);
  });
});
