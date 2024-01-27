provider "aws" {
  region = "us-east-1"
}

module "eks" {
  source          = "terraform-aws-modules/eks/aws"
  cluster_name    = "eldycare-cluster"
  cluster_version = "1.29"
  vpc_id          = "vpc-08804cac612ac531b"

  subnet_ids = ["subnet-09a57effd1d28e4b7", "subnet-0a679748a9ea1211b", "subnet-0a4193b9da2258c67", "subnet-0fef17bad484371a2"]
}
