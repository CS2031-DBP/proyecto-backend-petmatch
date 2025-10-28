# **PetMatch: Integración y apoyo en albergues para ayudar a los animales**
**Curso:** Desarrollo Basado en Plataformas (CS 2031)  

**Postman Collection:** [PostmanCollection](https://asd555-8170.postman.co/workspace/petmatch~a8e471f9-ed4f-4af2-aa04-8d6e9a733429/collection/43516792-bcb6e32d-31db-4ce8-aaa0-d2df52bfdbc0?action=share&creator=43516792&active-environment=43516792-ecbff733-47ce-4eb0-bbb8-2cf1f6e8cd2b)

**Integrantes:**  
- Valentín Tuesta Barrantes - 202410251  
- Rayhan Matos Copello - 202410377  
- Daniela Valentina Villacorta Sotelo - 202410253  
- Emma Anderson Gonzalez - 202410607  
- Juan Marcelo Ferreyra Gonzales - 202410166  
---

## **Índice**

1. **Introducción**
   * 1.1 Contexto
   * 1.2 Objetivos del Proyecto
2. **Identificación del Problema o Necesidad**
   * 2.1 Descripción del Problema
   * 2.2 Justificación
3. **Descripción de la Solución**
   * 3.1 Funcionalidades Implementadas
   * 3.2 Tecnologías Utilizadas
4. **Modelo de Entidades**
   * 4.1 Diagrama de Entidad-Relación (E-R)
   * 4.2 Descripción de Entidades
     * 4.2.1 Shelter
     * 4.2.2 User
     * 4.2.3 VolunteerProgram
     * 4.2.4 Animal
5. **Testing y Manejo de Errores**
   * 5.1 Niveles de Testing Realizados
   * 5.2 Resultados
   * 5.3 Manejo de Errores
6. **Medidas de Seguridad Implementadas**
   * 6.1 Seguridad de Datos
   * 6.2 Prevención de Vulnerabilidades
7. **Conclusión**
   * 7.1 Logros del Proyecto
   * 7.2 Aprendizajes Clave
   * 7.3 Trabajo Futuro
8. **Apéndices**
   * 8.1 Licencia
   * 8.2 Referencias
---

## **1. Introducción**

### **1.1 Contexto**

En el Perú, el abandono y la falta de control poblacional de animales domésticos se han convertido en un problema social y sanitario de creciente preocupación. Según Edward Briceño (2024):

> “En Perú, la población de perros callejeros supera los 6 millones, mientras que la de gatos supera el millón. A pesar de esto, al año 2018, el 40% de las familias urbanas peruanas no contaba con la presencia de mascotas en su hogar”.

Esta cifra evidencia una gran cantidad de animales sin hogar, pero también un potencial espacio para la adopción responsable que aún no ha sido plenamente aprovechado.

Ante esta realidad, numerosos albergues, refugios y organizaciones de rescate han surgido con la misión de brindar refugio, atención médica y una nueva oportunidad de vida a estos animales. 

Sin embargo, la mayoría de estos espacios opera de manera independiente y con recursos limitados, lo que dificulta la coordinación, el registro de animales y la difusión de sus actividades. Además, no existe una base de datos nacional o regional que centralice información sobre los animales en abandono ni sobre los albergues que los acogen, lo que complica la planificación y la colaboración interinstitucional.

En este contexto, **PetMatch** surge como una iniciativa que busca implementar un registro digital local de albergues y animales en adopción, con miras a una futura expansión nacional.

### **1.2 Objetivos del Proyecto**

El proyecto **PetMatch** tiene como objetivo principal **facilitar la interacción entre usuarios y albergues de animales** mediante una plataforma digital integral que centralice información y promueva la adopción responsable.

**Objetivos específicos:**
1. Registrar y organizar albergues locales, estableciendo una base de datos que sirva como punto de partida para una futura red nacional.  
2. Crear una interfaz intuitiva que permita buscar y adoptar animales de manera segura y transparente.  
3. Implementar un sistema de voluntariado donde los usuarios puedan participar en campañas y actividades de ayuda.  
4. Desarrollar un panel administrativo que facilite a los albergues la gestión de animales, adopciones y eventos.  
5. Integrar mecanismos de seguridad y autenticación para garantizar la protección de datos personales y organizacionales.  

---

## **2. Identificación del Problema o Necesidad**

### **2.1 Descripción del Problema**

A pesar de la labor constante de los albergues y organizaciones de rescate animal, en el Perú no existe un sistema estructurado que centralice la información sobre los animales en situación de abandono ni sobre las instituciones que los acogen. Tal como lo expresa Hernán Medrano (2023):

> “En el Perú no hay un registro nacional, provincial, ni distrital, de perros y gatos en situación de abandono”.

La fragmentación en la gestión y comunicación entre los distintos actores involucrados (albergues, voluntarios, adoptantes y entidades públicas) limita la eficiencia en las campañas de adopción y dificulta la identificación de las zonas con mayor necesidad de atención.

Asimismo, muchos albergues siguen gestionando manualmente sus registros, lo que se traduce en un trabajo extra con muchas imprecisiones; en consecuencia, se desaprovechan oportunidades de colaboración. Por otro lado, las personas interesadas en adoptar o donar carecen de un canal confiable y accesible que les permita encontrar información actualizada sobre los animales disponibles o las necesidades específicas de cada refugio.

En conjunto, estos factores reflejan un problema estructural de desconexión y desorganización digital en torno al bienestar animal en el país, que requiere una solución tecnológica sostenible, escalable y centrada en las comunidades locales.

### **2.2 Justificación**

Resolver esta problemática es esencial no solo para mejorar la calidad de vida de los animales abandonados, sino también para fortalecer la cultura de adopción y responsabilidad social en el Perú.  
Una plataforma digital que facilite la comunicación entre albergues y ciudadanos permitiría optimizar recursos, fomentar la transparencia en los procesos de adopción y promover la colaboración interinstitucional.

Además, la creación de registros locales constituye el primer paso hacia una red nacional de información sobre animales en adopción, capaz de ofrecer datos precisos para campañas de esterilización, políticas públicas y programas educativos.  

En este sentido, **PetMatch** no solo busca atender una necesidad inmediata de conexión y organización, sino también construir las bases para una gestión integral del bienestar animal a nivel nacional.

---

## **3. Descripción de la Solución**

El enfoque principal del proyecto es simplificar la interacción entre los distintos usuarios del sistema mediante una aplicación web y móvil.

### **3.1 Funcionalidades Implementadas**

El diseño funcional de **PetMatch** responde directamente a las necesidades identificadas en el contexto y el problema.  
Entre las principales funcionalidades se incluyen:

- **Registro de Usuarios y Albergues:** creación de perfiles diferenciados según el rol del usuario (adoptante, donante, voluntario o albergue).  
- **Reporte de Animales Abandonados:** registro de puntos donde se ha encontrado un animal, permitiendo una rápida respuesta.  
- **Adopción y Gestión de Solicitudes:** flujo completo de adopción, con seguimiento y validación por parte del albergue.  
- **Voluntariado y Eventos:** inscripción en programas y gestión automática de cupos.  

**Funcionalidades MVP (Producto Mínimo Viable):**
- Registro de usuarios y albergues.  
- Reporte de animales abandonados.

### **3.2 Tecnologías Utilizadas**

- **Backend:** Java, Spring Boot  
- **Base de Datos:** PostgreSQL  
- **Autenticación:** JWT  
- **Mensajería:** Email  

---

## **4. Modelo de Entidades**

### **4.1 Diagrama de Entidad-Relación (E-R)**
Imagen adjunta entre los files (entidadrelacion.jpg).

### **4.2 Descripción de Entidades**
Entre las entidades principales del sistema se encuentran: **Shelter**, **User**, **VolunteerProgram** y **Animal**.

#### **4.2.1 Shelter**
Representa a los albergues registrados dentro de la plataforma. Cada refugio gestiona animales, publicaciones y programas de voluntariado.

**Atributos:**
* `name`
* `password`
* `description`
* `latitude`, `longitude`
* `address`
* `phone`
* `capacity`
* `availableSpaces`
* `email`
* `rating`

**Relaciones:**

* `posts`: relación *uno a muchos* con `Post`.
* `volunteerPrograms`: relación *uno a muchos* con `VolunteerProgram`.
* `animals`: relación *uno a muchos* con `Animal`.
* `followers`: relación *muchos a muchos* con `User`.

#### **4.2.2 User**
Representa a toda persona registrada en el sistema, incluyendo adoptantes, donantes, voluntarios o administradores.

**Atributos:**
* `name`, `lastname`
* `email`
* `password`
* `role`

**Relaciones:**

* `comments`: relación *uno a muchos* con `Comments`.
* Puede seguir a varios refugios (`Shelter`) mediante una relación *many to many*.

Usa herencia con
`@Inheritance(strategy = InheritanceType.JOINED)`
para permitir la extensión hacia subclases especializadas (por ejemplo, `Volunteer` o `Admin`).

#### **4.2.3 VolunteerProgram**
Representa los programas de voluntariado organizados por los albergues para apoyar a los animales.

**Atributos:**
* `name`
* `description`
* `startDate`, `finishDate`
* `location`
* `necessaryVolunteers`
* `enrolledVolunteers`
* `status`

**Relaciones:**

* `shelter`: relación *muchos a uno* con `Shelter`.
* `enrolled`: relación *uno a muchos* con `Inscription`.

Permite administrar las inscripciones y el estado del programa (por ejemplo, activo o lleno).

#### **4.2.4 Animal**
Representa a los animales bajo cuidado o disponibles para adopción.

**Atributos:**
* `registered`
* `name`
* `breed`
* `image`

**Relaciones:**

* `shelter`: relación *muchos a uno* con `Shelter`.

Esta entidad es clave para registrar y gestionar la información de cada animal dentro del sistema.

---

## **5. Testing y Manejo de Errores**

### **5.1 Niveles de Testing Realizados**

Durante el desarrollo del sistema se implementaron diferentes niveles de prueba para asegurar la **calidad y estabilidad del software**:

* **Pruebas Unitarias:**
  Se validaron los métodos de servicios y utilidades de seguridad, como la generación y validación de tokens en `JwtService`, garantizando que las operaciones de autenticación funcionen correctamente.

* **Pruebas de Integración:**
  Se probaron componentes del sistema en conjunto, como los controladores (`UserController`, `AlbergueController`) junto con los servicios (`UserService`, `AlbergueService`), utilizando `MockMvc` para simular peticiones HTTP.
  Ejemplo: el test `UserControllerTest` valida el registro y autenticación de usuarios verificando códigos de estado y estructura del JSON devuelto.

* **Pruebas de Sistema:**
  Se realizaron pruebas de flujo completo en endpoints protegidos y públicos, comprobando el correcto funcionamiento de la seguridad JWT y la autorización de roles (`USER`, `ALBERGUE`).

* **Pruebas de Aceptación:**
  Se verificó que las funcionalidades principales —como registro, login, y acceso a los programas de voluntariado— cumplan con los requerimientos esperados por el usuario final.

### **5.2 Resultados**

* Los endpoints públicos (`/user/auth/**`, `/albergues/auth/**`, `/albergues`, `/voluntarios`) respondieron correctamente sin necesidad de token.
* Los endpoints protegidos solo permitieron el acceso con tokens JWT válidos, según el tipo de entidad (usuario o albergue).
* Se detectaron y corrigieron errores relacionados con:

  * **Autenticación inválida:** manejo de tokens expirados o manipulados.
  * **Autorización incorrecta:** validación de roles dentro del filtro `JwtAuthorizationFilter`.
  * **Errores de mapeo en DTOs:** ajustes en los campos devueltos por los controladores.

En todos los casos, los tests de controladores (`MockMvc`) retornaron los códigos de estado esperados (`200 OK`, `201 Created`, `403 Forbidden`, `401 Unauthorized`), confirmando la estabilidad del sistema.

### **5.3 Manejo de Errores**

El sistema implementa un manejo de errores **centralizado** mediante excepciones personalizadas y validaciones globales:

* **`JwtAuthorizationFilter`:**
  Maneja errores de autenticación y validación de tokens JWT, evitando el acceso a rutas protegidas sin un token válido.

* **`UserDetailsServiceImpl` y `AlbergueDetailsServiceImpl`:**
  Lanza `UsernameNotFoundException` cuando las credenciales no corresponden a un usuario o albergue registrado.

* **Validaciones en Controladores:**
  Se controlan errores de entrada (como datos incompletos o mal formateados) usando excepciones propias y respuestas JSON estandarizadas.

El manejo centralizado de excepciones permite ofrecer mensajes **claros y consistentes al usuario**, mejorar la **seguridad** del sistema y facilitar la **depuración de errores** durante las pruebas.

---

## **6. Medidas de Seguridad Implementadas**

### **6.1 Seguridad de Datos**

El sistema **PetMatch** garantiza la seguridad de la información de usuarios y albergues mediante distintos mecanismos de protección integrados en **Spring Security** y **JWT (JSON Web Token)**.

**Principales mecanismos:**

* **Autenticación basada en JWT:**
  Cada usuario o albergue registrado obtiene un token JWT firmado digitalmente con el algoritmo **HS256**.
  Este token contiene el correo electrónico y el tipo de entidad (`USER` o `ALBERGUE`), asegurando la identidad en cada solicitud.

* **Gestión de roles y permisos:**
  Se definen reglas de acceso diferenciadas:

  * Rutas `/user/**` → accesibles solo por usuarios con el rol `ROLE_USER`.
  * Rutas `/albergues/**` → accesibles solo por albergues con el rol `ROLE_ALBERGUE`.
  * Las rutas de registro, login y consulta pública son accesibles sin autenticación.

* **Encriptación de contraseñas:**
  Todas las contraseñas se almacenan usando **BCrypt**, un algoritmo seguro de hash con sal que protege los datos incluso ante filtraciones de la base de datos.

* **Sesiones sin estado (Stateless):**
  La autenticación se maneja exclusivamente mediante tokens JWT, sin mantener sesiones en el servidor, reduciendo la superficie de ataque y evitando el robo de sesiones.

* **Servicio de autenticación unificado (`CustomUserDetailsService`):**
  Este servicio permite validar credenciales tanto de **usuarios** como de **albergues**, consultando repositorios separados (`UserRepository` y `ShelterRepository`), lo que asegura un control coherente y centralizado.

### **6.2 Prevención de Vulnerabilidades**

Para prevenir vulnerabilidades comunes en aplicaciones web, **PetMatch** implementa las siguientes medidas:

* **Protección contra inyección SQL:**
  Todas las operaciones con base de datos se realizan a través de **Spring Data JPA**, utilizando consultas parametrizadas y ORM, evitando la concatenación de strings en sentencias SQL.

* **Prevención de ataques XSS (Cross-Site Scripting):**
  Las respuestas JSON no incluyen contenido dinámico no validado, y la comunicación entre frontend y backend se maneja con objetos fuertemente tipados, reduciendo el riesgo de ejecución de scripts maliciosos.

* **Desactivación de CSRF en contextos JWT:**
  Dado que la aplicación es **stateless** (sin cookies de sesión), el token JWT sustituye la validación CSRF, y este mecanismo se desactiva explícitamente en la configuración de seguridad:

  ```java
  http.csrf(AbstractHttpConfigurer::disable);
  ```

* **Validación del token en cada solicitud:**
  El filtro `JwtAuthorizationFilter` intercepta todas las peticiones y valida:

  * La existencia del encabezado `Authorization`.
  * La validez, firma y expiración del token.
  * La existencia del usuario o albergue en la base de datos.

* **Control de acceso centralizado:**
  Las rutas protegidas están declaradas explícitamente en `SecurityConfig`, garantizando que solo los roles adecuados puedan acceder a cada endpoint.
---
## **7. Conclusión**

### **7.1 Logros del Proyecto**

El desarrollo de **PetMatch** permitió construir una plataforma funcional y segura que facilita la **conexión entre refugios y usuarios** interesados en la adopción y el voluntariado animal.
Entre los principales logros alcanzados se encuentran:

1. Implementación de un **sistema de autenticación seguro** mediante **JWT (JSON Web Tokens)**, que diferencia entre usuarios y albergues, garantizando la protección de datos y accesos.
2. Modelado de las entidades principales —`User`, `Shelter`, `Animal` y `VolunteerProgram`— con una estructura relacional clara y eficiente.
3. Integración de **Spring Security** para gestionar roles, permisos y rutas protegidas de forma controlada.
4. Ejecución de **pruebas unitarias y de integración** que aseguraron la funcionalidad y estabilidad del sistema.
5. Diseño de un flujo coherente de registro, autenticación y manejo de datos que mejora la experiencia tanto para usuarios como para albergues.

En conjunto, el proyecto responde de manera efectiva a la **problemática del abandono animal en el Perú**, proporcionando una herramienta tecnológica que **promueve la adopción responsable y la participación ciudadana en programas de voluntariado**.

### **7.2 Aprendizajes Clave**

Durante el desarrollo del proyecto se adquirieron conocimientos relevantes en distintos ámbitos:

1. Comprensión del **ciclo completo de autenticación y autorización** utilizando JWT y Spring Security en entornos sin estado (*stateless*).
2. Aplicación de **buenas prácticas de modelado de datos** con JPA e Hibernate, incluyendo relaciones bidireccionales y el uso adecuado de anotaciones.
3. Fortalecimiento en el uso del **framework Spring Boot**, su estructura modular y la integración con servicios REST.
4. Implementación de estrategias de **manejo global de errores y excepciones**, mejorando la confiabilidad y claridad de las respuestas del sistema.
5. Consolidación del trabajo colaborativo y la documentación del código como parte del proceso de desarrollo ágil.

### **7.3 Trabajo Futuro**

Para fortalecer y ampliar las funcionalidades de **PetMatch**, se proponen las siguientes líneas de mejora:

1. Incorporar un **sistema de geolocalización avanzada** que permita a los usuarios encontrar refugios cercanos y ver animales disponibles en tiempo real.
2. Desarrollar un **módulo de adopción digital**, con seguimiento de solicitudes y firma electrónica de formularios.
3. Integrar **notificaciones automáticas por correo o aplicación móvil**, para mantener informados a los usuarios sobre nuevos animales o programas.
4. Añadir **métricas y paneles estadísticos** para los refugios, ayudándoles a evaluar su impacto social.
---

## **8. Apéndices**

### **8.1 Licencia**

**MIT License**

### **8.2 Referencias**

* Briceño, E. (2024). Más de 7 millones de perros y gatos abandonados y la oportunidad de volver a casa. _Convive_.
  [https://convoca.pe/convive/mas-de-7-millones-de-perros-y-gatos-abandonados-y-la-oportunidad-de-volver-casa](https://convoca.pe/convive/mas-de-7-millones-de-perros-y-gatos-abandonados-y-la-oportunidad-de-volver-casa)

* Medrano, H. (14 de mayo de 2023). Perros y gatos sin hogar en Lima: ¿Existe un registro de animales en situación de abandono? ¿Qué se sabe de la Ley 4 patas?. _El Comercio_.
  [https://elcomercio.pe/lima/sucesos/perros-y-gatos-sin-hogar-en-lima-existe-un-registro-de-animales-en-situacion-de-abandono-que-se-sabe-de-la-ley-4-patas-adopcion-responsable-esterilizacion-albergues-rescatistas-noticia/?ref=ecr](https://elcomercio.pe/lima/sucesos/perros-y-gatos-sin-hogar-en-lima-existe-un-registro-de-animales-en-situacion-de-abandono-que-se-sabe-de-la-ley-4-patas-adopcion-responsable-esterilizacion-albergues-rescatistas-noticia/?ref=ecr)

---
