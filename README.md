# Proyecto "Correos" para HUniversity Microservicios
#### Sistema enfocado en el seguimiento de paquetes entre centros de distribucion.
#### No se enfoca en el envio a la casa del cliente (ultima milla).
Autor: Alexis Valentin Richter

# Init proyecto
1. ```docker stop $(docker ps -aq)```
2. ```docker rm $(docker ps -aq)```
3. Ejecutar el proyecto ```.\start.ps1```
4. Levantar cada proyecto de el IDE

- Error de schema registro, no registra determinados esquemas
- Solucion:
  - Eliminar la carpeta 'kafka-streams' de los archivos temp (windows + r -> %temp%)

# Documentación
- Package Command: http://localhost:8001
- Package Query: http://localhost:8005
- Stats Consumer: http://localhost:8050
- Redpanda: http://localhost:8080

# [Diagrama](https://excalidraw.com/#json=TUPshf7i5k_mR55FC9Doc,FPde73sDSsVpDiIgMHhCpA)