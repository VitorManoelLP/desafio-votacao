import { DatePipe } from '@angular/common';
import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'dataBR'
})

export class DataBRPipe implements PipeTransform {

  private datePipe: DatePipe = new DatePipe('en-US');

  transform(value: any, format: string = 'dd/MM/yyyy HH:mm:ss'): any {
    return this.datePipe.transform(value, format);
  }
}
