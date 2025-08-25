FROM mcr.microsoft.com/devcontainers/base:alpine-3.21

RUN apk update
RUN apk add --no-cache \
        postgresql17-client \
        nodejs=22.15.1-r0 \
        npm=10.9.1-r0

RUN uname -a
RUN psql --version
RUN npm --version
RUN node --version