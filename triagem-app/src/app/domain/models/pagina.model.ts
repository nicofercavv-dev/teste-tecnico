export interface Pagina<T> {
  conteudo: T[];
  totalElementos: number;
  paginaAtual: number;
  tamanhoPagina: number;
}
