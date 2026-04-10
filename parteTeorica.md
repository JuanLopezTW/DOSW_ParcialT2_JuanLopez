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

### Autentificacion - Login

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
| email | String | SI |
| contrasena | String | SI |

| Campo (Response) | Tipo | Siempre presente |
|------------------|------|-----------------|
| token | String (JWT) | SI |
| tipo | String | SI |
| usuarioId | UUID | SI |
| nombre | String | SI |


#### f)

Request:

    {
      "email": "juan@email.com",
      "contrasena": "Pass123!"
    }

Response 200 OK:

    {
      "token": "eyJhbGciOiJIUzI1NiIs...",
      "tipo": "CLIENTE",
      "usuarioId": "a1b2c3d4-...",
      "nombre": "Juan Pérez"
    }

#### g)

- Input: email con formato válido, contraseña no vacía
- Negocio: el email debe estar registrado, la contraseña debe coincidir con la almacenada

#### H)


| Escenario | Código | Mensaje |
|-----------|--------|---------|
| Happy Path | 200 | Autenticación exitosa |
| Credenciales incorrectas | 401 | Credenciales inválidas |
| Campos inválidos | 400 | Los datos ingresados no son válidos |
| Error interno | 500 | Error interno del servidor |

### Consulta Producto QR

#### a) Verbo http:
get

#### b)
idempotente

#### c)
Es una operación de solo lectura. Múltiples llamadas con los mismos parámetros retornan el mismo resultado sin modificar el estado del servidor.

#### d)
usuarios registrados

#### e)

| Campo (Request) | Tipo | Obligatorio |
|-----------------|------|-------------|
| CodQR           | String | SI          |

| Campo (Response) | Tipo                                | Siempre presente |
|------------------|-------------------------------------|-----------------|
| CodQR            | String                              | SI |
| nombre           | String                              | SI |
| descripcion      | String                              | SI |
| precio           | Double                              | SI |
| stock            | Integer                             | SI |
| estado           | String (disponible / no disponible) | SI |


#### f)

Request: GET /productos?CodQR=String

Response 200 OK:

    [
      {
        "CodQR": "1111110...",
        "nombre": "cafe con leche",
        "descripcion": "cafe con leche no espumado caliente",
        "precio": 2500.00,
        "stock": 100,
        "estado": "disponible"
      }
    ]

#### g)

- Input: QR existente con formato válido: "1111110010101011111....."
- Validaciones de negocio: El QR debe de identificar un producto, si no, no es valido.

#### H)

| Escenario | Código | Mensaje |
|-----------|--------|---------|
| Happy Path | 200 | Producto retornado exitosamente |
| Producto no encontrado | 404 | Producto no encontrado |
| ID con formato inválido | 400 | El identificador proporcionado no es válido |
| No autenticado | 401 | No autorizado |
| Error interno | 500 | Error interno del servidor |

### Crear Pedido

#### a) Verbo http:
POST

#### b)
no idempotente

#### c)
Cada llamada agrega o incrementa la cantidad de un producto en el carrito, modificando el estado del recurso. Dos llamadas iguales no producen el mismo resultado final.

#### d)
Cliente

#### e)


| Campo (Request) | Tipo    | Obligatorio |
|-----------------|---------|-------------|
| productoId | String  | SI |
| cantidad | Integer | SI |

| Campo (Response) | Tipo | Siempre presente |
|------------------|------|-----------------|
| OrderId          | UUID | SI |
| productos        | List\<ItemCarrito\> | SI |
| total            | Double | SI |

| Campo (ItemOrder) | Tipo    |
|-------------------|---------|
| productoId        | String  |
| nombre            | String  |
| cantidad          | Integer |
| precioUnitario    | Double  |
| subtotal          | Double  |


#### f)

Request: POST /carrito/items

    {
      "productoId": "11111001...",
      "cantidad": 2
    }

Response 201 Created:

    {
      "orderId": "c1d2e3...",
      "productos": [
        {
          "productoId": "p1b2c3...",
          "nombre": "cafe",
          "cantidad": 2,
          "precioUnitario": 1500.00,
          "subtotal": 3000.00
        }
      ],
      "total": 3000.00
    }

#### g)

- Input:  productoId debe ser QR válido, cantidad debe ser mayor a 1
- Negocio: el producto debe existir y estar disponible, la cantidad solicitada no debe superar el stock disponible, un usuario solo debe de tener un pedido ACTIVO, los pedidos inician en estado CREADO

#### H)

| Escenario | Código | Mensaje |
|-----------|--------|---------|
| Happy Path | 201 | Producto agregado al carrito exitosamente |
| Stock insuficiente | 409 | Stock insuficiente para la cantidad solicitada |
| Producto no encontrado | 404 | Producto no encontrado |
| Campos inválidos | 400 | Los datos ingresados no son válidos |
| No autenticado | 401 | No autorizado |
| Error interno | 500 | Error interno del servidor |

