import { inject, Injectable } from '@angular/core';
import { ProcessoRepository } from './processo.repository';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Processo } from '../models/processo.model';
import { StatusProcesso } from '../models/status-processo.enum';
import { environment } from '../../../environments/environment';
import { Pagina } from '../models/pagina.model';

@Injectable({
  providedIn: 'root',
})
export class ProcessoHttpRepository implements ProcessoRepository {
  private http = inject(HttpClient);

  listarPaginado(pagina: number, tamanho: number): Observable<Pagina<Processo>> {
    return this.http.get<Pagina<Processo>>(`${environment.apiUrl}/processos`, {
      params: { page: pagina, size: tamanho },
    });
  }

  salvar(processo: Partial<Processo>): Observable<Processo> {
    return this.http.post<Processo>(`${environment.apiUrl}/processos`, processo);
  }

  atualizarStatus(id: number, status: StatusProcesso): Observable<Processo> {
    const params = new HttpParams().set('novoStatus', status);
    return this.http.patch<Processo>(
      `${environment.apiUrl}/processos/${id}/status`,
      {},
      { params },
    );
  }
}
