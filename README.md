# basic-container

## Contenedor

### Definición

Un contenedor Docker es un paquete de software estandarizado y ejecutable que incluye todo lo necesario para correr una aplicación: código, runtime, librerías y configuraciones. Es ligero, portable y aislado, garantizando que la aplicación se ejecute de manera uniforme en cualquier entorno.

### La Diferencia Clave: El Kernel

La distinción más importante es que cada Máquina Virtual incluye su propio Sistema Operativo completo y su propio Kernel (núcleo), mientras que un Contenedor Docker comparte el Kernel del SO anfitrión.

### Comandos Principales de Docker

* docker run [IMAGEN]	

    Crea y ejecuta un nuevo contenedor a partir de una imagen. Si la imagen no está en local, la descarga primero. Es la combinación de pull y create/start.

    * --name [NOMBRE]	
    
        N/A	Asigna un nombre al contenedor (ej: --name mi-servidor). Si se omite, Docker genera uno aleatoriamente.

    * -d	
        
        N/A	Ejecuta el contenedor en modo desatendido (detached), en segundo plano.

    * -i	
    
        N/A	Interactivo. Mantiene STDIN abierto incluso si no está adjunto, permitiendo la interacción.

    * -t	
        
        N/A	Asigna una pseudo-TTY (terminal). Se usa casi siempre con -i (ej: -it para sesiones de shell).

    * -p [HOST:CONTAINER]	
        
        N/A	Publica/Mapea un puerto. Conecta un puerto del host al contenedor (ej: -p 8080:80).

    * --network [RED]	
    
        N/A	Conecta el contenedor a una red específica (ej: --network mi-red-app).

    * -v [HOST:CONTAINER]	
        
        N/A	Monta un volumen/directorio. Permite compartir datos entre el host y el contenedor, o entre contenedores (ej: -v /datos/app:/app/data).

    * -e [KEY=VALUE]	
    
        N/A	Establece variables de entorno dentro del contenedor (ej: -e DB_PASSWORD=secreta).

    * --rm	
    
        N/A	Elimina automáticamente el contenedor cuando este finaliza su ejecución. Muy útil para pruebas o tareas únicas.

    * --restart [POLÍTICA]	
        
        N/A	Define la política de reinicio del contenedor (ej: always, on-failure, unless-stopped).

    * --link [CONTENEDOR:ALIAS]	
    
        N/A	(Obsoleto, pero aún usado) Crea un vínculo para que los contenedores se comuniquen. Se recomienda usar la opción --network para la resolución de nombres.

    * --memory [TAMAÑO]	
    
        N/A	Limita la memoria RAM disponible para el contenedor (ej: --memory 512m).

    * --cpus [CANTIDAD]	
        
        N/A	Limita el número de núcleos de CPU disponibles (ej: --cpus 1.5).

* docker start [ID/NOMBRE]	

    Inicia un contenedor que ha sido previamente creado y está detenido (stopped).

* docker stop [ID/NOMBRE]	

    Detiene la ejecución de un contenedor en funcionamiento.

* docker pull [IMAGEN]	

    Descarga una imagen desde un registro (como Docker Hub) a su máquina local.

* docker ps	

    Lista todos los contenedores que se están ejecutando (activos). Use docker ps -a para listar todos (incluidos los detenidos).

* docker images	

    Muestra una lista de todas las imágenes que están almacenadas localmente.

* docker build -t [TAG] .	

    Construye una nueva imagen a partir de un Dockerfile en el directorio actual. El argumento -t se usa para etiquetar (nombrar) la imagen.

* docker exec -it [ID/NOMBRE] [COMANDO]	

    Ejecuta un comando dentro de un contenedor en ejecución. El argumento -it es común para abrir una sesión de terminal interactiva (por ejemplo, docker exec -it [ID] /bin/bash).

* docker logs [ID/NOMBRE]	

    Muestra los registros (logs) de un contenedor en particular. Útil para depuración.

* docker inspect [OPCIONES] [ID_O_NOMBRE_DE_OBJETO]

    Devuelve información detallada en formato JSON sobre cualquier objeto de Docker que le especifique.

* docker rm [ID/NOMBRE]	

    Elimina uno o más contenedores detenidos.

* docker rmi [IMAGEN]	

    Elimina una o más imágenes de la máquina local.

* docker system prune	

    Limpia el sistema, eliminando todos los contenedores detenidos, redes no utilizadas, imágenes colgantes (dangling images) y caché de compilación. Esencial para liberar espacio.

