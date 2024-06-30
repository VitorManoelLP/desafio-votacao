import { Signal } from "@angular/core";
import { Page } from "./page";

export default interface PaginatorConfiguration {
  onPageChange: Signal<Page<any> | null>;
  itensPeerPage: number[];
}
