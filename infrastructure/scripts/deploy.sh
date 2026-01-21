#!/bin/bash

# ===========================================
# Deploy Script for Ticket Booking System
# ===========================================

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Script directory
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
INFRA_DIR="$(dirname "$SCRIPT_DIR")"

echo -e "${GREEN}=========================================${NC}"
echo -e "${GREEN}  Ticket Booking System - Deploy Script ${NC}"
echo -e "${GREEN}=========================================${NC}"

# Parse arguments
ACTION=""
COMPONENT=""
DETACH=true

while [[ $# -gt 0 ]]; do
    case $1 in
        up|down|restart|logs|status)
            ACTION="$1"
            shift
            ;;
        --infra)
            COMPONENT="infra"
            shift
            ;;
        --services)
            COMPONENT="services"
            shift
            ;;
        --all)
            COMPONENT="all"
            shift
            ;;
        --attach)
            DETACH=false
            shift
            ;;
        --help)
            echo "Usage: ./deploy.sh ACTION [options]"
            echo ""
            echo "Actions:"
            echo "  up        Start containers"
            echo "  down      Stop containers"
            echo "  restart   Restart containers"
            echo "  logs      Show container logs"
            echo "  status    Show container status"
            echo ""
            echo "Options:"
            echo "  --infra     Target infrastructure only (mysql, redis, kafka)"
            echo "  --services  Target microservices only"
            echo "  --all       Target all containers (default)"
            echo "  --attach    Run in foreground (for 'up' action)"
            echo "  --help      Show this help message"
            exit 0
            ;;
        *)
            echo -e "${RED}Unknown option: $1${NC}"
            exit 1
            ;;
    esac
done

# Validate action
if [ -z "$ACTION" ]; then
    echo -e "${RED}No action specified. Use: up, down, restart, logs, or status${NC}"
    exit 1
fi

# Default component
if [ -z "$COMPONENT" ]; then
    COMPONENT="all"
fi

# Change to infrastructure directory
cd "$INFRA_DIR"

# Docker compose files
INFRA_COMPOSE="docker-compose.yml"
SERVICES_COMPOSE="docker-compose-services.yml"

# Execute action
case $ACTION in
    up)
        echo -e "${BLUE}Starting containers...${NC}"
        
        DETACH_FLAG=""
        if [ "$DETACH" = true ]; then
            DETACH_FLAG="-d"
        fi
        
        case $COMPONENT in
            infra)
                echo -e "${YELLOW}Starting infrastructure...${NC}"
                docker-compose -f "$INFRA_COMPOSE" up $DETACH_FLAG
                ;;
            services)
                echo -e "${YELLOW}Starting services...${NC}"
                docker-compose -f "$SERVICES_COMPOSE" up $DETACH_FLAG
                ;;
            all)
                echo -e "${YELLOW}Starting infrastructure...${NC}"
                docker-compose -f "$INFRA_COMPOSE" up $DETACH_FLAG
                echo -e "${YELLOW}Waiting for infrastructure to be ready...${NC}"
                sleep 10
                echo -e "${YELLOW}Starting services...${NC}"
                docker-compose -f "$SERVICES_COMPOSE" up $DETACH_FLAG
                ;;
        esac
        ;;
        
    down)
        echo -e "${BLUE}Stopping containers...${NC}"
        
        case $COMPONENT in
            infra)
                docker-compose -f "$INFRA_COMPOSE" down
                ;;
            services)
                docker-compose -f "$SERVICES_COMPOSE" down
                ;;
            all)
                docker-compose -f "$SERVICES_COMPOSE" down
                docker-compose -f "$INFRA_COMPOSE" down
                ;;
        esac
        ;;
        
    restart)
        echo -e "${BLUE}Restarting containers...${NC}"
        
        case $COMPONENT in
            infra)
                docker-compose -f "$INFRA_COMPOSE" restart
                ;;
            services)
                docker-compose -f "$SERVICES_COMPOSE" restart
                ;;
            all)
                docker-compose -f "$SERVICES_COMPOSE" restart
                docker-compose -f "$INFRA_COMPOSE" restart
                ;;
        esac
        ;;
        
    logs)
        echo -e "${BLUE}Showing logs...${NC}"
        
        case $COMPONENT in
            infra)
                docker-compose -f "$INFRA_COMPOSE" logs -f --tail=100
                ;;
            services)
                docker-compose -f "$SERVICES_COMPOSE" logs -f --tail=100
                ;;
            all)
                docker-compose -f "$INFRA_COMPOSE" -f "$SERVICES_COMPOSE" logs -f --tail=100
                ;;
        esac
        ;;
        
    status)
        echo -e "${BLUE}Container status:${NC}"
        echo ""
        
        if [ "$COMPONENT" = "infra" ] || [ "$COMPONENT" = "all" ]; then
            echo -e "${YELLOW}Infrastructure:${NC}"
            docker-compose -f "$INFRA_COMPOSE" ps
            echo ""
        fi
        
        if [ "$COMPONENT" = "services" ] || [ "$COMPONENT" = "all" ]; then
            echo -e "${YELLOW}Services:${NC}"
            docker-compose -f "$SERVICES_COMPOSE" ps
        fi
        ;;
esac

echo -e "${GREEN}=========================================${NC}"
echo -e "${GREEN}  Operation completed!                  ${NC}"
echo -e "${GREEN}=========================================${NC}"
