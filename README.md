# Proyecto de Despliegue de Aplicación Spring Boot con MySQL en Kubernetes usando ArgoCD
# Alejandro Vargas Ocampo  
Arquitectura de Software
 

## Descripción

Este proyecto tiene como objetivo desplegar una aplicación desarrollada con Spring Boot (API REST) que se conecta a una base de datos MySQL, utilizando Kubernetes como plataforma de orquestación de contenedores. Para ello, se empleó Minikube como entorno local de clúster y ArgoCD como herramienta para la gestión continua de despliegues (GitOps).

## Objetivos

- Contenerizar una aplicación Java Spring Boot.
- Desplegar la aplicación y una base de datos MySQL en un clúster de Kubernetes.
- Utilizar ArgoCD para gestionar los despliegues automáticamente desde un repositorio Git.
- Observar y controlar el estado del sistema desde la interfaz de ArgoCD y el dashboard de Minikube.

## Tecnologías Utilizadas

- Docker
- Kubernetes
- Minikube
- ArgoCD
- Spring Boot
- MySQL
- kubectl

## Estructura del Proyecto

Los siguientes manifiestos Kubernetes se encuentran en la raíz del repositorio:

- `mysql-configmap.yaml`
- `mysql-deployment.yaml`
- `mysql-secret.yaml`
- `mysql-service.yaml`
- `springboot-deployment.yaml`
- `springboot-service.yaml`


minikube start --memory=4096 --cpus=2
