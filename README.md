# Evaluación Técnica Backend

## Objetivo

El objetivo de esta evaluación técnica es verificar el conocimiento del candidato en ciertas tecnologías. Los datos de interés y obligatorios de un candidato incluyen: nombre, apellido, tipo de documento (DNI, LE, LC), número de documento, fecha de nacimiento y las tecnologías que maneja. Para las tecnologías, se busca persistir el nombre (java, python, maven, hibernate, spring) y versión. Se establece una relación N a M entre Candidato y Tecnología, con un atributo adicional de años de experiencia que indica cuántos años de experiencia tiene el candidato en una tecnología específica.

## Implementación

### API Rest (CRUD) para Candidato y Tecnología

Se debe implementar una API Rest que permita realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar) para las entidades de Candidato y Tecnología. Esto implica la creación de controladores, servicios, repositorios y entidades.

#### Candidato

- Controlador: `CandidatoController`
- Servicio: `CandidatoService`
- Repositorio: `CandidatoRepository`
- Entidad: `Candidato`

#### Tecnología

- Controlador: `TecnologiaController`
- Servicio: `TecnologiaService`
- Repositorio: `TecnologiaRepository`
- Entidad: `Tecnologia`

### BD en memoria

Se utilizará una base de datos en memoria, por ejemplo, H2.

### Validaciones

Se implementarán validaciones para campos obligatorios, de fecha y numéricos.

### Excepciones

Se manejarán al menos dos excepciones personalizadas en el código.

### Logs

Se crearán registros de logs en puntos relevantes del código.#   P r u e b a T e c n i c a M o b y 2 d a R e v  
 