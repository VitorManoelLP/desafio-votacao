import { Expiration } from './expiration';

export interface RegisterSession {
  description: NamedCurve;
  expiration: Expiration;
}
