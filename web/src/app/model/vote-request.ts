type VoteOption = 'Sim' | 'Não';

export interface VoteRequest {
  session: string;
  voteOption: VoteOption
}
