#!/bin/bash

# ===========================================
# Build Script for Ticket Booking System
# ===========================================

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Project root directory
PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$PROJECT_ROOT"

echo -e "${GREEN}=========================================${NC}"
echo -e "${GREEN}  Ticket Booking System - Build Script  ${NC}"
echo -e "${GREEN}=========================================${NC}"

# Parse arguments
BUILD_ALL=false
BUILD_SERVICE=""
SKIP_TESTS=false
BUILD_DOCKER=false

while [[ $# -gt 0 ]]; do
    case $1 in
        --all)
            BUILD_ALL=true
            shift
            ;;
        --service)
            BUILD_SERVICE="$2"
            shift 2
            ;;
        --skip-tests)
            SKIP_TESTS=true
            shift
            ;;
        --docker)
            BUILD_DOCKER=true
            shift
            ;;
        --help)
            echo "Usage: ./build.sh [options]"
            echo ""
            echo "Options:"
            echo "  --all           Build all services"
            echo "  --service NAME  Build specific service (e.g., booking-service)"
            echo "  --skip-tests    Skip running tests"
            echo "  --docker        Build Docker images"
            echo "  --help          Show this help message"
            exit 0
            ;;
        *)
            echo -e "${RED}Unknown option: $1${NC}"
            exit 1
            ;;
    esac
done

# Build function
build_gradle() {
    local target=$1
    local test_flag=""
    
    if [ "$SKIP_TESTS" = true ]; then
        test_flag="-x test"
    fi
    
    echo -e "${YELLOW}Building: $target${NC}"
    ./gradlew "$target" $test_flag --parallel
}

# Build Docker image function
build_docker_image() {
    local service=$1
    local service_path="services/$service"
    
    if [ -f "$service_path/Dockerfile" ]; then
        echo -e "${YELLOW}Building Docker image for: $service${NC}"
        docker build -t "ticket-booking/$service:latest" "$service_path"
    else
        echo -e "${RED}Dockerfile not found for: $service${NC}"
    fi
}

# Main build logic
if [ "$BUILD_ALL" = true ]; then
    echo -e "${GREEN}Building all services...${NC}"
    build_gradle "clean build"
    
    if [ "$BUILD_DOCKER" = true ]; then
        echo -e "${GREEN}Building all Docker images...${NC}"
        for service in services/*/; do
            service_name=$(basename "$service")
            build_docker_image "$service_name"
        done
    fi
elif [ -n "$BUILD_SERVICE" ]; then
    echo -e "${GREEN}Building service: $BUILD_SERVICE${NC}"
    build_gradle ":services:$BUILD_SERVICE:clean :services:$BUILD_SERVICE:build"
    
    if [ "$BUILD_DOCKER" = true ]; then
        build_docker_image "$BUILD_SERVICE"
    fi
else
    echo -e "${YELLOW}No build target specified. Use --all or --service NAME${NC}"
    echo "Run './build.sh --help' for usage information"
    exit 1
fi

echo -e "${GREEN}=========================================${NC}"
echo -e "${GREEN}  Build completed successfully!         ${NC}"
echo -e "${GREEN}=========================================${NC}"
