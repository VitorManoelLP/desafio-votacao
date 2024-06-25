type ExpirationType = 'MINUTES' | 'HOURS' | 'DAYS';

export interface Expiration {
  value: number;
  expirationType: ExpirationType;
}
