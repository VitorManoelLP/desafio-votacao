import { Injectable } from "@angular/core";
import { HttpClientHelper } from "../client/http-client";
import { RegisterSession } from "../../model/register-session";
import { Request } from "../client/request";
import { Observable } from "rxjs";
import { RegisterSessionResponse } from "../../model/register-session.response";
import { CountReport } from "../../model/count-report";
import { VoteRequest } from "../../model/vote-request";
import { VotingSessionInfo } from "../../model/voting-session-info";
import { SessionsByMember } from "../../model/sessions-by.member";

@Injectable()
export class VotingSessionService {

  constructor(private httpClient: HttpClientHelper) {
    this.httpClient.init = '/api/session'
  }

  public init(body: RegisterSession): Observable<RegisterSessionResponse> {
    return this.httpClient.post<RegisterSession, RegisterSessionResponse>(Request.of<RegisterSession>({ endpoint: 'init', body: body }));
  }

  public vote(request: VoteRequest): Observable<void> {
    return this.httpClient.post(Request.of({ endpoint: 'vote/v1', body: request }));
  }

  public count(sessionCode: string): Observable<CountReport> {
    return this.httpClient.get(Request.of({ endpoint: `count/v1/${sessionCode}` }))
  }

  public view(sessionCode: string): Observable<VotingSessionInfo> {
    return this.httpClient.get(Request.of({ endpoint: `view/v1/${sessionCode}` }));
  }

  public getSessions(): Observable<SessionsByMember> {
    return this.httpClient.get(Request.of({}))
  }

}
