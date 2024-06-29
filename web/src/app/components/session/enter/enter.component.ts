import { Component, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Expiration } from '../../../shared/utils/expiration-utils';

@Component({
  selector: 'app-enter',
  templateUrl: './enter.component.html',
  styleUrl: './enter.component.css'
})
export class EnterComponent implements OnDestroy {

  threeDaysInMilliseconds = 3 * 24 * 60 * 60 * 1000;

  formGroup: FormGroup;

  voteScreenActived = {
    opened: false,
    description: 'yes or no?',
    code: '1111-2222-3333',
    startedAt: Date.now(),
    endDate: Date.now() + this.threeDaysInMilliseconds,
    expiration: Expiration.create(Date.now() + this.threeDaysInMilliseconds)
  };

  constructor() {
    this.formGroup = new FormBuilder().group({
      'part1': [''],
      'part2': [''],
      'part3': ['']
    });
  }

  ngOnDestroy(): void {
    this.voteScreenActived.expiration.done();
  }

  public enterSession() {
    this.voteScreenActived.opened = true;
    this.voteScreenActived.expiration.init();
  }

  public vote(option: 'Sim' | 'Não') {

  }

  public leaveSession() {
    this.voteScreenActived.opened = false;
  }

  public onPaste(event: ClipboardEvent) {
    const data = event.clipboardData?.getData('text');
    const parts = data?.split('-');
    if (parts && parts?.length > 1) {
      this.pasteByHifen(parts);
    } else {
    }
  }

  private pasteByHifen(parts: string[] | undefined) {
    if (parts?.length === 3) {
      this.formGroup.patchValue({
        'part1': parts[0],
        'part2': parts[1],
        'part3': parts[2]
      });
    }
  }

}