* docker network ls	

    Muestra una lista de todas las redes existentes en el host.

* docker network create [NOMBRE]	
    
    Crea una nueva red. El tipo de red por defecto es bridge.

* docker network connect [RED] [CONTENEDOR]	
    
    Conecta un contenedor en ejecución a una red específica.

* docker network disconnect [RED] [CONTENEDOR]	
    
    Desconecta un contenedor de una red.

* docker network rm [RED]	

    Elimina una red.

## Pasos para el Despliegue de Imagen Docker

```bash
docker build -t [NOMBRE_BASE]:[VERSION] .
docker login [REGISTRY]
docker tag [NOMBRE_BASE] [REGISTRY]/[IMAGEN]:[VERSION]
docker push [REGISTRY]/[IMAGEN]:[VERSION]
docker logout [REGISTRY]
```

* Ejemplo

```bash
docker build -t springboot-test:0.0.1 .
```

## Ejecución

* Opción 1

```bash
docker build -f DockerfileSimple -t springboot-test:0.0.1 . --load
```

* Opción 2

```bash
docker build -t springboot-test:0.0.1-alpine . --load
```

* Continuando

```bash
docker login nexus.registry.com.br
docker tag springboot-test:0.0.1-alpine nexus.registry.com.br/springboot-test:0.0.1-alpine
docker push nexus.registry.com.br/springboot-test:0.0.1-alpine
```

* Pruebas

```bash
docker images | grep springboot-test
docker run --name springboot-test -p 8080:8080 -it --rm springboot-test:0.0.1
docker run --name springboot-test -p 8080:8080 -it --rm -e APP_MESSAGE=Hola springboot-test:0.0.1-alpine
docker ps | grep springboot-test
```

```bash
docker pull nexus.registry.com.br/springboot-test:0.0.1-alpine
docker run --name springboot-test -p 8080:8080 -it --rm -e APP_MESSAGE=Hola nexus.registry.com.br/springboot-test:0.0.1-alpine
```

```bash
curl -s http://localhost:8080/hello | jq .
```

```bash
docker compose up
```

```bash
curl -s http://localhost:8080/hello | jq .
curl -s http://localhost:8081/hello | jq .
```

## Kubernetes

### Definición

Kubernetes (K8s) es un sistema de código abierto para la automatización del despliegue, escalado y gestión de aplicaciones en contenedores (como Docker). Actúa como un orquestador de contenedores que se encarga de que los clusters funcionen de forma fiable, gestionando recursos, realizando auto-recuperación y equilibrando la carga de trabajo.

### Comandos Principales

* kubectl get	

    Muestra recursos. Se usa para listar Pods, Services, Deployments, Nodes, etc. (Ej: kubectl get pods).

* kubectl describe	

    Muestra información detallada sobre un recurso específico, incluyendo su estado, eventos recientes y configuraciones. (Ej: kubectl describe pod [NOMBRE]).

* kubectl logs	

    Muestra los registros (logs) de un Pod o de un contenedor específico dentro de un Pod. Útil para depuración. (Ej: kubectl logs [NOMBRE_POD] -f).

* kubectl apply -f [ARCHIVO]	

    Aplica o actualiza una configuración de recurso definida en un archivo (generalmente YAML). Es el comando principal para el despliegue.

* kubectl delete	

    Elimina recursos por tipo y nombre, o por archivo. (Ej: kubectl delete deployment [NOMBRE]).

* kubectl exec	

    Ejecuta un comando dentro de un contenedor en un Pod en ejecución. Se usa a menudo para abrir una shell interactiva. (Ej: kubectl exec -it [NOMBRE] -- /bin/bash).

* kubectl port-forward	
    
    Mapea un puerto local a un puerto de un Pod o servicio. Permite acceder a una aplicación interna desde su máquina local. (Ej: kubectl port-forward [POD] 8080:80).

* kubectl rollout	
    
    Gestiona el despliegue de Deployments. Se usa para verificar el estado, pausar, reanudar o deshacer (undo) un despliegue.

* kubectl scale	

    Escala un Deployment o ReplicaSet cambiando el número de réplicas (copias). (Ej: kubectl scale deployment [NOMBRE] --replicas=5).

* kubectl config view	

    Muestra la configuración de kubeconfig, incluyendo clusters, usuarios y contextos definidos.

* kubectl version	

    Muestra la versión del cliente (kubectl) y del servidor de Kubernetes (API Server).