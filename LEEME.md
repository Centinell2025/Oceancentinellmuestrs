# OceanCentinell Business — muestra Android empresarial

Aplicación gratuita de demostración de Beacon of the Eagle LLC. Los contratos, integraciones y servicios B2B se acuerdan aparte.

El dashboard conserva el diseño azul intenso del HTML Enterprise. Incluye flota ficticia, activos, incidentes, mantenimiento, mapa geográfico, alcance de piloto y exportaciones para Excel.

## Para abrir o compilar

- La aplicación Android está en `android/`. Ábrela desde Android Studio.
- `dist/` contiene la interfaz que se incluye dentro de Android.
- `index.html` abre la vista previa de esa interfaz.
- Las compilaciones y pruebas están en la pestaña Actions del repositorio.
- `npm test` ejecuta las pruebas de datos y AIS sin instalar dependencias.
- `npm run verify:assets` comprueba que Android incluye exactamente los archivos revisados.
- Después de modificar `dist/`, ejecuta `npm run sync:android` antes de compilar.

## AIS

El servidor está preparado. Para recibir posiciones reales, la propietaria debe crear su cuenta AISStream y configurar `AISSTREAM_API_KEY` en el servicio Railway. La clave nunca debe ponerse en GitHub, en el APK ni en el chat. La app muestra claramente si falta activarla.

Las posiciones AIS regionales son una capa separada de los barcos ficticios de la muestra. El mapa no es un sistema de navegación.

## Estado de publicación

La ficha de OceanCentinell Business está creada en Google Play. La preparación de una ficha o un paquete no significa que Google haya aprobado la aplicación. El estado definitivo debe comprobarse en Play Console.

Consulta `docs/TESTING.md` para los resultados y `docs/GOOGLE_PLAY.md` para la preparación de tienda.
