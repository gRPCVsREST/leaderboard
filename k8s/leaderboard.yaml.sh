#!/bin/bash
cat <<YAML
apiVersion: apps/v1beta1
kind: Deployment
metadata:
  name: leaderboard
spec:
  replicas: 1
  template:
    metadata:
      labels:
        app: leaderboard
    spec:
      containers:
        - name: leaderboard
          image: gcr.io/alien-fold-180922/leaderboard:latest
          imagePullPolicy: Always
          ports:
            - containerPort: 8080
            - containerPort: 8090
          env:
            - name: foobar
              value: "$(date +%s)"
---
apiVersion: v1
kind: Service
metadata:
  name: leaderboard
spec:
  type: LoadBalancer
  selector:
    app: leaderboard
  ports:
   - port: 8080
     targetPort: 8080
     protocol: TCP
     name: rest
   - port: 8090
     targetPort: 8090
     protocol: TCP
     name: grpc
YAML