import { PageParameter } from "../model/page.parameter";

export class Request<T> {

  constructor(readonly endpoint?: string, readonly body?: T, readonly page?: PageParameter) {
    this.endpoint = endpoint;
    this.body = body;
    this.page = page;
  }

  public static of<T>(params: { endpoint?: string, body?: T, page?: PageParameter }) {
    return new Request(params.endpoint, params.body, params.page);
  }

  public getRequest(resource: string) {
    return {
      body: this.body,
      path: this.getEndpoint(resource)
    };
  }

  private getEndpoint(resource: string) {

    let finalEndpoint = resource;

    if (this.endpoint) {
      finalEndpoint += '/' + this.endpoint;
    }

    if (finalEndpoint?.includes('?') && this.page) {
      finalEndpoint += '&' + this.page.toQueryParams();
    }

    if (!finalEndpoint.includes('?') && this.page) {
      finalEndpoint += '?' + this.page?.toQueryParams();
    }


    return finalEndpoint;
  }

}
