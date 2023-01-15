FetchType Eager/Lazy, @EntityGraph, FetchMode SELECT/SUBSELECT/JOIN, @Query e @Modify

Relação “many to one/one to many” Course/Module: Para especificar a relação one to many/many to one  no lado muitos da relação é preciso declarar uma instância da entidade
no lado um da relação e anotá-la com a anotação @ManyToOne. O parâmetro opcional da anotação garante que as entidades do lado muitos estão obrigatoriamente vinculadas a um
módulo, em outras palavras ele indica se irá haver um relacionamento não nulo caso o parâmetro receba valor “false”.

Por exemplo: Na relação entre cursos e módulos, um curso pode ter vários módulos e um módulo está sempre associado a um curso. Para especificarmos para o JPA que existe
uma relação de muitos módulos para um curso é preciso declarar na classe módulo uma variável do tipo curso e anotar com anotação @ManyToOne e passar o valor “false” no
parâmetro “optional” para indicar uma relação não nulo entre as entidades.

No lado um ou One da relação é preciso declarar um conjunto Set<T> (da entidade no lado muitos) e anotar esta coleção com a anotação @OneToMany e no parâmetro mappedBy
desta mesma anotação é preciso declarar (em forma de String) o nome do atributo da entidade(do lado “um”)  declarado na classe que representa as entidades do lado muitos.

Exemplo: Como dito no exemplo acima um curso pode ter vários módulos. para especificarmos para o JPA que existe uma relação de um curso possuir vários módulos é preciso
declarar um conjunto Set<ModuleModel> Na classe de curso, em seguida é preciso anotar a coleção declarada com a notação @OneToMany e no parâmetro mappedBy  é preciso
declarar O mesmo nome da variável curso que foi declarada na classe módulo em formato de string isso garante a relação bidirecional  um para muitos entre as  entidades
curso e módulo

Observação: o não uso uso do mappedBy  não impede que o hibernate construa a relação um para muitos porém serão criadas tabelas auxiliares para implementar a relação
com o uso do mappedBy é criado apenas uma chave estrangeira na tabela de Module, está chave estrangeira referencia a tabela Course.


observação: Ou não uso de mappedBy  não impede que hibernate o construa a relação de um para muitos porém serão criadas tabelas auxiliares na relação para implementar
a relação, com o uso de mappedBy  o hibernate irá criar uma chave estrangeira nas entidades do lado muitos para referenciar a Entidade do lado um a qual todas as entidades
do lado muitos estão associadas.

FETCH TYPE

Define como os dados serão carregados do banco de dados. Isso acontece no lado muito da relação. Toda vez que o banco consultar uma entidade que tenha muitas entidades
associadas a ela, este FETCH será feito e nesse caso específico irá trazer ou não as muitas entidades associadas a ele no momento da consulta. Isso pode causar problemas
de desempenho caso o banco possua muitos registros associados à uma entidade, porque na maioria dos casos não há necessidade de ver todos os dados associados a um registro
apenas o registro em si será necessário. Para evitar esse cenário o FETCH.LAZY, que indica ao JPA um carregamento lento, irá trazer apenas um registro do banco de dados
sem a muitas entidades associadas a este registro. Mas em caso de haver a necessidade de trazer as muitas entidades associadas à entidade consultada, é usado o
FETCH.EAGLE, que indica ao JPA um carregamento rápido, que irá trazer o registro do banco de dados e as muitas entidades associadas a este registro. Para especificar
qual tipo de fetch será usado basta usar o parâmetro fetch da anotação @OneToMany e especificar o FetchType a ser usado. O uso de FETCH também é válido no caso de haver
uma consulta de uma entidade do lado muitos da relação e não seja necessário visualizar a entidade associado no lado um da relação.
