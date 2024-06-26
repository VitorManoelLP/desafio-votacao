import { RegisterSession } from "../../../model/register-session";
import { VoteRequest } from "../../../model/vote-request";
import { HttpClientHelper } from "../../client/http-client";
import { Request } from "../../client/request";
import { VotingSessionService } from "../voting-session.service";

describe('VotingSessionService', () => {

  let votingSessionService: VotingSessionService;
  let httpClientHelper: jasmine.SpyObj<HttpClientHelper>;

  beforeEach(() => {
    httpClientHelper = jasmine.createSpyObj('HttpClientHelper', ['init', 'post', 'get']);
    votingSessionService = new VotingSessionService(httpClientHelper);
  });

  it('should call init endpoint', () => {
    const body = { description: 'Yes?' };
    votingSessionService.init(body);
    expect(httpClientHelper.post).toHaveBeenCalledWith(Request.of({ endpoint: 'init', body: body }));
  });

  it('should call vote endpoint', () => {
    const body = { session: '1111-2222-3333', voteOption: 'Não' } as VoteRequest;
    votingSessionService.vote(body);
    expect(httpClientHelper.post).toHaveBeenCalledWith(Request.of({ endpoint: 'vote/v1', body: body }));
  });

  it('should call count', () => {
    votingSessionService.count('1111-2222-3333');
    expect(httpClientHelper.get).toHaveBeenCalledWith(Request.of({ endpoint: 'count/v1/1111-2222-3333' }));
  });

  it('should call view', () => {
    votingSessionService.view('1111-2222-3333');
    expect(httpClientHelper.get).toHaveBeenCalledWith(Request.of({ endpoint: 'view/v1/1111-2222-3333' }));
  });

  it('should getSessions', () => {
    votingSessionService.getSessions();
    expect(httpClientHelper.get).toHaveBeenCalledWith(Request.of({}));
  });

});
