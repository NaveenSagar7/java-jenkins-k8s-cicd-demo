Developer
   │
   │ push code
   ▼
GitHub Repository
   │
   │ webhook
   ▼
Jenkins Pipeline
   │
   ├── Checkout Code
   ├── Maven Build
   ├── SonarQube Scan
   │       │
   │       └── Quality Gate
   │             │
   │             └── PASS
   │
   ├── Build Docker Image
   ├── Push to DockerHub
   ├── Update Kubernetes Manifest
   │
   ▼
Kubernetes Cluster
   │
   └── Deploy Pod
         │
         └── Application Running
