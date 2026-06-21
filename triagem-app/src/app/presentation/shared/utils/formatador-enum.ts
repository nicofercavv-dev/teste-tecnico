export class FormatadorEnum {
  static formatarStatus(status: string): string {
    const dePara: Record<string, string> = {
      PENDENTE: 'PENDENTE',
      EM_ANALISE: 'EM ANÁLISE',
      CONCLUIDO: 'CONCLUÍDO',
    };
    return dePara[status] || status;
  }

  static formatarUrgencia(urgencia: string): string {
    const dePara: Record<string, string> = {
      BAIXA: 'BAIXA',
      MEDIA: 'MÉDIA',
      ALTA: 'ALTA',
    };
    return dePara[urgencia] || urgencia;
  }
}
