# ELDYCARE
EldyCare is a connected application that let relative always be connected to their elder and old parents in case of a medical urgency.

# File structure
We will follow this file structure :
```
ELDYCARE
|
|-Backend
|-Mobile
  |
  |- SmartPhone
  |- SmartWatch
```


## AWS Configuration

To configure AWS for the project run:

```
aws configure
```

Use the following credentials:

```
Access key: AKIA2FFRIWPI57TWEN7J
Secret access key: 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
```

Also to configure kubectl to use the EKS cluster run :

```
aws eks --region us-east-1 update-kubeconfig --name eldycare-cluster
```
And to list services, run:

```
kubctl aws get services
```

For logs, run:

```
kubectl logs -n default -l app=<service-name>
```