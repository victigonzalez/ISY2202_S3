-- =====================================================================
--  Crear el usuario de la aplicación (mínimo privilegio)
--  Ejecutar UNA vez conectado como ADMIN en la base "foro" (Autonomous Database).
--  La app NO usa ADMIN: usa RECETAS_APP, que solo puede trabajar en su propio esquema.
-- =====================================================================

-- Cambia la contraseña antes de ejecutar.
-- Reglas de Oracle Cloud: 12 a 30 caracteres, mayúscula, minúscula y número,
-- sin comillas dobles y sin contener el nombre de usuario.
CREATE USER RECETAS_APP IDENTIFIED BY "CambiaEstaClave2026";

GRANT CREATE SESSION  TO RECETAS_APP;
GRANT CREATE TABLE    TO RECETAS_APP;
GRANT CREATE SEQUENCE TO RECETAS_APP;

-- Espacio para sus tablas en el tablespace DATA de Autonomous Database
ALTER USER RECETAS_APP QUOTA 50M ON DATA;
