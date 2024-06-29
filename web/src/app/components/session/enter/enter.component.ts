import { Component, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Expiration } from '../../../shared/utils/expiration-utils';
import { VotingSessionService } from '../../../shared/services/voting-session.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-enter',
  templateUrl: './enter.component.html',
  styleUrl: './enter.component.css'
})
export class EnterComponent implements OnDestroy {

  threeDaysInMilliseconds = 3 * 24 * 60 * 60 * 1000;
  formGroup: FormGroup;
  voteScreenActived?: { expired: boolean, opened: boolean, description: string, code: string, expiration: Expiration | undefined, alreadyVoted: boolean };

  get code() {
    return `${this.formGroup.get('part1')?.value}-${this.formGroup.get('part2')?.value}-${this.formGroup.get('part3')?.value}`
  }

  constructor(private sessionService: VotingSessionService, private toastrService: ToastrService) {
    this.formGroup = new FormBuilder().group({
      'part1': [''],
      'part2': [''],
      'part3': ['']
    });
  }

  ngOnDestroy(): void {
    if (this.voteScreenActived) {
      this.voteScreenActived.expiration?.done();
    }
  }

  public enterSession() {
    this.sessionService.view(this.code).subscribe(session => {
      this.voteScreenActived = {
        code: this.code,
        description: session.topic,
        opened: true,
        expiration: session.alreadyVote ? undefined : Expiration.create(Date.parse(session.closeAt)),
        alreadyVoted: session.alreadyVote,
        expired: !session.isOpen
      };
      this.voteScreenActived.expiration?.init();
    });
  }

  public vote(option: 'Sim' | 'Não') {
    this.sessionService.vote({ session: this.code, voteOption: option }).subscribe({
      next: () => {
        if (this.voteScreenActived) {
          this.voteScreenActived.alreadyVoted = true;
        }
      },
      error: (err) => this.toastrService.error(err['message'], 'Ocorreu um erro ao votar')
    });
  }

  public leaveSession() {
    if (this.voteScreenActived) {
      this.voteScreenActived.expiration?.done();
      this.voteScreenActived.opened = false;
      this.formGroup.reset();
    }
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
