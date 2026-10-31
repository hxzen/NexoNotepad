# Nexo Notepad

Gerenciador de notas pessoais em Java + JavaFX (V1: CRUD de notas, categorias e pesquisa).
Fase atual: **Java e JavaFX com repository em memória**. O SQLite entra depois.

## Fluxo das camadas

    controller  ->  service  ->  repository (interface)  ->  model

- `model` e `exception` não dependem de nada do projeto.
- `service` só conhece a INTERFACE do repository.
- `controller` só fala com os services (sem SQL, sem regra de negócio).
- Só o `App` conhece as implementações concretas (memória / SQLite).
- Só `controller` e `util` importam `javafx`.

## Pacotes (raiz: br.nexo)

| Pacote | O que tem | Fase |
| --- | --- | --- |
| `br.nexo` | `App` (monta tudo e abre a janela) | Java |
| `model` | `Categoria`, `Nota` | Java |
| `exception` | `ValidacaoException`, `NaoEncontradoException` | Java |
| `repository` | interfaces `NotaRepository`, `CategoriaRepository` | Java |
| `repository.memoria` | implementações provisórias em memória | Java |
| `service` | `NotaService`, `CategoriaService` | Java |
| `controller` | `MainController` | JavaFX |
| `util` | `Alertas` | JavaFX |
| `resources/.../view` e `css` | `main.fxml`, `estilo.css` | JavaFX |
| `database` | `Conexao` | SQL (não mexer agora) |
| `repository.sqlite` | implementações com SQLite | SQL (não mexer agora) |

## Ordem sugerida

1. `Categoria` -> `Nota` (model)
2. Repositories em memória
3. Services (validações) + teste pelo `main`
4. `Alertas` + `MainController` (ligar a tela)
5. Só depois: `Conexao` e `repository.sqlite`, trocando a linha no `App`

## Status

- [ ] Categoria  - [ ] Nota  - [ ] Repositories memória  - [ ] Services
- [ ] Controller + tela  - [ ] Pesquisa  - [ ] Categorias na tela
- [ ] SQLite (depois)

## Rodar

    mvn clean javafx:run
