# NextStores

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/93f33d10a033448c88c93e07f0bb7a32)](https://app.codacy.com/gh/NextPlugins/NextStores?utm_source=github.com&utm_medium=referral&utm_content=NextPlugins/NextStores&utm_campaign=Badge_Grade_Settings)

Um sistema de lojas completo e 100% em menus! O sistema possibilita os jogadores de criar e gerenciar a sua loja.
Algumas [imagens in-game](https://imgur.com/a/DWBdfjb)

## Comandos

| Comando      | Descrição                        | Permissão                     |
|--------------|----------------------------------|-------------------------------|
| /lojas       | Abre o menu principal do sistema | `nextstores.command.store`    |
| /lojassetnpc | Seta o NPC                       | `nextstores.command.setstore` |
| /lojadelnpc  | Deleta o NPC                     | `nextstores.command.setstore` |

## Download

Você pode encontrar o plugin pronto para baixar [**aqui**](https://github.com/NextPlugins/NextStores/releases), ou se
você quiser, pode optar por clonar o repositório e dar
`build` no plugin com suas alterações.

## Outras permissões

- Permissão para criar uma loja: `nextstores.stores.create`

## Configuração

O plugin conta com diversos arquivos de configuração, em que você pode configurar a conexão SQL, menus, ícones,
mensagens, e outras opções.

## Dependências

O plugin não necessita de nenhuma dependência. As dependências de desenvolvimento são baixadas automaticamente.

**APIs e Frameworks**

- [inventory-api](https://github.com/HenryFabio/inventory-api) - API para criação e o gerenciamento de inventários
  customizados.
- [sql-provider](https://github.com/henryfabio/sql-provider) - Provê a conexão com o banco de dados.
- [configuration-injector](https://github.com/HenryFabio/configuration-injector) - Injeta valores dos arquivos de
  configuração em alguns campos definidos mo código.
- [update-checker](https://github.com/Yuhtin/update-checker) - Verifica se há alguma nova versão do plugin.
