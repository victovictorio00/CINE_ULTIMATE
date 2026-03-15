# CINE_ULTIMATE 🎥

**CINE_ULTIMATE** es una solución de software orientada a la gestión y automatización de procesos para cadenas de cine. Este sistema permite administrar de manera eficiente desde la cartelera de películas hasta la venta final de boletos, utilizando tecnologías modernas de desarrollo backend.

---

# ⭐ Características Principales

* **Gestión de Cartelera:** Registro, edición y eliminación de películas con detalles técnicos (género, duración, sinopsis).
* **Control de Salas y Funciones:** Configuración de salas físicas y programación de horarios específicos para cada película.
* **Sistema de Ventas:** Lógica para la selección de asientos y generación de tickets de entrada.
* **Administración de Usuarios:** Control de accesos y perfiles para administradores y clientes.
* **Arquitectura de Capas:** Código organizado en Controladores, Servicios, Repositorios y Entidades para facilitar el mantenimiento y la escalabilidad.

---

# ⚙️ Stack Tecnológico

* **Lenguaje:** Java
* **Framework:** Spring Boot (Data JPA, Web)
* **Gestor de Dependencias:** Maven
* **Base de Datos:** MySQL / PostgreSQL (Relacional)
* **Arquitectura:** Monolítica basada en capas (MVC)

---

# 📌 Requisitos Previos

Antes de ejecutar el proyecto, asegúrate de tener instalado:

* Java JDK 17 o superior
* Maven 3.6+
* Un motor de base de datos relacional activo

---

# 🧩 Instalación y Ejecución

## 1. Clonar el repositorio

```bash
git clone https://github.com/victovictorio00/CINE_ULTIMATE.git
cd CINE_ULTIMATE
```

---

## 2. Configurar la base de datos

Configura las credenciales de tu base de datos en el archivo:

```
src/main/resources/application.properties
```

---

## 3. Compilar el proyecto

```bash
mvn clean install
```

---

## 4. Ejecutar la aplicación

```bash
mvn spring-boot:run
```
