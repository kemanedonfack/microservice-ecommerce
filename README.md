# Microservices App Deployment to kubernetes with Helm via GitLab CI

## Introduction
In today's fast-paced software development environment, it is essential to deploy applications quickly and efficiently. **Microservices** architecture is becoming increasingly popular for its ability to create highly scalable and flexible applications. **Kubernetes**, a popular open-source container orchestration system, has also become the go-to solution for microservices deployment. In this article, we will walk through the process of deploying applications from microservices to a Kubernetes cluster with **Helm** via **GitLab CI**.

[GitLab CI](https://docs.gitlab.com/ee/ci/) is an integral part of the [GitLab](https://about.gitlab.com) platform and provides a robust continuous integration and deployment (CI/CD) pipeline. It allows developers to automate the building, testing and deployment of their applications. [Helm](https://helm.sh/) is a package manager for Kubernetes that allows users to install, upgrade and manage applications in a Kubernetes cluster.

## Prerequisites
To perform this demo, you will need to have the following prerequisites:
- **A Kubernetes cluster** : You can use an existing Kubernetes cluster or create a new one on a cloud provider such as Google Cloud, Amazon Web Services or Microsoft Azure.
- **A GitLab account** : You will need a GitLab account to set up the CI/CD pipeline.
- **Docker Hub account** : You will need a Docker Hub account to store Docker images.

## What is Helm ?
[Helm](https://helm.sh/) is a package manager for Kubernetes that allows you to easily install and manage applications on a Kubernetes cluster. Helm uses charts to define the structure and configuration of an application, which can be versioned and shared between teams.

[**Helm chart**](https://helm.sh/docs/topics/charts/#:~:text=Helm%20uses%20a%20packaging%20format,%2C%20caches%2C%20and%20so%20on.) is a package that contains all the Kubernetes manifests, configuration files and dependencies needed to install and run an application on a Kubernetes cluster. It defines the structure and configuration of an application, including items such as environment variables, ports, volumes and service dependencies.

## Step 1 : Create your own repository

You can find the full source code of the project at :  [source code](https://github.com/kemanedonfack/community/tree/master/kubernetes/microservice-deployment-kubernetes-gitlab-helm) so put all the content in your own repository.

Let's  briefly explain the folders in the repository

![4525](https://user-images.githubusercontent.com/70517765/229854543-4484940f-8a77-4a5c-b06d-652ade72f531.PNG)

The above screenshot shows the different directories of the source code : 
- **.gitlab/agents/k8s-cluster** : contains the configurations of our kubernetes agent for server we will discuss in more detail later in this article
- **ecommerce-api-gateway** : This directory contains the code for an API gateway that serves as an entry point for client requests to the microservices.
- **ecommerce-config-server** : This directory contains the code for a configuration server that centralizes configuration information for the microservices.
- **ecommerce-order-service** : This directory contains the code for a microservice that handles orders for the ecommerce application.
- **ecommerce-product-service** : This directory contains the code for a microservice that handles product-related functionality for the ecommerce application.
- **ecommerce-sale-service** : This directory contains the code for a microservice that handles sales-related functionality for the ecommerce application.
- **ecommerce-service-registry** : This directory contains the code for a service registry that keeps track of the available microservices in the system.
- **ecommerce-user-service** : This directory contains the code for a microservice that handles user-related functionality for the ecommerce application.
- **helm** : contains the different files to deploy our application under kubernetes via Helm
- **kubernetes** : contains the different files to deploy our application on kubernetes without using Helm
- **.gitlab-ci.yml** : which is the core of our pipeline is the file where we describe the different steps of our pipeline
- **docker-compose.yml** : this file allows us to deploy our application under docker

## Step 2 : Configuration of Gitlab CI

Open the `.gitlab-ci.yml` file located at the root of the repository
```
variables:
  GATEWAY_IMAGE_NAME: lugar2020/api-gateway
  CONFIG_IMAGE_NAME: lugar2020/config-server
  ORDER_IMAGE_NAME: lugar2020/order-service
  PRODUCT_IMAGE_NAME: lugar2020/product-service
  SALE_IMAGE_NAME: lugar2020/sale-service
  REGISTRY_IMAGE_NAME: lugar2020/registry-service
  USER_IMAGE_NAME: lugar2020/user-service
 
stages:
  - build_push_image
  - deploy

build_push_microservice_image:
  stage: build_push_image
  image: docker:20.10.16
  services:
    - docker:20.10.16-dind
  variables:
    DOCKER_TLS_CERTDIR: "/certs"
  before_script:
    - echo $DOCKER_PASSWORD | docker login -u $DOCKER_LOGIN --password-stdin
  script: 
    - docker build -t $GATEWAY_IMAGE_NAME ecommerce-api-gateway/.
    - docker push $GATEWAY_IMAGE_NAME
    - docker build -t $CONFIG_IMAGE_NAME ecommerce-config-server/.
    - docker push $CONFIG_IMAGE_NAME
    - docker build -t $ORDER_IMAGE_NAME ecommerce-product-service/.
    - docker push $ORDER_IMAGE_NAME
    - docker build -t $PRODUCT_IMAGE_NAME ecommerce-product-service/.
    - docker push $PRODUCT_IMAGE_NAME
    - docker build -t $SALE_IMAGE_NAME ecommerce-sale-service/.
    - docker push $SALE_IMAGE_NAME
    - docker build -t $REGISTRY_IMAGE_NAME ecommerce-service-registry/.
    - docker push $REGISTRY_IMAGE_NAME
    - docker build -t $USER_IMAGE_NAME ecommerce-user-service/.
    - docker push $USER_IMAGE_NAME

deploy_application:
  stage: deploy
  image: devth/helm:latest
  before_script:
    - cd helm/
    - kubectl config get-contexts
    - kubectl config use-context kemanedonfack/spring-microservice-deployment:k8s-cluster
  script: 
    - helm install mysqldb mysql
    - helm install config config-server
    - helm install --set-string SERVICE_REGISTRY=$SERVICE_REGISTRY registry registry-service
    - helm install --set-string CONFIG_SERVER=$CONFIG_SERVER gateway api-gateway
    - helm install --set-string CONFIG_SERVER=$CONFIG_SERVER order order-service
    - helm install --set-string CONFIG_SERVER=$CONFIG_SERVER product product-service
    - helm install --set-string CONFIG_SERVER=$CONFIG_SERVER sale sale-service
    - helm install --set-string CONFIG_SERVER=$CONFIG_SERVER user user-service
```
Now let's dive into our `.gitlab-ci.yml` file to get a better understanding 

```
variables:
  GATEWAY_IMAGE_NAME: lugar2020/api-gateway
  CONFIG_IMAGE_NAME: lugar2020/config-server
  ORDER_IMAGE_NAME: lugar2020/order-service
  PRODUCT_IMAGE_NAME: lugar2020/product-service
  SALE_IMAGE_NAME: lugar2020/sale-service
  REGISTRY_IMAGE_NAME: lugar2020/registry-service
  USER_IMAGE_NAME: lugar2020/user-service
```

At the top of the file you will find a variable declaring, these are the names of the docker images we will use in our pipeline. You have to create in your docker hub account the same repositories to store these images, **don't forget to change lugar2020 to your docker hub ID**.

```
stages:
  - build_push_image
  - deploy
```
Our pipeline will contain only two steps : **building and publishing** our images to the docker hub and **deploying** our application on a kubernetes cluster

### build & push stage

```
build_push_microservice_image:
  stage: build_push_image
  image: docker:20.10.16
  services:
    - docker:20.10.16-dind
  variables:
    DOCKER_TLS_CERTDIR: "/certs"
  before_script:
    - echo $DOCKER_PASSWORD | docker login -u $DOCKER_LOGIN --password-stdin
  script: 
    - docker build -t $GATEWAY_IMAGE_NAME ecommerce-api-gateway/.
    - docker push $GATEWAY_IMAGE_NAME
    - docker build -t $CONFIG_IMAGE_NAME ecommerce-config-server/.
    - docker push $CONFIG_IMAGE_NAME
    - docker build -t $ORDER_IMAGE_NAME ecommerce-product-service/.
    - docker push $ORDER_IMAGE_NAME
    - docker build -t $PRODUCT_IMAGE_NAME ecommerce-product-service/.
    - docker push $PRODUCT_IMAGE_NAME
    - docker build -t $SALE_IMAGE_NAME ecommerce-sale-service/.
    - docker push $SALE_IMAGE_NAME
    - docker build -t $REGISTRY_IMAGE_NAME ecommerce-service-registry/.
    - docker push $REGISTRY_IMAGE_NAME
    - docker build -t $USER_IMAGE_NAME ecommerce-user-service/.
    - docker push $USER_IMAGE_NAME
```
This step in the `.gitlab-ci.yml` file is called build_spring_image and is defined as stage: build_push_image. It builds and pushes the Docker image for the backend done in Spring boot to the docker hub.

- **image: docker:20.10.16** : uses the docker image docker:20.10.16 as the runtime environment for this step. Since we will have to execute docker commands we need a docker client and docker daemon which are available in this image. In this part we use the docker in docker concept for more information [see the article](https://blog.packagecloud.io/3-methods-to-run-docker-in-docker-containers/#:~:text=Docker%20In%20Docker%20)

- **services: docker:20.10.16-dind** : uses the docker image docker:20.10.16-dind as a service for Docker, which allows you to execute Docker commands inside the Docker container of this step.

- **variables: DOCKER_TLS_CERTDIR: "/certs"**: defines a `DOCKER_TLS_CERTDIR` environment variable that specifies the TLS certificate directory for Docker.

- **before_script:** a list of commands to be executed before the main task of this step is executed. In this step, there is one command to execute. it is to authenticate to the `Docker registry`.

To perform this step perfectly you need to create the variables `DOCKER_PASSWORD` and `DOCKER_LOGIN` in your project. These variables must contain the password and username of your docker hub account. To do this, go to `settings > CI/CD` then `variables`

<img width="341" alt="1" src="https://user-images.githubusercontent.com/70517765/226735876-f1236c68-ef6f-43da-940b-477f1cfb8555.png">

![Capture](https://user-images.githubusercontent.com/70517765/227020591-7e9746be-7a14-42c5-b6dc-38f11205e8d1.PNG)


- **script**: the main task of this step. In this step for each microservice, two Docker commands are executed. The first command `docker build -t IMAGE_NAME director/.` builds the Docker image for the application using the Dockerfile present in the current directory. The second `docker push IMAGE_NAME` command pushes the previously built image into the Docker registry specified by the variable `IMAGE_NAME`.
For more information on the configurations of this stage please refer to the official Gitlab CI documentation [here](https://docs.gitlab.com/ee/ci/docker/using_docker_build.html#use-docker-in-docker)

### Run the first stage

**important** : Before you start you need to make sure you can use the gitlab shared runners that we will use in this demo. If not, you can add our own runner to the project. To do this in your project go to `settings > CI/CD` then `Runners` then disable the shared runners and add your own by following the steps provided.

To be able to use the shared runners of gitlab you must validate your gitlab account. Launch the pipeline then a button to validate account will appear then you must add a credit card containing **1$**. Then you will have access to the gitlab shared runners in all your projects. 

Let's now launch our pipeline for that on your project go on `CI/CD > pipelines` then click on `Run pipeline`

![Capture](https://user-images.githubusercontent.com/70517765/227025049-8ed6015f-39da-4682-828d-a2261a10b281.PNG)

In the next screen, don't put anything, just click on Run Pipeline. After that you will get the result below. The build_push_image stage worked perfectly

<img width="457" alt="Capture d’écran 2023-04-03 170146" src="https://user-images.githubusercontent.com/70517765/229564965-f8fcf3ff-1019-43c2-a5f0-02f242240a87.png">

### deploy stage

**important**: In this step you will need a working Kubernetes cluster with Helm installed, then you will need to connect your cluster to gitlab. To do this you need to install an agent for Kubernetes in your cluster, go to `Infrastructure > Kubernetes clusters` click on `connect a cluster` then follow the steps, the name of our Kubernetes cluster is `k8s-cluster`. For more information, see the official documentation [here](https://docs.gitlab.com/ee/user/clusters/agent/install/index.html#register-the-agent-with-gitlab). 

In our case we have created a **kubernetes cluster** that we have already connected to our Gitlab project

![Capture](https://user-images.githubusercontent.com/70517765/229860467-6d2b6c34-4d90-4344-bbc3-96202044b04c.PNG)

The agent configuration file is located in the **.gitlab/agents/k8s-cluster** directory 

<img width="491" alt="Capture d’écran 2023-04-03 164230" src="https://user-images.githubusercontent.com/70517765/229560133-ff1cd09c-ce7f-4b6a-8456-d4a7746b8553.png">

**id** : contains the path to the project on gitlab

```
deploy_application:
  stage: deploy
  image: devth/helm:latest
  before_script:
    - cd helm/
    - kubectl config get-contexts
    - kubectl config use-context kemanedonfack/spring-microservice-deployment:k8s-cluster
  script: 
    - helm install mysqldb mysql
    - helm install config config-server
    - helm install --set-string SERVICE_REGISTRY=$SERVICE_REGISTRY registry registry-service
    - helm install --set-string CONFIG_SERVER=$CONFIG_SERVER gateway api-gateway
    - helm install --set-string CONFIG_SERVER=$CONFIG_SERVER order order-service
    - helm install --set-string CONFIG_SERVER=$CONFIG_SERVER product product-service
    - helm install --set-string CONFIG_SERVER=$CONFIG_SERVER sale sale-service
    - helm install --set-string CONFIG_SERVER=$CONFIG_SERVER user user-service
```

- **image: devth/helm:latest** : This step specifies the Docker image to use for this step, which is "devth/helm:latest". This image contains Helm Kubernetes package manager that we will use to deploy our application.

- **before_script** : This step defines the commands to be executed before the main script is executed. In this case, there are three commands executed. First, we move to the "helm" directory which contains the Helm files. Next, we use the `kubectl config get-contexts` command to display the list of available Kubernetes contexts. Finally, we use the `kubectl config use-context` command to select the Kubernetes context to use for the deployment which is the one of our agent.

- **script** : These commands use the Helm package manager to install applications based on Helm charts. Here is a brief description of each command: 
    - **helm install mysqldb mysql**: This command installs a MySQL instance using the mysql Helm chart.
    - **helm install config config-server**: This command installs a configuration server using the `config-server` Helm chart.
    - **helm install --set-string SERVICE_REGISTRY=$SERVICE_REGISTRY registry registry-service**: This command installs a Eureka service registry using the registry-service Helm chart, by setting the `SERVICE_REGISTRY` environment variable value.
    - **helm install --set-string CONFIG_SERVER=$CONFIG_SERVER gateway api-gateway**: This command installs an API gateway using the api-gateway Helm chart, by setting the `CONFIG_SERVER` environment variable value.
   
you have to create two variables `SERVICE_REGISTRY` and `CONFIG_SERVER` who will content the link to each services. You should have respectifly somethings like that `http://16.170.253.192:30081/eureka` and `http://16.170.253.192:30090`

![jkkkop](https://user-images.githubusercontent.com/70517765/229897444-14495bc1-1fc1-4d74-9fef-55fb14a16086.PNG)

## Step 3 : Launch our pipeline

Now let's run our pipeline one more time and after a while you will get the following result

![54547](https://user-images.githubusercontent.com/70517765/229869161-7370c83a-e85c-40eb-916e-a2427a03c2f8.PNG)

Great, everything went well, let's check our Kuberntes cluster and our application if everything works as expected.

<img width="559" alt="Capture d’écran 2023-04-04 170023" src="https://user-images.githubusercontent.com/70517765/229890144-4cff669c-4be6-4b98-8980-2555bddce328.png">

<img width="938" alt="Capture d’écran 2023-04-04 165351" src="https://user-images.githubusercontent.com/70517765/229889229-367a31b3-5092-42f0-9fa7-364512221a9b.png">

<img width="950" alt="Capture d’écran 2023-04-04 165526" src="https://user-images.githubusercontent.com/70517765/229889286-05f14969-c7f0-44cd-9b31-884518ade70b.png">


<img width="955" alt="Capture d’écran 2023-04-04 165905" src="https://user-images.githubusercontent.com/70517765/229889326-01730c05-d2e3-424d-b495-501434fc54da.png">
