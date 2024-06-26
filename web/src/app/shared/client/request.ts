export class Request<T> {

  constructor(readonly endpoint?: string, readonly body?: T) {
    this.endpoint = endpoint;
    this.body = body;
  }

  public static of<T>(params: { endpoint?: string, body?: T }) {
    return new Request(params.endpoint, params.body);
  }

  public getRequest(resource: string) {
    return {
      body: this.body,
      path: `${resource}${this.endpoint ? '/' + this.endpoint : ''}`
    };
  }

}
