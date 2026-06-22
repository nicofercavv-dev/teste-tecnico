import { CommonModule } from '@angular/common';
import { Component, effect, inject, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormControl, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSelectModule } from '@angular/material/select';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { TriagemStore } from './store/triagem.store';
import { StatusProcesso } from '../../../domain/models/status-processo.enum';
import { TipoAcao } from '../../../domain/models/tipo-acao.enum';
import { NgxMaskDirective, NgxMaskPipe } from 'ngx-mask';
import { FormatadorEnum } from '../../shared/utils/formatador-enum';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ClipboardModule } from '@angular/cdk/clipboard';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatPaginator, MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { Processo } from '../../../domain/models/processo.model';

@Component({
  selector: 'app-triagem',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatTableModule,
    MatPaginatorModule,
    MatButtonModule,
    MatInputModule,
    MatSelectModule,
    MatCardModule,
    MatIconModule,
    MatProgressSpinnerModule,
    NgxMaskDirective,
    NgxMaskPipe,
    ClipboardModule,
    MatTooltipModule,
  ],
  templateUrl: './triagem.component.html',
  styleUrls: ['./triagem.component.scss'],
})
export class TriagemComponent implements OnInit {
  private snackBar = inject(MatSnackBar);
  public store = inject(TriagemStore);
  private fb = inject(FormBuilder);

  public Formatador = FormatadorEnum;
  public TipoAcaoEnum = TipoAcao;

  public listaTiposAcao = Object.keys(TipoAcao).map((chave) => ({
    chave: chave as TipoAcao,
    label: TipoAcao[chave as keyof typeof TipoAcao],
  }));

  public StatusProcessoEnum = {
    PENDENTE: 'PENDENTE' as StatusProcesso,
    EM_ANALISE: 'EM_ANALISE' as StatusProcesso,
    CONCLUIDO: 'CONCLUIDO' as StatusProcesso,
  };

  public colunasExibidas: string[] = ['numeroProcesso', 'tipoAcao', 'urgencia', 'status', 'acoes'];
  public dataSource = new MatTableDataSource<Processo>([]);

  public paginaAtual = 0;
  public tamanhoPagina = 5;

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor() {
    effect(() => {
      this.dataSource.data = this.store.processos();
    });
  }

  public formulario = this.fb.group({
    numeroProcesso: ['', [Validators.required, Validators.minLength(20), Validators.maxLength(20)]],
    tipoAcao: new FormControl<TipoAcao | null>(null, [
      Validators.required,
      Validators.maxLength(100),
    ]),
    urgencia: new FormControl<string | null>(null, [Validators.required]),
    descricaoResumida: ['', [Validators.maxLength(500)]],
  });

  ngOnInit(): void {
    this.store.carregarProcessos(this.paginaAtual, this.tamanhoPagina);
  }

  ngAfterViewInit() {
    if (this.paginator) {
      this.paginator._intl.itemsPerPageLabel = 'Itens por página:';
      this.paginator._intl.nextPageLabel = 'Próxima página';
      this.paginator._intl.previousPageLabel = 'Página anterior';

      this.paginator._intl.getRangeLabel = (page: number, pageSize: number, length: number) => {
        if (length === 0 || pageSize === 0) {
          return `0 de ${length}`;
        }
        length = Math.max(length, 0);
        const startIndex = page * pageSize;
        const endIndex =
          startIndex < length ? Math.min(startIndex + pageSize, length) : startIndex + pageSize;
        return `${startIndex + 1} – ${endIndex} de ${length}`;
      };
    }
  }

  async submeterFormulario(): Promise<void> {
    if (this.formulario.invalid) return;

    const sucesso = await this.store.cadastrarProcesso(this.formulario.value as any);
    if (sucesso) {
      this.formulario.reset({
        tipoAcao: null,
        urgencia: null,
      });
    }
  }

  alterarStatus(id: number, novoStatus: StatusProcesso): void {
    this.store.mudarStatus(id, novoStatus);
  }

  traduzirTipoAcao(chave: any): string {
    return TipoAcao[chave as keyof typeof TipoAcao] || chave;
  }

  notificarCopia(): void {
    this.snackBar.open('Número do processo copiado!', 'Fechar', {
      duration: 2000,
      panelClass: ['snackbar-sucesso'],
    });
  }

  aoMudarPagina(evento: PageEvent): void {
    this.paginaAtual = evento.pageIndex;
    this.tamanhoPagina = evento.pageSize;

    this.store.carregarProcessos(this.paginaAtual, this.tamanhoPagina);
  }
}
