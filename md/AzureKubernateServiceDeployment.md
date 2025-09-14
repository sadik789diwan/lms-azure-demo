# AKS Deployment & CI/CD Quick Reference (Windows CMD / PowerShell)

> Complete step-by-step cheat-sheet to build, push, and deploy a Spring Boot JAR (`springboot-app.jar`) to Azure AKS, plus useful `kubectl` and troubleshooting commands.

---

## Table of contents

1. Prerequisites
2. Quick high-level flow
3. Azure setup (Resource Group, Provider registration, ACR, AKS)
4. Build Docker image (Windows CMD)
5. Kubernetes manifests (deployment + service + readiness/liveness)
6. Deploy to AKS and verify
7. Useful `kubectl` commands (pods, svc, logs, exec, port-forward)
8. CICD snippets (Dockerfile, Jenkinsfile)
9. Common troubleshooting & fixes
10. Cleanup commands

---

## 1) Prerequisites

* Azure account & subscription
* Azure CLI installed and logged in (`az login`)
* Docker installed and running
* `kubectl` installed (or use `az aks install-cli`)
* (Optional) Helm if you use charts
* Java + Maven/Gradle to build JAR locally

## 2) High-level flow

1. Build Spring Boot JAR: `mvn clean package`
2. Write `Dockerfile`
3. Build Docker image locally and push to Azure Container Registry (ACR)
4. Create AKS cluster and attach ACR
5. Apply Kubernetes manifests (Deployment + Service)
6. Validate and access app via LoadBalancer IP or port-forward

---

## 3) Azure setup (Windows CMD / PowerShell examples)

### 3.0 Login & set subscription

```cmd
az login
az account set --subscription "Azure subscription 1"
```

### 3.1 Register required providers (if you see MissingSubscriptionRegistration)

```cmd
az provider register --namespace Microsoft.ContainerRegistry
az provider register --namespace Microsoft.ContainerService
# check status
az provider show --namespace Microsoft.ContainerRegistry --query registrationState
az provider show --namespace Microsoft.ContainerService --query registrationState
```

### 3.2 Create resource group (you already have this)

```cmd
az group create --name springboot-rg --location eastus
```

### 3.3 Create Azure Container Registry (ACR)

* NOTE: `--name` must be globally unique

```cmd
az acr create --resource-group springboot-rg --name <your-acr-name> --sku Basic
# example used in chat: springbootacr12345
```

### 3.4 Build/push (login to ACR)

```cmd
az acr login --name <your-acr-name>
# or docker login <your-acr-name>.azurecr.io (az acr login performs docker login automatically)
```

### 3.5 Create AKS and attach ACR

```cmd
az aks create --resource-group springboot-rg --name springboot-aks --node-count 1 \
  --enable-addons monitoring --generate-ssh-keys --attach-acr <your-acr-name>

# After creation get credentials for kubectl
az aks get-credentials --resource-group springboot-rg --name springboot-aks --overwrite-existing
```

> If your subscription needed provider registration, wait until both providers show `Registered` before creating ACR/AKS.

---

## 4) Build Docker image (Windows CMD)

Assumes `springboot-app.jar` exists in `target/`.

```cmd
cd C:\Path\to\project
mvn clean package -DskipTests
:: build docker image (use your ACR name)
docker build -t <acrName>.azurecr.io/springboot-app:v1 .
:: push image
docker push <acrName>.azurecr.io/springboot-app:v1
```

Example using `springbootacr12345`:

```cmd
docker build -t springbootacr12345.azurecr.io/springboot-app:v1 .
docker push springbootacr12345.azurecr.io/springboot-app:v1
```

---

## 5) Kubernetes manifests (use containerPort = 9191 to match your app)

### deployment.yaml

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: springboot-app
spec:
  replicas: 2
  selector:
    matchLabels:
      app: springboot-app
  template:
    metadata:
      labels:
        app: springboot-app
    spec:
      containers:
        - name: springboot-app
          image: <acrName>.azurecr.io/springboot-app:v1
          ports:
            - containerPort: 9191
          readinessProbe:
            httpGet:
              path: /actuator/health
              port: 9191
            initialDelaySeconds: 10
            periodSeconds: 5
          livenessProbe:
            httpGet:
              path: /actuator/health
              port: 9191
            initialDelaySeconds: 30
            periodSeconds: 20
```

### service.yaml (LoadBalancer)

```yaml
apiVersion: v1
kind: Service
metadata:
  name: springboot-service
spec:
  type: LoadBalancer
  selector:
    app: springboot-app
  ports:
    - port: 80         # external port
      targetPort: 9191 # container port inside pods
      protocol: TCP
