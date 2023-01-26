
# SEMANA 2 - Spring Data JPA: Do Básico ao Avançado
## Entities, Repositories, OnetoMany e ManyToOne, FetchType Eager/Lazy, @EntityGraph, FetchMode SELECT/SUBSELECT/JOIN, @Query e @Modify, Cascade.ALL, ISO 8601 UTC Date


### FetchType Eager/Lazy, @EntityGraph, FetchMode SELECT/SUBSELECT/JOIN, @Query e @Modify

#### Relação “many to one/one to many” Course/Module:
Para especificar a relação **one to many/many** to one  no lado muitos da relação é preciso declarar uma instância da entidade  no lado um da relação e anotá-la com a anotação **@ManyToOne**. O parâmetro **opcional** da anotação garante que as entidades do lado muitos estão obrigatoriamente vinculadas a um  módulo, em outras palavras ele indica se irá haver um relacionamento não nulo caso o parâmetro receba valor “false”.

Por exemplo: Na relação entre cursos e módulos, **um curso pode ter vários módulos** e **um módulo está sempre associado a um curso**. Para especificar para o JPA a relação de muitos módulos para um curso é preciso declarar na classe [Module](https://github.com/DeadRon/cuorse/commit/222084bbe7f9e08d935b7753c9e59a8b529cd244) uma variável do tipo curso e anotar com anotação **@ManyToOne** e passar o valor boolean **false** no  parâmetro **optional** para indicar uma relação não nulo entre as entidades igual mostra o trecho de código abaixo:

``` java
@ManyToOne(optional = false)
private CourseModel course
```

No lado **um ou One da relação** é preciso declarar um conjunto Set<T> (da entidade no lado muitos) e anotar esta coleção com a anotação **@OneToMany** e no parâmetro mappedBy  desta anotação é preciso declarar (em forma de String) o nome do atributo da entidade(do lado “um”)  declarado na classe que representa as entidades do lado muitos.

***Exemplo**: Como dito no exemplo acima Cuorse pode ter vários módulos. Para especificarmos para o JPA que existe uma relação de um curso possuir vários módulos é preciso  declarar um conjunto Set<ModuleModel> na classe de [Course](https://github.com/DeadRon/cuorse/commit/c1cebd9c73bcadb374ffe4e7d613a9dd7cfb830b), em seguida é preciso anotar a coleção declarada com a notação **@OneToMany** e no parâmetro **mappedBy**  é preciso declarar o mesmo nome da variável curso que foi declarada na classe módulo em formato de string isso garante a relação bidirecional  um para muitos entre as  entidades  curso e módulo.*
 ``` java
@OneToMany(mappedBy = "module")
private Set<LessonModel> lessons;
```

Observação: o não uso uso do **mappedBy**  não impede que o hibernate construa a relação um para muitos porém serão criadas tabelas auxiliares para implementar a relação  com o uso do mappedBy o hibernate irá criar uma chave estrangeira nas entidades(**Modules**) do lado muitos para referenciar a Entidade do lado um'**(**Cuorse**) a qual todas as entidades  do lado muitos estão associadas.

#### FETCH TYPE

Define **SE** os dados **serão ou não carregados** do banco de dados no momento em que a consulta é feita. Isso é definido no lado muitos ou many da relação. Toda vez que o banco consultar uma entidade que tenha muitas entidades  associadas a ela, este FETCH será feito e nesse caso específico irá trazer ou não as muitas entidades associadas a ele no momento da consulta. Isso pode causar problemas  de desempenho caso o banco possua muitos registros associados à uma entidade, porque na maioria dos casos não há necessidade de ver todos os dados associados a um registro, apenas o registro em si será necessário. Para evitar esse cenário o **FETCH.LAZY**, que indica ao JPA um carregamento lento, irá trazer apenas um registro do banco de dados sem a muitas entidades associadas a este registro. Mas em caso de haver a necessidade de trazer as muitas entidades associadas à entidade consultada, é usado o  **FETCH.EAGLE**, que indica ao JPA um carregamento rápido, que irá trazer o registro do banco de dados e as muitas entidades associadas a este registro. Para especificar  qual tipo de fetch será usado basta usar o parâmetro *fetch* da anotação [**@OneToMany**](https://github.com/DeadRon/cuorse/commit/71837839bff8ba45e0b03d67ab55f927966d4a5e) e especificar o FetchType a ser usado. O uso de **FETCH** também é válido no caso de haver  uma consulta de uma entidade do lado muitos da relação e não seja necessário visualizar a entidade associado no lado um da relação.

Essas estratégias de carregamento não podem ser alteradas em tempos de execução, são estáticas. Se eventualmente for necessário carregar dados de um Módulo ESPECÍFICO de forma que sejam exibidos dados do curso que o módulo está vinculado e se o fetch ser definido como LAZY haverá um problema. Para situações de exceção como essa em que é preciso alterar o tipo de carregamento em tempo de execução é usado o [**@EntityGraph**](https://github.com/DeadRon/cuorse/commit/750c54944eab676755ce5795e564622cc1cf2d3e), dessa forma é como se o tipo do fetch para Curso definido na classe Módulo seja alterado para EAGLE ao invés de LAZY em tempo de execução.

#### FETCH MODE
**Define o tipo de carregamento** utilizado quando ouver a consulta. Há 3 formas de definir o tipo de carregamento:

- [**SELECT**](https://github.com/DeadRon/cuorse/commit/e148ad9e65baf8261ad4cce35247cf0f85105c07): Faz uma consulta para buscar uma entidade e depois faz uma consulta para cada entidade associada a esta entidade. Em um cenário de elevado volume de dados isso pode causar problemas de desempenho.
  Exemplo: Se cinco módulos associados a um curso com o FETCH MODE tipo SELECT será feito uma consulta para primeiro buscar o curso e em seguida seriam feitos consultas individuais para cada módulo associado à este curso

- [**JOIN**](https://github.com/DeadRon/cuorse/commit/7f546ca345559590b43ed77d95bbf51aaff3a12d): Faz uma única consulta, levando em conta o cenário de que um curso pode ter vários módulos não iria ocorrer o mesmo cenário que ocorre com o FETCH MODE tipo SELECT. No FETCH MODE tipo JOIN é feito apenas uma única consulta para trazer todos os elementos da relação. Este FETCH MODE afeta o FetchType definido na anotação @OneToMany, irá alterar de LAZY para EAGER o tipo de carregamento.

- [**SUBSELECT**](https://github.com/DeadRon/cuorse/commit/b2f3385f2c2d971e08ace19ef68a9ba31926b0f0): Faz duas consultas, uma para trazer o curso e outra para trazer os módulos associados com este curso. Esta abordagem comparada com as anteriores é a mais adequada pois são feitas apenas duas consultas para recuperar os dados do banco.

Caso não seja especificado o tipo de FETCH.TYPE o JPA por padrão usa o FETCH.MODE JOIN para buscar os dados.

**Diferença entre FETCH.MODE e FETCH.TYPE**: FM define **como** o hibernate irá buscar os dados do banco no momento da consulta ser feita. FT define **se** o hibernate irá ou não buscar os dados do banco no momento da consulta.
[**@Query**](https://github.com/DeadRon/cuorse/commit/b269de8d7e154201196773594ce75173ebb8027e): Permite fazer consultas personalizadas.
*cenário: buscar APENAS os módulos de um curso através do id desse curso.*
[**@Modifying**](https://github.com/DeadRon/cuorse/commit/ce21df24031af43ef3bfc6f62439ee00b67bca8a): permite fazer queries para realizar alterações personalizadas no banco
*cenário: atualizar algum dado específico de um módulo do curso com base no id do curso e do módulo.*

**Formas de Exclusão de Course, Module e Lessons**

- [**Delegar a exclusão para o JPA**](https://github.com/DeadRon/cuorse/commit/ff4bc982f6b6824a7eaf24bf7f5f14f2d9e60547)
  Na anotação @OneToMany É preciso adicionar mais dois parâmetros: *cascade* com valor **CascadeType.ALL** e *“orphanRemoval”* com o boolean **true**. O **primeiro parâmetro** faz uma deleção em cascata Isto é, quando um curso ser excluído os módulos vinculados a este curso também serão excluídos. ***A regra de negócio dita que não podem existir módulos sem vínculo com o curso***. O **segundo parâmetro** garante que quando o módulo estiver sem curso relacionado este será deletado. Nessa abordagem ao deletar um módulo, o JPA irá fazer um SQL para excluir o curso e para cada módulo associado a ele também será preciso fazer um SQL para cada um dos módulos associados a ele para realizar a exclusão. *Essa abordagem pode causar problemas de acordo com o volume de dados presente na base*.
```java
@OneToMany(mappedBy = "course", fetch = FetchType.LAZY, 
cascade = CascadeType.ALL, orphanRemoval = true)
@Fetch(FetchMode.SUBSELECT)
private Set<ModuleModel> modules;
```
- [**Exclusão com @OnDelete**](https://github.com/DeadRon/cuorse/commit/6b7e53eb7907a013e3dc5c8b0a62271ea58c07a1)
  Usado no lado “um” da relação semelhante à abordagem anterior porém a responsabilidade da deleção passa a ser do banco de dados. Essa abordagem pode prejudicar a performance de acordo com o volume de dados a ser excluído.
```java
@OnDelete(action = OnDeleteAction.CASCADE)
private Set<ModuleModel> modules;
```
- **Exclusão usando @Transactional**
  Essa anotação abre uma transação com o banco de dados, ou seja, será permitido fazer operações que irão alterar o estado do banco de dados (no caso: exclusão de Course e Module relacionados ao curso e exclusão de Lessons relacionadas com cada Module. Ao final da transação se não houver nenhum erro, a transação será confirmada e as operações de exclusão feitas serão gravadas no banco de dados. Passos:
  - Na regra de negócio, deve havar exclusão de cursos, por consequência, exclusão de módulos e exclusão de liçoes. Primeiro, devem ser excluidas as lições, depois os módulos e apenas depois disso curso pode ser excluído. Isso garante que não ocorra incosistências em bancos de dados. **Importante lembrar que regras de negócio são implementas em classes de SERVICE dentro do exemplo usado no curso**.
  -  [@Query especifica para exclusão de Lessons](https://github.com/DeadRon/cuorse/commit/660cc6bcdb310be904962fe520419a7377204420)
  - [Exclusão de Lessons relacionado com um Module](https://github.com/DeadRon/cuorse/commit/1a4e604fefdcaf67de08c0e79ca4b295e5998642).
  - [Exclusão de Modules relacionado com um Course](https://github.com/DeadRon/cuorse/commit/85df331e11a37fa256afb8283c92faabf6bda7dc).
  - [Exclusão do Course](https://github.com/DeadRon/cuorse/commit/85df331e11a37fa256afb8283c92faabf6bda7dc)


### Criar API RESTful para Course com Validação Spring Validation


**[Cuorse End Pont para](https://github.com/DeadRon/cuorse/commit/f894f216b7d76607d27ef08122ea2259861e9423)**:
- [CourseDTO](https://github.com/DeadRon/cuorse/commit/160108269670d0e6514c4ab5f776b01c3baa0366)
- Salvar Course
````java
    @PostMapping
    public ResponseEntity<Object> saveCuorse(@RequestBody @Valid CourseDTO courseDTO){
        var cuorseModel = new CourseModel();
        BeanUtils.copyProperties(courseDTO, cuorseModel);
        cuorseModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        cuorseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.save(cuorseModel));
    }
````
- Deletar Course
````java
    @DeleteMapping("/{courseId}")
public ResponseEntity<Object> deleteCuorse(@PathVariable(value = "courseId") UUID courseId){
        Optional<CourseModel> cuorseModelOptional = courseService.findBy(courseId);
        if(!cuorseModelOptional.isPresent()){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found");
        }
        courseService.delete(cuorseModelOptional.get());

        return ResponseEntity.status(HttpStatus.OK).body("Course deleted successfully");
        }
````
- Atualizar Course
````java
    @PutMapping("/{courseId}")
    public ResponseEntity<Object> updateCuorse(@PathVariable(value = "courseId") UUID courseId, @RequestBody @Valid CourseDTO courseDTO){
        Optional<CourseModel> cuorseModelOptional = courseService.findBy(courseId);
        if(!cuorseModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cuorse Not Found");
        }
        CourseModel courseModel = cuorseModelOptional.get();
        courseModel.setName(courseDTO.getName());
        courseModel.setDescription(courseDTO.getDescription());
        courseModel.setImageURL(courseDTO.getImageUrl());
        courseModel.setCourseStatus(courseDTO.getCourseStatus());
        courseModel.setCourseLevel(courseModel.getCourseLevel());
        courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        courseService.save(courseModel);

        return ResponseEntity.status(HttpStatus.OK).body(courseService.save(courseModel));
    }
````
- Buscar todos os Course's
````java
    @GetMapping
    public ResponseEntity<List<CourseModel>> getAllCourses(){
        return ResponseEntity.status(HttpStatus.OK).body(courseService.findAll());
    }
````
- Buscar Course por Id
````java
    @GetMapping("/{courseId}")
    public ResponseEntity<Object> getCourseById(@PathVariable(value = "courseId") UUID courseId){

        Optional<CourseModel> courseModelOptional = courseService.findBy(courseId);
        if(!courseModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found");
        }

        return ResponseEntity.status(HttpStatus.OK).body(courseModelOptional);
    }
````


**[Novos métodos de CourseService](https://github.com/DeadRon/cuorse/commit/c6f16acb20264832bd7d3505893daf3d617d2d5d)**:
- Salvar Course
````java
CourseModel save(CourseModel cuorseModel);
````
- Buscar Course por Id
````java
Optional<CourseModel> findBy(UUID cuorseId);
````
- Buscar todos os Course's
````java
List<CourseModel> findAll();
````

**Implementações de Cuorse Service**
- Salvar Course
````java
    @Override
    public CourseModel save(CourseModel cuorseModel) {
        return courseRepository.save(cuorseModel);
    }
````
- Buscar Course por Id
````java
    @Override
    public Optional<CourseModel> findBy(UUID courseId) {
        return courseRepository.findById(courseId);
    }
````
- Buscar todos os Course's
````java
    @Override
    public List<CourseModel> findAll() {
        return courseRepository.findAll();
    }
````

[Correção da consulta](https://github.com/DeadRon/cuorse/commit/79474415c0d707c8221a35c05be6e40203144c9a) SQL para recuperar todos os módulos relacionados à um curso
````java
    @Query(value = "SELECT * FROM tb_modules WHERE course_course_id = :courseId", nativeQuery = true)
    List<ModuleModel> findAllModulesIntoCourse(@Param("courseId") UUID courseId);
````

## Criar RESTful para Module com Validações

***Ponto de atenção: um módulo está sempre associado com um Curso, então qualquer ação que envolva algum módulo deve levar em consideração o Curso ao qual este Módulo pertence***

- **End Point para salvar Module**
  - [Criar ModuleDTO](https://github.com/DeadRon/cuorse/commit/27f92b253bbad043e36cf583614615edfa8d34f0)
  - Verificar se Course existe através do CourseId, copiar propriedades do DTO para um Model, setar creationDate e lastUpdate com dia/hora atual e salvar Module
  - ````java
    @PostMapping("/courses/{courseId}/modules")
    public ResponseEntity<Object> saveModule(@PathVariable("courseId")UUID cuorseId, @RequestBody @Valid ModuleDTO moduleDTO){

        Optional<CourseModel> courseModelOptional = courseService.findBy(cuorseId);
        if(!courseModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found");
        }

        var moduleModel = new ModuleModel();
        BeanUtils.copyProperties(moduleDTO, moduleModel);
        moduleModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        moduleModel.setCourse(courseModelOptional.get());
        return ResponseEntity.status(HttpStatus.CREATED).body(moduleService.save(moduleModel));
    }
    ````
  - Declarar método Save na interface ModuleService
    - ````java
      ModuleModel save(ModuleModel moduleModel);
      ````
      - Implementar método Save na implementação da interface ModuleService e chamar ModuleRepository e salvar o módulo com método Save
      - ````java
        @Override
        public ModuleModel save(ModuleModel moduleModel) {
            return moduleRepository.save(moduleModel);
        }
        ````

- **End Point para deletar Module**
  - Verificar se Course existe através do CourseId - para um Module existir precisa estar vínculado com um Course
  - Verificar se Module existe através do ModuleId
  - ````java
    @Transactional
    @Override
    public void delete(ModuleModel module) {
        List<LessonModel> lessonModels = lessonRepository.findAllLessonsIntoCourse(module.getModuleId());
        if(!lessonModels.isEmpty()){
            lessonRepository.deleteAll(lessonModels);
        }
        moduleRepository.delete(module);
    }
    ````
    - Declarar método findModuleIntoCourse na interface ModuleService, implementar findModuleIntoCourse Save na implementação da interface ModuleService - verifica se um módulo existe dentro de um curso
    - Declarar método findModuleIntoCourse na interface ModuleRepository, deve retornar um Optinal<ModuleModel>.
      - extrair valor dos parâmetos CourseId e ModuleId com @Param
      - Usar SQL Query para buscar um módulo que tenha CourseId e ModuleId iguais ao valores CourseId e ModuleId
        passados no parâmetro do método findModuleIntoCourse.
        - SELECT * FROM tb_modules WHERE course_course_id = :courseId
    - ````java
      @Query(value = "SELECT * FROM tb_modules WHERE course_course_id = :courseId AND module_Id = :moduleId", nativeQuery = true)
      Optional<ModuleModel> findModuleIntoCourse(@Param("courseId") UUID courseId, @Param("moduleId")UUID moduleId);
      ````

  - No controller usar método delete de ModuleService para deletar Module. Na implementação chamar método **delete** do Repository
  - retornar status OK

- **End Point para atulizar Module**
  - Parâmetros necessários: CourseId, ModuleId, ModuleDTO
  - Verificar de Module existe antes de atualizar
    - Usar método findModuleIntoCourse do ModuleService
  - Atualizar Module - Apenas Title e Description podem ser atualizados
  - Chamar método Save da Interface ModuleSerive para salvar as alterações
  - Retornar status OK

- **End point para buscar todos os módulos de um curso**
  - retornar ResponseEntity com status OK com um corpo na resposta
  - ````java
    public ResponseEntity<Object> updateModule(@PathVariable("courseId")UUID courseId,
                                               @PathVariable("moduleId")UUID moduleId,
                                               @RequestBody @Valid ModuleDTO moduleDTO){

        Optional<ModuleModel> moduleModelOptional = moduleService.findModuleIntoCourse(courseId, moduleId);
        if(!moduleModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found for this course.");
        }

        var moduleModel = moduleModelOptional.get();
        moduleModel.setDescription(moduleDTO.getDescription());
        moduleModel.setTitle(moduleDTO.getTitle());

        return ResponseEntity.status(HttpStatus.OK).body(moduleService.save(moduleModel));
    }    
    ````
    - chamar o método findAllByCourse da interface ModuleService e passar como argumento o id de um curso
      - declarar o método findAllByCourse na interface ModuleService e implentar o método findAllByCourse declarado na interface
      - ````java
        Optional<ModuleModel> findModuleIntoCourse(UUID courseId, UUID moduleId); 
        ````
      - declarar no ModuleRepository o método findAllByCourse, recebe como argumento o id o do curso
        - usar @query para criar uma uma consulta SQL nativa                                  
        - ````sql
          SELECT * FROM tb_modules WHERE course_course_id = :courseId AND module_Id = :moduleId
          ````
      - ````java
        @Query(value = "SELECT * FROM tb_modules WHERE course_course_id = :courseId", nativeQuery = true)
        Optional<ModuleModel> findModuleIntoCourse(UUID courseId, UUID moduleId);
        ````

- **End point para buscar um módulo**
  - receber como argumento um id de curso e módulo
  - chamar o método findModuleIntoCourse do ModuleService para encontrar um módulo
    - passar id de curso e módulo como parâmetro
  - retornar status OK com o módulo achado
  - ````java
     @GetMapping("/courses/{courseId}/modules/{moduleId}")
     public ResponseEntity<Object> getOneModule(@PathVariable("courseId")UUID courseId,
     @PathVariable("moduleId")UUID moduleId){
     Optional<ModuleModel> moduleModelOptional = moduleService.findModuleIntoCourse(courseId, moduleId);
         if(!moduleModelOptional.isPresent()){
             return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found for this course.");
         }

         return ResponseEntity.status(HttpStatus.OK).body(moduleModelOptional.get());
     }
    ````

  - declarar método findModuleIntoCourse na interface ModuleService e implementar
       - ````java
         Optional<ModuleModel> findModuleIntoCourse(UUID courseId, UUID moduleId);
         ```` 
  - declarar método findModuleIntoCourse na interface ModuleRepository
    - usar @query para criar uma uma consulta SQL nativa
      - "SELECT * FROM tb_modules WHERE course_course_id = :courseId"
    - ````java
      @Query(value = "SELECT * FROM tb_modules WHERE course_course_id = :courseId AND module_Id = :moduleId", nativeQuery = true)
      Optional<ModuleModel> findModuleIntoCourse(@Param("courseId") UUID courseId, @Param("moduleId")UUID moduleId);
      ````  

### Criar RESTful para Lesson com Validações
- **URLs são definidas a nivel de método**


- **[Criar Lesson Controller](https://github.com/DeadRon/cuorse/commit/6f0f55f4755aa6b6c667efee20ab8e5e0deded3a)**
  - **End point para salvar um lesson**
    - URL: "modules/{moduleId}/lessons
    - Deve estar vinculado à um módulo obrigatóriamente
    - Criar LessonDTO - título e videoUrl são obrigatórios
    - Buscar módulo por Id
      - Service e repository. Usar o findById de JPARepository
    - Setar data de criação do lesson com data/hora atual
    - Vincular com o módulo buscado
    - Salvar
    - Retornar Status 200 OK com os do Lesson criado

  - Deletar lesson por Id
    - URL: "modules/{moduleId}/lessons/{lessonId}
    - Deve estar vinculado à um módulo obrigatóriamente
    - Buscar lesson com o método findLessonIntoModule do LessonService.
      - Declarar método na interface LessonService e na Interface LessonRepository. No repository declarar método findLessonIntoModule
        - Retorna um Optional de Lesson
      - O funcionamento é semelhante lógica de encontrar um módulo dentro de curso feito antes
      - Como parâmetro recebe muduleId e lessonId.
    - Lesson nao tem uma associação em cascata, usar o método delete do JPARepository

  - Update Lesson
    - URL: "modules/{moduleId}/lessons/{lessonId}
    - Receber novas informações no LessonDTO
    - Deve estar vinculado à um módulo obrigatóriamente
    - Buscar lesson com o método findLessonIntoModule do LessonService.
      - Atulizar dados title, description e videoURL
    - Retornar Status 200 OK com os do Lesson atualizado

  - Recuperar todos os Lessons de um Module
    - URL: /modules/{moduleId}/lessons
    - chamar método findAllByModule do LessonService.
      - Declarar método findAllLessonsByModule na interface [LessonService](https://github.com/DeadRon/cuorse/commit/94914cda8563316b0bc0e90b8a2ab95b0ea6184d) e [implementar](https://github.com/DeadRon/cuorse/commit/94914cda8563316b0bc0e90b8a2ab95b0ea6184d)
      - declarar método findAllLessonsByModule na interface [LessonRepository](https://github.com/DeadRon/cuorse/commit/9bde2b4a00baa2e6f118a038585c146e9d4dacac) com @Query
        - Usar SQL semelhante ao de buscar todos os cursos de UM módulos.

  - Recuperar Lesson por ModuleId e LessonId
    - URL: "modules/{moduleId}/lessons/{lessonId}
    - Deve estar vinculado à um módulo obrigatóriamente
    - Buscar lesson com o método findLessonIntoModule do LessonService.
    - Retornar Status 200 OK com os do Lesson buscado

  - Teste end point de deleções de Cursos para verficar se a deleção em cascata funciona
