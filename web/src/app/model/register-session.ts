import { Expiration } from './expiration';

export interface RegisterSession {
  description: string;
  expiration?: Expiration;
}
