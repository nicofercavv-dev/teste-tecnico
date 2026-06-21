import { StatusProcesso } from "./status-processo.enum";
import { TipoAcao } from "./tipo-acao.enum";
import { UrgenciaProcesso } from "./urgencia-processo.enum";

export interface Processo {
  id?: number;
  numeroProcesso: string;
  tipoAcao: TipoAcao;
  status: StatusProcesso;
  urgencia: UrgenciaProcesso;
  dataRecebimento: string;
  descricaoResumida?: string;
}
