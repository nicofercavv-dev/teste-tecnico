import { computed, inject, Injectable, signal } from "@angular/core";
import { ProcessoHttpRepository } from "../../../../domain/repository/processo-http.repository";
import { Processo } from "../../../../domain/models/processo.model";
import { firstValueFrom } from "rxjs";
import { StatusProcesso } from "../../../../domain/models/status-processo.enum";

@Injectable({
  providedIn: 'root'
})
export class TriagemStore {
  private repository = inject(ProcessoHttpRepository);

  // TODO: finalizar ajuste de paginação na store e no paginador do componente de triagem
  private _processos = signal<Processo[]>([]);
  private _totalElementos = signal<number>(0);
  private _loading = signal<boolean>(false);
  private _erro = signal<string | null>(null);

  public processos = this._processos.asReadonly();
  public totalElementos = this._processos.asReadonly();
  public loading = this._loading.asReadonly();
  public erro = this._erro.asReadonly();

  public totalProcessos = computed(() => this._processos().length);
  public processosPendentes = computed(() => this._processos().filter(p => p.status === 'PENDENTE'));

  async carregarProcessos(): Promise<void> {
    this._loading.set(true);
    this._erro.set(null);
    try {
      const dados = await firstValueFrom(this.repository.listarTodos());
      this._processos.set(dados);
    } catch (err) {
      this._erro.set('Falha ao carregar a lista de processos da triagem.');
    } finally {
      this._loading.set(false);
    }
  }

  async cadastrarProcesso(novoProcesso: Partial<Processo>): Promise<boolean> {
    this._loading.set(true);
    try {
      const processoSalvo = await firstValueFrom(this.repository.salvar(novoProcesso));
      // Imutabilidade: Adiciona o novo processo ao estado atualizando o Signal
      this._processos.update(lista => [...lista, processoSalvo]);
      return true;
    } catch (err: any) {
      this._erro.set(err.error?.erro || 'Erro ao cadastrar novo processo.');
      return false;
    } finally {
      this._loading.set(false);
    }
  }

  async mudarStatus(id: number, novoStatus: StatusProcesso): Promise<void> {
    try {
      const atualizado = await firstValueFrom(this.repository.atualizarStatus(id, novoStatus));
      // Atualiza apenas o processo alterado dentro do estado reativo do Signal
      this._processos.update(lista =>
        lista.map(p => p.id === id ? atualizado : p)
      );
    } catch (err: any) {
      alert(err.error?.erro || 'Não foi possível alterar o status do processo.');
    }
  }
}
