import { Component, EventEmitter, Input, OnInit, Output, effect } from '@angular/core';
import PaginatorConfiguration from '../../model/paginator.config';
import { Page } from '../../model/page';
import { PageParameter } from '../../model/page.parameter';

@Component({
  selector: 'app-paginator',
  templateUrl: './paginator.component.html',
  styleUrl: './paginator.component.css'
})
export class PaginatorComponent {

  @Input({ required: true }) config!: PaginatorConfiguration;
  @Output() pageChanges: EventEmitter<PageParameter> = new EventEmitter();

  pageParameter!: PageParameter;;
  pageNumbers: { number: number, active: boolean }[] = [];

  constructor() {
    effect(() => {
      const initialPage: Page<any> | null = this.config.onPageChange();
      this.definePageNumbers(initialPage);
    });
  }

  public goToFirstPage() {
    this.pageParameter.page = 0;
    this.pageChanges.next(this.pageParameter);
  }

  public goToLastPage() {
    if (this.config.onPageChange()) {
      this.pageParameter.page = (this.config.onPageChange()?.totalPages ?? 1) - 1;
      this.pageChanges.next(this.pageParameter);
    }
  }

  public changePage(pageSelected: { number: number, active: boolean }) {
    this.pageParameter.page = pageSelected.number - 1;
    this.pageChanges.next(this.pageParameter);
  }

  public changePageSize(event: any) {
    this.pageParameter.size = event.target.value;
    this.pageParameter.page = 0;
    this.pageChanges.next(this.pageParameter);
  }

  private definePageNumbers(initialPage: Page<any> | null) {

    if (!this.pageParameter) {
      this.pageParameter = PageParameter.withSize(this.config.itensPeerPage[0])
    }

    this.pageNumbers = [];

    if (!initialPage || initialPage?.empty) {
      this.pageNumbers.push({ number: 1, active: true });
      return;
    }

    if (initialPage.first) {
      this.defineFirstPage(initialPage);
      return;
    }

    if (initialPage.last) {
      this.defineLastPageParams(initialPage);
      return;
    }

    if (initialPage.number + 2 === initialPage.totalPages || initialPage.number - 1 === 0) {
      this.pageNumbers.push({ number: initialPage.number, active: false });
      this.pageNumbers.push({ number: initialPage.number + 1, active: true });
      this.pageNumbers.push({ number: initialPage.number + 2, active: false });
      return;
    }

    this.pageNumbers.push({ number: initialPage.number + 1, active: true });
    this.pageNumbers.push({ number: initialPage.number + 2, active: false });
    this.pageNumbers.push({ number: initialPage.number + 3, active: false });
  }

  private defineFirstPage(initialPage: Page<any>) {
    for (let i = 0; i <= initialPage.totalPages - 1 && i != 3; i++) {
      this.pageNumbers.push({
        active: i == initialPage.number,
        number: i + 1
      });
    }
  }

  private defineLastPageParams(initialPage: Page<any>) {
    const startPage = Math.max(0, initialPage.totalPages - 3);
    for (let i = startPage; i < initialPage.totalPages; i++) {
      this.pageNumbers.push({
        active: i === initialPage.number,
        number: i + 1
      });
    }
  }

}