```

Apply manifests:

```cmd
kubectl apply -f deployment.yaml
kubectl apply -f service.yaml
```

---

## 6) Deploy to AKS and verify (useful commands)

### Get cluster nodes & context

```cmd
kubectl get nodes
kubectl config current-context
az aks show --resource-group springboot-rg --name springboot-aks --query "fqdn"
```

### Check pods & services

```cmd
kubectl get pods -o wide
kubectl get svc
kubectl get svc springboot-service
```

### Describe resources for debugging

```cmd
kubectl describe pod <pod-name>
kubectl describe svc springboot-service
kubectl describe deployment springboot-app
```

### View logs

```cmd
kubectl logs <pod-name>
kubectl logs -f <pod-name>        # follow
kubectl logs <pod-name> -c <container-name>
```

### Exec into pod (note: container may not have curl)

```cmd
kubectl exec -it <pod-name> -- sh   # or bash if available
# if curl not present, test with busybox temporary pod instead
kubectl run --rm -it busybox-test --image=busybox -- sh
# from busybox shell
wget -qO- http://springboot-service:80/api/hello
```

### port-forward (local testing)

```cmd
kubectl port-forward svc/springboot-service 9191:80
# open http://localhost:9191/api/hello
```

### Rolling update (set new image)

```cmd
kubectl set image deployment/springboot-app springboot-app=<acrName>.azurecr.io/springboot-app:<new-tag> --record
kubectl rollout status deployment/springboot-app
kubectl rollout undo deployment/springboot-app
```

### Scale deployment

```cmd
kubectl scale deployment springboot-app --replicas=3
kubectl get deployment springboot-app -o wide
```

---

## 7) Useful network & troubleshooting commands

* See Service external IP and port mapping:

```cmd
kubectl get svc -o wide
kubectl describe svc springboot-service
```

* Events & cluster-wide issues:

```cmd
kubectl get events --sort-by=.metadata.creationTimestamp
kubectl describe node <node-name>
```

* If LoadBalancer external IP stuck in `<pending>`:

    * Ensure AKS created a public IP and that cloud provider resources were provisioned
    * Check `kubectl describe svc <service>` events for errors
    * Use `az network lb list -g springboot-rg -o table` to inspect Azure LB

* Check ingress controller (if you install one):

```cmd
helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx
helm repo update
helm install ingress-nginx ingress-nginx/ingress-nginx --namespace ingress-nginx --create-namespace
kubectl get svc -n ingress-nginx
```

* Create DNS name for a public IP (optional; example uses Azure Public IP resource steps):

    1. Create a Public IP with DNS label or associate DNS label to existing public IP created for the load balancer.
    2. Or create an Azure Front Door / Application Gateway in front of AKS for friendly domain and TLS.

---

## 8) CI/CD & helper files

### Dockerfile (sample)

```dockerfile
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/springboot-app.jar app.jar
EXPOSE 9191
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Jenkinsfile (declarative) - simple pipeline snippet

```groovy
pipeline {
  agent any
  environment {
    ACR = '<acrName>'
    IMAGE = "${ACR}.azurecr.io/springboot-app"
  }
  stages {
    stage('Checkout') { steps { git 'https://your-git-repo.git' } }
    stage('Build')    { steps { sh 'mvn clean package -DskipTests' } }
    stage('DockerBuild') {
      steps {
        sh "docker build -t ${IMAGE}:${BUILD_NUMBER} ."
        sh "az acr login --name ${ACR}"
        sh "docker push ${IMAGE}:${BUILD_NUMBER}"
      }
    }
    stage('Deploy') {
      steps {
        sh "az aks get-credentials -g springboot-rg -n springboot-aks --overwrite-existing"
        sh "kubectl set image deployment/springboot-app springboot-app=${IMAGE}:${BUILD_NUMBER} --record"
      }
    }
  }
}
```

---

## 9) Common troubleshooting & tips (from your session)

* **MissingSubscriptionRegistration** → register provider: `az provider register --namespace Microsoft.ContainerRegistry` and/or `Microsoft.ContainerService`.
* **Service targetPort mismatch** → ensure `containerPort` in pod and `targetPort` in Service match (you used `9191`).
* **`kubectl exec` errors (no curl)** → container minimal image may not have utilities; use `kubectl run busybox` or `kubectl exec` to open shell and test local endpoints.
* **APIs use actuator** → declare readiness/liveness using `/actuator/health` to avoid routing to not-ready pods.
* **If service accessible but returns 404** → verify context-path and controller mapping (`/api/hello` etc.).

---

## 10) Cleanup commands

```cmd
kubectl delete -f deployment.yaml
kubectl delete -f service.yaml
az aks delete --name springboot-aks --resource-group springboot-rg --yes --no-wait
az acr delete --name <your-acr-name> --resource-group springboot-rg
az group delete --name springboot-rg --yes --no-wait
```

---

## Appendix: Helpful aliases & shortcuts

```cmd
:: Windows powershell-style examples
az acr login --name <acrName>
az acr repository list --name <acrName> --output table
az acr repository show-tags --name <acrName> --repository springboot-app
kubectl get all -o wide
kubectl top pod
kubectl get events --sort-by='.lastTimestamp'
```

---