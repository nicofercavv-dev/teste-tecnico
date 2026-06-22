import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TriagemComponent } from './triagem.component';
import { ReactiveFormsModule } from '@angular/forms';
import { provideNgxMask } from 'ngx-mask';
import { vi } from 'vitest';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { TriagemStore } from './store/triagem.store';
import { TipoAcao } from '../../../domain/models/tipo-acao.enum';
import { StatusProcesso } from '../../../domain/models/status-processo.enum';
import { PageEvent } from '@angular/material/paginator';

describe('TriagemComponent', () => {
  let component: TriagemComponent;
  let fixture: ComponentFixture<TriagemComponent>;
  let storeMock: any;

  beforeEach(async () => {
    storeMock = {
      processos: vi
        .fn()
        .mockReturnValue([
          {
            id: 1,
            numeroProcesso: '12345671220264013400',
            tipoAcao: TipoAcao.MANDADO_DE_SEGURANCA,
            urgencia: 'ALTA',
            status: StatusProcesso.PENDENTE,
          },
        ]),
      totalElementos: vi.fn().mockReturnValue(1),
      loading: vi.fn().mockReturnValue(false),
      erro: vi.fn().mockReturnValue(null),
      carregarProcessos: vi.fn().mockResolvedValue(undefined),
      cadastrarProcesso: vi.fn().mockResolvedValue(true),
      mudarStatus: vi.fn(),
    };

    await TestBed.configureTestingModule({
      imports: [TriagemComponent, ReactiveFormsModule],
      providers: [
        provideAnimationsAsync(),
        provideNgxMask(),
        { provide: TriagemStore, useValue: storeMock },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(TriagemComponent);
    component = fixture.componentInstance;

    fixture.detectChanges();
  });

  it('deve criar o componente com sucesso', () => {
    expect(component).toBeTruthy();
  });

  it('deve buscar a primeira página de processos ao inicializar a tela (ngOnInit)', () => {
    expect(storeMock.carregarProcessos).toHaveBeenCalledWith(0, 5);
  });

  it('deve atualizar o estado e pedir novos dados ao mudar de página no paginador', () => {
    const eventoPaginator: PageEvent = {
      pageIndex: 1,
      previousPageIndex: 0,
      pageSize: 10,
      length: 100,
    };

    component.aoMudarPagina(eventoPaginator);

    expect(component.paginaAtual).toBe(1);
    expect(component.tamanhoPagina).toBe(10);

    expect(storeMock.carregarProcessos).toHaveBeenCalledWith(1, 10);
  });

  it('deve validar o formato do número CNJ do processo no formulário', () => {
    const inputNumero = component.formulario.get('numeroProcesso');

    expect(inputNumero?.valid).toBeFalsy();

    inputNumero?.setValue('12345');
    expect(inputNumero?.valid).toBeFalsy();

    inputNumero?.setValue('12345671220264013400');
    expect(inputNumero?.valid).toBeTruthy();
  });

  it('deve disparar o cadastro na Store e limpar o formulário se a submissão for válida', async () => {
    component.formulario.setValue({
      numeroProcesso: '12345671220264013400',
      tipoAcao: TipoAcao.EXECUCAO_FISCAL,
      urgencia: 'MEDIA',
      descricaoResumida: 'Processamento de execução de dívida.',
    });

    expect(component.formulario.valid).toBeTruthy();

    await component.submeterFormulario();

    await fixture.whenStable();

    expect(storeMock.cadastrarProcesso).toHaveBeenCalled();
    expect(component.formulario.get('numeroProcesso')?.value).toBeNull();
  });
});
