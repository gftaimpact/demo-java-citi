# LinkLister.java: Extractor de Enlaces Web

## Resumen

LinkLister es una clase Java que proporciona funcionalidad para extraer enlaces de una página web dada. Ofrece dos métodos principales: uno básico para obtener enlaces y otro con validación adicional de direcciones IP privadas.

## Flujo del Proceso

```mermaid
graph TD
    A[Inicio] --> B{Método llamado}
    B -->|getLinks| C[Conectar a URL]
    B -->|getLinksV2| D[Validar URL]
    C --> E[Extraer enlaces]
    E --> F[Retornar lista de enlaces]
    D --> G{¿Es IP privada?}
    G -->|Sí| H[Lanzar BadRequest]
    G -->|No| I[Llamar a getLinks]
    I --> F
    F --> J[Fin]
```

## Insights

- Utiliza la biblioteca Jsoup para el análisis HTML y la extracción de enlaces.
- Implementa validación de IP privadas en `getLinksV2` para prevenir accesos no autorizados.
- Maneja excepciones y las convierte en `BadRequest` en `getLinksV2`.
- Los enlaces extraídos se devuelven como URLs absolutas.

## Dependencias

```mermaid
graph LR
    LinkLister --- |"Utiliza"| Jsoup
    LinkLister --- |"Lanza"| BadRequest
    LinkLister --- |"Accede"| URL_externa["URL externa"]
```

- `Jsoup`: Biblioteca utilizada para conectar y analizar páginas web HTML.
- `BadRequest`: Excepción personalizada lanzada en caso de errores o uso de IP privadas.
- `URL externa`: La URL proporcionada como entrada para extraer enlaces.

## Manipulación de Datos (SQL)

| Atributo | Tipo    | Descripción                                   |
|----------|---------|-----------------------------------------------|
| url      | String  | URL de la página web de la que extraer enlaces |
| result   | List<String> | Lista de enlaces extraídos de la página web  |
