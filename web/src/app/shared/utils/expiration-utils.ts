export class Expiration {

  private oneSecond: number = 1000;
  private intervalReference?: any;
  private _value = '00:00:00';

  get value() {
    return this._value;
  }

  constructor(private readonly end: number) {
    this.end = end;
  }

  static create(end: number) {
    return new Expiration(end);
  }

  init() {
    this.intervalReference = setInterval(() => {
      const distance = this.end - Date.now();


      if (distance < 0) {
        this.done();
        this._value = 'Expirado';
        return;
      }

      const hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
      const minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
      const seconds = Math.floor((distance % (1000 * 60)) / 1000);

      this._value = `${this.pad(hours)}:${this.pad(minutes)}:${this.pad(seconds)}`;

      console.log(this._value);


    }, this.oneSecond);
  }

  private pad(num: number): string {
    return num < 10 ? '0' + num : num.toString();
  }

  done() {
    clearInterval(this.intervalReference);
  }

}
