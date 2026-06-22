import { Observable } from 'rxjs';
import { Processo } from '../models/processo.model';
import { StatusProcesso } from '../models/status-processo.enum';
import { Pagina } from '../models/pagina.model';

export interface ProcessoRepository {
  listarPaginado(pagina: number, tamanho: number): Observable<Pagina<Processo>>;
  salvar(processo: Partial<Processo>): Observable<Processo>;
  atualizarStatus(id: number, status: StatusProcesso): Observable<Processo>;
}
