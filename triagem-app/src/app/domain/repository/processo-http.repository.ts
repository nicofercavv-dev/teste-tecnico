import { inject, Injectable } from "@angular/core";
import { ProcessoRepository } from "./processo.repository";
import { HttpClient, HttpParams } from "@angular/common/http";
import { Observable } from "rxjs";
import { Processo } from "../models/processo.model";
import { StatusProcesso } from "../models/status-processo.enum";
import { environment } from "../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class ProcessoHttpRepository implements ProcessoRepository {
  private http = inject(HttpClient);

  listarTodos(): Observable<Processo[]> {
    return this.http.get<Processo[]>(`${environment.apiUrl}/processos`);
  }

  salvar(processo: Partial<Processo>): Observable<Processo> {
    return this.http.post<Processo>(`${environment.apiUrl}/processos`, processo);
  }

  atualizarStatus(id: number, status: StatusProcesso): Observable<Processo> {
    const params = new HttpParams().set('novoStatus', status);
    return this.http.patch<Processo>(`${environment.apiUrl}/processos/${id}/status`, {}, { params });
  }
}