### actualizar pedido 

#### a) Verbo http:
PATCH

#### b)
idempotente

#### c)
Cada llamada actualiza un recurso (order) en el sistema. Dos requests con los mismos datos no generan conflicto o duplicado, por lo tante el mismo resultado.

#### d)
administrador

#### e)

| Campo (Request) | Tipo   | Obligatorio |
|-----------------|--------|-------------|
| orderID         | UUID   | SI |
| status          | String | SI |

| Campo (Response) | Tipo | Siempre presente |
|------------------|------|-----------------|
| ordenId | UUID | SI |
| estado | String | SI ||
| resumen | List\<ItemOrden\> | SI |
| total | Double | SI |

| Campo (ItemOrden) | Tipo    |
|-------------------|---------|
| productoId | String  |
| nombre | String  |
| cantidad | Integer |
| subtotal | Double  |


#### f)

Request: PATCH /ordenes/{ordenId}/pago

    {
      "status": "EN_PREPARACION",
      "orderId": "TXN-98765"
    }

Response 200 OK:

    {
      "ordenId": "o1p2q3...",
      "estado": "EN_PREPARACION",
      "transaccionId": "TXN-98765",
      "resumen": [
        {
          "productoId": "p1b2c3...",
          "nombre": "cafe",
          "cantidad": 2,
          "subtotal": 1500.00
        }
      ],
      "total": 3000.00,
    }

#### g)

- Input: Status no debe ser CANCELADO, orderId no vacío
- Negocio: la orden debe existir, actualizar stock de cada producto

#### H)

| Escenario | Código | Mensaje                                                            |
|-----------|--------|--------------------------------------------------------------------|
| Happy Path | 200 | orden modificada, orden actualizada exitosamente                   |
| Orden no encontrada | 404 | Orden no encontrada                                                |
| Orden en estado inválido | 409 | La orden no se encuentra en un estado válido para procesar el pago |
| No autenticado | 401 | No autorizado                                                      |
| Error interno | 500 | Error interno del servidor                                         |

### Cancelar orden

### actualizar pedido

#### a) Verbo http:
PATCH

#### b)
idempotente

#### c)
Cada llamada actualiza un recurso (order) en el sistema. Dos requests con los mismos datos no generan conflicto o duplicado, por lo tante el mismo resultado.

#### d)
Cliente y Administrador

#### e)

| Campo (Request) | Tipo   | Obligatorio |
|-----------------|--------|-------------|
| orderID         | UUID   | SI |
| status          | String | SI |

| Campo (Response) | Tipo | Siempre presente |
|------------------|------|-----------------|
| ordenId | UUID | SI |
| estado | String | SI ||
| resumen | List\<ItemOrden\> | SI |
| total | Double | SI |

| Campo (ItemOrden) | Tipo    |
|-------------------|---------|
| productoId | String  |
| nombre | String  |
| cantidad | Integer |
| subtotal | Double  |


#### f)

Request: PATCH /ordenes/{ordenId}/pago

    {
      "status": "CANCELADO",
      "orderId": "TXN-98765"
    }

Response 200 OK:

    {
      "ordenId": "o1p2q3...",
      "estado": "CANCELADO",
      "transaccionId": "TXN-98765",
      "resumen": [
        {
          "productoId": "p1b2c3...",
          "nombre": "cafe",
          "cantidad": 2,
          "subtotal": 1500.00
        }
      ],
      "total": 3000.00,
    }
###G)

- Input: Status solo debe ser CREADO, orderId no vacío.
- Validaciones de negocio: El cliente puede cancelar el pedido solo en estado CREADO

#### H)

| Escenario | Código | Mensaje                                                            |
|-----------|--------|--------------------------------------------------------------------|
| Happy Path | 200 | orden cancelada, orden actualizada exitosamente                    |
| Orden no encontrada | 404 | Orden no encontrada                                                |
| Orden en estado inválido | 409 | La orden no se encuentra en un estado válido para procesar el pago |
| No autenticado | 401 | No autorizado                                                      |
| Error interno | 500 | Error interno del servidor                                         |

## 2: Explique la diferencia entre Validaciones de input y Validaciones de negocio
#### respuesta:
las validaciones de input son aquellas que verifican lo escrito en los campos sea adecuado, y las de negocio hacen parte de validaciondes de logica.

## 3: Explique la diferencia entre autenticación, autorización e integridad.
#### respuesta: 
auntenticacion: verificar que alguien es alguien
autorizacion: ese alguien esta permitido de hacer algo
integridad: ese algo debe de seguir ciertas reglas, para no alterar y dañar la base de datos.

