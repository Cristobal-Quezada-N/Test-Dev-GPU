# Proyecto de desarrollo para gestión de ludoteca

> Competencia de Desarrollo CEII
> Sistema de Gestión para la Ludoteca del Departamento de Ingeniería Informática – USACH

# 1. Contexto

La vida universitaria no se limita a clases, evaluaciones y laboratorios. También se construye a través de espacios que fomentan el compañerismo, el bienestar y la recreación. En este contexto, la Ludoteca del CEII se ha consolidado como un punto de encuentro entre estudiantes de Ingeniería Informática, un lugar para compartir juegos y consolas, liberar estrés y fortalecer vínculos.
Sin embargo, su funcionamiento actual depende de registros informales, sin trazabilidad ni control eficiente, lo que limita su sostenibilidad en el tiempo.
Por ello, el Centro de Estudiantes de Ingeniería Informática (CEII) lanza esta competencia, abierta a estudiantes de la carrera, con el fin de diseñar y desarrollar un sistema completo para la gestión segura, moderna y automatizada de los préstamos de la Ludoteca. Se trata de un desafío técnico real, con impacto directo en la comunidad, que permitirá a las alianzas participantes aplicar conocimientos adquiridos en la carrera en un caso práctico y con proyección de uso.

# 2. Requerimientos Técnicos

## 2.1 Requerimientos funcionales

### Autenticación y roles

- Registro de usuarios mediante correo institucional con código de confirmación y contraseña.

- Roles del sistema: estudiante y administrador.

- La ID de cada usuario será un código alfanumérico aleatorizado y serializado, no basado en información personal.


- Sistema de préstamos


Solo estudiantes autenticados pueden solicitar préstamos.


Cada solicitud debe registrar:


ID del solicitante.


Fecha y hora de la solicitud.


Tiempo estimado de uso.


El préstamo debe ser aprobado por un administrador autenticado en ese momento, requiriendo doble factor de autenticación (2FA) para dicha acción.


Gestión de inventario


Los administradores pueden registrar, editar y eliminar juegos o consolas.


Gestión de cuentas


Los administradores pueden visualizar, editar y eliminar cuentas de usuario.


Notificaciones automáticas


El sistema debe enviar correos electrónicos en caso de atrasos en la devolución de préstamos.


Seguridad avanzada


Las contraseñas deben almacenarse de forma segura, usando hashing más cifrado.


Se exige el uso de ChaCha20-Poly1305 o un esquema mejorado (por ejemplo: Argon2id combinado con cifrado simétrico autenticado).


Las sesiones deben cerrarse automáticamente tras registrarse como usuario, pasar 3 minutos de inactividad, o solicitar un pŕestamo en caso de estudiantes, y sólo tras presionar el logout o después de media hora de sesión, cerrar sesión del admin.


Doble factor de autenticación con código al correo (2FA)


No se aplicará en el login regular.


Será requerido sólo en el registro


Puede implementarse mediante TOTP (Google Authenticator u otros), correo electrónico o mecanismos equivalentes.



2.2 Requerimientos no funcionales
Despliegue


El sistema debe ejecutarse en entorno local utilizando Docker, con al menos tres servicios separados:


Backend


Frontend


Base de datos (a libre elección: PostgreSQL, MongoDB, etc.)


Restricciones


Queda estrictamente prohibido el uso de CMS o frameworks prefabricados como WordPress, Wix, Joomla, etc.


Buenas prácticas


Se espera una arquitectura limpia, uso de control de versiones (Git), patrones de diseño apropiados, y código modular.


El backend debe exponer una API RESTful bien documentada.


Disponibilidad física


El sistema será accedido exclusivamente desde un dispositivo físico provisto por el CEII, esperando disponerlo en el espacio de la Ludoteca operando de forma local. La idea es una futura implementación mediante autenticaciones con credencial.



