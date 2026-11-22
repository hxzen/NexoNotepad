# Nexo Notepad

Aplicativo desktop para criar, organizar e pesquisar notas pessoais, feito em Java com JavaFX e SQLite. É a base de um gerenciador de conhecimento pessoal (PKM) e foi desenvolvido como projeto de Programação Orientada a Objetos, em dupla.

**Autores:** [Henrique Oliveira] e [Henrique Mitsuka]
**Disciplina / Professor:** [POO // Professor Gabriel]

## O que o app faz

- Cria, edita e exclui notas, cada uma com título, conteúdo, data de criação e data de edição.
- Organiza as notas em categorias. Cada categoria tem uma cor própria, que aparece como etiqueta na lista de notas.
- Permite criar, renomear e excluir categorias. Uma categoria que ainda tem notas não pode ser excluída.
- Pesquisa notas pelo título e pelo conteúdo.
- Filtra a lista de notas por categoria.
- Guarda tudo em um banco SQLite, então os dados continuam depois de fechar o app.
- Valida o que o usuário digita e mostra mensagens de erro e de confirmação em pop-ups.
- Tem tema escuro.

## Tecnologias

- Java 21
- JavaFX 21 (controles e FXML) para a interface
- SQLite, acessado por JDBC
- Maven para gerenciar as dependências
- Git e GitHub para o trabalho em dupla

## Como rodar

**Requisitos:** JDK 21 e Maven (o IntelliJ já traz um Maven embutido).

1. Clone o repositório e abra a pasta no IntelliJ.
2. Confira se o SDK do projeto é o Java 21 (*File > Project Structure*).
3. Deixe o Maven carregar as dependências.
4. Rode com `mvn clean javafx:run` no terminal, ou pela janela do Maven: *Plugins > javafx > javafx:run*.

O banco de dados é criado sozinho na primeira execução, em `~/.nexo/nexo.db` (dentro da pasta do usuário). Para começar do zero, é só apagar esse arquivo com o app fechado.

## Estrutura do projeto

```
src/main/java/br/nexo/
├── App.java                 ponto de entrada, monta as peças e abre a janela
├── model/                   Nota, Categoria
├── exception/               ValidacaoException, NaoEncontradoException
├── repository/              interfaces NotaRepository e CategoriaRepository
│   ├── sqlite/              implementações que usam o banco SQLite
│   └── memoria/             implementações em memória (usadas no início do desenvolvimento)
├── database/                Conexao (abre o banco e cria as tabelas)
├── service/                 NotaService, CategoriaService (regras de negócio)
├── controller/              controllers das telas
└── util/                    Alertas (pop-ups) e Cores

src/main/resources/br/nexo/
├── view/                    telas em FXML
└── css/                     estilo.css (tema escuro)
```

## Como o código está organizado

O programa é dividido em camadas, e cada uma só conversa com a seguinte:

```
controller  ->  service  ->  repository  ->  banco
```

- **Controller:** recebe os cliques e o que o usuário digita, chama o service e mostra o resultado. Não tem SQL nem regra de negócio.
- **Service:** valida os dados e aplica as regras (título obrigatório, nome de categoria sem repetir, categoria em uso não pode ser excluída).
- **Repository:** é o único que mexe nos dados. É uma interface com duas implementações, uma em SQLite e outra em memória.
- **Model:** só guarda os dados.

O service conhece o repository apenas pela interface. Por isso, trocar o SQLite pela versão em memória (ou outro banco no futuro) só exige mudar duas linhas no `App`, sem mexer no resto.

## Conceitos de POO usados

- **Encapsulamento:** atributos privados nas classes de modelo, acessados por getters e setters.
- **Sobrecarga de construtores:** `Nota` e `Categoria` têm construtores diferentes para objeto novo (sem id) e para objeto que vem do banco.
- **Interface e polimorfismo:** os repositories são interfaces com mais de uma implementação, e o service funciona com qualquer uma delas.
- **Exceções próprias:** `ValidacaoException` e `NaoEncontradoException` separam erro de regra de negócio de erro de busca.
- **Sobrescrita de métodos:** `toString`, `equals` e `hashCode` nas classes de modelo.
- **Composição:** uma `Nota` tem uma `Categoria`.

## Decisões de projeto

- A categoria da nota é opcional.
- Uma categoria só pode ser excluída se nenhuma nota a usa.
- O id começa vazio (`null`) e só ganha valor quando o objeto é salvo.
- As datas são guardadas no SQLite como texto no formato ISO e convertidas de volta ao ler.
- Cada operação no banco abre uma conexão nova e a fecha ao terminar.
- Os comandos SQL usam `PreparedStatement`, que evita problemas com aspas e SQL injection.
- As chaves estrangeiras são ligadas em toda conexão, porque o SQLite vem com elas desligadas.

## Limitações conhecidas

- Se o banco falhar de verdade (por exemplo, o arquivo estar bloqueado), o erro aparece só no console e não em um pop-up.
- Não há testes automatizados. Os testes foram feitos manualmente.
- Ainda não existe exportação de notas nem backup do banco.

## Próximos passos

O diferencial de um PKM é conectar notas relacionadas. Os planos para as próximas versões são:

1. Conexões entre notas e lista de notas relacionadas.
2. Mapa visual, com as notas como nós e as conexões como linhas, que depende do item anterior.
3. Tags, favoritos e arquivamento.
4. Ordenação e filtros mais completos.
5. Exportação para Markdown e backup do banco.

## Divisão do trabalho

| Integrante          | Partes desenvolvidas                                                                                                            |
|---------------------|---------------------------------------------------------------------------------------------------------------------------------|
| [Henrique Oliveira] | Categoria, NotaRepositoryMemoria, NotaService, Alertas, estilo.css (tema escuro), tela de gerenciar categorias, MainController. |
| [Henrique Mitsuka]  | Nota, CategoriaRepositoryMemoria, CategoriaService, SQLite (Conexao e os dois repositories), cores das categorias.              |