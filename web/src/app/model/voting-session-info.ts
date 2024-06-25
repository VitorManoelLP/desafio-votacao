export interface VotingSessionInfo {
  topic: string;
  code: string;
  isOpen: boolean;
  alreadyVote: boolean;
  yourVote: string;
  openedAt: Date;
  closeAt: Date;
}
