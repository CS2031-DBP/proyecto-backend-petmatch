# **PetMatch: Integración y apoyo en albergues para ayudar a los animales**
**Curso:** Desarrollo Basado en Plataformas (CS 2031)  
**Integrantes:**  
- Valentín Tuesta Barrantes - 202410251  
- Rayhan Matos Copello - 202410377  
- Daniela Valentina Villacorta Sotelo - 202410253  
- Emma Anderson Gonzalez - 202410607  
- Juan Marcelo Ferreyra Gonzales - 202410166  

---

## **Índice**
1. Introducción  
   1.1 Contexto  
   1.2 Objetivos del Proyecto  
2. Identificación del Problema o Necesidad  
   2.1 Descripción del Problema  
   2.2 Justificación  
3. Descripción de la Solución  
   3.1 Funcionalidades Implementadas  
   3.2 Tecnologías Utilizadas  
4. Modelo de Entidades  
   4.1 Diagrama E-R  
   4.2 Descripción de Entidades  
5. Testing y Manejo de Errores  
6. Medidas de Seguridad Implementadas  
7. Eventos y Asincronía  
8. Conclusiones  
9. Apéndices  

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

---

## **6. Medidas de Seguridad Implementadas**

La seguridad de **PetMatch** se basa en el uso de **Spring Security** y **JWT (JSON Web Tokens)** para garantizar una autenticación robusta, separación de roles y protección de rutas sensibles. A continuación, se detallan los principales componentes y mecanismos aplicados.

### **6.1. Autenticación y Autorización**

El sistema implementa un flujo **stateless** (sin sesiones en servidor) basado en tokens JWT.
Cada vez que un usuario o albergue inicia sesión correctamente, se genera un token firmado que contiene:

* El correo electrónico del usuario (subject).
* El tipo de entidad (`USER` o `ALBERGUE`).
* Una fecha de expiración configurable.

Este token debe enviarse en el encabezado HTTP `Authorization: Bearer <token>` en cada petición posterior.

### **6.2. Generación y Validación del Token**

El servicio `JwtService` es el encargado de manejar los procesos de emisión y verificación de tokens.
Utiliza una **clave secreta HMAC-SHA256** almacenada en el archivo de configuración (`application.properties`) y un tiempo de expiración definido por variable de entorno (`jwt.expiration`).

**Características clave:**

* Método `generateToken()` → genera el token con los *claims* de tipo y correo.
* Método `isTokenValid()` → verifica su validez temporal y firma.
* Método `extractEntityType()` → diferencia entre usuarios y albergues autenticados.

### **6.3. Filtro de Autorización**

El componente `JwtAuthorizationFilter` intercepta cada solicitud HTTP antes de llegar al controlador.
Sus principales tareas son:

1. Extraer el token del encabezado `Authorization`.
2. Validar su autenticidad mediante `JwtService`.
3. Determinar el tipo de entidad (`USER` o `ALBERGUE`).
4. Cargar los detalles de la entidad correspondiente desde la base de datos (a través de `UserDetailsServiceImpl` o `AlbeguerDetailsServiceImpl`).
5. Configurar el contexto de seguridad (`SecurityContextHolder`) con las credenciales válidas.

Esto garantiza que solo las peticiones con tokens válidos puedan acceder a rutas protegidas.

### **6.4. Configuración de Roles y Rutas Protegidas**

El archivo `SecurityConfig` define las políticas de acceso para cada endpoint.
El sistema diferencia claramente entre **rutas públicas** y **rutas restringidas**:

**Rutas públicas**

* `/user/auth/**` → registro e inicio de sesión de usuarios.
* `/albergues/auth/**` → registro e inicio de sesión de albergues.
* `/albergues`, `/albergues/near` → consulta de refugios sin autenticación.
* `/voluntarios`, `/voluntarios/{id}/programas` → acceso libre a programas de voluntariado.

**Rutas protegidas**

* `/user/**` → requiere autenticación con rol `ROLE_USER`.
* `/albergues/**` → requiere autenticación con rol `ROLE_ALBERGUE`.

Cualquier otra ruta requiere autenticación por defecto.
El manejo de sesiones se establece como **STATELESS**, reforzando la seguridad del modelo JWT.

### **6.5. Encriptación de Contraseñas**

Las contraseñas se almacenan utilizando el algoritmo **BCrypt**, implementado mediante el `PasswordEncoder` de Spring Security.
Este mecanismo añade un *salt* aleatorio en cada hash, lo que evita ataques de tipo *rainbow table* y mejora la integridad de las credenciales.

### **6.6. Separación de Servicios de Autenticación**

Para garantizar un control granular y seguro, se implementaron dos servicios distintos que extienden `UserDetailsService`:

* **`UserDetailsServiceImpl`** → gestiona la autenticación de usuarios comunes.
* **`AlbeguerDetailsServiceImpl`** → gestiona la autenticación de albergues.

Esta separación permite una gestión más clara de roles, permisos y validación de credenciales en función del tipo de entidad que accede al sistema.

---
## **7. Eventos y Asincronía**

---

## **8. Conclusión**

---

## **9. Apéndices**

### **9.1. Licencia**

**MIT License**

### **9.2. Referencias**

* Briceño, E. (2024). Más de 7 millones de perros y gatos abandonados y la oportunidad de volver a casa. _Convive_.
  [https://convoca.pe/convive/mas-de-7-millones-de-perros-y-gatos-abandonados-y-la-oportunidad-de-volver-casa](https://convoca.pe/convive/mas-de-7-millones-de-perros-y-gatos-abandonados-y-la-oportunidad-de-volver-casa)

* Medrano, H. (14 de mayo de 2023). Perros y gatos sin hogar en Lima: ¿Existe un registro de animales en situación de abandono? ¿Qué se sabe de la Ley 4 patas?. _El Comercio_.
  [https://elcomercio.pe/lima/sucesos/perros-y-gatos-sin-hogar-en-lima-existe-un-registro-de-animales-en-situacion-de-abandono-que-se-sabe-de-la-ley-4-patas-adopcion-responsable-esterilizacion-albergues-rescatistas-noticia/?ref=ecr](https://elcomercio.pe/lima/sucesos/perros-y-gatos-sin-hogar-en-lima-existe-un-registro-de-animales-en-situacion-de-abandono-que-se-sabe-de-la-ley-4-patas-adopcion-responsable-esterilizacion-albergues-rescatistas-noticia/?ref=ecr)

---
