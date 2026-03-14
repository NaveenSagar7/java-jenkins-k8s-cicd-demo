Developer Push
      │
      ▼
GitHub Webhook
      │
      ▼
Jenkins Pipeline
      │
      ├── Checkout
      ├── Maven Build
      ├── SonarQube Scan
      ├── Quality Gate
      │
      ├── Docker Build
      ├── Docker Push
      │
      ├── Update Kubernetes Manifest
      ├── kubectl apply
      │
      ├── kubectl rollout status
      │
      └── Auto Rollback if Failure
