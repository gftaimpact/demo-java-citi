# LinksController.java: Controlador REST para Listar Enlaces

## Overview

Este controlador REST proporciona endpoints para listar enlaces de una URL dada, ofreciendo dos versiones de la funcionalidad.

## Process Flow

```mermaid
graph TD
    A[Cliente] -->|"GET /links?url=..."| B[LinksController]
    A -->|"GET /links-v2?url=..."| B
    B -->|"getLinks(url)"| C[LinkLister]
    B -->|"getLinksV2(url)"| C
    C -->|"Lista de enlaces"| B
    B -->|"Respuesta JSON"| A
```

## Insights

- Utiliza Spring Boot para la configuración automática y la creación de endpoints REST.
- Ofrece dos versiones del endpoint para listar enlaces: `/links` y `/links-v2`.
- Maneja diferentes tipos de excepciones para cada versión del endpoint.
- Produce respuestas en formato JSON.

## Dependencies

```mermaid
graph LR
    LinksController --- |"Utiliza"| LinkLister
    LinksController --- |"Depende de"| Spring_Boot
    LinksController --- |"Maneja"| BadRequest
```

- `LinkLister`: Clase utilizada para obtener los enlaces de una URL dada.
- `Spring Boot`: Framework utilizado para la configuración y creación de la aplicación REST.
- `BadRequest`: Excepción personalizada manejada en la versión 2 del endpoint.

## Data Manipulation (SQL)

Esta sección no es aplicable ya que el código no realiza manipulación directa de datos SQL.
