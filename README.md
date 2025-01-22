### Score API

Esta é uma API para cadastro de pessoas e retorno da descrição do score do Serasa de acordo com valor deste para cada pessoa. 
Os endpoints estão documentados através do Swagger disponível no endereço padrão: /v2/api-docs.

A porta padrão do contâiner do TomCat é a 8080. Portanto, as requisições deverão ser enviadas para http://localhost:8080 quando esta API for servida.
Para consumir a API de forma correta é necessário ter o JWT fornecido pela mesma. Este JWT não possui expiração, facilitando requisições através do Postman/Insomnia para testes da aplicação.

Existem dois usuários já pré-configurados ao iniciar o app. São eles:

Usuário administrador:  
email: admin@serasa.com.br  
senha: admin@s12

Usuário comum:  
email: user@serasa.com.br  
senha: user@s12

Uma requisição com email e senha deverá ser enviada usando o verbo POST para /api/auth/v1/logon para recuperar o JWT. Uma vez recuperado, o token deve ser incluído no header de todas as requisições usando a palavrar "Bearer " exatamente antes dele.

#### Como cadastrar um usuário
É possível também registrar um usuário na API através da rota /api/auth/v1/registrar passando dados como: nome, email e senha. Os usuários registrados serão sempre usuários comuns (sem os acessos de administrador).

#### Consumo da API

Conforme disponível no Swagger, é possível cadastrar, buscar, listar, excluir e atualizar pessoas. No entanto apenas o usuário administrador pode cadastrar, atualizar e remover os registros de uma pessoa através da API.  
Para fins de testes das funcionalidades da API, já existem pessoas cadastradas ao iniciar aplicação. Basta listá-las através do endpoint de paginação de usuários.

#### Bibliotecas de terceiros

Para a checagem dos CEP passados no body da requisição de cadastro de pessoas é utilizado o serviço do https://viacep.com.br  

Outras bibliotecas usadas:  
* SpringFox 3.0.0 para a implementação do Swagger em compatibilidade com o Java 11 e o Spring 2.7.18;
* Java JWT para a implementação do JWT;
* Gson para a serialização e deserialização de JSONs


