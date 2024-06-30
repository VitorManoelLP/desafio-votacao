export class PageParameter {

  page: number;
  size: number;

  constructor(size: number, page: number) {
    this.size = size;
    this.page = page;
  }

  static withSize(size: number) {
    return new PageParameter(size, 0);
  }

  public toQueryParams() {
    return `page=${this.page}&size=${this.size}`;
  }

}
