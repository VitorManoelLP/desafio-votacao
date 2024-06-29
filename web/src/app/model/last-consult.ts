import { VotingSessionInfo } from "./voting-session-info";

export interface LastConsult {
  sessionInfo: VotingSessionInfo,
  consultedAt: string;
}
