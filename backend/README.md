# 2025-GPU-Ludoteca

Desafío para las alianzas informáticas 2025! CPU 🖥️⚡GPU 💻

## Requisitos previos para Backend

- Tener instalado **PostgreSQL**.
- Tener acceso al usuario de PostgreSQL con permisos suficientes para crear bases de datos y tablas.
- **Maven** instalado en tu máquina. Si no tienes Maven, sigue [este enlace](https://maven.apache.org/install.html) para instalarlo.
- Tener acceso al archivo `.env` que contiene las variables de configuración para la base de datos.

Las variables de entorno necesarias son:

- `APP_HOST`: Dirección de la aplicación web (dominio, ip, etc.).
- `DB_HOST`: Dirección del servidor de la base de datos (por defecto, `localhost`).
- `DB_PORT`: Puerto del servidor de la base de datos de Postgres (por defecto, `5432`).
- `DB_NAME`: Nombre de la base de datos a crear.
- `DB_USERNAME`: Nombre de usuario para acceder a la base de datos.
- `DB_PASSWORD`: Contraseña del usuario de la base de datos.
- `MAIL_HOST`: Por lo general gmail o lo que sea (ej: smtp.gmail.com).
- `MAIL_PORT`: Por defecto 587.
- `MAIL_USERNAME`: Nombre de la cuenta que envía correos (ej: <email@usach.cl>).
- `MAIL_PASSWORD`: Contraseña o token de la cuenta que envía correos.
- `SECRET`: Token para el algoritmo de encriptación para JWT, que sea lo más seguro posible.

## Imagen Docker

Uso de la imagen docker.

1. Construccion de imagen:

```sh
docker build [--rm] -t <image-name> .
```

2. Ejecucion de container:

```sh
docker run \
    [--rm] \
    -p 8090:8090 \
    -e SPRING_PROFILES_ACTIVE=<profile-name> \
    [--env-file <file-name>] \
    [-d] \
    -it <image-name>
```
