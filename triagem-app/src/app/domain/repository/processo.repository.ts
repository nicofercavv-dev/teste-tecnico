import { Observable } from 'rxjs';
import { Processo } from '../models/processo.model';
import { StatusProcesso } from '../models/status-processo.enum';

export interface ProcessoRepository {
  listarTodos(): Observable<Processo[]>;
  salvar(processo: Partial<Processo>): Observable<Processo>;
  atualizarStatus(id: number, status: StatusProcesso): Observable<Processo>;
}
