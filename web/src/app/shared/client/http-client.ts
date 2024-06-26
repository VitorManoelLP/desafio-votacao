import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Request } from "./request";
import { Observable } from "rxjs";

@Injectable()
export class HttpClientHelper {

  private _resource?: string;

  constructor(private httpClient: HttpClient) {
  }

  get resource() {
    if (!this._resource) {
      throw new Error('Resource not defined');
    }
    return this._resource;
  }

  set init(resource: string) {
    this._resource = resource;
  }

  public get<T, S>(request: Request<T>): Observable<any> {
    return this.httpClient.get<S>(request.getRequest(this.resource).path);
  }

  public post<T, S>(request: Request<T>) {
    const requestBuild = request.getRequest(this.resource);
    return this.httpClient.post<S>(requestBuild.path, requestBuild.body);
  }

}
