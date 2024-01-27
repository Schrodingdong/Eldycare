variable "control_plane_subnet_ids" {
  description = "List of subnet IDs for the EKS control plane"
  type        = list(string)
  default     = []
}

variable "subnet_ids" {
  description = "List of subnet IDs for the EKS worker nodes"
  type        = list(string)
  default     = []
}