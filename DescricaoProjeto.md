# TriagemApp

## Execução Frontend
- Para executar o front basta dar ``npm install`` e depois ``npm run start``
- Para rodar os testes basta dar ``npm run test``

## Execução Backend
- Para executar o backend basta dar ``./gradlew bootRun``
- Para rdar os testes basta dar ``./gradlew test``

## Tech Stack
### Front
- Node 22
- Angular 21
- Reactive Forms
- Signals
- Observables
- Angular Material
- ngx-mask
- RxJS
- Testes com Vitest
- Estrutura de pastas Nx-Style / Módulos por Domínio

### Back
- Java 21
- Gradle
- Spring Boot 3.5
- Spring Web
- Spring data JPA
- Banco em Memória H2
- Validation
- Lombok
- Clean Architecture

## Decisões Aquiteturais
### Backend
- **Clean Architecture & Domain-Driven Design (DDD)**: O núcleo da aplicação (entidades de negócio e casos de uso) foi completamente isolado das tecnologias externas (Spring, JPA, Banco de Dados). Isso garante que as regras jurídicas da triagem possam ser testadas e alteradas sem que modificações na camada de infraestrutura quebrem o negócio.
- **Padrão UseCase**: Cada ação do usuário (Criar, Listar Paginado, Alterar Status) foi mapeada para um Caso de Uso. Isso evita a criação de services gigantes com muitas responsabilidades misturadas (God Classes), facilitando a manutenção do código.
### Frontend
- **Arquitetura Zoneless**: O projeto foi feito utilizando a nova engine do Angular que dispensa o Zone.js. Isso melhora a performance de renderização, reduz o tamanho do bundle final e deixa o sistema atualizado com as melhores práticas modernas do framework.
- **Gerenciamento de Estado Nativo com Signals**: O fluxo de dados e a reatividade da tela de triagem utilizam Signals (através da abordagem de Store). Isso substitui o uso complexo de múltiplos pipes assíncronos do RxJS no template, deixando o código declarativo e mais limpo.
### Trade-offs
- Foi utilizado o Banco em Memória **H2** no projeto, para facilitar a execução pelo avaliador. O projeto não exige instalação de instâncias Docker ou configurações complexas de infraestrutura, mas por outro lado o H2 não suporta concorrência pesada em caso de uso em produção e perde todos os dados ao reiniciar a aplicação. Como foi uso em um Teste Técnico considerou-se que seria suficiente para este caso.
- Foi criado um único componente **TriagemComponent** que contém todo o visual da aplicação, para a entrega mais rápida do Teste foi optado fazer desta forma, embora a melhor abordagem fosse uma quebra em componentes menores para aumentar a reutilização e evitar quebrar o princípio da Responsabilidade Única.

### Melhorias Futuras
- **Criação de Migrations**: Substituir por PostgreSQL e adicionar migrations traria o gerenciamento de das versões de schemas do banco de forma segura.
- **Componentização Avançada**: Dividira aplicação do front em componentes menores como **FormulárioProcessoComponent**, **TabelaProcessosComponent**.