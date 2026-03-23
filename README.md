# API Roadmap


---

## v0.1.0 — CRUD Completo

- [x] GET `/book` — listar todos
- [x] GET `/book/{id}` — buscar por ID
- [x] GET `/book/name?name=` — buscar por nome
- [x] POST `/book` — criar livro
- [x] Validações com Bean Validation (`@NotBlank`, `@Size`)
- [x] Tratamento de erros global (`@RestControllerAdvice`)
- [x] Testes unitários do BookService
- [x] PUT `/book/{id}` — atualizar livro
- [x] DELETE `/book/{id}` — deletar livro
- [x] Testes de integração do BookController (MockMvc)
- [X] Trocar H2 por PostgreSQL
- [X] Docker Compose com aplicação + banco

---

## v0.2.0 — Autenticação

- [ ] Spring Security + JWT
- [ ] `POST /auth/register`
- [ ] `POST /auth/login` — retorna JWT
- [ ] Roles: `ADMIN`, `USER`
- [ ] Cada usuário só vê/edita os livros que cadastrou
- [ ] Proteção de rotas por role

---

## v0.3.0 — Arquitetura

- [ ] Estudar e implementar FilterPipeline com filtros por entidade
- [ ] Value Object `Author` (DDD)
- [ ] Refatorar para Arquitetura Hexagonal completa (ports & adapters)
- [ ] Separação clara: domínio, aplicação, infraestrutura

---

## v0.4.0 — RBAC Granular

- [ ] Modelo de permissões granular (`book.view`, `book.update.1,2,3`)
- [ ] Tabelas: `permissions`, `role_permissions`, `user_permissions`
- [ ] Filter de autorização granular
- [ ] Endpoints de gerenciamento de permissões (admin)

---

## v0.5.0 — Integração Externa

- [ ] Integração com Open Library API (gratuita, sem key)
- [ ] `GET /book/isbn/{isbn}` — preview antes de salvar
- [ ] Auto-preencher autor, páginas e capa ao cadastrar
- [ ] Tratamento de erros externos (ISBN não encontrado, timeout)

---

## v0.6.0 — Mensageria

- [ ] Kafka — publicar evento ao criar/atualizar livro
- [ ] Consumer de exemplo processando o evento
- [ ] Docker Compose atualizado com Kafka + Zookeeper

---

## v1.0.0 — Production Ready

- [ ] Testes de integração com banco real (Testcontainers + PostgreSQL)
- [ ] Checkstyle ou SonarQube
- [ ] Documentação automática com Swagger/OpenAPI (`springdoc-openapi`)
- [ ] Revisão geral de segurança e performance