2.3 Historias de Usuario
Estas historias representan los casos principales que el sistema debe cubrir, desde la perspectiva de sus actores.
[HU-01] Registro como estudiante
 Como estudiante, quiero registrarme en el sistema con mi correo institucional y contraseña, para poder acceder a los juegos y consolas disponibles.


[HU-02] Solicitar préstamo
 Como estudiante, quiero iniciar sesión y solicitar el préstamo de un ítem, indicando el tiempo estimado de uso, para utilizarlo en la Ludoteca.


[HU-03] Aprobar préstamo como administrador
 Como administrador, quiero autenticarme y usar un segundo factor para aprobar solicitudes de préstamo, asegurando que los préstamos sean validados correctamente.


[HU-04] Recibir recordatorio por atraso
 Como estudiante, quiero recibir un correo si me atraso en la devolución de un juego o consola, para estar informado y evitar sanciones.


[HU-05] Agregar nuevo ítem al inventario
 Como administrador, quiero poder registrar un nuevo juego o consola en el sistema, para ampliar el catálogo disponible.


[HU-06] Gestionar cuentas de usuario
 Como administrador, quiero visualizar y editar los perfiles de los estudiantes registrados, para mantener actualizada la base de usuarios.


[HU-07] Seguridad de sesiones
 Como usuario, quiero que mi sesión se cierre automáticamente al pedir un juego o consola, registrarme, o si no hago nada durante tres minutos, para proteger mis datos.




3. Organización de la competencia
Inicio: Lunes 25 de agosto 00:00 hrs.
Cada alianza deberá elegir sus 10 integrantes coordinando por el grupo de whatsapp e informarlos por el mismo medio antes del Lunes 25 de agosto a las 00:00 hrs.


Entrega final: Viernes 29 de agosto, hasta las 13:00 hrs.
Instancia de cierre: Cada equipo presentará su sistema funcionando ante un jurado compuesto por profesores del Departamento de Ingeniería Informática (DIINF), el cual será confirmado próximamente.


Equipos: Hasta 10 integrantes por grupo. Este número se considera óptimo para mantener una buena coordinación y una distribución efectiva de tareas.


Repositorio GitHub oficial por equipo:
 A cada alianza se le asignará un repositorio privado en una organización de GitHub administrada por el CEII.


Todos los integrantes deberán ser registrados como colaboradores usando su correo institucional USACH.


Este repositorio será el único canal oficial de entrega y evaluación del código fuente.


Se espera que las contribuciones estén debidamente versionadas mediante git, manteniendo buenas prácticas de control de cambios y trabajo en ramas.

4. Criterios de evaluación
La evaluación será realizada por el jurado bajo los siguientes criterios:
Cumplimiento técnico de los requerimientos funcionales y no funcionales.


Correcta implementación del doble factor de autenticación en acciones críticas.


Calidad del diseño arquitectónico y modularidad del sistema.


Seguridad en el tratamiento de credenciales y control de sesiones.


Claridad del código fuente, uso de control de versiones y documentación.


Experiencia de usuario y coherencia en el flujo de navegación.


Argumentación técnica en la defensa del proyecto.



5. Observaciones finales
Esta competencia representa más que un desafío académico: es una invitación a construir una solución con impacto real. La propuesta ganadora será seleccionada para su implementación en la Ludoteca del CEII, beneficiando a futuras generaciones de estudiantes.
Se espera de los equipos participantes una solución robusta, técnicamente sólida y pensada para las personas que la usarán. Esta es una oportunidad concreta para aplicar conocimientos, trabajar en equipo y dejar un aporte significativo en la comunidad del DIINF.


6. Premios
El premio para el equipo campeón será una copa dorada donde serán grabados los nombres de las personas que trabajaron en el proyecto que será exhibida en el DIINF para la posteridad buscando comenzar con esta competencia como tradición de nuestra comunidad además de la correspondiente puntuación otorgada a la alianza definida en el documento de puntajes.


(Imagen Referencial)


 Redacción realizada en colaboración con ChatGPT (modelo GPT-5, OpenAI).  
