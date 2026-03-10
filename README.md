## PROYECTO DE LA ASIGNATURA INGENIERIA DEL SOFTWARE

Sistema de gestion del Comedor de la UCV desarrollado como aplicacion de escritorio en Java (Swing). El proyecto sigue una separacion MVC (Modelo, Vista, Controlador) y usa archivos de texto como almacenamiento local.

## Caracteristicas
- Inicio de sesion por rol (Administrador y usuarios regulares).
- Registro de usuarios con validacion contra el listado de Secretaria.
- Gestion de menus (creacion, edicion y eliminacion).
- Monedero con recargas, historial y cobros por turno.
- Saldo Pana: transferencia de saldo entre estudiantes usando CI destino.
- Reserva de turnos con verificacion de cupos, horario limite y reconocimiento facial basico.
- Cobro por tipo de estudiante: regular, becario (porcentaje configurable) y exonerado.
- Configuracion de CCB y tarifas para desayuno/almuerzo.
- Reporte administrativo por servicio (desayuno/almuerzo) con desglose de comensales.

## Requisitos
- Java 17 (JDK 17).
- Maven 3.x.

## Ejecucion
Desde la carpeta del proyecto:

```bash
cd comedor
mvn -q -DskipTests package
java -cp target/classes com.example.Main
```

## Pruebas
```bash
cd comedor
mvn -q test
```

## Estructura del proyecto
- [comedor/src/main/java/com/example/Controlador/](comedor/src/main/java/com/example/Controlador/) controladores de flujo (login, admin, monedero, registro).
- [comedor/src/main/java/com/example/Modelo/](comedor/src/main/java/com/example/Modelo/) logica de negocio y acceso a datos locales.
- [comedor/src/main/java/com/example/Vista/](comedor/src/main/java/com/example/Vista/) interfaces Swing.
- [comedor/src/test/java/](comedor/src/test/java/) pruebas unitarias de modelos y vistas.

## Almacenamiento local (archivos .txt)
Los archivos viven en la carpeta [comedor/](comedor/) y se usan como base de datos local:

- [comedor/Usuarios_UCV.txt](comedor/Usuarios_UCV.txt)
	- Formato: `email,nombre,rol,facultad,escuela,ci`
	- Se usa para validar el correo en el registro.
- [comedor/Usuarios.txt](comedor/Usuarios.txt)
	- Formato: `nombre,email,password,rol,telefono`
	- Se usa para autenticacion.
- [comedor/Menus.txt](comedor/Menus.txt)
	- Formato: `id,fecha,turno,platos` (platos separados por `|`).
	- Si no existe, se generan menus iniciales.
- [comedor/Monedero.txt](comedor/Monedero.txt)
	- Formato: `email,fecha_iso,monto` (monto positivo = recarga, negativo = cobro).
- [comedor/Beneficios_Comensal.txt](comedor/Beneficios_Comensal.txt)
	- Formato: `ci,tipo,porcentaje_cobro,fecha_iso`.
	- Tipo: `EXONERADO` o `BECARIO`.
- [comedor/Asistencias_Comedor.txt](comedor/Asistencias_Comedor.txt)
	- Formato: `fecha_iso,servicio,email,ci,tipo_comensal,monto_cobrado`.
- [comedor/CCB.txt](comedor/CCB.txt)
	- Registros de costos y tarifas para el calculo del CCB.
- [comedor/Fotos_Secretaria.txt](comedor/Fotos_Secretaria.txt)
	- Formato: `email,ruta_foto`.
	- Las fotos base viven en [comedor/fotos/](comedor/fotos/).

## Credenciales y flujo de registro
- Administrador demo: usar el correo `admin@ucv.ve` con cualquier contrasena no vacia.
- Registro de usuario: el correo debe existir en [comedor/Usuarios_UCV.txt](comedor/Usuarios_UCV.txt) y pertenecer al dominio `@ucv.ve`.

## Notas
- La aplicacion es 100% local (sin base de datos ni servicios externos).
- Algunas vistas usan datos simulados (por ejemplo, los turnos).
