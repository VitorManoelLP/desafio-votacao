import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Expiration } from '../../../shared/utils/expiration-utils';
import { VotingSessionService } from '../../../shared/services/voting-session.service';
import { ToastrService } from 'ngx-toastr';
import { LastConsult } from '../../../model/last-consult';
import { VotingSessionInfo } from '../../../model/voting-session-info';

@Component({
  selector: 'app-enter',
  templateUrl: './enter.component.html',
  styleUrl: './enter.component.css'
})
export class EnterComponent implements OnDestroy, OnInit {

  formGroup: FormGroup;
  voteScreenActived?: {
    opened: boolean,
    session?: VotingSessionInfo,
    expiration: Expiration | undefined
  };
  lastConsultedSession?: LastConsult;
  isLoadingTimerCalculation: boolean = true;

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

  ngOnInit(): void {
    this.getLastConsulted();
  }

  ngOnDestroy(): void {
    if (this.voteScreenActived) {
      this.voteScreenActived.expiration?.done();
    }
  }

  public getTimeSinceLastSession(): string {

    if (!this.lastConsultedSession) {
      return '';
    }

    const seconds = Math.floor((Date.now() - Date.parse(this.lastConsultedSession.consultedAt)) / 1000);
    const minutes = Math.floor(seconds / 60);
    const hours = Math.floor(minutes / 60);
    const days = Math.floor(hours / 24);

    if (days > 0) {
      return `Há ${days} dia${days > 1 ? 's' : ''}`;
    } else if (hours > 0) {
      return `Há ${hours} hora${hours > 1 ? 's' : ''}`;
    } else if (minutes > 0) {
      return `Há ${minutes} minuto${minutes > 1 ? 's' : ''}`;
    } else {
      return `Há ${seconds} segundo${seconds > 1 ? 's' : ''}`;
    }
  }

  public enterLastSession() {
    if(this.lastConsultedSession) this.initSession(this.lastConsultedSession.sessionInfo);
  }

  public enterSession() {
    this.sessionService.view(this.code).subscribe({
      next: session => this.initSession(session),
      error: (err) => this.toastrService.error(err['error']['message'])
    });
  }

  public vote(option: 'Sim' | 'Não') {
    this.sessionService.vote({ session: this.code, voteOption: option }).subscribe({
      next: () => {
        if (this.voteScreenActived && this.voteScreenActived.session) {
          this.voteScreenActived.session.alreadyVote = true;
          this.voteScreenActived.session.yourVote = option;
          this.toastrService.success('Voto computado com sucesso. Obrigado por votar!')
        }
      },
      error: (err) => this.toastrService.error(err['message'], 'Ocorreu um erro ao votar')
    });
  }

  public leaveSession() {
    if (this.voteScreenActived && this.voteScreenActived.session) {
      this.voteScreenActived.expiration?.done();
      this.voteScreenActived.opened = false;
      this.formGroup.reset();
      this.isLoadingTimerCalculation = true;
      this.getLastConsulted();
    }
  }

  public onPaste(event: ClipboardEvent) {
    const data = event.clipboardData?.getData('text');
    const parts = data?.split('-');
    if (parts && parts?.length > 1) {
      this.pasteByHifen(parts);
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

  private initSession(session: VotingSessionInfo) {
    this.voteScreenActived = {
      opened: true,
      session: session,
      expiration: Expiration.create(Date.parse(session.closeAt)),
    };
    this.voteScreenActived.expiration?.init(() => this.isLoadingTimerCalculation = false);
  }

  private getLastConsulted() {
    this.sessionService.getLastConsultedSession()
      .subscribe(session => this.lastConsultedSession = session);
  }

}
