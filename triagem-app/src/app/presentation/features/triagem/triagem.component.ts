import { CommonModule } from '@angular/common';
import { Component, effect, inject, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSelectModule } from '@angular/material/select';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { TriagemStore } from './store/triagem.store';
import { UrgenciaProcesso } from '../../../domain/models/urgencia-processo.enum';
import { StatusProcesso } from '../../../domain/models/status-processo.enum';
import { TipoAcao } from '../../../domain/models/tipo-acao.enum';
import { NgxMaskDirective, NgxMaskPipe } from 'ngx-mask';
import { FormatadorEnum } from '../../shared/utils/formatador-enum';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ClipboardModule } from '@angular/cdk/clipboard';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';

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
export default class TriagemComponent implements OnInit {
  private snackBar = inject(MatSnackBar);
  // Injetando nossa Store de Signals do DDD
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

  // Definição das colunas da tabela do Material
  public colunasExibidas: string[] = ['numeroProcesso', 'tipoAcao', 'urgencia', 'status', 'acoes'];
  public dataSource = new MatTableDataSource<any>([]);

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor() {
    // Efeito moderno do Angular 21: Sempre que o Signal de processos mudar na Store,
    // ele atualiza automaticamente o DataSource da tabela
    effect(() => {
      this.dataSource.data = this.store.processos();
      if (this.paginator) {
        this.dataSource.paginator = this.paginator;
      }
    });
  }

  // Formulário Reativo fortemente tipado com validações básicas
  public formulario = this.fb.group({
    numeroProcesso: ['', [Validators.required, Validators.minLength(20), Validators.maxLength(20)]],
    tipoAcao: [null, [Validators.required, Validators.maxLength(100)]],
    urgencia: [null, [Validators.required]],
    descricaoResumida: ['', [Validators.maxLength(500)]],
  });

  ngOnInit(): void {
    // Carrega a lista de processos ao iniciar a tela
    this.store.carregarProcessos();
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;

    // Tradução opcional do Paginator para Português
    this.paginator._intl.itemsPerPageLabel = 'Itens por página:';
    this.paginator._intl.nextPageLabel = 'Próxima página';
    this.paginator._intl.previousPageLabel = 'Página anterior';
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
}
