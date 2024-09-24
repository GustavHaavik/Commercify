#!/bin/bash

# Define the project directories for each service
BASE_DIR=$(pwd)
DISCOVERY_SERVER_DIR="$BASE_DIR/DiscoveryServer"
API_GATEWAY_DIR="$BASE_DIR/ApiGateway"
ORDER_SERVICE_DIR="$BASE_DIR/OrderService"
PAYMENT_SERVICE_DIR="$BASE_DIR/PaymentService"
PRODUCT_SERVICE_DIR="$BASE_DIR/ProductsService"
USER_SERVICE_DIR="$BASE_DIR/UserService"

# Name of the tmux session
SESSION_NAME="commercify"

# Function to start a service in a new tmux window
start_service() {
  local service_name=$1
  local service_dir=$2
  local window_name=$service_name

  tmux new-window -t $SESSION_NAME -n "$window_name" "cd $service_dir && mvn spring-boot:run"
  echo "$service_name started in tmux window"
}

# Start the tmux session if it doesn't exist
if ! tmux has-session -t $SESSION_NAME 2>/dev/null; then
  tmux new-session -d -s $SESSION_NAME -n "discovery-server" "cd $DISCOVERY_SERVER_DIR && mvn spring-boot:run"
  echo "discovery-server started in tmux window"
fi

# Start the rest of the services in new windows
start_service "api-gateway" "$API_GATEWAY_DIR"
start_service "order-service" "$ORDER_SERVICE_DIR"
start_service "payment-service" "$PAYMENT_SERVICE_DIR"
start_service "product-service" "$PRODUCT_SERVICE_DIR"
start_service "user-service" "$USER_SERVICE_DIR"

# Optional: Attach to the tmux session
tmux attach-session -t $SESSION_NAME
