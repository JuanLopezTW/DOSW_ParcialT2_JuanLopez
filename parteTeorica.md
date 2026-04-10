## punto 1 funcionalidades

### registro de usuario

#### a) Verbo http: 
POST

#### b) 
No idempotente

#### c) 
Cada llamada crea un nuevo recurso (usuario) en el sistema. Dos requests con los mismos datos generan conflicto o duplicado, no el mismo resultado.

#### d) 
Sin rol

#### e) 

| Campo (Request) | Tipo | Obligatorio |
|-----------------|------|-------------|
| nombre | String | SI |
| email | String | SI |
| contrasena | String | SI |

| Campo (Response) | Tipo | Siempre presente |
|------------------|------|-----------------|
| id | UUID | SI |
| nombre | String | SI |
| email | String | SI |


#### f)

Request:

    {
      "nombre": "Juan Pérez",
      "email": "juan@mail.escuelaing.edu.co",
      "contrasena": "Pass123!"
    }

Response 201 Created:

    {
      "id": "a1b2c3d4-...",
      "nombre": "Juan Pérez",
      "email": "juan@mail.escuelaing.edu.co",
    }

#### g)

- Input: email con formato válido: "@mail.escuelaing.edu.co"
- Validaciones de negocio: El email no tiene que ya estar registrado en el sistema

#### H)

| Escenario | Código | Mensaje |
|-----------|--------|---------|
| Happy Path | 201 | Usuario creado exitosamente |
| Email ya registrado | 409 | El correo ya se encuentra registrado |
| Campos inválidos | 400 | Los datos ingresados no son válidos |
| Error interno | 500 | Error interno del servidor |

### https://github.com/JuanLopezTW/ECI-SportLife.git

