import { ComponentFixture, TestBed } from '@angular/core/testing';
import { PaginatorComponent } from './paginator.component';
import { Page } from '../../model/page';
import { Signal, signal } from '@angular/core';
import PaginatorConfiguration from '../../model/paginator.config';

describe('PaginatorComponent', () => {
  let component: PaginatorComponent;
  let fixture: ComponentFixture<PaginatorComponent>;

  let mockPage: Page<any>;
  let mockConfig: PaginatorConfiguration;
  let mockSignal: Signal<Page<any> | null>;

  beforeEach(async () => {
    mockPage = {
      content: [],
      empty: false,
      first: true,
      last: false,
      number: 0,
      size: 10,
      totalElements: 30,
      totalPages: 3
    };

    mockSignal = signal(mockPage);

    mockConfig = {
      onPageChange: mockSignal,
      itensPeerPage: [10, 20, 30]
    };

    await TestBed.configureTestingModule({
      declarations: [PaginatorComponent]
    }).compileComponents();

    fixture = TestBed.createComponent(PaginatorComponent);
    component = fixture.componentInstance;
    component.config = mockConfig;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should define initial page numbers', () => {
    expect(component.pageNumbers.length).toBe(3);
    expect(component.pageNumbers[0].number).toBe(1);
    expect(component.pageNumbers[0].active).toBe(true);
  });

  it('should call onPageChange and define page numbers on initialization', () => {
    component.config.onPageChange();
    expect(component.pageNumbers.length).toBe(3);
  });

  it('should change page and emit page changes', () => {
    spyOn(component.pageChanges, 'next');
    const pageSelected = { number: 2, active: false };

    component.changePage(pageSelected);

    expect(component.pageParameter.page).toBe(1);
    expect(component.pageChanges.next).toHaveBeenCalledWith(component.pageParameter);
  });

  it('should change page size and emit page changes', () => {
    spyOn(component.pageChanges, 'next');
    const event = { target: { value: 20 } };

    component.changePageSize(event);

    expect(component.pageParameter.size).toBe(20);
    expect(component.pageParameter.page).toBe(0);
    expect(component.pageChanges.next).toHaveBeenCalledWith(component.pageParameter);
  });

  it('should go to first page and emit page changes', () => {
    spyOn(component.pageChanges, 'next');

    component.goToFirstPage();

    expect(component.pageParameter.page).toBe(0);
    expect(component.pageChanges.next).toHaveBeenCalledWith(component.pageParameter);
  });

  it('should go to last page and emit page changes', () => {
    spyOn(component.pageChanges, 'next');
    component.config.onPageChange = signal(mockPage);

    component.goToLastPage();

    expect(component.pageParameter.page).toBe(2);
    expect(component.pageChanges.next).toHaveBeenCalledWith(component.pageParameter);
  });
});
